package ct.dao.products;

import ct.products.Product;

import java.util.Set;

public interface ProductDS
{
    Set<String> getProductIDs();
    Product getProduct(String id);
    void updateProductInformation(Product product);
    void removeProduct(String prodID);
}
