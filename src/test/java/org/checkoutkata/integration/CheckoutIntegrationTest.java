package org.checkoutkata.integration;

import org.checkoutkata.checkout.Checkout;
import org.checkoutkata.checkout.CheckoutException;
import org.checkoutkata.checkout.DefaultCheckout;
import org.checkoutkata.model.Item;
import org.checkoutkata.model.ItemFactory;
import org.checkoutkata.model.SKU;
import org.checkoutkata.pricing.DefaultPricingService;
import org.checkoutkata.pricing.PricingService;
import org.checkoutkata.promotion.DefaultPromotionService;
import org.checkoutkata.promotion.Promotion;
import org.checkoutkata.promotion.PromotionService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutIntegrationTest {
    private final PricingService pricingService = new DefaultPricingService();
    private final PromotionService promotionService = new DefaultPromotionService();
    private final Checkout checkout = new DefaultCheckout(pricingService, promotionService);
    Promotion promotionA = new Promotion(SKU.A, 3, 130);
    Promotion promotionB = new Promotion(SKU.B, 2, 45);

    @Test
    void checkout_emptyList_returnZero() throws Exception {
        assertEquals(0, checkout.getTotalPrice(Collections.emptyList()));
    }

    @Test
    void checkout_noPromotions_returnDefaultTotal() throws Exception {
        List<Item> items= List.of(ItemFactory.makeItem(SKU.A), ItemFactory.makeItem(SKU.B), ItemFactory.makeItem(SKU.C));
        assertEquals(100, checkout.getTotalPrice(items));
    }

    @Test
    void checkout_oneItemDoesNotHavePrice_throwException() {
        List<Item> items = List.of(ItemFactory.makeItem(SKU.A), ItemFactory.makeItem(SKU.E));
        assertThrows(CheckoutException.class, () -> checkout.getTotalPrice(items));
    }

    /**
     * Basket has [5A,3B,1C] and the default (PDF) promotion is applied
     * [5A] => 3 for 130 and 2 x 50
     * [3B] => 2 for 45 and 30
     */
    @Test
    void checkout_itemsHavePromotions_returnCorrectTotal() throws Exception {
        List<Item> items = getListOfItems(SKU.A, 5);
        items.addAll(getListOfItems(SKU.B, 3));
        items.addAll(getListOfItems(SKU.C, 1));

        promotionService.addPromotion(promotionA);
        promotionService.addPromotion(promotionB);
        assertEquals(325, checkout.getTotalPrice(items));
    }

    /**
     * Basket has [5A,3B,1C] and the default (PDF) promotion is applied
     * [5A] => 3 for 130 and 2 x 50
     * [4B] => 2 for 45 twice
     */
    @Test
    void checkout_itemsHavePromotionsTwo_returnCorrectTotal() throws Exception {
        List<Item> items = getListOfItems(SKU.A, 5);
        items.addAll(getListOfItems(SKU.B, 4));
        items.addAll(getListOfItems(SKU.C, 1));

        promotionService.addPromotion(promotionA);
        promotionService.addPromotion(promotionB);

        assertEquals(340, checkout.getTotalPrice(items));
    }

    /**
     * Basket has [5A,3B,1C] and the default (PDF) promotion is applied
     * [5A] => [3 for 130 + 2 x 50] vs [4 for 150 + 50]
     * [3B] => [2 for 45] + 30
     */
    @Test
    void checkout_itemsHaveMultiplePromotions_returnCorrectTotal() throws Exception {
        List<Item> items = getListOfItems(SKU.A, 5);
        items.addAll(getListOfItems(SKU.B, 3));
        items.addAll(getListOfItems(SKU.C, 1));

        promotionService.addPromotion(promotionA);
        promotionService.addPromotion(promotionB);
        promotionService.addPromotion(new Promotion(SKU.A, 4, 150));

        assertEquals(295, checkout.getTotalPrice(items));
    }

    /**
     * Basket has [5A,3B,1C] and the default (PDF) promotion is applied
     * [5A] => [3 for 130 + 2 x 50] vs [4 for 150 + 50]
     * [3B] => [2 for 45] + 30
     */
    @Test
    void checkout_pricesChangeLive_returnUpdatedCorrectTotal() throws Exception {
        List<Item> items = getListOfItems(SKU.A, 5);
        items.addAll(getListOfItems(SKU.B, 3));
        items.addAll(getListOfItems(SKU.C, 1));

        promotionService.addPromotion(promotionA);
        promotionService.addPromotion(promotionB);
        assertEquals(325, checkout.getTotalPrice(items));

        pricingService.updatePriceForSKU(SKU.B, 25);
        assertEquals(320, checkout.getTotalPrice(items));
    }

    // No promotions for SKU A
    @Test
    void checkout_promotionsChangeLive_returnUpdatedCorrectTotal() throws Exception {
        List<Item> items = getListOfItems(SKU.A, 5);
        items.addAll(getListOfItems(SKU.B, 3));
        items.addAll(getListOfItems(SKU.C, 1));

        promotionService.addPromotion(promotionB);
        assertEquals(345, checkout.getTotalPrice(items));

        promotionService.addPromotion(promotionA);
        assertEquals(325, checkout.getTotalPrice(items));
    }

    private List<Item> getListOfItems(SKU sku, int count) {
        List<Item> items = new LinkedList<>();
        for(int i = 0; i < count; i++) {
            items.add(ItemFactory.makeItem(sku));
        }
        return items;
    }
}
