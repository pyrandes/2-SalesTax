package ct.dao.products;

import ct.products.Product;
import ct.products.ProductType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProductCSVDS implements ProductDS
{
    Map<String, Product> productMap;

    public ProductCSVDS(Path csvPath)
    {
        productMap = loadProductData(csvPath);
    }

    /**
     * Retrieves a current set of Product IDS
     * @return HashSet of product ids in the current backing map
     */
    @Override
    public Set<String> getProductIDs() {
        return new HashSet<>(productMap.keySet());
    }

    /**
     * Retrieves a Product for a given id
     * @param id id of a product in the current backing map
     * @return Product for a given id
     */
    @Override
    public Product getProduct(String id) {
        if (!productMap.containsKey(id)) return null;  // ideally would use an Optional here instead of a null
        return productMap.get(id);
    }

    /**
     * adds/restocks a product to the current backing map
     * @param product product to add or restock
     */
    @Override
    public void restockProduct(Product product) {
        if (productMap.containsKey(product.getId())) {
            Product existProd = productMap.get(product.getId());
            existProd.setStockQty(existProd.getStockQty() + product.getStockQty());
            return;
        }
        productMap.put(product.getId(), product);
    }

    /**
     * removes a product from the current backing map
     * @param prodID ID of product to remove
     */
    @Override
    public void removeProduct(String prodID) {
        if (!productMap.containsKey(prodID)) return;
        productMap.remove(prodID);
    }

    /**
     * loads product data from a backing .csv file
     * @param csvPath path to the backing CSV store
     * @return HashMap of ID to Product information
     */
    private Map<String, Product> loadProductData(Path csvPath)
    {
        Map<String, Product> prodMap = new HashMap<>();

        try(BufferedReader prodIn = Files.newBufferedReader(csvPath)) {
            String lineIn = "";
            while((lineIn = prodIn.readLine()) != null) {
                String[] prodCSV = lineIn.split(",");
                Product prod = new Product(
                        prodCSV[0],                             // id
                        prodCSV[1],                             // name
                        ProductType.getTypeForName(prodCSV[2]), // productType
                        Boolean.parseBoolean(prodCSV[3]),       // isImport
                        Float.parseFloat(prodCSV[4]),           // MSRP
                        Integer.parseInt(prodCSV[5]));          // init QTY

                prodMap.put(prod.getId(), prod);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return prodMap;
    }
}
