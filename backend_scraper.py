import os
import re
import subprocess
import time
import json
import winreg
import undetected_chromedriver as uc
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from google import genai
from flask import Flask, request, jsonify
import urllib.parse
import logging
import threading
import shutil
import tempfile

# Setup logging with absolute path and console output
log_file = os.path.join(os.getcwd(), 'scraper.log')
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler(log_file, mode='a'),
        logging.StreamHandler()
    ]
)
logging.info(f"Scraper starting, logging to {log_file}")

app = Flask(__name__)
scrape_lock = threading.Lock()

# --- CONFIGURATION ---
GEMINI_API_KEY = ""
# Standalone profile to avoid locks
CHROME_PROFILE_PATH = os.path.join(os.getcwd(), "selenium_profile_main")

client = genai.Client(api_key=GEMINI_API_KEY)

def get_chrome_version():
    """Detect main Chrome version from Windows Registry."""
    try:
        reg_path = r"Software\Google\Chrome\BLBeacon"
        key = winreg.OpenKey(winreg.HKEY_CURRENT_USER, reg_path, 0, winreg.KEY_READ)
        version, _ = winreg.QueryValueEx(key, "version")
        winreg.CloseKey(key)
        major_version = int(version.split('.')[0])
        print(f"🔍 System Chrome version detected: {major_version}")
        return major_version
    except Exception as e:
        print(f"⚠️ Could not detect Chrome version from registry: {e}")
        return None

def kill_chrome():
    print("🧹 Cleaning up old Chrome processes...")
    try:
        subprocess.run("taskkill /F /IM chrome.exe /T", shell=True, capture_output=True)
        subprocess.run("taskkill /F /IM chromedriver.exe /T", shell=True, capture_output=True)
    except:
        pass

def analyze_profile_text(text, target_company):
    prompt = f"""
    You are an expert data extractor. I have scraped the visible text from a LinkedIn profile. 
    Analyze this text and extract the following details for the person, specifically focusing on their relationship with "{target_company}".
    
    Text content:
    {text[:20000]} 
    
    Return ONLY a valid JSON object matching this exact structure (use null if a field cannot be found in the text):
    {{
        "name": "The person's full name",
        "headline": "Their main profile headline/tagline",
        "about_summary": "A brief summary of their 'About' section, if present",
        "current_role": {{
            "designation": "Job title at {target_company}",
            "company_name": "{target_company}",
            "tenure_duration": "How long they have been there (e.g., '4 yrs 2 mos')"
        }},
        "previous_role": {{
            "designation": "Their most recent job title before {target_company}",
            "company_name": "Name of previous company",
            "tenure_duration": "How long they were there"
        }},
        "experiences": [
            {{
                "designation": "Job title",
                "company_name": "Company name",
                "tenure_duration": "Duration",
                "description": "Short summary of responsibilities if available"
            }}
        ],
        "company_size": "Number of employees (Only if explicitly mentioned in the text)",
        "team_size": "Approximate number of employees at the company",
        "company_website": "The company's official website URL",
        "company_linkedin_url": "The company's LinkedIn profile URL",
        "linkedin_url": "The LinkedIn URL if found in the text",
        "potential_referrals": [
            {{
                "name": "Name of person",
                "headline": "Their headline or job title"
            }}
        ],
        "status": "found" // or "not_found" if they clearly don't work at {target_company}
    }}

    Instructions for "current_role" and "previous_role":
    - The Experience section usually lists roles in reverse chronological order.
    - Treat the FIRST (top-most) item in the Experience section as the "current_role".
    - Treat the SECOND item in the Experience section as the "previous_role".
    
    Instructions for "experiences":
    Extract all professional experiences found in the profile, listed in reverse chronological order as they appear on the page.
    
    Instructions for "potential_referrals":
    Look through the text for sections like "People also viewed", "Others named", or sidebar lists of other professionals. 
    Extract up to 5 people who have titles like "Owner", "Founder", "CEO", "Director", or similar executive roles in the same or related industry.
    """
    
    try:
        response = client.models.generate_content(
            model='gemini-2.0-flash', 
            contents=prompt
        )
        raw_text = response.text.strip()
        match = re.search(r"(\{.*\})", raw_text, re.DOTALL)
        if match:
            clean_json = match.group(1)
        else:
            clean_json = raw_text.replace("```json", "").replace("```", "").strip()
        return json.loads(clean_json)
    except Exception as e:
        print(f"⚠️ Raw Gemini Response: {response.text if 'response' in locals() else 'No response'}")
        return {"error": f"Gemini Extraction Failed: {str(e)}"}

