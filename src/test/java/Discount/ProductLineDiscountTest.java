package Discount;

import Product.Product;
import Product.ProductLine;
import ShoppingCart.ShoppingCart;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductLineDiscountTest {

    private static final String LAPTOPS = "Laptops";
    private static final String MACBOOK = "Macbook";
    private static final String ALIENWARE = "Alienware";

    @Test
    void should_apply_absolute_discount_to_a_product_line() {
        //Given two products for a line product in a shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);
        ProductLine line = new ProductLine(LAPTOPS);
        Product macBook = new Product(MACBOOK, new BigDecimal(3500), line);
        Product alienware = new Product(ALIENWARE, new BigDecimal(2250), line);
        cart.addProduct(macBook);
        cart.addProduct(alienware);

        //Given a discount for a product line
        ProductLineDiscount productLineDiscount = new ProductLineDiscount(DiscountType.ABSOLUTE, new BigDecimal(100), LAPTOPS);

        //When the discount is applied
        productLineDiscount.apply(cart);

        //Then the new price for products in the line are the correct ones
        assertEquals(new BigDecimal(3400), macBook.getPrice());
        assertEquals(new BigDecimal(2150), alienware.getPrice());

        //And the cart total is updated
        assertEquals(new BigDecimal(5550).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());

    }

    @Test
    void should_apply_percentage_discount_to_a_product() {
        //Given two products for a line product in a shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);
        ProductLine line = new ProductLine(LAPTOPS);
        Product macBook = new Product(MACBOOK, new BigDecimal(3500), line);
        Product alienware = new Product(ALIENWARE, new BigDecimal(2250), line);
        cart.addProduct(macBook);
        cart.addProduct(alienware);

        //Given a discount for a product line
        ProductLineDiscount productLineDiscount = new ProductLineDiscount(DiscountType.PERCENTAGE, new BigDecimal(20), LAPTOPS);

        //When the discount is applied
        productLineDiscount.apply(cart);

        //Then the new price for products in the line are the correct ones
        assertEquals(new BigDecimal(2800), macBook.getPrice());
        assertEquals(new BigDecimal(1800), alienware.getPrice());

        //And the cart total is updated
        assertEquals(new BigDecimal(4600).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());

    }
}
