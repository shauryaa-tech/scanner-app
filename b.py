import os
import subprocess
import time
import json
import undetected_chromedriver as uc
from selenium.webdriver.common.by import By
from google import genai

def kill_chrome():
    print("🧹 Cleaning up old Chrome processes...")
    try:
        subprocess.run("taskkill /F /IM chrome.exe /T", shell=True, capture_output=True)
        subprocess.run("taskkill /F /IM chromedriver.exe /T", shell=True, capture_output=True)
    except:
        pass

# --- CONFIGURATION ---
GEMINI_API_KEY = "AIzaSyBFmk4kHcW0TH--WYnlczj5HBB236wEsj8" # Put your key here

# Using a DEDICATED profile folder for this script to avoid "profile in use" errors
# This folder will be created in your current directory
CHROME_PROFILE_PATH = os.path.join(os.getcwd(), "selenium_profile")

# Configure Gemini (New Client Style)
client = genai.Client(api_key=GEMINI_API_KEY)

import re

def analyze_profile_text(text, target_company):
    """
    Sends the raw scraped text to Gemini to extract structured data.
    """
    prompt = f"""
    You are an expert data extractor. I have scraped the visible text from a LinkedIn profile. 
    Analyze this text and extract the following details for the person, specifically focusing on their relationship with "{target_company}".
    
    Text content:
    {text[:12000]} 
    
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
        "company_size": "Number of employees (Only if explicitly mentioned in the text)",
        "linkedin_url": "The LinkedIn URL if found in the text",
        "potential_referrals": [
            {{
                "name": "Name of person",
                "headline": "Their headline or job title"
            }}
        ],
        "status": "found" // or "not_found" if they clearly don't work at {target_company}
    }}

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
        
        # Robust JSON extraction using regex
        # Look for the first '{' and the last '}'
        match = re.search(r"(\{.*\})", raw_text, re.DOTALL)
        if match:
            clean_json = match.group(1)
        else:
            clean_json = raw_text.replace("```json", "").replace("```", "").strip()
            
        return json.loads(clean_json)
    except Exception as e:
        print(f"⚠️ Raw Gemini Response: {response.text if 'response' in locals() else 'No response'}")
        return {"error": str(e)}

def agent_search_and_scrape(person_name, company_name):
    # REMOVED: kill_chrome() here to avoid closing windows you might be using to login
    print(f"🤖 Agent waking up to find: {person_name} at {company_name}...")
    
    options = uc.ChromeOptions()
    options.binary_location = r"C:\Program Files\Google\Chrome\Application\chrome.exe"
    options.add_argument(f"--user-data-dir={CHROME_PROFILE_PATH}")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    options.add_argument("--disable-gpu")
    options.add_argument("--disable-infobars")
    options.add_argument("--remote-debugging-port=9222")
    
    try:
        print(f"🚀 Launching Chrome (v145) with profile: {CHROME_PROFILE_PATH}")
        driver = uc.Chrome(options=options, version_main=145)
    except Exception as e:
        print(f"❌ Could not reach Chrome: {e}")
        print("\n💡 SOLUTION: Ek baar saare Chrome windows band karein aur fir chalaein.")
        return
    
    try:
        # 1. Search directly inside LinkedIn
        query = f"{person_name} {company_name}"
        search_url = f"https://www.linkedin.com/search/results/people/?keywords={query.replace(' ', '%20')}"
        
        print(f"🔍 Navigating to: {search_url}")
        driver.get(search_url)
        time.sleep(3) 

        # --- WAIT FOR LOGIN ---
        if "login" in driver.current_url or "checkpoint" in driver.current_url or "authwall" in driver.current_url:
            print("\n🔑 LOGIN REQUIRED!")
            print("👉 Browser window mein LinkedIn log-in karein.")
            print("⌛ Waiting for you to finish login...")
            
            # Wait until the URL contains search results (meaning login is done)
            while "search/results" not in driver.current_url:
                time.sleep(2)
                # If they navigate away or close, we might want to break, but for now just wait
            
            print("✅ Login detected! Proceeding with search...")
            driver.get(search_url) # Refresh to ensure we see the results
            time.sleep(5)

        # 2. Click the first profile result
        try:
            firstName = person_name.split()[0]
            print(f"🖱️ Clicking profile for: {firstName}")
            link = driver.find_element(By.PARTIAL_LINK_TEXT, firstName)
            profile_url = link.get_attribute("href")
            print(f"🎯 Target Found: {profile_url}")
            driver.execute_script("arguments[0].click();", link)
            time.sleep(8)
        except Exception as e:
            print(f"❌ Could not click name: {e}")
            return {"error": "Could not find profile. Check if the name matches search results."}

        time.sleep(5) 
        
        # 3. Scrape text
        page_text = driver.find_element(By.TAG_NAME, "body").text
        
        # 4. Analyze
        print("🧠 Analyzing data with Gemini...")
        data = analyze_profile_text(page_text, company_name)
        data['scraped_url'] = driver.current_url
        
        print("\n" + "="*30)
        print(json.dumps(data, indent=2))
        print("="*30)

    except Exception as e:
        print(f"❌ Error: {e}")

    finally:
        print("💤 Agent sleeping...")
        driver.quit()

if __name__ == "__main__":
    agent_search_and_scrape("Manisha Chaudhary", "ENjay IT SOlutions LTD")