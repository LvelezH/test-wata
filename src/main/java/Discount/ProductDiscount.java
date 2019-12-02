package Discount;

import ShoppingCart.ShoppingCart;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ProductDiscount implements Discount {
    @NonNull private DiscountType type;
    @NonNull private BigDecimal quantity;
    @NonNull private String productName;

    public void apply(ShoppingCart cart) {
        cart.getProducts()
        .stream()
        .filter(p -> p.getName().equals(productName))
        .forEach(productToDiscount -> productToDiscount.setPrice(getDiscountedAmount(productToDiscount.getPrice())));
    }

    private BigDecimal getDiscountedAmount(BigDecimal amount) {
        if (this.getType() == DiscountType.ABSOLUTE) {
            return amount.subtract(this.getQuantity());
        } else {
            return amount.subtract(percentage(amount, this.getQuantity()));
        }
    }

    private static BigDecimal percentage(BigDecimal base, BigDecimal pct){
        return base.multiply(pct).divide(new BigDecimal(100));
    }
}
