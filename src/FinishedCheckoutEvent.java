public class FinishedCheckoutEvent extends Event {

    private CheckoutLane lane;

    public FinishedCheckoutEvent(Customer customer, double finishedCheckoutTime, CheckoutLane lane) {
        super(customer, finishedCheckoutTime);
        this.lane = lane;
    }

    public CheckoutLane getLane() {return lane;}



    public String toString() {
        return getTime() + ": Finished Checkout Customer " + getCustomer().getCustomerID();
    }


}
