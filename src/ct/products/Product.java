package ct.products;

public class Product
{
    private final String id, name;
    private final ProductType type;
    private final float msrp;

    public Product(String id, String name, ProductType type, float msrp)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.msrp = msrp;
    }

    public float getMsrp() {
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
