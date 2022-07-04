# 2-SalesTax  
## Design Considerations   
* We needed something to act as a central area to perform actions, like a **StoreFront** class.  While this class didn't need to create all the objects needed to perform actions such as shopping cart additions and checkout, we did need to inject these in.
* The **TaxEngine** was plotted out to be its own thing from the start with access to a Tax data store injected into the class.  
  * When designing the algorithm to calculate taxes, it was decided to eventually use java.math.BigDecimal to preserve floating point integrity 
* **Product**s would have their own "data store", with access to a .csv file as a very basic table in lieu of a SQL or other data implementation; an interface would be passed to the StoreFront for basic access needs.
* **ShoppingCart** maintains a list of CartItems, which could be manipulated and totalled appropriately.
* **CartItem** contains a reference to the Product added for convenience
* **Receipt** contains a copy of the shopping cart, without the need to total a Tax Total or a Grand Total (these are just passed in)

When providing an interface, implemented a rudimentary command line first for ease of testing
## Testing  


### Issues?  
* Output 3 indicated a total sales tax of 6.70, but after doing manual calculations, it should have been 6.65 (and subsequently, the imported box of chocolates 11.80 instead of 11.85)
