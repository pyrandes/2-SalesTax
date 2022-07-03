package ct.products;

import java.math.BigDecimal;

public class Product
{
    private final String id, name;
    private final ProductType type;
    private final boolean isImport;
    private final BigDecimal msrp;

    public Product(String id, String name, ProductType type, boolean isImport, BigDecimal msrp)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isImport = isImport;
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

    public boolean isImport() {
        return isImport;
    }
}
