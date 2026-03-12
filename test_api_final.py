import requests
import json

url = "http://172.20.10.7:5000/scrape"
payload = {"person_name": "Test", "company_name": "ABC"}

try:
    print(f"Sending POST to {url}...")
    response = requests.post(url, json=payload, timeout=10)
    print(f"Status: {response.status_code}")
    print(f"Response: {response.text}")
except Exception as e:
    print(f"Error: {e}")
