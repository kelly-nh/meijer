
public class Event implements Comparable <Event> {


    private Customer customer;
    private double time;

    public Event(Customer customer, double time) {
        this.customer = customer;
        this.time = time;
    }


    public Customer getCustomer() {
        return this.customer;
    }

    public double getTime() {
        return time;
    }



//@Override
    public int compareTo(Event o) {
        if (this.time < o.time) { return -1;}
        else if (this.time > o.time) { return 1;}
        else return 0;
    }

    public String toString() {
        return customer + " " + time;
    }

}
