package ct.products;

import java.math.BigDecimal;

public class Product
{
    private final String id, name;
    private final ProductType type;
    private final BigDecimal msrp;

    public Product(String id, String name, ProductType type, BigDecimal msrp)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.msrp = msrp;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public ProductType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
