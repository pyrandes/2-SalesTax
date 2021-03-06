package ct.store;

import ct.dao.products.ProductCSVDS;
import ct.dao.products.ProductDS;
import ct.dao.taxes.FlatTaxDS;
import ct.store.taxes.TaxEngine;
import ct.users.User;
import ct.users.UserInfo;
import ct.users.UserType;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BasicStoreFrontTest
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
    public void storeTest1()
    {
        User customer = new User("1", UserType.CUSTOMER, new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

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
                    assertEquals(16.49f, item.getTotal());
                    break;
                case "3":
                    assertEquals(0.85f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(1.50f, rec.getTotalTax());
        assertEquals(29.83f, rec.getGrandTotal());
    }

    @Test
    public void storeTest2()
    {
        User customer = new User("1", UserType.CUSTOMER, new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        sf.addItemToShoppingCart(customer, "4", 1);
        sf.addItemToShoppingCart(customer, "5", 1);

        Receipt rec = sf.checkout(customer);

        for(CartItem item: rec.getItemList()) {
            switch(item.getProduct().getId()) {
                case "4":
                    assertEquals(10.50f, item.getTotal());
                    break;
                case "5":
                    assertEquals(54.65f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(7.65f, rec.getTotalTax());
        assertEquals(65.15f, rec.getGrandTotal());
    }

    @Test
    public void storeTest3()
    {
        User customer = new User("1", UserType.CUSTOMER, new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        sf.addItemToShoppingCart(customer, "6", 1);
        sf.addItemToShoppingCart(customer, "7", 1);
        sf.addItemToShoppingCart(customer, "8", 1);
        sf.addItemToShoppingCart(customer, "9", 1);

        Receipt rec = sf.checkout(customer);

        for(CartItem item: rec.getItemList()) {
            switch(item.getProduct().getId()) {
                case "6":
                    assertEquals(32.19f, item.getTotal());
                    break;
                case "7":
                    assertEquals(20.89f, item.getTotal());
                    break;
                case "8":
                    assertEquals(9.75f, item.getTotal());
                    break;
                case "9":
                    assertEquals(11.85f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(6.70f, rec.getTotalTax());
        assertEquals(74.68f, rec.getGrandTotal());
    }

    @Test
    public void storeTest4()
    {
        User customer = new User("1", UserType.CUSTOMER, new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        sf.addItemToShoppingCart(customer, "1", 2);
        sf.addItemToShoppingCart(customer, "3", 2);
        sf.addItemToShoppingCart(customer, "8", 1);
        sf.addItemToShoppingCart(customer, "8", 1);  // woops, this customer forgot they needed an extra bottle of pills

        Receipt rec = sf.checkout(customer);

        for(CartItem item: rec.getItemList()) {
            switch(item.getProduct().getId()) {
                case "1":
                    assertEquals(24.98f, item.getTotal());
                    break;
                case "3":
                    assertEquals(1.7f, item.getTotal());
                    break;
                case "8":
                    assertEquals(19.5f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(0f, rec.getTotalTax());
        assertEquals(46.18f, rec.getGrandTotal());
    }

    @Test
    public void storeTest5()
    {
        User customer = new User("1", UserType.CUSTOMER, new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        sf.addItemToShoppingCart(customer, "2", 3);
        sf.addItemToShoppingCart(customer, "5", 1);
        sf.addItemToShoppingCart(customer, "9", 1);
        sf.addItemToShoppingCart(customer, "9", 1);  // woops, this customer forgot they needed an extra belgian chocolate!

        Receipt rec = sf.checkout(customer);

        for(CartItem item: rec.getItemList()) {
            switch(item.getProduct().getId()) {
                case "2":
                    assertEquals(49.47f, item.getTotal());
                    break;
                case "5":
                    assertEquals(54.65f, item.getTotal());
                    break;
                case "9":
                    assertEquals(23.65f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(12.8f, rec.getTotalTax());
        assertEquals(127.77f, rec.getGrandTotal());
    }

    @Test
    public void storeTest6()
    {
        User customer = new User("1", UserType.CUSTOMER, new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        sf.addItemToShoppingCart(customer, "2", 3);
        sf.addItemToShoppingCart(customer, "5", 1);
        sf.addItemToShoppingCart(customer, "9", 1);
        sf.addItemToShoppingCart(customer, "9", -1);  // woops, this customer didnt mean to add, so they reduced the qty instead of removing it

        Receipt rec = sf.checkout(customer);

        for(CartItem item: rec.getItemList()) {
            switch(item.getProduct().getId()) {
                case "2":
                    assertEquals(49.47f, item.getTotal());
                    break;
                case "5":
                    assertEquals(54.65f, item.getTotal());
                    break;
                case "9":
                    assertEquals(0f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(11.65f, rec.getTotalTax());
        assertEquals(104.12f, rec.getGrandTotal());
    }
}