def agent_search_and_scrape(person_name, company_name):
    print(f"🤖 Agent version: Robust-v7 (Ultimate Extraction)")
    print(f"🤖 Searching for: {person_name} at {company_name}...")
    
    options = uc.ChromeOptions()
    options.binary_location = r"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"
    # Create a temporary unique profile based on the main one if it exists
    temp_profile = tempfile.mkdtemp(prefix="selenium_profile_")
    
    options.add_argument(f"--user-data-dir={temp_profile}")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    options.add_argument("--disable-gpu")
    options.add_argument("--disable-infobars")
    
    chrome_version = get_chrome_version()
    try:
        print(f"🖥️ Launching Browser with profile: {temp_profile} (Version: {chrome_version})")
        driver = uc.Chrome(options=options, version_main=chrome_version)
    except Exception as e:
        logging.exception("❌ Browser Launcher Exception")
        print(f"❌ Browser Failed: {e}")
        return {"error": f"Chrome failed: {str(e)}. Is Chrome already open with this profile or version mismatch?"}
    
    try:
        # 1. Search Logic
        query = f"{person_name} {company_name}"
        encoded_query = urllib.parse.quote(query)
        search_url = f"https://www.linkedin.com/search/results/people/?keywords={encoded_query}"
        
        print(f"🔍 Navigating to Search: {search_url}")
        driver.get(search_url)
        time.sleep(4) 
        
        if "login" in driver.current_url or "checkpoint" in driver.current_url or "authwall" in driver.current_url:
            print("\n🔑 LOGIN REQUIRED! Please log in in the browser window.")
            while "search/results" not in driver.current_url:
                time.sleep(2)
            print("✅ Login detected!")
            driver.get(search_url)
            time.sleep(5)

        # 2. Find Profile
        try:
            firstName = person_name.split()[0]
            print(f"⏳ Waiting for search results for {firstName}...")
            
            # Wait for any result link to appear
            try:
                WebDriverWait(driver, 15).until(
                    EC.presence_of_element_located((By.CSS_SELECTOR, ".reusable-search__result-container, .app-aware-link"))
                )
            except:
                print("⚠️ Search results took too long or didn't appear.")

            print(f"🖱️ Clicking on {firstName}...")
            
            target_link = None
            # 1. Try modern LinkedIn search result link class
            try:
                result_links = driver.find_elements(By.CSS_SELECTOR, "span.entity-result__title-text a.app-aware-link")
                for link in result_links:
                    if firstName.lower() in link.text.lower():
                        target_link = link
                        print(f"🎯 Found via CSS selector: {link.get_attribute('href')}")
                        break
            except:
                pass

            if not target_link:
                # 2. Fallback: Loop all links with /in/
                links = driver.find_elements(By.TAG_NAME, "a")
                for l in links:
                    href = str(l.get_attribute("href") or "")
                    text = str(l.text or "").lower()
                    if "/in/" in href and (firstName.lower() in text or firstName.lower() in href.lower()):
                        target_link = l
                        print(f"🎯 Found via href scan: {href}")
                        break

            if not target_link:
                # 3. Last Resort: Partial Link Text
                try:
                    target_link = driver.find_element(By.PARTIAL_LINK_TEXT, firstName)
                    print(f"🎯 Found via partial link text: {target_link.get_attribute('href')}")
                except:
                    pass

            if target_link:
                driver.execute_script("arguments[0].click();", target_link)
                time.sleep(8)
            else:
                raise Exception(f"Could not find or click profile for {person_name}")

        except Exception as inner_e:
            driver.save_screenshot("search_fail.png")
            print(f"❌ Click Failed: {inner_e}. Saved search_fail.png")
            return {"error": str(inner_e)}

        # --- DEEP EXTRACTION ON PROFILE ---
        print("⏳ Waiting for profile content to load...")
        try:
            # Wait specifically for the name element
            WebDriverWait(driver, 20).until(
                EC.presence_of_element_located((By.CSS_SELECTOR, "h1, .pv-top-card--list"))
            )
        except:
            print("⚠️ Timeout waiting for profile headers.")

        if "access-denied" in driver.page_source.lower() or "security" in driver.current_url:
            print("🛡️ Security Wall detected!")
            return {"error": "LinkedIn security check appeared. Please solve it in the browser."}

        # 3. Expansion (Phase 1: Initial Reveal)
        try:
            print("🔓 Expanding initial sections...")
            # Click standard "More" buttons
            driver.execute_script("""
                document.querySelectorAll('button.inline-show-more-text__button, button.lt-line-clamp__more').forEach(b => b.click());
            """)
            time.sleep(2)
        except:
            pass

        # 4. Deep Scrolling (Iterative & Slow)
        print("📜 Slow scrolling to load all lazy sections...")
        last_height = driver.execute_script("return document.body.scrollHeight")
        for i in range(25): # Increased for more thorough coverage
            # Scroll in smaller increments for better lazy loading
            driver.execute_script("window.scrollBy(0, 600);")
            time.sleep(1.5)
            
            # Try to click "Show more" or "See all" during scroll
            try:
                driver.execute_script("""
                    const buttons = [
                        ...document.querySelectorAll('button[aria-label*="See all"], .pvs-navigation__link'),
                        ...document.querySelectorAll('.inline-show-more-text__button'),
                        ...document.querySelectorAll('button.lt-line-clamp__more'),
                        ...document.querySelectorAll('a[href*="/details/experience/"]')
                    ];
                    buttons.forEach(b => {
                        if(b && !b.innerText.includes('People also viewed') && !b.classList.contains('processed')) {
                            b.click();
                            b.classList.add('processed');
                        }
                    });
                """)
            except:
                pass
                
            new_height = driver.execute_script("return document.body.scrollHeight")
            if new_height == last_height and i > 15:
                break
            last_height = new_height
            print(f"   - Scrolled step {i+1}/25...")

        # 5. Final Expansion (Deep Dive)
        try:
            print("🔓 Final expansion and detail capture...")
            # Click any remaining "See all experience" links if they are in the DOM as links
            experience_links = driver.find_elements(By.CSS_SELECTOR, 'a[href*="/details/experience/"]')
            for link in experience_links:
                try:
                    driver.execute_script("arguments[0].scrollIntoView({block: 'center'});", link)
                    time.sleep(1)
                    link.click()
                    print("✅ Clicked Detail Experience link.")
                    time.sleep(3)
                    driver.back() # Go back to main profile after loading details? No, maybe just stay if it's a modal.
                    time.sleep(2)
                except:
                    pass

            driver.execute_script("""
                document.querySelectorAll('button.pv-profile-section__card-action-bar, button.inline-show-more-text__button').forEach(b => b.click());
            """)
            time.sleep(2)
        except:
            pass
        
        # Capture text from multiple sources
        print("📊 Finalizing text capture...")
        main_text = ""
        try: main_text = driver.find_element(By.TAG_NAME, "main").text
        except: pass
        
        body_text = driver.find_element(By.TAG_NAME, "body").text
        
        # Merge logic: Use main if valid, but append body if it has significantly more data
        page_text = main_text if len(main_text) > 3000 else body_text
        if len(body_text) > len(main_text) + 2000:
            page_text = body_text # Sometimes main misses sidebars
            
        print(f"📊 Capture Stats: {len(page_text)} chars.")
        
        if len(page_text) < 500:
            print("❌ Critical: Very low character count. Using fallback method.")
            raw_source = str(driver.page_source)
            # Remove scripts and style for cleaner raw text
            clean_source = re.sub(r'<(script|style).*?>.*?</\1>', '', raw_source, flags=re.DOTALL)
            page_text = re.sub('<[^<]+?>', '', clean_source)[:30000]
            driver.save_screenshot("low_data_v8.png")
        else:
            print(f"💊 Snippet: {page_text[:400].replace('\\n', ' ')}...")

        # 5. Analyze
        print("🧠 Analyzing with Gemini...")
        data = analyze_profile_text(page_text, company_name)
        data['linkedin_url'] = driver.current_url
        
        print("\n✅ EXTRACTION COMPLETE:")
        print(json.dumps(data, indent=2))
        return data

    except Exception as e:
        logging.error(f"❌ Execution error: {e}", exc_info=True)
        print(f"❌ Execution error: {e}")
        return {"error": str(e)}

    finally:
        print("💤 Agent finished.")
        try:
            driver.quit()
        except:
            pass
        try:
            # Clean up temp profile
            shutil.rmtree(temp_profile, ignore_errors=True)
        except:
            pass

@app.route('/scrape', methods=['POST'])
def scrape_endpoint():
    with open("debug_hit.txt", "a") as f:
        f.write(f"Request received at {time.ctime()} (RESTORED VERSION)\n")
    data = request.get_json()
    if not data or 'person_name' not in data or 'company_name' not in data:
        return jsonify({"error": "Missing input"}), 400
    
    logging.info(f"Received scrape request for: {data.get('person_name')} @ {data.get('company_name')}")
    
    with scrape_lock:
        logging.info("Lock acquired, starting scrape...")
        result = agent_search_and_scrape(data['person_name'], data['company_name'])
    
    if result is None or "error" in result:
        with open("debug_hit.txt", "a") as f:
            f.write(f"Scrape failed: {result}\n")
        logging.error(f"Scrape failed: {result}")
        status_code = 500
    else:
        logging.info("Scrape successful")
        status_code = 200
        
    return jsonify(result), status_code

@app.errorhandler(Exception)
def handle_exception(e):
    logging.exception("Unhandled Exception occurred in Flask")
    return jsonify({"error": str(e), "type": str(type(e))}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5762, debug=True, use_reloader=False)
