package Discount;

import ShoppingCart.ShoppingCart;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartDiscountTest {

    @Test
    void should_not_apply_discount_if_customer_group_not_match() {
        //Given a shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.STANDARD);

        //And a shopping cart discount
        ShoppingCartDiscount discount = new ShoppingCartDiscount(DiscountType.ABSOLUTE, new BigDecimal(50), CustomerGroup.NEW);

        //When applying the discount to another group
        discount.apply(cart);

        //Then the discount is not applied
        assertFalse(Optional.ofNullable(cart.getDiscount()).isPresent());
    }

    @Test
    void should_apply_discount_if_customer_group_match() {
        //Given a shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);

        //And a shopping cart discount
        ShoppingCartDiscount discount = new ShoppingCartDiscount(DiscountType.ABSOLUTE, new BigDecimal(50), CustomerGroup.NEW);

        //When applying the discount to another group
        discount.apply(cart);

        //Then the discount is not applied
        assertTrue(Optional.ofNullable(cart.getDiscount()).isPresent());
    }
}
