package org.checkoutkata.promotion;

import org.checkoutkata.model.SKU;

import java.util.Set;

/**
 * Promotion service interface. Has the role of managing the registered promotions
 */
public interface PromotionService {
    /**
     * Add promotion
     * @param promotion the promotion
     * @throws PromotionException if promotion exists already
     */
    void addPromotion(Promotion promotion) throws PromotionException;

    /**
     * Check if any promotion exists for the given SKU
     * @param sku the SKU
     * @return true if exists, false otherwise
     */
    boolean hasPromotionForSKU(SKU sku);

    /**
     * Get the set of promotions for the given SKU. If there is no promotion, empty set is returned
     * @param sku the SKU
     * @return set of promotions for the given SKU. Empty set if no promotion is found.
     */
    Set<Promotion> getPromotionsForSKU(SKU sku);
}
