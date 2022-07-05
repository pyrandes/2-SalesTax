package ct.products;

public class Product
{
    private final String id, name;
    private final ProductType type;
    private final boolean isImport;
    private float msrp;
    private int stockQty;

    public Product(String id, String name, ProductType type, boolean isImport, float msrp, int stockQty)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isImport = isImport;
        this.msrp = msrp;
        this.stockQty = stockQty;
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

    public boolean isImport() {
        return isImport;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public void setMsrp(float msrp) {
        this.msrp = msrp;
    }
}

