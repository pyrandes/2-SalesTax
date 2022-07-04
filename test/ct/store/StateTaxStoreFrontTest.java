package ct.store;

import ct.dao.products.ProductCSVDS;
import ct.dao.products.ProductDS;
import ct.dao.taxes.FlatTaxDS;
import ct.dao.taxes.StateFreeTaxDS;
import ct.store.taxes.TaxEngine;
import ct.users.Customer;
import ct.users.User;
import ct.users.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StateTaxStoreFrontTest
{
    StoreFront sf;

    @Before
    public void setupStoreFront()
    {
        TaxEngine te = new TaxEngine(new StateFreeTaxDS());
        ProductDS prodDS = new ProductCSVDS(Paths.get("data", "product", "sample_products.csv"));
        sf = new StoreFront(prodDS, te);
    }

    @Test
    public void storeTest1MN()
    {
        User customer = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        sf.addItemToShoppingCart(customer, "1", 1);
        sf.addItemToShoppingCart(customer, "2", 1);
        sf.addItemToShoppingCart(customer, "3", 1);

        Receipt rec = sf.checkout(customer);

        for(CartItem item: rec.getItemList()) {
            switch(item.getProduct().getId()) {
                case "1":
                    assertEquals(12.49f, item.getTotal());
                    break;
                case "2":
                    assertEquals(14.99f, item.getTotal());
                    break;
                case "3":
                    assertEquals(0.85f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(0.0f, rec.getTotalTax());
        assertEquals(28.33f, rec.getGrandTotal());
    }

    @Test
    public void storeTest2MN()
    {
        User customer = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        sf.addItemToShoppingCart(customer, "4", 1);
        sf.addItemToShoppingCart(customer, "5", 1);

        Receipt rec = sf.checkout(customer);

        for(CartItem item: rec.getItemList()) {
            switch(item.getProduct().getId()) {
                case "4":
                    assertEquals(10.50f, item.getTotal());
                    break;
                case "5":
                    assertEquals(49.90f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(2.90f, rec.getTotalTax());
        assertEquals(60.40f, rec.getGrandTotal());
    }

    @Test
    public void storeTest3MN()
    {
        User customer = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        sf.addItemToShoppingCart(customer, "6", 1);
        sf.addItemToShoppingCart(customer, "7", 1);
        sf.addItemToShoppingCart(customer, "8", 1);
        sf.addItemToShoppingCart(customer, "9", 1);

        Receipt rec = sf.checkout(customer);

        for(CartItem item: rec.getItemList()) {
            switch(item.getProduct().getId()) {
                case "6":
                    assertEquals(29.39f, item.getTotal());
                    break;
                case "7":
                    assertEquals(18.99f, item.getTotal());
                    break;
                case "8":
                    assertEquals(9.75f, item.getTotal());
                    break;
                case "9":
                    assertEquals(11.80f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(1.95f, rec.getTotalTax());
        assertEquals(69.93f, rec.getGrandTotal());
    }
}
