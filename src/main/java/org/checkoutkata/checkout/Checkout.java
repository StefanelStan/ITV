package org.checkoutkata.checkout;

import org.checkoutkata.model.Item;

import java.util.List;

/**
 * Checkout interface
 */
public interface Checkout {
    /**
     * Get total price for the list of items
     * @param items the item
     * @return
     * @throws CheckoutException
     */
    int getTotalPrice(List<Item> items) throws CheckoutException;
}
