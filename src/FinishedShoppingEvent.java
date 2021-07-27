public class FinishedShoppingEvent extends Event {


    public FinishedShoppingEvent(Customer customer, double finishedShoppingTime) {
        super(customer, finishedShoppingTime);
    }

    public double getFinishedShoppingTime() {return super.getTime();}

    public String toString() {
        return super.getTime() + ": Finished Shopping Customer " + getCustomer().getCustomerID();
    }


}
