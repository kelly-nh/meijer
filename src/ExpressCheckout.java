public class ExpressCheckout extends CheckoutLane {
    public ExpressCheckout () {
        super(0.1, 1.0);
    }

    public String toString() {
        return "Express Lane [" + super.getNumCustomers() + " customer]";
    }
}
