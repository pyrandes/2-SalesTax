package ct.store;

import ct.products.Product;

import java.math.BigDecimal;

public class CartItem
{
    private Product product;
    private int qty;
    private BigDecimal tax;

    public CartItem(Product product, int qty)
    {
        this.product = product;
        this.qty = qty;
        this.tax = new BigDecimal(0f);
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getTotal()
    {
        return product.getMsrp().multiply(new BigDecimal(qty));
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
