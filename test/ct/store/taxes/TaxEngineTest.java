package ct.store.taxes;

import ct.dao.taxes.FlatTaxDS;
import ct.products.Product;
import ct.products.ProductType;
import ct.store.CartItem;
import ct.store.ShoppingCart;
import ct.users.Customer;
import ct.users.User;
import ct.users.UserInfo;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TaxEngineTest
{
    private static Set<ProductType> nonTaxedGoods;

    @BeforeClass
    public static void classInit()
    {
        nonTaxedGoods = new HashSet<>();
        nonTaxedGoods.add(ProductType.Book);
        nonTaxedGoods.add(ProductType.Food);
        nonTaxedGoods.add(ProductType.Medical);
    }

    @Test
    public void testFlatTax1()
    {
        ShoppingCart sc = new ShoppingCart();
        Product p;
        p = new Product("1", "tales of", ProductType.Book, false, 12.49f);
        sc.addProduct(p, 1);
        p = new Product("2", "Prowler", ProductType.CD, false, 14.99f);
        sc.addProduct(p, 1);
        p = new Product("3", "Godiva Dark Mint", ProductType.Food, false,0.85f);
        sc.addProduct(p, 3);

        User user = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        TaxEngine te = new TaxEngine(new FlatTaxDS());
        te.calculateTaxes(user, sc);

        for(CartItem item: sc.getItemsInCart()) {
            if (nonTaxedGoods.contains(item.getProduct().getType()))
                assertEquals(0.0f, item.getTax());
            else
                assertEquals(1.50f, item.getTax());
        }
    }

    @Test
    public void testImportFlatTax()
    {
        ShoppingCart sc = new ShoppingCart();
        Product p;
        p = new Product("1", "imported chocolate", ProductType.Food, true, 10.00f);
        sc.addProduct(p, 1);
        p = new Product("2", "imported perfume", ProductType.OTHER, true, 47.50f);
        sc.addProduct(p, 1);

        User user = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        TaxEngine te = new TaxEngine(new FlatTaxDS());
        te.calculateTaxes(user, sc);

        for(CartItem item: sc.getItemsInCart()) {
            if (nonTaxedGoods.contains(item.getProduct().getType()))
                assertEquals(0.50f, item.getTax());
            else
                assertEquals(7.15f, item.getTax());
        }
    }

    @Test
    public void testImportFlatTax2()
    {
        ShoppingCart sc = new ShoppingCart();
        Product p;
        p = new Product("1", "imported chocolate", ProductType.Food, true, 10.00f);
        sc.addProduct(p, 1);
        p = new Product("2", "imported perfume", ProductType.OTHER, true, 47.50f);
        sc.addProduct(p, 10);

        User user = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        TaxEngine te = new TaxEngine(new FlatTaxDS());
        te.calculateTaxes(user, sc);

        for(CartItem item: sc.getItemsInCart()) {
            if (nonTaxedGoods.contains(item.getProduct().getType()))
                assertEquals(0.50f, item.getTax());
            else
                assertEquals(71.25f, item.getTax());
        }
    }

    @Test
    public void testImportFlatTaxHighQtyBought()
    {
        ShoppingCart sc = new ShoppingCart();
        Product p;
        p = new Product("1", "imported chocolate", ProductType.Food, true, 10.00f);
        sc.addProduct(p, 1);
        p = new Product("2", "imported perfume", ProductType.OTHER, true, 47.50f);
        sc.addProduct(p, 1000);

        User user = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        TaxEngine te = new TaxEngine(new FlatTaxDS());
        te.calculateTaxes(user, sc);

        for(CartItem item: sc.getItemsInCart()) {
            if (nonTaxedGoods.contains(item.getProduct().getType()))
                assertEquals(0.50f, item.getTax());
            else
                assertEquals(7125f, item.getTax());
        }
    }

    @Test
    public void testShoppingCartMix()
    {
        ShoppingCart sc = new ShoppingCart();
        Product p;
        p = new Product("1", "imported chocolate", ProductType.Food, true, 11.25f);
        sc.addProduct(p, 1);
        p = new Product("2", "imported perfume", ProductType.OTHER, true, 27.99f);
        sc.addProduct(p, 1);
        p = new Product("3", "domestic perfume", ProductType.OTHER, false, 18.99f);
        sc.addProduct(p, 1);
        p = new Product("4", "headache pills", ProductType.Medical, false, 9.75f);
        sc.addProduct(p, 1);

        User user = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        TaxEngine te = new TaxEngine(new FlatTaxDS());
        te.calculateTaxes(user, sc);

        for(CartItem item: sc.getItemsInCart()) {
            switch(item.getProduct().getId()) {
                case "1":
                    assertEquals(0.55f, item.getTax());
                    break;
                case "2":
                    assertEquals(4.20f, item.getTax());
                    break;
                case "3":
                    assertEquals(1.90f, item.getTax());
                    break;
                case "4":
                    assertEquals(0f, item.getTax());
                    break;
            }
        }
    }
}
