package ct.ui;

import ct.products.Product;
import ct.store.CartItem;
import ct.store.Receipt;
import ct.store.ShoppingCart;
import ct.store.StoreFront;
import ct.users.User;
import ct.users.UserInfo;
import ct.users.UserType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLineStore {
    private final StoreFront store;
    private final Map<String, User> availUsers;
    private User currentUser;

    public CommandLineStore(StoreFront store)
    {
        this.store = store;
        this.availUsers = new HashMap<>();

        User customer = new User("1", UserType.CUSTOMER, new UserInfo("f", "", "l", "123 testing way", "emma" , "CA", "12345"));
        availUsers.put(customer.getUserID(), customer);

        customer = new User("2", UserType.EMPLOYEE, new UserInfo("dd", "", "johnson", "123 testing way", "emma" , "MS", "12345"));
        availUsers.put(customer.getUserID(), customer);
    }

    public void enterStore()
    {
        try (BufferedReader clrIn = new BufferedReader(new InputStreamReader(System.in))) {
            boolean running = true;
            while (running) {
                displayMenuOptions();
                String lineIn = clrIn.readLine();
                switch(lineIn.toUpperCase()) {
                    case "Q":
                        running = false;
                        break;
                    case "S":
                        changeState(clrIn);
                        break;
                    case "P":
                        listProducts();
                        break;
                    case "C":
                        checkOut();
                        break;
                    case "A":
                        addProductToCart(clrIn);
                        break;
                    case "V":
                        viewShoppingCart();
                        break;
                    case "D":
                        removeFromShoppingCart(clrIn);
                        break;
                    case "R":
                        restockItems(clrIn);
                        break;
                    default:
                        System.out.println("Invalid option!  Please choose again!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("\nGoodbye!  Come again!");
    }

    private void restockItems(BufferedReader clrIn) throws Exception
    {

    }

    private void checkOut()
    {
        Receipt rec = store.checkout(currentUser);

        System.out.println("\nHere's your receipt for this transaction!  Thank you for shopping with us!");
        System.out.println(String.format("[%2s] %30s %5s    %s    %s", "ID", "Product Name", "QTY", "MSRP", "Total"));
        System.out.println("------------------------------------------------------------------------------");
        for(CartItem item: rec.getItemList()) {
            System.out.println(String.format("[%2s] %30s %5d  %6.2f   %6.2f", item.getProduct().getId(), item.getProduct().getName(), item.getQty(), item.getProduct().getMsrp(), item.getTotal()));
        }

        System.out.println(String.format("%50s  %6.2f", "Tax Total", rec.getTotalTax()));
        System.out.println(String.format("%50s  %6.2f", "Grand Total", rec.getGrandTotal()));
    }

    private void removeFromShoppingCart(BufferedReader clrIn) throws Exception
    {
        ShoppingCart cart = displayCartItems();
        if (cart == null) return; // no cart items to display, dont trying to remove items!

        System.out.print("Enter the ID to remove: ");
        String id = clrIn.readLine();

        store.removeItemFromShoppingCart(currentUser, id);
    }


    private void viewShoppingCart()
    {
        ShoppingCart cart = displayCartItems();
        if (cart == null) return; // no cart items to display, dont bother displaying total

        System.out.println("------------------------------------------------------------------------------");
        System.out.println(String.format("%50s  %6.2f", " ", cart.getGrandTotal()));
        System.out.println("Total is without taxes -- Taxes and Duty will be calculated at checkout!");
    }

    private ShoppingCart displayCartItems() {
        ShoppingCart cart = store.getShoppingCart(currentUser);
        if (cart == null) {
            System.out.println("\nNo shopping cart exists! Please add an item before viewing again.");
            return null;
        }

        System.out.println("\nCurrent Shopping Cart!");
        System.out.println(String.format("[%2s] %30s %5s    %s    %s", "ID", "Product Name", "QTY", "MSRP", "Total"));
        System.out.println("------------------------------------------------------------------------------");
        for(CartItem item: cart.getItemsInCart()) {
            System.out.println(String.format("[%2s] %30s %5d  %6.2f   %6.2f", item.getProduct().getId(), item.getProduct().getName(), item.getQty(), item.getProduct().getMsrp(), item.getTotal()));
        }
        return cart;
    }

    private void addProductToCart(BufferedReader clrIn) throws Exception
    {
        System.out.println("Please choose from our selection of products!");
        listProducts();
        System.out.print("Enter a Product ID to add: ");
        String id = clrIn.readLine();

        int qty = 0;
        while(true) {
            try {
                System.out.print("Enter Quantity: ");
                String qtyStr = clrIn.readLine();

                qty = Integer.parseInt(qtyStr);
                break;
            } catch (Exception ex) {
                System.err.println("Could not validate quantity... try entering again (Y/N)?");
                if (!clrIn.readLine().equalsIgnoreCase("Y")) {
                    qty = 0;
                    break;
                }
            }
        }

        if (qty == 0) {
            System.out.println("Could not add " + id + " to the shopping cart!");
        }

        if (!store.addItemToShoppingCart(currentUser, id, qty)) {
            System.err.println(store.getLastError());
            return;
        }
        System.out.println("Item added successfully!");
    }

    private void listProducts()
    {
        System.out.println();
        List<Product> prodList = store.getProductListing();
        System.out.println(String.format("[%2s] %30s %10s %10s   %s", "ID", "Product Name", "Type", "Is Import?", "Sale Price"));
        System.out.println("---------------------------------------------------------------------------------------");
        for(Product prod: prodList) {
            if (prod.getStockQty() <= 0) continue;  // only display products currently in stock

            System.out.println(String.format("[%2s] %30s %10s %10s   $%3.2f", prod.getId(), prod.getName(), prod.getType().name(), prod.isImport() ? "I" : "", prod.getMsrp()));
        }
    }

    private void changeState(BufferedReader clrIn) throws Exception
    {
        System.out.println("\nCurrent State: " + currentUser.getUserInfo().getState());
        System.out.print("Change state to: ");
        String stateCd = clrIn.readLine();
        UserInfo custInfo = new UserInfo(
                currentUser.getUserInfo().getFirstName(),
                currentUser.getUserInfo().getMi(),
                currentUser.getUserInfo().getLastName(),
                currentUser.getUserInfo().getAddress(),
                currentUser.getUserInfo().getCity(),
                stateCd,
                currentUser.getUserInfo().getZipCode()
        );
        currentUser = new User(currentUser.getUserID(), currentUser.getUserType(), custInfo);

    }

    private void  displayEntryOptions()
    {
        System.out.println("\nWelcome to the Store!  Please choose from the following options!");
        System.out.println("   ");
        System.out.println("   [L]ogin as user");
        System.out.println("   [Q]uit -- exit store");
    }


    private  void displayMenuOptions()
    {
        System.out.println("\nWelcome to the Store!  Please choose from the following options!");
        System.out.println("   ");
        System.out.println("   [A]dd products to shopping cart");
        System.out.println("   [V]iew shopping cart");
        System.out.println("   [D]elete items from shopping cart");
        System.out.println("   [S]tate -- change state code");
        System.out.println("   [P]roducts -- list available products");
        System.out.println("   [C]heckout");

        if (currentUser.getUserType() == UserType.EMPLOYEE) {
            System.out.println("   [R]estock Products");
        }
        System.out.println("   [Q]uit -- exit store");
        System.out.print("   Please enter an option: ");
    }
}

