package Discount;

import Product.Product;
import Product.ProductLine;
import ShoppingCart.ShoppingCart;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
}
