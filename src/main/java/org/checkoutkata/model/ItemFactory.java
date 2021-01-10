package org.checkoutkata.model;

public class ItemFactory {
    public static Item makeItem(SKU sku) {
        return switch (sku) {
            case A -> new Item(SKU.A);
            case B -> new Item(SKU.B);
            case C -> new Item(SKU.C);
            case D -> new Item(SKU.D);
            default -> new Item(SKU.E);
        };
    }
}
