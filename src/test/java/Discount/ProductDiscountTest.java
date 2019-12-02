package Discount;

import Product.Product;
import Product.ProductLine;
import ShoppingCart.ShoppingCart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDiscountTest {

    private static final String MACBOOK = "Macbook";
    private static final String LAPTOPS = "Laptops";

    @Test
    void should_apply_absolute_discount_to_a_product() {
        //Given a product in a shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);
        Product product = new Product(MACBOOK, new BigDecimal(3500), new ProductLine(LAPTOPS));
        cart.addProduct(product);

        //Given a discount for a product
        ProductDiscount productDiscount = new ProductDiscount(DiscountType.ABSOLUTE, new BigDecimal(100), MACBOOK);

        //When the discount is applied
        productDiscount.apply(cart);

        //Then the new price is the correct one
        assertEquals(new BigDecimal(3400), product.getPrice());

        //And the cart total is updated
        assertEquals(new BigDecimal(3400).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());

    }

    @Test
    void should_apply_percentage_discount_to_a_product() {
        //Given a product in a shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);
        Product product = new Product(MACBOOK, new BigDecimal(3500), new ProductLine(LAPTOPS));
        cart.addProduct(product);

        //Given a discount for a product
        ProductDiscount productDiscount = new ProductDiscount(DiscountType.PERCENTAGE, new BigDecimal(50), MACBOOK);

        //When the discount is applied
        productDiscount.apply(cart);

        //Then the new price is the correct one
        assertEquals(new BigDecimal(1750), product.getPrice());

        //And the cart total is updated
        assertEquals(new BigDecimal(1750).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());

    }

    @ParameterizedTest
    @MethodSource("createParametersForInvalidDiscountsTest")
    void should_not_apply_invalid_discount_percentage(DiscountType type, BigDecimal discount) {
        //Given a product in a shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);
        Product product = new Product(MACBOOK, new BigDecimal(3500), new ProductLine(LAPTOPS));
        cart.addProduct(product);

        //Given an invalid discount for a product
        ProductDiscount productDiscount = new ProductDiscount(type, discount, MACBOOK);

        //When the discount is applied
        productDiscount.apply(cart);

        //Then the new price is the same one
        assertEquals(new BigDecimal(3500), product.getPrice());

        //And the cart total is not updated
        assertEquals(new BigDecimal(3500).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());
    }

    private static Stream<Arguments> createParametersForInvalidDiscountsTest() {
        return Stream.of(
                Arguments.of(DiscountType.PERCENTAGE, new BigDecimal(120)),
                Arguments.of(DiscountType.ABSOLUTE, new BigDecimal(4300)));
    }
}
