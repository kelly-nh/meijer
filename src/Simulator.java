import java.io.File;
import java.util.*;

public class Simulator {

    /*Get Customer List
     *@param: file contains data
     *@return: customer list - customerID, numItems, timePerItem, arrivalTime, waitTime
     */

    public static ArrayList<Customer> getCustomerList (String file) throws Exception {

        // Read in data from file and store them to associated lists
        int customerID = 0;
        int numItems;
        double timePerItem;
        double arrivalTime;

        ArrayList<Customer> customers = new ArrayList<>();

        Scanner in = new Scanner(new File(file));

        while (in.hasNext()) {

            // Read in data on each line
            String[] line = in.nextLine().split("\\s+"); // \s+ match one or multiple white spaces
            line[0] = line[0].trim();
            arrivalTime = Double.parseDouble(line[0]);
            line[1] = line[1].trim();
            numItems = Integer.parseInt(line[1]);
            line[2] = line[2].trim();
            timePerItem = Double.parseDouble(line[2]);

            // Create a customer object for each person and add them to customers list
            customers.add(new Customer(customerID, numItems, timePerItem, arrivalTime, 0));

            customerID++;

        }
        return customers;
    }


    /*Get Event List
     *@param: customer list
     *@return: event list - customer, associated arrival time
     */

    public static PriorityQueue<Event> getEventList (ArrayList<Customer> customerList) {
        PriorityQueue<Event> events = new PriorityQueue<>();

        for (Customer customer : customerList) {
            events.add(new ArrivalEvent(customer, customer.getArrivalTime()));
        }

        return events;
    }


    /*Create Express Lane
     *@param: number of lanes
     *@return: express lane queue
     */
    public static PriorityQueue<CheckoutLane> createExpressLane (int numLane) {
        PriorityQueue<CheckoutLane> expressLanes = new PriorityQueue<>();
        for (int i = 0; i < numLane; i++) {
            ExpressCheckout express = new ExpressCheckout();
            expressLanes.add(express);
        }
        return expressLanes;
    }

    /*Create Regular Lane
     *@param: number of lanes
     *@return: regular lane queue
     */
    public static  PriorityQueue<CheckoutLane> createRegularLane (int numLane) {
        PriorityQueue<CheckoutLane> regularLanes = new PriorityQueue<>();
        for (int i = 0; i < numLane; i++) {
            RegularCheckout regular = new RegularCheckout();
            regularLanes.add(regular);
        }
        return regularLanes;
    }


    /*Print Average Wait Time
     *@param: customer list
     */
    public static void printAverageWaitTime (ArrayList<Customer> customerList) {

        double totalWaitTime = 0;
        double averageWaitTime;

        for (Customer c : customerList) {
            totalWaitTime += c.waitTime();
        }

        averageWaitTime = totalWaitTime / customerList.size();

        System.out.println("Total customer : " + customerList.size());
        System.out.print("The average wait time: ");
        System.out.printf("%.2f", averageWaitTime);
        System.out.print(" minutes." );
    }

