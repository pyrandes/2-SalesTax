package ct.dao;

import ct.products.Product;

import java.util.Set;

public interface ProductDS
{
    public Set<String> getProductIDs();
    public Product getProduct(String id);
}
