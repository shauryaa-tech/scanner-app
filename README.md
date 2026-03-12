# SmartLead Scanner - Usage & Cost Breakdown

This document provides an overview of the credits and costs associated with the AI-powered features of the application.

## 1. Card Scanning (Extraction)
When you scan a business card, the app uses **Gemini 2.0 Flash** to extract text and contact information.

| Metric | Value |
| :--- | :--- |
| **Input Credit** | ~450 (Prompt + Image Data) |
| **Output Credit** | ~150 (JSON formatted data) |
| **Total Credit** | **~600 Credits** |
| **Estimated Cost** | **$0.00008** (approx. ₹0.007) |

---

## 2. Lead Scraping (AI Research)
When you click the Scrap icon to find background details about a lead, the app uses **Gemini 2.0 Flash** with a deep research prompt.

| Metric | Value |
| :--- | :--- |
| **Input Credit** | ~400 (Research Query) |
| **Output Credit** | ~400 (Detailed Profile JSON) |
| **Total Credit** | **~800 Credits** |
| **Estimated Cost** | **$0.00015** (approx. ₹0.012) |

---

## 3. WhatsApp Message Generation
The app automatically (or via regenerate button) creates a personalized outreach message.

| Metric | Value |
| :--- | :--- |
| **Input Credit** | ~350 (Lead Meta + Scrap Data) |
| **Output Credit** | ~100 (Personalized message) |
| **Total Credit** | **~450 Credits** |
| **Estimated Cost** | **$0.00006** (approx. ₹0.005) |

---

## Summary of Total Costs
For a complete workflow (**Scan + Scrap + Message Gen**), the total usage is:

- **Total Credits**: ~1,850 Credits
- **Total Combined Cost**: **$0.00029** (approx. ₹0.024 per lead)

> [!NOTE]
> Credits are calculated based on 1 Token = 1 Credit. 
> Costs are estimates based on standard Google Gemini API pricing for Flash models ($0.075 per 1M input tokens and $0.30 per 1M output tokens).
