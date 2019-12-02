package ShoppingCart;

import Discount.CustomerGroup;
import Discount.Discount;
import Discount.DiscountType;
import Discount.ShoppingCartDiscount;
import Product.Product;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ShoppingCart {

    private List<Product> products;
    @NonNull private CustomerGroup customerGroup;
    private ShoppingCartDiscount discount;
    private BigDecimal totalCost;
    private List<Discount> discounts;

    public ShoppingCart(CustomerGroup group) {
        this.products = new ArrayList<>();
        this.discounts = new ArrayList<Discount>();
        this.customerGroup = group;
        this.totalCost = BigDecimal.ZERO;
    }

    public void addProduct(Product product) {
        this.getProducts().add(product);
    }

    public void addDiscount(Discount discount) {
        this.discounts.add(discount);
    }

    public void applyDiscounts() {
        this.getDiscounts()
        .forEach(d -> d.apply(this));
    }

    public BigDecimal calculateTotals() {
        this.totalCost = this.getProducts()
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Optional.ofNullable(this.getDiscount())
                .ifPresent(discount -> this.totalCost = getDiscountedAmount(this.totalCost, discount));


        return totalCost.setScale(1, RoundingMode.HALF_UP);
    }

    private BigDecimal getDiscountedAmount(BigDecimal amount, ShoppingCartDiscount discount) {
        if (discount.getType() == DiscountType.ABSOLUTE) {
            return amount.subtract(discount.getQuantity());
        } else {
            return amount.subtract(percentage(amount, discount.getQuantity()));
        }
    }

    private static BigDecimal percentage(BigDecimal base, BigDecimal pct){
        return base.multiply(pct).divide(new BigDecimal(100));
    }
}
