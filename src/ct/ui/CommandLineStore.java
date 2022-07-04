package ct.ui;

import ct.products.Product;
import ct.store.StoreFront;
import ct.users.Customer;
import ct.users.User;
import ct.users.UserInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class CommandLineStore {
    private  StoreFront store;
    private  User customer;

    public CommandLineStore(StoreFront store, User customer)
    {
        this.store = store;
        this.customer = customer;
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
                        break;
                    case "A":
                        break;
                    default:
                        // do nothing?
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("\nGoodbye!  Come again!");
    }

    private void listProducts()
    {
        System.out.println();
        List<Product> prodList = store.getProductListing();
        System.out.println(String.format("[%2s] %30s %10s %s", "ID", "Product Name", "Type", "Is Import?"));
        System.out.println("---------------------------------------------------------------");
        for(Product prod: prodList)
            System.out.println(String.format("[%2s] %30s %10s %s", prod.getId(), prod.getName(), prod.getType().name(), prod.isImport()));
    }

    private void changeState(BufferedReader clrIn) throws Exception
    {
        System.out.println("\nCurrent State: " + customer.getUserInfo().getState());
        System.out.print("Change state to: ");
        String stateCd = clrIn.readLine();
        UserInfo custInfo = new UserInfo(
                customer.getUserInfo().getFirstName(),
                customer.getUserInfo().getMi(),
                customer.getUserInfo().getLastName(),
                customer.getUserInfo().getAddress(),
                customer.getUserInfo().getCity(),
                stateCd,
                customer.getUserInfo().getZipCode()
        );
        customer = new Customer(customer.getUserID(), custInfo);

    }

    private  void displayMenuOptions()
    {
        System.out.println("\nWelcome to the Store!  Please choose from the following options!");
        System.out.println("   ");
        System.out.println("   [A]dd products to shopping cart");
        System.out.println("   [S]tate -- change state code");
        System.out.println("   [P]roducts -- list available products");
        System.out.println("   [C]heckout");
        System.out.println("   [Q]uit -- exit store");
        System.out.print("   Please enter an option: ");
    }
}
