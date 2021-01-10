package org.checkoutkata.promotion;

import org.checkoutkata.model.SKU;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PromotionTest {

    @Test
    void getTotalPriceForItem_promotionIsNotApplicable_throwException() throws Exception {
        Promotion promotion = new Promotion(SKU.A, 3, 130);
        assertThrows(PromotionException.class, () -> promotion.getTotalPriceForItem(SKU.B, 3, 30));
    }

    @Test
    void getTotalPriceForItem_promotionIsApplicable_returnCorrectTotal() throws Exception {
        Promotion promotion = new Promotion(SKU.A, 3, 130);
        assertEquals(180, promotion.getTotalPriceForItem(SKU.A, 4, 50));
    }

    @Test
    void equalsAndHashCode_isCalculatedCorrectly() {
        Promotion promotionOne = new Promotion(SKU.A, 3, 130);
        Promotion promotionTwo = new Promotion(SKU.A, 3, 130);
        Promotion promotionThree = new Promotion(SKU.A, 4, 150);

        assertEquals(promotionOne, promotionTwo);
        assertEquals(promotionOne.hashCode(), promotionTwo.hashCode());
        assertNotEquals(promotionOne, promotionThree);
        assertNotEquals(promotionOne.hashCode(), promotionThree.hashCode());
    }

}