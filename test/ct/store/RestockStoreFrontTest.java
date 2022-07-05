package ct.store;

import ct.dao.products.ProductCSVDS;
import ct.dao.products.ProductDS;
import ct.dao.taxes.FlatTaxDS;
import ct.products.Product;
import ct.products.ProductType;
import ct.store.taxes.TaxEngine;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class RestockStoreFrontTest
{
    StoreFront sf;

    @Before
    public void setupStoreFront()
    {
        TaxEngine te = new TaxEngine(new FlatTaxDS());
        ProductDS prodDS = new ProductCSVDS(Paths.get("data", "product", "sample_products.csv"));
        sf = new StoreFront(prodDS, te);
    }

    @Test
    public void storeTestRestock()
    {
        Product prod = sf.getProductWithID("1");
        prod.setStockQty(30);
        sf.updateProductInformation(prod);
        assertEquals(40, sf.getProductWithID("1").getStockQty());

        prod = sf.getProductWithID("6");
        prod.setStockQty(-10);
        sf.updateProductInformation(prod);
        assertEquals(-5, sf.getProductWithID("6").getStockQty());
    }

    @Test
    public void removeProductsTest()
    {
        sf.removeProduct("5");
        assertNull(sf.getProductWithID("5"));

        sf.removeProduct("8");
        assertNull(sf.getProductWithID("8"));

        assertNotNull(sf.getProductWithID("1"));
    }

    @Test
    public void addProductsTest()
    {
        Product newProd = new Product("", "test add", ProductType.OTHER, false, 15.99f, 10);
        sf.updateProductInformation(newProd);

        Product prod = sf.getProductWithID("10");
        assertNotNull(prod);

        assertEquals("10", prod.getId());
        assertEquals("test add", prod.getName());
        assertEquals(ProductType.OTHER, prod.getType());
        assertEquals(false, prod.isImport());
        assertEquals(15.99f, prod.getMsrp());
        assertEquals(10, prod.getStockQty());
    }

    @Test
    public void changeProductsTest()
    {
        Product prod = sf.getProductWithID("2");
        prod.setMsrp(12.99f);
        prod.setStockQty(0);

        sf.updateProductInformation(prod);
        assertEquals(12.99f, sf.getProductWithID("2").getMsrp());
    }
}

