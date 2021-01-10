package org.checkoutkata.checkout;

import org.checkoutkata.model.Item;
import org.checkoutkata.model.ItemFactory;
import org.checkoutkata.model.SKU;
import org.checkoutkata.pricing.PricingService;
import org.checkoutkata.pricing.PricingServiceException;
import org.checkoutkata.promotion.Promotion;
import org.checkoutkata.promotion.PromotionService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultCheckoutTest {

    private final PricingService pricingService = mock(PricingService.class);
    private final PromotionService promotionService = mock(PromotionService.class);
    private final Promotion promotion1 = mock(Promotion.class);
    private final Promotion promotion2 = mock(Promotion.class);

    private final Checkout checkout = new DefaultCheckout(pricingService, promotionService);

    @Test
    void getTotalPrice_emptyListOfItems_returnZero() throws CheckoutException {
        assertEquals(0, checkout.getTotalPrice(Collections.emptyList()));
    }

    @Test
    void getTotalPrice_pricingServiceThrowsException_throwException() throws Exception {
        when(pricingService.getPriceForSKU(SKU.A)).thenThrow(new PricingServiceException());

        assertThrows(CheckoutException.class, () -> checkout.getTotalPrice(List.of(ItemFactory.makeItem(SKU.A))));

        verify(pricingService, times(1)).getPriceForSKU(SKU.A);
    }

    @Test
    void getTotalPrice_noPromotionsExist_returnCorrectPrice() throws Exception {
        List<Item> items = List.of(ItemFactory.makeItem(SKU.A), ItemFactory.makeItem(SKU.B), ItemFactory.makeItem(SKU.A));

        when(pricingService.getPriceForSKU(SKU.A)).thenReturn(50);
        when(pricingService.getPriceForSKU(SKU.B)).thenReturn(30);
        when(promotionService.hasPromotionForSKU(SKU.A)).thenReturn(false);
        when(promotionService.hasPromotionForSKU(SKU.B)).thenReturn(false);

        assertEquals(130, checkout.getTotalPrice(items));

        verify(pricingService, times(1)).getPriceForSKU(SKU.A);
        verify(pricingService, times(1)).getPriceForSKU(SKU.B);
        verify(promotionService, times(1)).hasPromotionForSKU(SKU.A);
        verify(promotionService, times(1)).hasPromotionForSKU(SKU.B);
    }

    /**
     *  Complex case: 2 promotions: 3xA for 130, 2xB for 45; Basket has [5A,4B,1C]
     *  So in case of B promotion should apply twice
     */
    @Test
    void getTotalPrice_multiplePromotionsExist_returnCorrectPrice() throws Exception {
        List<Item> items = List.of(ItemFactory.makeItem(SKU.A), ItemFactory.makeItem(SKU.B), ItemFactory.makeItem(SKU.A),
                ItemFactory.makeItem(SKU.A),ItemFactory.makeItem(SKU.A), ItemFactory.makeItem(SKU.C), ItemFactory.makeItem(SKU.A),
                ItemFactory.makeItem(SKU.B), ItemFactory.makeItem(SKU.B));

        when(pricingService.getPriceForSKU(SKU.A)).thenReturn(50);
        when(pricingService.getPriceForSKU(SKU.B)).thenReturn(30);
        when(pricingService.getPriceForSKU(SKU.C)).thenReturn(20);

        when(promotionService.hasPromotionForSKU(SKU.A)).thenReturn(true);
        when(promotionService.getPromotionsForSKU(SKU.A)).thenReturn(Collections.singleton(promotion1));
        when(promotion1.getTotalPriceForItem(SKU.A, 5, 50)).thenReturn(230);

        when(promotionService.hasPromotionForSKU(SKU.B)).thenReturn(true);
        when(promotionService.getPromotionsForSKU(SKU.B)).thenReturn(Collections.singleton(promotion2));
        when(promotion2.getTotalPriceForItem(SKU.B, 3, 30)).thenReturn(75);

        when(promotionService.hasPromotionForSKU(SKU.C)).thenReturn(false);

        assertEquals(325, checkout.getTotalPrice(items));

        verify(pricingService, times(3)).getPriceForSKU(any(SKU.class));
        verify(promotionService, times(3)).hasPromotionForSKU(any(SKU.class));
        verify(promotionService, times(2)).getPromotionsForSKU(any(SKU.class));
        verify(promotion1, times(1)).getTotalPriceForItem(SKU.A, 5, 50);
        verify(promotion2, times(1)).getTotalPriceForItem(SKU.B, 3, 30);
    }
}