package org.checkoutkata.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Item class.
 */
public class Item {
    /** The SKU */
    private final SKU sku;
    // other private attributes

    /**
     * package Contructor. Use the {@link} ItemFactory to create new items
     * @param sku the sku
     */
    Item(SKU sku) {
        this.sku = sku;
    }

    /**
     * Get SKU
     * @return sku
     */
    public SKU getSku() {
        return sku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return new EqualsBuilder().append(sku, item.sku).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(sku).toHashCode();
    }
}
