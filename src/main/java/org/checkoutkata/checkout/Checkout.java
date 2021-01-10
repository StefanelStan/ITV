package org.checkoutkata.checkout;

import org.checkoutkata.model.Item;

import java.util.List;

public interface Checkout {
    int getTotalPrice(List<Item> items) throws CheckoutException;
}
