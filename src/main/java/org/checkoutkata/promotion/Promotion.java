package org.checkoutkata.promotion;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.checkoutkata.model.SKU;

/**
 * Promotion class
 */
public class Promotion {
    /** SKU */
    private final SKU sku;

    /** Min quantity for which this promotion applies */
    private final int minQuantity;

    /** Promotion price of the given min quantity */
    private final int promotionPrice;

    /**
     * Constructor
     * @param sku SKU
     * @param minQuantity min quantity
     * @param promotionPrice promotion price of minimum quantity
     */
    public Promotion(SKU sku, int minQuantity, int promotionPrice) {
        this.sku = sku;
        this.minQuantity = minQuantity;
        this.promotionPrice = promotionPrice;
    }

    /**
     * Get the SKU of this promotion
     * @return the SKU
     */
    public SKU getSku() {
        return sku;
    }

    /**
     * Get total price for the given number of items
     * @param sku the sku type of item
     * @param itemQuantity quantity of items
     * @param price price
     * @return the total price
     * @throws PromotionException if this promotion is not suitable for the given SKU type
     */
    public int getTotalPriceForItem(SKU sku, int itemQuantity, int price) throws PromotionException {
        if (isPromotionApplicable(sku)) {
            return calculateTotalPrice(itemQuantity, price);
        } else {
            throw new PromotionException(String.format("This promotion is not applicable to item sku %s", sku.name()));
        }
    }

    /**
     * Can this promotion be applicable to the sku type
     * @param sku the SKU
     * @return true if promotion is suitable for this sku
     */
    private boolean isPromotionApplicable(SKU sku) {
        return this.sku == sku;
    }

    /**
     * Calculate total price of quantity and price
     * @param quantity the quantity
     * @param price the default price per unit
     * @return total price of quantity, price and any applicable promotion
     */
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
