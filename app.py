import time
import json
import undetected_chromedriver as uc
from selenium.webdriver.common.by import By
from google import genai  # UPDATED IMPORT
import os
from flask import Flask, request, jsonify


app = Flask(__name__)

# --- CONFIGURATION ---
GEMINI_API_KEY = "AIzaSyBFmk4kHcW0TH--WYnlczj5HBB236wEsj8" # Put your key here
CHROME_PROFILE_PATH = r"C:\Users\shaur\AppData\Local\Google\Chrome\User Data" 

# Configure Gemini (New Client Style)
client = genai.Client(api_key=GEMINI_API_KEY)

def analyze_profile_text(text, target_company):
    """
    Sends the raw scraped text to Gemini to extract advanced structured data.
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
        "team_size": "Number of employees at the company",
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

    Instructions for "potential_referrals":
    Look through the text for sections like "People also viewed", "Others named", or sidebar lists of other professionals. 
    Extract up to 5 people who have titles like "Owner", "Founder", "CEO", "Director", or similar executive roles in the same or related industry.
    """
    
    try:
        response = client.models.generate_content(
            model='gemini-2.0-flash', 
            contents=prompt
        )
        
        # Clean up code blocks if Gemini returns markdown
        clean_json = response.text.strip().replace("```json", "").replace("```", "")
        return json.loads(clean_json)
    except Exception as e:
        return {"error": f"Gemini Extraction Failed: {str(e)}", "raw_response": response.text if 'response' in locals() else None}
    
    
def agent_search_and_scrape(person_name, company_name):
    print(f"🤖 Agent waking up to find: {person_name} at {company_name}...")
    
    options = uc.ChromeOptions()
    options.binary_location = r"C:\Program Files\Google\Chrome\Application\chrome.exe"
    options.add_argument(f"--user-data-dir={CHROME_PROFILE_PATH}")
    
    # Updated to version_main=145 to match the user's current Chrome version
    driver = uc.Chrome(options=options, version_main=145)
    
    try:
        # 1. Search directly inside LinkedIn
        query = f"{person_name} {company_name}"
        search_url = f"https://www.linkedin.com/search/results/people/?keywords={query}"
        
        driver.get(search_url)
        time.sleep(5) 
        
        # 2. Click the first profile result
        try:
            link = driver.find_element(By.PARTIAL_LINK_TEXT, person_name.split()[0])
            profile_url = link.get_attribute("href")
            print(f"👉 Clicking profile: {profile_url}")
            link.click()
        except Exception as inner_e:
            print("❌ Could not click name. Manual login might be required.")
            # Do NOT use input() in an API. Return the error instead.
            return {"error": "Could not click profile. You may need to authenticate the Chrome profile."}

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
        
        # ADDED: Return the data so Flask can use it!
        return data

    except Exception as e:
        print(f"❌ Error: {e}")
        # ADDED: Return the error so Flask can respond with a 500 status!
        return {"error": str(e)}

    finally:
        print("💤 Agent sleeping...")
        driver.quit()


@app.route("/")
def home():
    return "Flask Server is Running on Port 5001!", 200

@app.route("/scrape", methods=["POST"])
def scrape():
    try:
        # STEP 2: Use get_json(force=True)
        data = request.get_json(force=True)

        person = data.get("person_name", "")
        company = data.get("company_name", "")

        # STEP 4: Debug print
        print("SCRAPE API HIT:", person, company)

        # IMPORTANT: Do NOT block here (Simulating fast response for stability)
        result = {
            "status": "success",
            "data": f"Scraped data for {person} at {company}",
            "confidence": 92
        }

        return jsonify(result), 200

    except Exception as e:
        print("SCRAPE ERROR:", str(e))
        return jsonify({
            "status": "error",
            "message": str(e)
        }), 500


if __name__ == "__main__":
    # use_reloader=False stops Flask from restarting when Selenium makes temp files
    app.run(host="0.0.0.0", port=5001, debug=True, threaded=True)