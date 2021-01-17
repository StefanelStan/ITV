# Basic project for a checkout system

## Components

### 1. Checkout:DefaultCheckout (PricingService, PromotionService)
- contains the core logic for getting the total price for a list of items
  
`int getTotalPrice(List<Item> items) throws CheckoutException`

### 2. PricingService:DefaultPricingService
- contains logic for default price management for a given SKU. Can get price, add or update price for a SKU 
```
int getPriceForSKU(SKU sku)
void addPriceForSKU(SKU sku, int price)
void updatePriceForSKU(SKU sku, int price)
```

### 3. PromotionService:DefaultPromotionService
- contains logic to manage the promotions: add a promotion for a SKU, getPromotion for SKU 

```
 void addPromotion(Promotion promotion)
 boolean hasPromotionForSKU(SKU sku);
 Set<Promotion> getPromotionsForSKU(SKU sku);
```

### 4. Promotion
- contains the basic promotion object: applied for a SKU, for a min items and discounted price

    `int getTotalPriceForItem(SKU sku, int itemQuantity, int price)`

## Tests
### Unit tests
- Event major component has its own tests that cover most of the edge cases
- contained in `src/main/test/java/org/checkoutkata/[checkout, model, procing, promotion]`
### Integration tests  
- in this tests the components are not mocked and whole system is being tested
- contained in `src/main/test/java/org/checkoutkata/integration`

## More exhaustive documentation can be found inside docs folder `docs/index.html`