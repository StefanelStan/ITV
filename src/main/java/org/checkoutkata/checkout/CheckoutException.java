package org.checkoutkata.checkout;

/**
 * Exception for Checkout functionality
 */
public class CheckoutException extends Exception {

    public CheckoutException() {
    }

    public CheckoutException(String message) {
        super(message);
    }
}
