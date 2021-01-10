package org.checkoutkata.promotion;

import org.checkoutkata.model.SKU;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DefaultPromotionServiceTest {

    PromotionService promotionService = new DefaultPromotionService();

    @Test
    void addPromotion_promotionDoesNotExist_addIt() throws Exception {
        promotionService.addPromotion(new Promotion(SKU.A, 3, 130));
        assertEquals(1, promotionService.getPromotionsForSKU(SKU.A).size());
    }

    @Test
    void addPromotion_promotionExist_throwException() throws Exception {
        promotionService.addPromotion(new Promotion(SKU.A, 3, 130));
        assertThrows(PromotionException.class, () -> promotionService.addPromotion(new Promotion(SKU.A, 3, 130)));
    }

    @Test
    void hasPromotionForSKU_promotionsExistOrNot_returnTrueOrFalse() throws Exception {
        promotionService.addPromotion(new Promotion(SKU.A, 3, 130));
        assertTrue(promotionService.hasPromotionForSKU(SKU.A));
        assertFalse(promotionService.hasPromotionForSKU(SKU.B));
    }

    @Test
    void getPromotionsForSKU_promotionsDoNotExist_returnEmptySet() {
        assertTrue(promotionService.getPromotionsForSKU(SKU.A).isEmpty());
    }

    @Test
    void getPromotionsForSKU_promotionsExist_returnUnmodifiableSet() throws Exception {
        Promotion promotionA = new Promotion(SKU.A, 3, 130);
        Promotion promotionB = new Promotion(SKU.B, 2, 45);
        Promotion promotionC = new Promotion(SKU.C, 3, 15);
        promotionService.addPromotion(promotionA);
        promotionService.addPromotion(promotionB);

        Set<Promotion> promotionsA = promotionService.getPromotionsForSKU(SKU.A);
        assertEquals(1, promotionsA.size());
        assertEquals(promotionA, promotionsA.iterator().next());

        Set<Promotion> promotionsB = promotionService.getPromotionsForSKU(SKU.B);
        assertEquals(1, promotionService.getPromotionsForSKU(SKU.B).size());
        assertEquals(promotionB, promotionsB.iterator().next());

        assertThrows(UnsupportedOperationException.class, () -> promotionsA.add(promotionC));
    }
}