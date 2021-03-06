package ct.store;

import ct.products.Product;

import java.math.BigDecimal;

public class CartItem
{
    private Product product;
    private int qty;
    private float tax;

    public CartItem(Product product, int qty)
    {
        this.product = product;
        this.qty = qty;
        this.tax = 0f;
    }

    public Product getProduct() {
        return product;
    }

    public float getTotal()
    {
        BigDecimal total = new BigDecimal(product.getMsrp());
        total = total.multiply(new BigDecimal(qty));
        total = total.add(new BigDecimal(tax));
        return total.floatValue();
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
