package ct.store;

import ct.products.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShoppingCart
{
    private Map<String, CartItem> itemsInCart;

    public ShoppingCart()
    {
        this.itemsInCart = new HashMap<>();
    }
    public void addProduct(Product item, int qty)
    {
        if (!itemsInCart.containsKey(item.getId())) {
            itemsInCart.put(item.getId(), new CartItem(item, qty));
        }

        // user may have updated qty to something below 0, so we'll remove the item from the shopping cart
        if (itemsInCart.get(item.getId()).getQty() <= 0)
            removeProduct(item);
    }

    public void removeProduct(Product item)
    {
        if (itemsInCart.containsKey(item.getId()))
          this.itemsInCart.remove(item.getId());
    }

    public List<CartItem> getItemsInCart()
    {
        // Java Stream to retrieve a listing of cart items
        // return itemsInCart.values().stream().collect(Collectors.toList());

        List<CartItem> items = new LinkedList<>();
        for(CartItem item: itemsInCart.values())
            items.add(item);
        return items;
    }

    public BigDecimal getGrandTotal()
    {
        // return itemsInCart.values().parallelStream().map(item -> item.getTotal() + item.getTax()).reduce(0f, (t1, t2) -> t1+t2);

        BigDecimal total = new BigDecimal(0f);
        for(CartItem item: itemsInCart.values()) {
            total = total.add(item.getTotal()).add(item.getTax());
        }
        return total;
    }

    public BigDecimal getTotalTax()
    {
        // return itemsInCart.values().parallelStream().map(item -> item.getTax()).reduce(0f, (t1, t2) -> t1+t2);

        BigDecimal totalTax = new BigDecimal(0f);
        for(CartItem item: itemsInCart.values()) {
            totalTax = totalTax.add(item.getTax());
        }
        return totalTax;
    }
}
