package ct.store;

import java.util.LinkedList;
import java.util.List;

public class Receipt
{
    private List<CartItem> itemList;
    private float totalTax, grandTotal;

    public Receipt(ShoppingCart cart)
    {
        itemList = cart.getItemsInCart();
        totalTax = cart.getTotalTax();
        grandTotal = cart.getGrandTotal();
    }

    public List<CartItem> getItemList() {
        return itemList;
    }

    public float getGrandTotal() {
        return grandTotal;
    }

    public float getTotalTax() {
        return totalTax;
    }
}
