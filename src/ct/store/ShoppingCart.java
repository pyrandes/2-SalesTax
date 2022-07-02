package ct.store;

import ct.products.Product;

import java.util.Map;

public class ShoppingCart
{
    private Map<String, CartItem> itemsInCart;

    public void addProduct(Product item, int qty)
    {

    }

    public float getGrandTotal()
    {
        // TODO: retrieve grand total of all  itemsInCart, including tax
        return 0f;
    }

    public float getTotalTax()
    {
        // TODO: calculate the total tax needed for for each itemInCart
        return 0f;
    }
}
