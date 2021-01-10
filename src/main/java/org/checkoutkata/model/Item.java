package org.checkoutkata.model;

public class Item {
    private final SKU sku;
    // other private attributes

    Item(SKU sku) {
        this.sku = sku;
    }

    public SKU getSku() {
        return sku;
    }
}
