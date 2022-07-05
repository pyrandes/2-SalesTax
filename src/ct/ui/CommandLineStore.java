package ct.ui;

import ct.products.Product;
import ct.products.ProductType;
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
        boolean mustLogin = true;
        try (BufferedReader clrIn = new BufferedReader(new InputStreamReader(System.in))) {
            boolean running = true;
            while (running) {
                if (mustLogin) {
                    currentUser = displayEntryOptions(clrIn);
                    if (currentUser == null) {
                        running = false;
                        continue;
                    }
                    mustLogin = false;
                }

                displayMenuOptions();
                String lineIn = clrIn.readLine();
                switch(lineIn.toUpperCase()) {
                    case "Q":
                        mustLogin = true;
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
                        addProductsToCart(clrIn);
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
        while(true)
        {
            System.out.println("\nRestocking options");
            System.out.println("   [A]dd new product");
            System.out.println("   [R]estock current product");
            System.out.println("   [V]iew current products");
            System.out.println("   [D]elete product");
            System.out.println("   [E]xit restocking");
            System.out.print("Restocking option? ");

            String opt = clrIn.readLine();
            switch(opt.toUpperCase())
            {
                case "A":
                    addNewProduct(clrIn);
                    break;
                case "R":
                    restockProduct(clrIn);
                    break;
                case "V":
                    viewCurrentProducts();
                    break;
                case "D":
                    removeProduct(clrIn);
                    break;
                case "E":
                    return;
                default:
                    System.out.println("Invalid option!  Please choose again!");
            }
        }
    }

    private void addNewProduct(BufferedReader clrIn) throws Exception
    {
        System.out.print("Name: ");
        String name = clrIn.readLine();

        System.out.println("\n Choose a type:");
        for(ProductType t: ProductType.values())
            System.out.print(t.name() + " ");
        System.out.println();
        System.out.print("Product Type: ");
        String typeStr = clrIn.readLine();
        ProductType type = ProductType.getTypeForName(typeStr);

        System.out.print("\nIs Import (Y/N)?");
        boolean isImport = clrIn.readLine().equalsIgnoreCase("Y");

        System.out.print("\nSale Price: ");
        String msrpStr = clrIn.readLine();
        float msrp = 0f;
        while(true) {
            try {
                msrp = Float.parseFloat(msrpStr);
                break;
            } catch (Exception ex) {
                System.out.print("Invalid price, please enter like [n.nn]: ");
            }
        }

        System.out.print("\nInitial Stock QTY: ");
        String qtyStr = clrIn.readLine();
        int qty = 0;
        while(true) {
            try {
                qty = Integer.parseInt(qtyStr);
                break;
            } catch (Exception ex) {
                System.out.print("Invalid QTY, please enter like [nnn]: ");
            }
        }

        store.restockProduct(new Product("", name, type, isImport, msrp, qty));
    }

    private void restockProduct(BufferedReader clrIn) throws Exception
    {

        viewCurrentProducts();
        System.out.print("Choose product to restock: ");
        String id = clrIn.readLine();
        Product prod = store.getProductWithID(id);
        if (prod == null) {
            System.err.println("No product exists with that ID, returning to previous menu");
            return;
        }

        System.out.print("Qty to add: ");
        String qtyStr = clrIn.readLine();
        int qty = 0;
        while(true) {
            try {
                qty = Integer.parseInt(qtyStr);
                break;
            } catch (Exception ex) {
                System.out.print("Invalid QTY, please enter like [nnn]: ");
            }
        }
        prod.setStockQty(qty);
        store.restockProduct(prod);
    }

    private void removeProduct(BufferedReader clrIn) throws  Exception
    {
        viewCurrentProducts();
        System.out.print("Enter product to remove: ");
        String id = clrIn.readLine();
        Product prod = store.getProductWithID(id);
        if (prod == null) {
            System.err.println("No product exists with that ID, returning to previous menu");
            return;
        }

        store.removeProduct(id);
    }

    private void viewCurrentProducts()
    {
        System.out.println();
        List<Product> prodList = store.getProductListing();
        System.out.println(String.format("[%2s] %30s %10s %10s   %s  %s", "ID", "Product Name", "Type", "Is Import?", "QTY", "Sale Price"));
        System.out.println("---------------------------------------------------------------------------------------");
        for(Product prod: prodList) {
            if (prod.getStockQty() <= 0) continue;  // only display products currently in stock

            System.out.println(String.format("[%2s] %30s %10s %10s   %3d  $%3.2f", prod.getId(), prod.getName(), prod.getType().name(), prod.isImport() ? "I" : "", prod.getStockQty(), prod.getMsrp()));
        }
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

    private void addProductsToCart(BufferedReader clrIn) throws Exception
    {
        System.out.println("Please choose from our selection of products!");

        while (true) {
            listProducts();
            System.out.println("[E]xit product selection");
            System.out.print("Enter a Product ID to add: ");
            String id = clrIn.readLine();
            if (id.equalsIgnoreCase("E")) return;

            int qty = 0;
            while (true) {
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
                continue;
            }
            System.out.println("Item added successfully!");
        }
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
        System.out.print("Change state to (blank will keep old state): ");
        String stateCd = clrIn.readLine();
        if (stateCd.isEmpty()) return;
        
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

    private User displayEntryOptions(BufferedReader clrIn) throws Exception
    {
        System.out.println("\nPlease choose from the following options!");
        System.out.println("   ");
        System.out.println("   [L]ogin as user");
        System.out.println("   [Q]uit or any other key -- exit store");
        System.out.print("   Please enter an option: ");

        String opt = clrIn.readLine();
        if (!opt.equalsIgnoreCase("L")) {
            return null;
        }

        System.out.println("\nLogIn as a following user:");
        for(User u: availUsers.values()) {
            System.out.println(String.format("[%2s] %30s %10s", u.getUserID(), u.getUserInfo().getFirstName() + " " + u.getUserInfo().getLastName(), u.getUserType().name()));
        }
        System.out.print("ID to login? ");
        String id = clrIn.readLine();
        if (!availUsers.containsKey(id)) return null;
        return availUsers.get(id);
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

