package ct;

import ct.dao.products.ProductCSVDS;
import ct.dao.products.ProductDS;
import ct.dao.taxes.StateFreeTaxDS;
import ct.dao.taxes.TaxDS;
import ct.store.StoreFront;
import ct.store.taxes.TaxEngine;
import ct.ui.CommandLineStore;

import java.nio.file.Paths;

public class Main
{
    private static StoreFront store;

    public static void main(String[] args)
    {
        init();
        CommandLineStore clstore = new CommandLineStore(store);
        clstore.enterStore();
    }



    private static void init()
    {
        ProductDS prodDS = new ProductCSVDS(Paths.get("data","product", "sample_products.csv"));
        TaxDS taxds = new StateFreeTaxDS();
        TaxEngine te = new TaxEngine(taxds);
        store = new StoreFront(prodDS, te);


    }
}
