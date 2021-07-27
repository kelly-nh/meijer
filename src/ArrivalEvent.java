public class ArrivalEvent extends Event implements Comparable <Event> {


    public ArrivalEvent(Customer customer, double arrivalTime) {
        super(customer, arrivalTime);
    }

    public double getFinishedShoppingTime() {
        return Math.round(((getCustomer().getNumItems() * getCustomer().getTimePerItem() + getCustomer().getArrivalTime())*100.0))/100.0;
    }


    public String toString() {
        return super.getTime() + ": Arrival Customer " + getCustomer().getCustomerID();
    }




}
