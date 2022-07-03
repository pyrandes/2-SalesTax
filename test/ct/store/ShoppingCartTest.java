package ct.store;

import static org.junit.Assert.assertEquals;

import ct.products.Product;
import ct.products.ProductType;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ShoppingCartTest
{
    ShoppingCart sc;

    @Before
    public void setupShoppingCart()
    {
        sc = new ShoppingCart();
    }

    @Test
    public void testWithNoTaxes()
    {
        Product p;

        p = new Product("1", "tales of", ProductType.Book, false, new BigDecimal("12.49"));
        sc.addProduct(p, 1);

        p = new Product("2", "Prowler", ProductType.CD, false, new BigDecimal("14.99"));
        sc.addProduct(p, 1);

        p = new Product("3", "Godiva Dark Mint", ProductType.Food, false, new BigDecimal("0.85"));
        sc.addProduct(p, 3);

        assertEquals(new BigDecimal("30.03"), sc.getGrandTotal());
    }
}
