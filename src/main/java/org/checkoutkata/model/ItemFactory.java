package org.checkoutkata.model;

/**
 * Item Factory
 */
public class ItemFactory {
    /**
     * Make an Item based on the SKU type
     * @param sku the sku
     * @return an item based on sku type
     */
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
