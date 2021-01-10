package org.checkoutkata.checkout;

import org.checkoutkata.model.Item;
import org.checkoutkata.model.SKU;
import org.checkoutkata.pricing.PricingService;
import org.checkoutkata.pricing.PricingServiceException;
import org.checkoutkata.promotion.Promotion;
import org.checkoutkata.promotion.PromotionException;
import org.checkoutkata.promotion.PromotionService;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class DefaultCheckout implements Checkout {

    private final PricingService pricingService;
    private final PromotionService promotionService;

    public DefaultCheckout(final PricingService pricingService, final PromotionService promotionService) {
        this.pricingService = pricingService;
        this.promotionService = promotionService;
    }

    @Override
    public int getTotalPrice(final List<Item> items) throws CheckoutException {
        Map<SKU, Integer> skuMap = new EnumMap<>(SKU.class);
        items.forEach(item -> skuMap.merge(item.getSku(), 1, Integer::sum));
        try {
            int total = 0;
            for (Map.Entry<SKU, Integer> entry : skuMap.entrySet()) {
                total += getTotalForSku(entry.getKey(), entry.getValue());
            }
            return total;
        } catch (final PricingServiceException | PromotionException e) {
            throw new CheckoutException("Unable to calculate checkout because " + e.getMessage());
        }
    }

    private int getTotalForSku(SKU sku, int count) throws PricingServiceException, PromotionException {
        final int itemPrice = pricingService.getPriceForSKU(sku);
        int totalPrice = itemPrice * count;
        if (promotionService.hasPromotionForSKU(sku)) {
            for(Promotion p : promotionService.getPromotionsForSKU(sku)) {
                totalPrice = Math.min(totalPrice, p.getTotalPriceForItem(sku, count, itemPrice));
            }
        }
        return totalPrice;
    }
}
