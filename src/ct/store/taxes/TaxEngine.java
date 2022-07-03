package ct.store.taxes;

import ct.dao.taxes.TaxDS;
import ct.products.ProductType;
import ct.store.CartItem;
import ct.store.ShoppingCart;
import ct.users.User;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
            BigDecimal taxTotal = new BigDecimal("0.0");
            if (!nonTaxedGoods.contains(item.getProduct().getType())) {
                taxTotal = taxTotal.add(baseTotal.multiply(stateTax));
            }
            if (item.getProduct().isImport()) {
                taxTotal = taxTotal.add(baseTotal.multiply(importTax));
            }

            // taxTotal should be in increments of 0.05 after all taxes have been calculated

            // for rounding, use a base of 2 decimal digits (precision), adding 1 for each numeric digit
            int precision = 2 + Integer.toString(taxTotal.intValue()).length();
            taxTotal = taxTotal.round(new MathContext(precision, RoundingMode.HALF_UP));
            BigDecimal lastTaxDec = taxTotal.movePointRight(2).remainder(new BigDecimal(5)).round(new MathContext(0));

            if (!(lastTaxDec.intValue() == 0 || lastTaxDec.intValue() == 5)) {
                if (lastTaxDec.intValue() < 3) {
                    taxTotal = taxTotal.subtract(lastTaxDec);
                } else {
                    lastTaxDec = new BigDecimal(5).subtract(lastTaxDec).movePointLeft(2);
                    taxTotal = taxTotal.add(lastTaxDec);
                }
            }

            item.setTax(taxTotal);
        }
    }
}

