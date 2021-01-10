package org.checkoutkata.pricing;

import org.checkoutkata.model.SKU;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultPricingServiceTest {

    private final PricingService pricingService = new DefaultPricingService();

    @ParameterizedTest
    @MethodSource("getSkusAndPrices")
    void getPriceForSKU_allSKUHavePrices_returnsPriceForSelectedSKU(SKU sku, int price) throws Exception {
        assertEquals(price, pricingService.getPriceForSKU(sku));
    }

    private static Stream<Arguments> getSkusAndPrices() {
        return Stream.of(
                Arguments.of(SKU.A, 50),
                Arguments.of(SKU.B, 30),
                Arguments.of(SKU.C, 20),
                Arguments.of(SKU.D, 15)

        );
    }

    @Test
    void getPriceForSKU_SKUDoesNotHavePrice_throwException() {
        assertThrows(PricingServiceException.class, () -> pricingService.getPriceForSKU(SKU.E));
    }

    @Test
    void addPriceForSKU_SKUHasPriceAlready_throwException() throws Exception {
        assertThrows(PricingServiceException.class, () -> pricingService.addPriceForSKU(SKU.A, 10));
    }

    @Test
    void addPriceForSKU_SKUDoesNotHavePrice_addPriceAndReturnTrue() throws Exception {
        pricingService.addPriceForSKU(SKU.E, 10);
        assertEquals(10, pricingService.getPriceForSKU(SKU.E));
    }

    @Test
    void addPriceForSKU_invalidPrice_throwException() throws Exception {
        assertThrows(PricingServiceException.class, () -> pricingService.addPriceForSKU(SKU.E, -10));
    }

    @Test
    void updatePriceForSKU_SKUDoesNotExist_throwException() throws Exception {
        assertThrows(PricingServiceException.class, () -> pricingService.updatePriceForSKU(SKU.E, 10));
    }

    @Test
    void updatePriceForSKU_invalidPrice_throwException() throws Exception {
        assertThrows(PricingServiceException.class, () -> pricingService.updatePriceForSKU(SKU.A, -10));
    }

    @ParameterizedTest
    @MethodSource("getUpdatedSKUPrices")
    void updatePriceForSKU_SKUExist_updatePrice(SKU sku, int beforeUpdate, int afterUpdate) throws Exception {
        assertEquals(beforeUpdate, pricingService.getPriceForSKU(sku));
        pricingService.updatePriceForSKU(sku, afterUpdate);
        assertEquals(afterUpdate, pricingService.getPriceForSKU(sku));
    }

    private static Stream<Arguments> getUpdatedSKUPrices() {
        return Stream.of(
                Arguments.of(SKU.A, 50, 51),
                Arguments.of(SKU.B, 30, 31),
                Arguments.of(SKU.C, 20, 21),
                Arguments.of(SKU.D, 15, 16)
        );
    }
}