package ct.store.taxes;

import ct.dao.taxes.TaxDS;
import ct.products.ProductType;
import ct.store.CartItem;
import ct.store.ShoppingCart;
import ct.users.User;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.Set;

public class TaxEngine
{
    private TaxDS taxDataStore;
    private Set<ProductType> nonTaxedGoods;

    public TaxEngine(TaxDS taxDataStore)
    {
        this.taxDataStore = taxDataStore;

        // TODO: temporary -- this should be loaded from a data store offline that can be loaded as needed
        nonTaxedGoods = new HashSet<>();
        nonTaxedGoods.add(ProductType.Book);
        nonTaxedGoods.add(ProductType.Food);
        nonTaxedGoods.add(ProductType.Medical);
    }

    public void calculateTaxes(User user, ShoppingCart cart)
    {
        BigDecimal stateTax = taxDataStore.getStateTaxRate(user.getUserInfo().getState());
        BigDecimal importTax = taxDataStore.getImportTaxRate();

        for(CartItem item: cart.getItemsInCart()) {
            BigDecimal baseTotal = new BigDecimal(item.getQty()).multiply(item.getProduct().getMsrp());
            BigDecimal taxTotal = new BigDecimal("0.0", new MathContext(2));
            if (!nonTaxedGoods.contains(item.getProduct().getType())) {
                taxTotal = taxTotal.add(baseTotal.multiply(stateTax).round(new MathContext(2)));
            }
            if (item.getProduct().isImport()) {
                taxTotal = taxTotal.add(baseTotal.multiply(importTax));
            }
            item.setTax(taxTotal);
        }
    }
}
