/*Author: Cody Fuller
 *Course: CSE-598*/

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Store implements Runnable{
    static volatile boolean exit = false;
    private OrderClass orderObj = new OrderClass();
    private String timeStamp = "";
    private String storeName;

    /*Changes thread priority if thread is blocked too long. Evaluates the price, creates an order object and
    sends it to the encoder to convert into a string, and sends the order object to the buffer*/
    @Override
    public void run() {
        while (!exit) {
            LocalDateTime start = LocalDateTime.now();
            if (getSecondsElapsed(start) > 0.5) {
                    Thread.currentThread().setPriority(5);
            }

            if (getSecondsElapsed(start)  > 1) {
                Thread.currentThread().setPriority(10);
            }

            ComputerMaker computerMaker = new ComputerMaker();
            Random random = new Random();

            try {
                Thread.sleep(random.nextInt(2000 - 500) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int price = computerMaker.getPrice();
            System.out.println(Thread.currentThread().getName() + " has computers for a price of $"
                        + price);

            if (computerMaker.getPrevPrice() - price >= 200) orderObj.setQuantity(2);
            else orderObj.setQuantity(1);

            orderObj.setCardNo(random.nextInt(9999 - 1000) + 1000);
            orderObj.setSenderID(getStoreName());
            String order = encoder(orderObj);
            timeStamp = getTimeStamp();

            try {
                MultiCellBuffer.setOneCell(order);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread.currentThread().setPriority(2);
        }
    }

    //event handler that is called when a price cut occurs
    public void lowerPrice(int pr)
    {
        System.out.println(getStoreName() + " has computers on sale" +
                        " for a low price of $" + pr);
    }

    //creates a string containing order object information
    public String encoder(OrderClass OrderObj)
    {
        String orderobj = OrderObj.getSenderID() + "/" +
                OrderObj.getCardNo() + "/" +
                OrderObj.getQuantity();

        return orderobj;
    }

    //gets the name of the store
    public String getStoreName() {
        return storeName;
    }

    //sets the name of the store
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    //creates a timestamp
    public String getTimeStamp(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return currentDateTime.format(format);
    }

    //gets the number of seconds passed between two timestamps
    public long getSecondsElapsed(LocalDateTime time){
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, time);
        long seconds = Math.abs(duration.getSeconds());
        return seconds;
    }

    //order confirmation event handler
    public void orderConfirmation(String name, String price)
    {
        System.out.println(timeStamp + " Order confirmed for " + name +". Total cost: $" + price);
    }

}
