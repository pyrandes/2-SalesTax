package ct.products;

public enum ProductType
{
    Food,
    Book,
    Medical,
    CD,
    OTHER
    ;

    public static ProductType getTypeForName(String name) {
        for(ProductType val: values())
            if (val.name().equalsIgnoreCase(name)) return val;
        return OTHER;
    }
}
