/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2018 Alvaro Monge <alvaro.monge@csulb.edu>
 *
 */

package csulb.cecs323.app;

// Import all of the entity classes that we have written for this application.
import csulb.cecs323.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * A simple application to demonstrate how to persist an object in JPA.
 * <p>
 * This is for demonstration and educational purposes only.
 * </p>
 * <p>
 *     Originally provided by Dr. Alvaro Monge of CSULB, and subsequently modified by Dave Brown.
 * </p>
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2021 David Brown <david.brown@csulb.edu>
 *
 */
public class CustomerOrders {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the CustomerOrders
    * class, and create an instance of CustomerOrders in the main.
    */
   private EntityManager entityManager;

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(CustomerOrders.class.getName());

   /**
    * The constructor for the CustomerOrders class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public CustomerOrders(EntityManager manager) {
      this.entityManager = manager;
   }

   /**
    * Main method creates instances of customerOrders then it begins a transaction. Inside the transaction
    * a list of products and customers is created where we add values for each. Next we persist products and
    * customers to the database. Next is where the logic of the program is implemented. A customer object and
    * list is created to store the orders and orderlines the customer will place. Inside a loop the method
    * prompts the user to answer questions regarding the order being placed. The program runs until the user
    * chooses to finish order.
    * @param args
    */

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("CustomerOrders");
      EntityManager manager = factory.createEntityManager();
      // Create an instance of CustomerOrders and store our new EntityManager as an instance variable.
      CustomerOrders customerOrders = new CustomerOrders(manager);


      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      tx.begin();
      // List of Products that I want to persist.  I could just as easily done this with the seed-data.sql
      List<Products> products = new ArrayList<Products>();
      // Load up my List with the Entities that I want to persist.  Note, this does not put them
      // into the database.
      products.add(new Products("076174517163", "hickory hammer", "Stanley Tools", "1", 9.97, 50));
      products.add(new Products("0123456789", "screwdriver", "Stanley Tools", "88", 10.50, 100));
      products.add(new Products("9288938444", "ladder", "Home Depot", "10", 149.99, 30));
      products.add(new Products("447573610405", "safety goggles", "The Dewalt", "15", 20.00, 60));
      products.add(new Products("4568399222", "light bulb", "GE", "55", 1.50, 500));
      customerOrders.createEntity(products);
      // Create the list of customers in the database.
      List<Customers> customers = new ArrayList<>();
      //Load up List with the Entities that I want to persist.
      customers.add(new Customers("Simpson", "Audrey", "Blackwillow", "92808", "909-680-1717"));
      customers.add(new Customers("Momma", "Joe", "Bofa", "92633", "714-655-2243"));
      customers.add(new Customers("Hernandez", "Karina", "Euclid", "90804", "562-739-8942"));
      customers.add(new Customers("Brown", "David", "Database", "90813", "562-789-4356"));
      customerOrders.createEntity(customers);
      //Show user a menu of available customers and products

      //Prompt user input for customer
     /*System.out.println("Enter customer name: ");
      customerMenu(customers);
      //int customerName = getInt();
      //System.out.println(customers.get(customerName - 1));
      //Prompt user input for product
      System.out.println("Enter product name: ");
      productMenu(products);*/
      //int productName = getInt();
      //System.out.println(products.get(productName - 1));
//      System.out.println("Would you like to place an order? (yes/no)?");
      //String place = getString();
      Customers c = new Customers();
      boolean answer = true;
      //if (place.toLowerCase() == "no") {
         //answer = false;

      //} else {
      System.out.println("Please select the customer that is placing the order: ");
      c = customerMenu(customers);
         //answer = true;
     // }
      List<Orders> orders = new ArrayList<>();
      List<Order_lines> orderLines = new ArrayList<>();
      double totals = 0;
      //(!place.toLowerCase().equals("no"))

      while(answer) {
         LocalDateTime time = LocalDateTime.now();
         System.out.println("The current time for this order is: " + time);
         System.out.println("Please enter the name of the person who sold the order to this customer: ");
         String rep = getString();
//         orders.add( new Orders(c, time, rep));
         Orders o = new Orders(c, time, rep);
         orders.add(o);
         System.out.println("What product is this customer looking to purchase?");
         Products p = productMenu(products);
         System.out.println("How much of this product does the customer wish to purchase?");
         int i = getInt();
         if (i > p.getUnits_in_stock()) {
            System.out.println("There is only " + p.getUnits_in_stock() + "left. This is as all we can add to your order.");
            i = p.getUnits_in_stock();
         }
         if(p.getUnits_in_stock() == 0){
            while(p.getUnits_in_stock() == 0){
               System.out.println(p.getProd_name() + " product is unavailable please select another product from list: ");
               products.remove(p);
               p = productMenu(products);
               i = p.getUnits_in_stock();
            }
         }
         int temp = p.getUnits_in_stock() - i;
         p.setUnits_in_stock(temp);
         double price = p.getUnit_list_price();
         orderLines.add(new Order_lines(p, o, i, price));
         totals += i * price;
         System.out.println("Would this customer like to add more products to their order? please enter 'Yes' or 'No'");
         String place = getString();

         if(place.equalsIgnoreCase("no"))
         {
            answer = false;
           // break;

         }/*else{
            answer = true;
         }*/
      }
      System.out.println("Here is the customer's order:\n" + orderLines);
      System.out.println("The customer's total is: " + totals);
      System.out.println("Would the customer like to place the order? please enter 'yes' or 'no'");
      String placeOrder = getString();
//      customerOrders.createEntity(orders);
      //customerOrders.createEntity(orderLines);
      if(placeOrder.equalsIgnoreCase("no")){
         //ima try something, dont mind me kk
         System.out.println("You have cancled the placement of order");
//         Order_lines delete = manager.find(Order_lines.class,c);
//         manager.remove(delete);
         //break;
      }else{
         customerOrders.createEntity(orders);
         customerOrders.createEntity(orderLines);
      }

