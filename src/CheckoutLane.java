import java.util.LinkedList;
import java.util.Queue;

public abstract class CheckoutLane implements Comparable<CheckoutLane> {

    private Queue<Customer> customers;
    private double scanPerItemTime;
    private double paymentTime;

    public CheckoutLane (double scanPerItemTime, double paymentTime) {
        customers = new LinkedList<>();
        this.scanPerItemTime = scanPerItemTime;
        this.paymentTime = paymentTime;
    }


    public double getCheckoutTime() {

        double checkoutTime = 0;
        Customer tempCustomer;

        for (int i = 0; i < customers.size(); i ++ ) {

            // poll the next customer from the queue and store them in a temporary variable of type Customer
            tempCustomer = customers.poll();

            // increment the checkoutTime by the time it will take this customer to check out
            checkoutTime += tempCustomer.getNumItems()*scanPerItemTime + paymentTime;

            // offer the customer back to the queue
            customers.offer(tempCustomer);
        }

        return checkoutTime;
    }

    public void addCustomer(Customer customer) {
        customers.offer(customer);
    }


    public void remove() {
        customers.poll();
    }


    public Customer getNext() {
        return customers.peek();
    }


    public int getNumCustomers() {
        return customers.size();
    }



    public String toString(){
        return customers + " in this lane.";
    }

    @Override
    public int compareTo(CheckoutLane o) {

        if (this.customers.size() < o.customers.size()) {
            return -1;
        }
        else if (this.customers.size() > o.customers.size()) {
            return 1;
        }
        return 0;
    }

}
