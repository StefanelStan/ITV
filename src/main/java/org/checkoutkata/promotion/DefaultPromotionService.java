package org.checkoutkata.promotion;

import org.checkoutkata.model.SKU;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultPromotionService implements PromotionService {
    private final Map<SKU, Set<Promotion>> promotions = new EnumMap<>(SKU.class);

    @Override
    public void addPromotion(Promotion promotion) throws PromotionException {
        Set<Promotion> skuPromotions = new HashSet<>(getPromotionsForSKU(promotion.getSku()));

        if (skuPromotions.contains(promotion)) {
            throw new PromotionException("Promotion already exists");
        } else {
            skuPromotions.add(promotion);
            promotions.put(promotion.getSku(), Collections.unmodifiableSet(skuPromotions));
        }
    }

    @Override
    public boolean hasPromotionForSKU(SKU sku) {
        return promotions.containsKey(sku);
    }

    @Override
    public Set<Promotion> getPromotionsForSKU(SKU sku) {
        return promotions.getOrDefault(sku, new HashSet<>());
    }


}
