public class Customer implements Comparable<Customer> {

    private int customerID;
    private int numItems;
    private double timePerItem;
    private double arrivalTime;
    private double waitTime;

    public Customer (int customerID, int numItems, double timePerItem, double arrivalTime, double waitTime) {
        this.customerID = customerID;
        this.numItems = numItems;
        this.timePerItem = timePerItem;
        this.arrivalTime = arrivalTime;
        this.waitTime = waitTime;

    }


    public int getCustomerID() {
        return this.customerID;
    }


    public int getNumItems() {
        return this.numItems;
    }


    public double getTimePerItem() {
        return this.timePerItem;
    }


    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public double getWaitTime () {
        return waitTime;
    }

    public double waitTime() {
        return waitTime;
    }


    public double getArrivalTime() {return arrivalTime;}


    public double getFinishedShoppingTime() {
        return (numItems * timePerItem) + arrivalTime;
    }


    public String toString() {
        return "Customer " +customerID+ " {Arrive: " + arrivalTime + " - Num Items: " + this.numItems + " - Time Per Item: " + this.timePerItem + "\n";
    }


    @Override
    public int compareTo(Customer o) {
        if (this.arrivalTime < o.arrivalTime) {
            return -1;
        }
        if (this.arrivalTime > o.arrivalTime) {
            return 1;
        }
        return 0;
    }
}