    public static void main(String[] args) throws Exception {

        PriorityQueue<CheckoutLane> expressLanes = createExpressLane(2);
        PriorityQueue<CheckoutLane> regularLanes = createRegularLane(4);

        ArrayList<Customer> customers = getCustomerList("arrival medium.txt");

        PriorityQueue<Event> events = getEventList(customers);

        // Check type of each event and add them to events list
        while (!events.isEmpty()) {

            Event thisEvent = events.poll();

            /* ARRIVAL EVENT */
            if (thisEvent instanceof ArrivalEvent) {

                System.out.println(thisEvent);

                // Create FinishedShoppingEvent for this customer
                FinishedShoppingEvent ese = new FinishedShoppingEvent(thisEvent.getCustomer(), ((ArrivalEvent) thisEvent).getFinishedShoppingTime());

                // Add this event to pq with the right time
                events.offer(ese);


            }


            /* FINISHED SHOPPING EVENT */
            if (thisEvent instanceof FinishedShoppingEvent) {

                System.out.println(thisEvent);

                if (thisEvent.getCustomer().getNumItems() > 12) {
                    System.out.println("More than 12, chose Regular Lane (" + thisEvent.getCustomer().getNumItems() +")"  );
                }

                else {
                    System.out.println("12 or fewer, chose Express Lane (" + thisEvent.getCustomer().getNumItems() +")"  );
                }


                if (thisEvent.getCustomer().getNumItems() < 12) {

                    CheckoutLane thisLane = expressLanes.poll();

                    // Add customer to express lane
                    thisLane.addCustomer(thisEvent.getCustomer());


                    // Schedule a FinishedCheckoutEvent for that customer-- If the checkout lane to which you just added this customer only has one
                    //                                                      customer in it (the one you just added), go ahead and schedule the
                    //                                                      finished checkout event for that customer. if there are more people in line,
                    //                                                      do not schedule the end checkout event yet.
                    if (thisLane.getNumCustomers() == 1) {

                        double finishedCheckoutTime = thisLane.getCheckoutTime() +
                                ((FinishedShoppingEvent) thisEvent).getFinishedShoppingTime();

                        //since this person was immediately at the front of the checkout lane when they joined it,
                        //so set their wait time to zero.
                        thisEvent.getCustomer().setWaitTime(0);

                        //Schedule Finished Checkout event
                        FinishedCheckoutEvent finishedCheckoutEvent = new FinishedCheckoutEvent(thisEvent.getCustomer(), finishedCheckoutTime, thisLane);
                        events.offer(finishedCheckoutEvent);

                        System.out.println("Finished Checkout Customer " + thisEvent.getCustomer().getCustomerID() + " (" + thisEvent.getCustomer().getWaitTime() + " minute wait, "
                                            + thisLane.getNumCustomers() + " in line -- finished shopping at " + thisEvent.getCustomer().getFinishedShoppingTime()
                                            + ", got to the front of the line at " + thisEvent.getCustomer().getFinishedShoppingTime()+ ")");
                    }

                    // Add lane to lanes pq
                    expressLanes.offer(thisLane);

                }

                else {
                    if ( expressLanes.peek().getNumCustomers() >= regularLanes.peek().getNumCustomers() ) {

                        CheckoutLane thisLane = regularLanes.poll();

                        // Add customer to regular lane
                        if (thisLane != null) {
                            thisLane.addCustomer(thisEvent.getCustomer());
                        }


                        if (thisLane != null && thisLane.getNumCustomers() == 1) {


                            double finishedCheckoutTime = thisLane.getCheckoutTime() +((FinishedShoppingEvent) thisEvent).getFinishedShoppingTime();

                            //since this person was immediately at the front of the checkout lane when they joined it,
                            //so set their wait time to zero.
                            thisEvent.getCustomer().setWaitTime(0);

                            //Schedule Finished Checkout event
                            FinishedCheckoutEvent finishedCheckoutEvent = new FinishedCheckoutEvent(thisEvent.getCustomer(), finishedCheckoutTime, thisLane);
                            events.offer(finishedCheckoutEvent);


                            System.out.println("Finished Checkout Customer " + thisEvent.getCustomer().getCustomerID() + " (" + thisEvent.getCustomer().getWaitTime() + " minute wait, "
                                            + thisLane.getNumCustomers() + " in line -- finished shopping at " + thisEvent.getCustomer().getFinishedShoppingTime()
                                            + ", got to the front of the line at " + thisEvent.getCustomer().getFinishedShoppingTime() + ")");
                            }
                                // Add lane to lanes pq
                                regularLanes.offer(thisLane);

                        }

                        else {
                            CheckoutLane thisLane = expressLanes.poll();

                            // Add customer to lane
                        if (thisLane != null) {
                            thisLane.addCustomer(thisEvent.getCustomer());
                        }


                        // Schedule a FinishedCheckoutEvent for that customer
                        if (thisLane != null && thisLane.getNumCustomers() == 1) {

                            double finishedCheckoutTime = thisLane.getCheckoutTime() + ((FinishedShoppingEvent) thisEvent).getFinishedShoppingTime();

                            //since this person was immediately at the front of the checkout lane when they joined it,
                            //so set their wait time to zero.
                            thisEvent.getCustomer().setWaitTime(0);

                            //Schedule Finished Checkout event
                            FinishedCheckoutEvent finishedCheckoutEvent = new FinishedCheckoutEvent(thisEvent.getCustomer(), finishedCheckoutTime, thisLane);
                            events.offer(finishedCheckoutEvent);

                            System.out.println("Finished Checkout Customer " + thisEvent.getCustomer().getCustomerID() + " (" + thisEvent.getCustomer().getWaitTime() + " minute wait, "
                                            + thisLane.getNumCustomers() + " in line -- finished shopping at " + thisEvent.getCustomer().getFinishedShoppingTime()
                                            + ", got to the front of the line at " + thisEvent.getCustomer().getFinishedShoppingTime() + ")");
                            }

                            // Add lane to lanes pq
                            expressLanes.offer(thisLane);

                            System.out.println("12 or fewer, chose Express Lane (" + thisLane.getNumCustomers() +")"  );
                    }
                }
            }


            /* FINISHED CHECKOUT EVENT */
            if (thisEvent instanceof FinishedCheckoutEvent) {

                System.out.println(thisEvent);

                // Remove that customer
                ((FinishedCheckoutEvent) thisEvent).getLane().remove();

                // Schedule an end checkout event for customers left in associated lane
                if (((FinishedCheckoutEvent) thisEvent).getLane().getNumCustomers() > 0) {

                    Customer nextPerson = ((FinishedCheckoutEvent) thisEvent).getLane().getNext();

                    double finishedCheckoutTime = thisEvent.getTime() + ((FinishedCheckoutEvent) thisEvent).getLane().getCheckoutTime();

                    //this Customer's (nextPerson's) wait time
                    thisEvent.getCustomer().setWaitTime(thisEvent.getTime() - nextPerson.getFinishedShoppingTime());

                    FinishedCheckoutEvent finishedCheckoutEvent = new FinishedCheckoutEvent(nextPerson, finishedCheckoutTime,((FinishedCheckoutEvent) thisEvent).getLane());

                    events.offer(finishedCheckoutEvent);

                    System.out.println("Finished Checkout Customer " + thisEvent.getCustomer().getCustomerID() + " (" + thisEvent.getCustomer().getWaitTime() + " minute wait, "
                            + ((FinishedCheckoutEvent) thisEvent).getLane().getNumCustomers() + " in line -- finished shopping at " + nextPerson.getFinishedShoppingTime()
                            + ", got to the front of the line at " + nextPerson.getFinishedShoppingTime() + nextPerson.getWaitTime());
                }

            }

        } // ends while loops

        printAverageWaitTime(customers);
    }
}
