package ct.store;

import ct.products.Product;

public class CartItem
{
    private Product item;
    private int qty;
    private float tax;

    public CartItem(Product item, int qty)
    {
        this.item = item;
        this.qty = qty;
    }

    public float getTotal()
    {
        return item.getMsrp() * qty;
    }

    public void addToQty(int qty)
    {
        this.qty += qty;
    }

    public int getQty() {
        return qty;
    }

    public float getTax()
    {
        return tax;
    }

    public void setTax(float tax)
    {
        this.tax = tax;
    }
}
