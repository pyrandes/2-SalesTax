package ct.store;

import ct.dao.products.ProductCSVDS;
import ct.dao.products.ProductDS;
import ct.dao.taxes.FlatTaxDS;
import ct.dao.taxes.StateFreeTaxDS;
import ct.products.Product;
import ct.store.taxes.TaxEngine;
import ct.users.Customer;
import ct.users.User;
import ct.users.UserInfo;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StateTaxStoreFrontTest
{
    static StoreFront sf;
    static ProductDS prodDS;

    @BeforeClass
    public static void setupStoreFront()
    {
        TaxEngine te = new TaxEngine(new StateFreeTaxDS());
        prodDS = new ProductCSVDS(Paths.get("data", "product", "sample_products.csv"));
        sf = new StoreFront(prodDS, te);
    }

    @AfterClass
    public static void tearDownStoreFront()
    {
        prodDS.getProductIDs();
        for(String id: prodDS.getProductIDs()) {
            Product prod = prodDS.getProduct(id);
            switch(prod.getId()) {
                case "1":
                case "2":
                case "4":
                case "7":
                case "8":
                case "9":
                    assertEquals(8, prod.getStockQty());
                    break;
                case "3":
                    assertEquals(298, prod.getStockQty());
                    break;
                case "5":
                case "6":
                    assertEquals(3, prod.getStockQty());
                    break;
            }
        }
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
    public void storeTest1CA()
    {
        User customer = new Customer("4", new UserInfo("f", "", "l", "123 testing way", "emma" , "CA", "12345"));

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
    public void storeTest2MN()
    {
        User customer = new Customer("2", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

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
        User customer = new Customer("3", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

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

    @Test
    public void storeTest2CA()
    {
        User customer = new Customer("5", new UserInfo("f", "", "l", "123 testing way", "emma" , "CA", "12345"));

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
    public void storeTest3CA()
    {
        User customer = new Customer("6", new UserInfo("f", "", "l", "123 testing way", "emma" , "CA", "12345"));

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
                    assertEquals(11.80f, item.getTotal());
                    break;
                default:
                    fail("Invalid item added to the cart!");
            }
        }

        assertEquals(6.65f, rec.getTotalTax());
        assertEquals(74.63f, rec.getGrandTotal());
    }
}
