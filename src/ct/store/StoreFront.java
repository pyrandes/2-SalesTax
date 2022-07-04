package ct.store;

import ct.dao.products.ProductDS;
import ct.products.Product;
import ct.store.taxes.TaxEngine;
import ct.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreFront
{
    private ProductDS productDS;
    private TaxEngine taxEng;
    private Map<String, ShoppingCart> userShoppingCarts;

    public StoreFront(ProductDS productDS, TaxEngine taxEng)
    {
        this.productDS = productDS;
        this.taxEng = taxEng;
        userShoppingCarts = new HashMap<>();
    }

    /**
     * Retrieves a listing of current products
     * @return listing of products in the store
     */
    public List<Product> getProductListing()
    {
        List<Product> prodList = new ArrayList<>();
        for(String id: productDS.getProductIDs()) {
            prodList.add(productDS.getProduct(id));
        }
        return prodList;
    }

    /**
     * Adds a specified item to a shopping cart at a specified qty
     * @param customer User adding an item to a shopping cart
     * @param productID of a Product to add to a shopping cart
     * @param qty # of Product to add
     * @return success of addition
     */
    public boolean addItemToShoppingCart(User customer, String productID, int qty)
    {
        if (!userShoppingCarts.containsKey(customer.getUserID())) {
            userShoppingCarts.put(customer.getUserID(), new ShoppingCart());
        }

        Product product = productDS.getProduct(productID);
        if (product.getStockQty() - qty < 0) {
            return false; // cannot add this a product with this qty, it'll bring our stock total to 0
        }

        product.setStockQty(product.getStockQty() - qty);
        userShoppingCarts.get(customer.getUserID()).addProduct(product, qty);
        return true;
    }

    /**
     * Removes a specified item from the shopping cart
     * @param customer  User removing an item from their shopping cart
     * @param productID product id of the item to remove
     */
    public void removeItemFromShoppingCart(User customer, String productID)
    {
        if (!userShoppingCarts.containsKey(customer.getUserID())) return;  // cannot remove what is not there!

        ShoppingCart cart = userShoppingCarts.get(customer.getUserID());
        cart.removeProduct(productID);
        if (cart.getItemsInCart().size() == 0)
            userShoppingCarts.remove(customer.getUserID());
    }

    /**
     * Performs a checkout on a customer's current shopping cart, "printing" a reciept once taxes have been
     * calculated
     * @param customer customer requesting a checkout
     * @return a Receipt representing the shopping cart items
     */
    public Receipt checkout(User customer)
    {
        // ideally I'd use an Optional here instead of relying on null checks
        if (!userShoppingCarts.containsKey(customer.getUserID())) return null;

        ShoppingCart sc = userShoppingCarts.get(customer.getUserID());

        taxEng.calculateTaxes(customer, sc);
        // ideally, there would be other actions here, like payment, et al. before printing a receipt

        // -- add a history entry here, so if a customer views their profile they can bring up this order
        //    as a part of "order history"
        userShoppingCarts.remove(customer.getUserID());

        return new Receipt(sc);
    }
}
