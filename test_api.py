import requests
import json

url = "http://127.0.0.1:5762/scrape"
payload = {"person_name": "Satya Nadella", "company_name": "Microsoft"}

try:
    print(f"Sending POST to {url}...")
    response = requests.post(url, json=payload, timeout=60)
    print(f"Status: {response.status_code}")
    print(f"Response: {response.text}")
except Exception as e:
    print(f"Error: {e}")
