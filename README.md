# Haiilo — Checkout Kata

Hi Haiilo team! 👋 Thanks for reviewing my task

## TL;DR
- **Extensible offers** tried to make it easy to add new types of offers.
- **Multiple offers per item** are supported.
- **Customer-oriented** service - picks best offers for the customer, giving max discount.
- **Embedded H2** with seeded data - items and offers, check data.sql.
- **Usage limits** per offer configurable, could be > 1 for the same offer
- However I assumed a limitation that 1 item can be discounted only by 1 offer, because of rational sense
- **Money** in minor units (cents).

Run Application.main() to start the server
To overview H2 DB go to: http://localhost:8080/h2

Example curl to call an endpoint:
```
curl -X POST http://localhost:8080/api/checkout -H "Content-Type: application/json" -d "{\"items\":[\"APPLE\",\"BANANA\",\"APPLE\",\"BANANA\",\"BANANA\"]}"
```

That's it, hope you enjoy.
