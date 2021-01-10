package org.checkoutkata.pricing;

import org.checkoutkata.model.SKU;

import java.util.EnumMap;
import java.util.Map;

public class DefaultPricingService implements PricingService {

    private final Map<SKU, Integer> prices = new EnumMap<>(SKU.class);

    public DefaultPricingService() {
        prices.put(SKU.A, 50);
        prices.put(SKU.B, 30);
        prices.put(SKU.C, 20);
        prices.put(SKU.D, 15);
    }

    @Override
    public int getPriceForSKU(SKU sku) throws PricingServiceException {
        if (prices.containsKey(sku)) {
            return prices.get(sku);
        } else {
            throw new PricingServiceException(String.format("Unable to find price for SKU %s.", sku.name()));
        }
    }

    @Override
    public void addPriceForSKU(final SKU sku, final int price) throws PricingServiceException {
        validatePrice(price);
        if (prices.containsKey(sku)) {
            throw new PricingServiceException(String.format("%s price exists already.", sku.name()));
        } else {
            prices.put(sku, price);
        }
    }

    @Override
    public void updatePriceForSKU(final SKU sku, final int price) throws PricingServiceException {
        validatePrice(price);
        if(prices.containsKey(sku)) {
            prices.put(sku, price);
        } else {
            throw new PricingServiceException(String.format("SKU %s does not have a price.", sku.name()));
        }
    }

    private void validatePrice(final int price) throws PricingServiceException {
        if (price <= 0) {
            throw new PricingServiceException(String.format("Price %d is not greater than zero.", price));
        }
    }
}
