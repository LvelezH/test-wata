package Discount;

import ShoppingCart.ShoppingCart;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ShoppingCartDiscount implements Discount {
    @NonNull private DiscountType type;
    @NonNull private BigDecimal quantity;
    @NonNull private CustomerGroup customerGroup;

    @Override
    public void apply(ShoppingCart cart) {
        //Applies the discount if the customer belongs to the group
        if (customerGroup.equals(cart.getCustomerGroup())) {
            cart.setDiscount(new ShoppingCartDiscount(this.type, this.quantity, customerGroup));
        }
    }
}
