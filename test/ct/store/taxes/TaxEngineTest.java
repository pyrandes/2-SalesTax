package ct.store.taxes;

import static org.junit.Assert.assertEquals;

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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
        p = new Product("1", "tales of", ProductType.Book, false, new BigDecimal("12.49"));
        sc.addProduct(p, 1);
        p = new Product("2", "Prowler", ProductType.CD, false, new BigDecimal("14.99"));
        sc.addProduct(p, 1);
        p = new Product("3", "Godiva Dark Mint", ProductType.Food, false, new BigDecimal("0.85"));
        sc.addProduct(p, 3);

        User user = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        TaxEngine te = new TaxEngine(new FlatTaxDS());
        te.calculateTaxes(user, sc);

        for(CartItem item: sc.getItemsInCart()) {
            if (nonTaxedGoods.contains(item.getProduct().getType()))
                assertEquals(new BigDecimal("0.0"), item.getTax());
            else
                assertEquals(new BigDecimal("1.50"), item.getTax());
        }
    }

    @Test
    public void testFlatTax2()
    {
        ShoppingCart sc = new ShoppingCart();
        Product p;
        p = new Product("1", "imported chocolate", ProductType.Food, true, new BigDecimal("10.00"));
        sc.addProduct(p, 1);
        p = new Product("2", "imported perfume", ProductType.OTHER, true, new BigDecimal("47.50"));
        sc.addProduct(p, 1);

        User user = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));

        TaxEngine te = new TaxEngine(new FlatTaxDS());
        te.calculateTaxes(user, sc);

        for(CartItem item: sc.getItemsInCart()) {
            if (nonTaxedGoods.contains(item.getProduct().getType()))
                assertEquals(new BigDecimal("0.50").floatValue(), item.getTax().floatValue());
            else
                assertEquals(new BigDecimal("7.15").floatValue(), item.getTax().floatValue());
        }
    }
}
