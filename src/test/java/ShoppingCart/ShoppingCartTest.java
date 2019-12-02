package ShoppingCart;

import Discount.CustomerGroup;
import Discount.DiscountType;
import Discount.ProductDiscount;
import Discount.ProductLineDiscount;
import Discount.ShoppingCartDiscount;
import Product.Product;
import Product.ProductLine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private static final String LAPTOPS = "Laptops";
    private static final String CONSOLES = "Consoles";
    private static final String MACBOOK = "Macbook";
    private static final String ALIENWARE = "Alienware";
    private static final String SWITCH = "Switch";
    private static final String PS4 = "PS4";
    private static final String XBOX = "XboX";

    @Test
    void should_have_empty_list_of_products_and_zero_cost_for_new_cart() {
        //Given a new shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);

        //Then new cart is empty
        assertTrue(cart.getProducts().isEmpty());

        //And total cost is zero
        assertEquals(BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());

    }

    @Test
    void should_add_products_to_cart_correctly() {
        //Given a new shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);

        //Given a product
        Product product = new Product(MACBOOK, new BigDecimal(3500), new ProductLine(LAPTOPS));
        cart.addProduct(product);

        //Then the product has been added correctly to the cart
        assertEquals(1, cart.getProducts().size());
        assertEquals(MACBOOK, cart.getProducts().get(0).getName());

    }

    @Test
    void should_update_total_cost_when_adding_a_product() {
        //Given a new shopping cart
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);

        //Given a product is added to the cart
        Product product = new Product(MACBOOK, new BigDecimal(3500), new ProductLine(LAPTOPS));
        cart.addProduct(product);

        //Then the total cost is updated
        assertEquals(1, cart.getProducts().size());
        assertEquals(new BigDecimal(3500).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());

    }

    @Test
    void total_correct_with_no_discounts() {
        //Given some products added to the shopping cart
        ShoppingCart cart = givenCartWithProducts();

        //Then the calculated total is correct
        assertEquals(new BigDecimal(6493).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());
    }

    @Test
    void total_correct_when_discount_for_product_added() {
        //Given some products added to the shopping cart
        ShoppingCart cart = givenCartWithProducts();

        //Given a discount in a product is added
        ProductDiscount productDiscount = new ProductDiscount(DiscountType.ABSOLUTE, new BigDecimal(100), SWITCH);
        cart.addDiscount(productDiscount);

        //When discounts are applied
        cart.applyDiscounts();

        //Then the total price is correct
        assertEquals(new BigDecimal(6393).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());

    }

    @Test
    void total_correct_when_discount_for_product_line__added() {
        //Given some products added to the shopping cart
        ShoppingCart cart = givenCartWithProducts();

        //Given a discount in a product is added
        ProductLineDiscount productLineDiscount = new ProductLineDiscount(DiscountType.PERCENTAGE, new BigDecimal(50), CONSOLES);
        cart.addDiscount(productLineDiscount);

        //When discounts are applied
        cart.applyDiscounts();

        //Then the total price is correct
        assertEquals(new BigDecimal(6121.5), cart.calculateTotals());

    }

    @Test
    void total_correct_when_discount_for_shopping_cart_added() {
        //Given some products added to the shopping cart
        ShoppingCart cart = givenCartWithProducts();

        //Given a shopping cart discount for the matching customer group
        ShoppingCartDiscount shoppingCartDiscount = new ShoppingCartDiscount(DiscountType.ABSOLUTE, new BigDecimal(500), CustomerGroup.NEW);
        cart.addDiscount(shoppingCartDiscount);

        //When discounts are applied
        cart.applyDiscounts();

        //Then the total price is correct
        assertEquals(new BigDecimal(5993).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals());
    }

    @Test
    void total_correct_when_multiple_discounts_added() {
        //Given some products added to the shopping cart
        ShoppingCart cart = givenCartWithProducts();

        //Given some product discounts are added
        ProductDiscount switchDiscount = new ProductDiscount(DiscountType.ABSOLUTE, new BigDecimal(100), SWITCH);
        ProductDiscount macbookDiscount = new ProductDiscount(DiscountType.PERCENTAGE, BigDecimal.TEN, PS4);

        cart.addDiscount(switchDiscount);
        cart.addDiscount(macbookDiscount);

        //Given a productLine discount is added
        ProductLineDiscount laptopsDiscount = new ProductLineDiscount(DiscountType.ABSOLUTE, new BigDecimal(20), LAPTOPS);

        cart.addDiscount(laptopsDiscount);

        //Given a shopping cart discount is added for the matching customer group
        ShoppingCartDiscount newClientDiscount = new ShoppingCartDiscount(DiscountType.ABSOLUTE, new BigDecimal(350), CustomerGroup.NEW);

        cart.addDiscount(newClientDiscount);

        //When discounts are applied
        cart.applyDiscounts();

        //Then the total price is correct
        assertEquals(new BigDecimal(5963.1).setScale(1, RoundingMode.HALF_UP), cart.calculateTotals().setScale(1, RoundingMode.HALF_UP));
    }

    private ShoppingCart givenCartWithProducts() {
        ShoppingCart cart = new ShoppingCart(CustomerGroup.NEW);

        ProductLine laptops = new ProductLine(LAPTOPS);
        ProductLine consoles = new ProductLine(CONSOLES);

        Product macBook = new Product(MACBOOK, new BigDecimal(3500), laptops);
        Product alienware = new Product(ALIENWARE, new BigDecimal(2250), laptops);

        Product nintendoSwitch = new Product(SWITCH, new BigDecimal(329), consoles);
        Product ps4 = new Product(PS4, new BigDecimal(399), consoles);
        Product xbox = new Product(XBOX, new BigDecimal(15), consoles);

        cart.addProduct(macBook);
        cart.addProduct(alienware);
        cart.addProduct(nintendoSwitch);
        cart.addProduct(ps4);
        cart.addProduct(xbox);

        return cart;
    }
}
