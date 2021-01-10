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

/**
 * Default implementation of Checkout
 */
public class DefaultCheckout implements Checkout {

    /** Pricing Service */
    private final PricingService pricingService;

    /** Promotion service */
    private final PromotionService promotionService;

    /**
     * Constructor
     * @param pricingService the pricing service
     * @param promotionService the promotion service
     */
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

    /**
     * Get total cost of items backed by the sku
     * @param sku sku type
     * @param count number of sku units
     * @return total price of the items for the given sku type
     * @throws PricingServiceException if sku does not have a price
     * @throws PromotionException if promotion encounters errors while calculating the value
     */
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