      // Commit the changes so that the new data persists and is visible to other users.
      tx.commit();
      LOGGER.fine("End of Transaction");

   }
      // End of the main method
   /**
     * Method utilizes scanner to take in user input, stores the line
     * Then returns a String when method is called
     * @return String input
    */
   public static String getString() {
      Scanner in = new Scanner(System.in);
      String input = in.nextLine();
      return input;
   }
   /**
    * Scanner takes in user input, stores integer. Then it checks if user input is a valid int.
    * If it's invalid then it prints out error message and clears the invalid input. If it is valid
    * then it returns the int when method is called.
    * @return int answer
    */
   public static int getInt () {
      Scanner in = new Scanner(System.in);
      int answer = 0;
      boolean valid = false;
      while (!valid) {
         if (in.hasNextInt()) {
            answer = in.nextInt();
            valid = true;
         } else {
            in.next(); //clear invalid string
            System.out.println("Invalid Input.");
         }
      }
      return answer;
   }

      /**
       * Create and persist a list of objects to the database.
       * @param entities   The list of entities to persist.  These can be any object that has been
       *                   properly annotated in JPA and marked as "persistable."  I specifically
       *                   used a Java generic so that I did not have to write this over and over.
       */
      public <E> void createEntity (List <E> entities) {
         for (E next : entities) {
            LOGGER.info("Persisting: " + next);
            // Use the CustomerOrders entityManager instance variable to get our EntityManager.
            this.entityManager.persist(next);
         }

         // The auto generated ID (if present) is not passed in to the constructor since JPA will
         // generate a value.  So the previous for loop will not show a value for the ID.  But
         // now that the Entity has been persisted, JPA has generated the ID and filled that in.
         for (E next : entities) {
            LOGGER.info("Persisted object after flush (non-null id): " + next);
         }
      } // End of createEntity member method

      /**
       * Think of this as a simple map from a String to an instance of Products that has the
       * same name, as the string that you pass in.  To create a new Cars instance, you need to pass
       * in an instance of Products to satisfy the foreign key constraint, not just a string
       * representing the name of the style.
       * @param UPC        The name of the product that you are looking for.
       * @return The Products instance corresponding to that UPC.
       */
      public Products getProduct (String UPC){
         // Run the native query that we defined in the Products entity to find the right style.
         List<Products> products = this.entityManager.createNamedQuery("ReturnProduct",
                 Products.class).setParameter(1, UPC).getResultList();
         if (products.size() == 0) {
            // Invalid style name passed in.
            return null;
         } else {
            // Return the style object that they asked for.
            return products.get(0);
         }
      }// End of the getStyle method
   /**
    * This method passes in the products list and displays them and prompts the user to select a product by
    * entering the product number. It returns the chosen product object.
    * @param <> p array list of Products
    * @return Product object
    */
      public static Products productMenu (List <Products> p) {
//         System.out.println("Please select a product by enter the product's number on the menu: \n1) " + p.get(0).getProd_name() + "\n2) " + p.get(1).getProd_name() + "\n3) " + p.get(2).getProd_name() + "\n4) " + p.get(3).getProd_name() + "\n5) " + p.get(4).getProd_name());
         System.out.println("Please select a product by entering the product's number on the menu: ");
         for(int i = 0; i < p.size(); i++){
            System.out.println(i+1 + ") " + p.get(i).getProd_name() + "\n");
         }
         int prod = getInt() - 1;
         return p.get(prod);
      }
    /**
    * This method passes in the Customers list. It displays the list and prompts the user to select a Customers by
    * entering the Customer ID number. It returns the chosen Customer object.
    * @param <> p array list of Customers
    * @return Product object
    */
      public static Customers customerMenu (List <Customers> c) {
         System.out.println("Please select a customer by entering the Customer ID Number:\n" + c.get(0) +
                 "\n " + c.get(1) + "\n " + c.get(2) + "\n " + c.get(3));
         int cust = 0;
         cust = getInt() - 1;
         return c.get(cust);
      }

      public Orders createOrder (Customers c, LocalDateTime t, String s ){
         Orders the_order = new Orders(c, t, s);
         return the_order;

      }





} // End of CustomerOrders class


