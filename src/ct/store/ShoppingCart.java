package ct.store;

import ct.products.Product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShoppingCart
{
    private final Map<String, CartItem> itemsInCart;

    public ShoppingCart()
    {
        this.itemsInCart = new HashMap<>();
    }

    /**
     * Adds a product to this shopping cart
     * @param item product to add
     * @param qty  # of items to add
     */
    public void addProduct(Product item, int qty)
    {
        if (!itemsInCart.containsKey(item.getId())) {
            itemsInCart.put(item.getId(), new CartItem(item, qty));
        }

        // user may have updated qty to something below 0, so we'll remove the item from the shopping cart
        if (itemsInCart.get(item.getId()).getQty() <= 0)
            removeProduct(item.getId());
    }

    /**
     * Remove a product from this shopping cart
     * @param productID product id to remove
     */
    public void removeProduct(String productID)
    {
        if (!itemsInCart.containsKey(productID)) return; // nothing to do, so exit early

        Product prod = itemsInCart.get(productID).getProduct();
        prod.setStockQty(prod.getStockQty() + itemsInCart.get(productID).getQty());
        this.itemsInCart.remove(productID);
    }

    /**
     * Retrieve a list of CartItems
     * @return a lsting of items currently in this cart
     */
    public List<CartItem> getItemsInCart()
    {
        // Java Stream to retrieve a listing of cart items
        // return itemsInCart.values().stream().collect(Collectors.toList());
        return new LinkedList<>(itemsInCart.values());
    }

    /**
     * Calculates grand total, including taxes
     * @return grand total
     */
    public float getGrandTotal()
    {
        // return itemsInCart.values().parallelStream().map(item -> item.getTotal().add(item.getTax())).reduce(new BigDecimal("0", (t1, t2) -> t1.add(t2));

        BigDecimal total = new BigDecimal(0f);
        for(CartItem item: itemsInCart.values()) {
            total = total.add(new BigDecimal(item.getTotal()));
        }

        int precision = 2 ;
        if (!(total.floatValue() < 1))
            precision += Integer.toString(total.intValue()).length();
        return total.round(new MathContext(precision)).floatValue();
    }

    /**
     * Calculates the total tax/duty owed
     * @return totalTax
     */
    public float getTotalTax()
    {
        // return itemsInCart.values().parallelStream().map(item -> item.getTax()).reduce(new BigDecimal(0f), (t1, t2) -> t1.add(t2));

        BigDecimal totalTax = new BigDecimal(0f);
        for(CartItem item: itemsInCart.values()) {
            totalTax = totalTax.add(new BigDecimal(item.getTax()));
        }

        int precision = 2 ;
        if (!(totalTax.floatValue() < 1))
            precision += Integer.toString(totalTax.intValue()).length();
        return totalTax.round(new MathContext(precision)).floatValue();
    }
}
