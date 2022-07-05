# 2-SalesTax  
## Design Considerations   
* We needed something to act as a central area to perform actions, like a **StoreFront** class.  While this class didn't need to create all the objects needed to perform actions such as shopping cart additions and checkout, we did need to inject these in.
  * An :employee: type user will have access to methods to manipulate store stock, while the store is running
  * An :customer: type user will only have access for shopping purposes
  * The StoreFront assumes only a single user at a time, for prototyping purposes.  A multi-user storefront would need threading considerations along with ensuring that stock was not manipulated while being accessed 
* The **TaxEngine** was plotted out to be its own thing from the start with access to a Tax data store injected into the class.  
  * When designing the algorithm to calculate taxes, it was decided to eventually use java.math.BigDecimal to preserve floating point integrity 
* **Product**s would have their own "data store", with access to a .csv file as a very basic table in lieu of a SQL or other data implementation; an interface would be passed to the StoreFront for basic access needs.
  * The ProductCSV DS class does a very rudimentary .csv parse, which will not take into account escaped quotes or the name field having "," within the name.  IE, for testing/prototype purposes only
* **ShoppingCart** maintains a list of CartItems, which could be manipulated and totalled appropriately.
* **CartItem** contains a reference to the Product added for convenience
* **Receipt** contains a copy of the shopping cart, without the need to total a Tax Total or a Grand Total (these are just passed in)

A rudimentary command line was implemented for ease of testing/prototyping alongside of the test cases supplied and created.  Users were added to this interface instead of a login for prototyping.  Ideally, we would have a User data store to pull user information from.