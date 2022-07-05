# 2-SalesTax  
## Design Considerations   
* We needed something to act as a central area to perform actions, like a **StoreFront** class.  This class would be a central access point, but would not create any of the access points needed (UI, Product Data Store, Tax Calculations, et al).  Instead, these modules would utilize the StoreFront or be injected into the StoreFront to be utilized.  For users:
  * An :employee: type user will have access to methods to manipulate store stock, while the store is running
  * An :customer: type user will only have access for shopping purposes
  * The StoreFront assumes only a single user at a time, for prototyping purposes.  A multi-user storefront would need threading considerations along with ensuring that stock was not manipulated while being accessed
  * A Receipt would be the only object produced out of a StoreFront
* The **TaxEngine** was plotted out to be its own thing from the start with access to a Tax data store injected into the class.  
  * When designing the algorithm to calculate taxes, it was decided to eventually use java.math.BigDecimal to preserve floating point integrity 
* **Product**s would have their own "data store", with access to a .csv file as a very basic table in lieu of a SQL or other data implementation; an interface would be passed to the StoreFront for basic access needs.
  * The ProductCSV DS class does a very rudimentary .csv parse, which will not take into account escaped quotes or the name field having "," within the name.  IE, for testing/prototype purposes only
* **ShoppingCart** maintains a list of CartItems, which could be manipulated and totalled appropriately.
* **CartItem** contains a reference to the Product added for convenience
* **Receipt** contains a copy of the shopping cart, without the need to total a Tax Total or a Grand Total (these are just passed in). 
  * Ideally the receipt would be pushed to an "Order History" data table at the same time that it is presented to a User, for record keeping purposes
* Testing was performed using JUnit 4.0

A rudimentary command line was implemented for ease of testing/prototyping alongside of the test cases supplied and created.  Users were added to this interface instead of a login for prototyping.  Ideally, we would have a User data store to pull user information from.  

The Command Line UI currently uses the StateFreeTaxDS for a tax data store, injected into the TaxEngine.  We can easily switch this out for a FlatTaxDS, or another TaxDS creation.  The StateFreeTaxDS will consider any state starting with an "M" as tax-free for state taxes.  Its entry point is in **ct.Main**.

## Command Line Menu "Screens"
### Login
[L]ogin as user  
[Q]uit or any other key -- exit store
Please enter an option: 

### Store Front
[A]dd products to shopping cart  
[V]iew shopping cart  
[D]elete items from shopping cart  
[S]tate -- change state code  
[P]roducts -- list available products  
[C]heckout  
[R]estock Products  
[Q]uit -- exit store  
Please enter an option:  

### Restocking 
[A]dd new product  
[R]estock current product  -- setting so it is < 0 will remove it from a Customer's viewset
[V]iew current products  
[D]elete product  
[E]xit restocking  
Restocking option?  

