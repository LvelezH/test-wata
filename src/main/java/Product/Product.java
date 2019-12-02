package Product;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class Product {
    @NonNull private String name;
    @NonNull private BigDecimal price;
    @NonNull private ProductLine line;
}
