package Discount;

import ShoppingCart.ShoppingCart;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ProductLineDiscount implements Discount {
    @NonNull private DiscountType type;
    @NonNull private BigDecimal quantity;
    @NonNull private String lineName;

    public void apply(ShoppingCart cart) {
         cart.getProducts()
                .stream()
                .filter(product -> product.getLine().getName().equals(lineName))
                .forEach(p -> {
                    ProductDiscount productDiscount = new ProductDiscount(this.type, this.quantity, p.getName());
                    productDiscount.apply(cart);
                });
    }
}
