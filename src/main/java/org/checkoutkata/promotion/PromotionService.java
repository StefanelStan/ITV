package org.checkoutkata.promotion;

import org.checkoutkata.model.SKU;

import java.util.Set;

public interface PromotionService {
    void addPromotion(Promotion promotion) throws PromotionException;
    boolean hasPromotionForSKU(SKU sku);
    Set<Promotion> getPromotionsForSKU(SKU sku);
}
