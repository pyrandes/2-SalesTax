package ct;

import ct.dao.products.ProductCSVDS;
import ct.dao.products.ProductDS;
import ct.dao.taxes.FlatTaxDS;
import ct.dao.taxes.TaxDS;
import ct.store.StoreFront;
import ct.store.taxes.TaxEngine;
import ct.ui.CommandLineStore;
import ct.users.Customer;
import ct.users.User;
import ct.users.UserInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class Main
{
    private static StoreFront store;
    private static User customer;

    public static void main(String[] args)
    {
        init();
        CommandLineStore clstore = new CommandLineStore(store, customer);
        clstore.enterStore();
    }



    private static void init()
    {
        ProductDS prodDS = new ProductCSVDS(Paths.get("data","product", "sample_products.csv"));
        TaxDS taxds = new FlatTaxDS();
        TaxEngine te = new TaxEngine(taxds);
        store = new StoreFront(prodDS, te);

        customer = new Customer("1", new UserInfo("f", "", "l", "123 testing way", "emma" , "MN", "12345"));
    }
}
