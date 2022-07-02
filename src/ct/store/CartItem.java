package ct.store;

import ct.products.Product;

public class CartItem
{
    private Product mItem;
    private int mQty;
    private float tax;

    public float getTotal()
    {
        // TODO: calc total of this line item's price once Product is implemented
        return 0f;
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
