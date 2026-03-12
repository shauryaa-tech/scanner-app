import undetected_chromedriver as uc
import time

try:
    print("🚀 Starting test browser...")
    driver = uc.Chrome()
    print("✅ Browser started successfully!")
    driver.get("https://www.google.com")
    print(f"Page title: {driver.title}")
    time.sleep(5)
    driver.quit()
    print("👋 Browser closed.")
except Exception as e:
    print(f"❌ Browser test failed: {e}")
