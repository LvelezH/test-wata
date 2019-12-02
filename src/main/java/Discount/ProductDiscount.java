package Discount;

import ShoppingCart.ShoppingCart;
import lombok.Data;
import lombok.NonNull;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

import static org.apache.log4j.helpers.UtilLoggingLevel.INFO;

@Data
public class ProductDiscount implements Discount {

    final static Logger logger = Logger.getLogger(ProductDiscount.class);

    @NonNull private DiscountType type;
    @NonNull private BigDecimal quantity;
    @NonNull private String productName;

    public void apply(ShoppingCart cart) {
        cart.getProducts()
        .stream()
        .filter(p -> p.getName().equals(productName))
        .forEach(productToDiscount -> {
            if(isValidDiscount(productToDiscount.getPrice())) {
                productToDiscount.setPrice(getDiscountedAmount(productToDiscount.getPrice()));
            } else {
                logger.log(INFO, "Product discount not valid and not applied -> " + this.toString());
            }
        });
    }

    private boolean isValidDiscount(@NonNull BigDecimal price) {
        if((this.type.equals(DiscountType.PERCENTAGE)
                && (this.quantity.compareTo(new BigDecimal(100)) == 1))) {
            return false;
        }

        if(this.type == DiscountType.ABSOLUTE && this.quantity.compareTo(price) == 1) {
            return false;
        }

        return true;
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
