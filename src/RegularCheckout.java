public class RegularCheckout extends CheckoutLane {
    public RegularCheckout () {
        super(0.05, 2.0);
    }


    public String toString() {
        return "Regular Lane [" + super.getNumCustomers() + " customer]";
    }
}
