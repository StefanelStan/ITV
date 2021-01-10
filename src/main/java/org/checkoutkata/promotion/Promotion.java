package org.checkoutkata.promotion;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.checkoutkata.model.SKU;

public class Promotion {
    private final SKU sku;
    private final int minQuantity;
    private final int promotionPrice;

    public Promotion(SKU sku, int minQuantity, int promotionPrice) {
        this.sku = sku;
        this.minQuantity = minQuantity;
        this.promotionPrice = promotionPrice;
    }

    public SKU getSku() {
        return sku;
    }

    public int getTotalPriceForItem(SKU sku, int itemQuantity, int price) throws PromotionException {
        if (isPromotionApplicable(sku)) {
            return calculateTotalPrice(itemQuantity, price);
        } else {
            throw new PromotionException(String.format("This promotion is not applicable to item sku %s", sku.name()));
        }
    }

    private boolean isPromotionApplicable(SKU sku) {
        return this.sku == sku;
    }

    private int calculateTotalPrice(int quantity, int price) {
        return (quantity / minQuantity) * promotionPrice + (quantity % minQuantity) * price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Promotion other = (Promotion) o;

        return new EqualsBuilder()
                .append(minQuantity, other.minQuantity)
                .append(promotionPrice, other.promotionPrice)
                .append(sku, other.sku)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(sku).append(minQuantity).append(promotionPrice).toHashCode();
    }

}
