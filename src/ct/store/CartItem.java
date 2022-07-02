package ct.store;

import ct.products.Product;

import java.math.BigDecimal;

public class CartItem
{
    private Product item;
    private int qty;
    private BigDecimal tax;

    public CartItem(Product item, int qty)
    {
        this.item = item;
        this.qty = qty;
        this.tax = new BigDecimal(0f);
    }

    public BigDecimal getTotal()
    {
        return item.getMsrp().multiply(new BigDecimal(qty));
    }

    public void addToQty(int qty)
    {
        this.qty += qty;
    }

    public int getQty() {
        return qty;
    }

    public BigDecimal getTax()
    {
        return tax;
    }

    public void setTax(BigDecimal tax)
    {
        this.tax = tax;
    }
}
