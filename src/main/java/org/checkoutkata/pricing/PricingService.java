package org.checkoutkata.pricing;

import org.checkoutkata.model.SKU;

import java.util.Map;

public interface PricingService {
    int getPriceForSKU(SKU sku) throws PricingServiceException;
    void addPriceForSKU(SKU sku, int price) throws PricingServiceException;
    void updatePriceForSKU(SKU sku, int price) throws PricingServiceException;
}
