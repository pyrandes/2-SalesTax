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
    private final TaxDS taxDataStore;
    private final Set<ProductType> nonTaxedGoods;

    /**
     * constructor that will accept a TaxDS as a data store/data access object
     * @param taxDataStore a DataStore to retrieve tax rates from
     */
    public TaxEngine(TaxDS taxDataStore)
    {
        this.taxDataStore = taxDataStore;

        // TODO: temporary -- this should be loaded from a data store offline that can be loaded as needed
        nonTaxedGoods = new HashSet<>();
        nonTaxedGoods.add(ProductType.Book);
        nonTaxedGoods.add(ProductType.Food);
        nonTaxedGoods.add(ProductType.Medical);
    }

    /**
     * calculates the total tax owed in the supplied shopping cart for a particular user
     *  ** this will update the tax for each @CartItem in the shopping cart **
     * @param user a User to utilize when calculating taxes
     * @param cart a ShoppingCart to calculate new taxes on
     */
    public void calculateTaxes(User user, ShoppingCart cart)
    {
        BigDecimal stateTax = new BigDecimal(taxDataStore.getStateTaxRate(user.getUserInfo().getState()));
        BigDecimal importTax = new BigDecimal(taxDataStore.getImportTaxRate());

        for(CartItem item: cart.getItemsInCart()) {
            BigDecimal baseTotal = new BigDecimal(item.getQty()).multiply(new BigDecimal(item.getProduct().getMsrp()));
            BigDecimal taxTotal = new BigDecimal("0.0");
            if (!nonTaxedGoods.contains(item.getProduct().getType())) {
                taxTotal = taxTotal.add(baseTotal.multiply(stateTax));
            }
            if (item.getProduct().isImport()) {
                taxTotal = taxTotal.add(baseTotal.multiply(importTax));
            }
            taxTotal = roundCalculatedTaxTotal(taxTotal);

            item.setTax(taxTotal.floatValue());
        }
    }

    /**
     * rounds the calculated tax owed to the nearest $0.05
     * @param taxTotal a supplied BigDecimal to round
     * @return a BigDecimal of the rounded tax total supplied to the nearest nickle
     */
    private BigDecimal roundCalculatedTaxTotal(BigDecimal taxTotal) {
        int precision = 2 ;
        if (!(taxTotal.floatValue() < 1))
            precision += Integer.toString(taxTotal.intValue()).length();
        taxTotal = taxTotal.round(new MathContext(precision, RoundingMode.HALF_UP));
        BigDecimal lastTaxDec = taxTotal.movePointRight(2);
        lastTaxDec = lastTaxDec.remainder(new BigDecimal(5));
        lastTaxDec = lastTaxDec.round(new MathContext(0));

        if (!(lastTaxDec.intValue() == 0 || lastTaxDec.intValue() == 5)) {
//            if (lastTaxDec.intValue() < 3) {
//                lastTaxDec = new BigDecimal(0).subtract(lastTaxDec).movePointLeft(2);
//            } else {
                lastTaxDec = new BigDecimal(5).subtract(lastTaxDec).movePointLeft(2);
//            }
            taxTotal = taxTotal.add(lastTaxDec);
        }
        return taxTotal;
    }
}

