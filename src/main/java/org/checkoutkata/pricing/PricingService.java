package org.checkoutkata.pricing;

import org.checkoutkata.model.SKU;

/**
 * Interface describing pricing service. It is responsible with maintaining the prices of each SKU
 */
public interface PricingService {
    /**
     * Get price for SKU
     * @param sku the SKU
     * @return price of SKU
     * @throws PricingServiceException if SKU does not exist
     */
    int getPriceForSKU(SKU sku) throws PricingServiceException;

    /**
     * Add price for the given SKU
     * @param sku the SKU
     * @param price price
     * @throws PricingServiceException if the SKU already exists
     */
    void addPriceForSKU(SKU sku, int price) throws PricingServiceException;

    /**
     * Update price for SKU
     * @param sku the sku to update price
     * @param price the price
     * @throws PricingServiceException if SKU does not exist or price is lesser than 0
     */
    void updatePriceForSKU(SKU sku, int price) throws PricingServiceException;
}
