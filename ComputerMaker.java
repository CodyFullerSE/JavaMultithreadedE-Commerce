/*Author: Cody Fuller
 *Course: CSE-598*/

import java.util.Random;

public class ComputerMaker implements Runnable {

    private static int p = 0;
    private static int computerPrice = 1500;
    private static int previousPrice = 1500;
    private static Random random = new Random();
    volatile boolean exit = false;

    /*saves previous price, calculates computer price, gets order, and
      sends order to be processed by orderProcessThread*/
    @Override
    public void run() {
        //runs until 10 price cuts are made and then terminates thread
        while (!exit) {
            while (p < 10) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                previousPrice = computerPrice;
                pricingModel();
                String order = null;
                try {
                    order = MultiCellBuffer.getOneCell();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (order != null && order != "") {
                    OrderClass orderObj = decoder(order);
                    OrderProcessing orderProcessing = new OrderProcessing();
                    orderProcessing.SetOrderObj(orderObj);
                    Thread orderProcessThread = new Thread(new Thread(orderProcessing));
                    orderProcessThread.start();
                }
            }
            exit = true;
        }
    }

    //gets previous price
    public synchronized int getPrevPrice() {
        int price = previousPrice;
        return price;
    }

    //gets current price
    public synchronized int getPrice() {
        int price = computerPrice;
        return price;
    }

    //changes the price
    public synchronized void setPrice(int price) {
        // acquire lock and set the current computer price
        computerPrice = price;
    }

    //returns the counter for price cuts
    public synchronized int getCounter() {
        return p;
    }

    //randomly changes the price and notifies subscribers if the price decreases
    public void pricingModel() {
        int price = random.nextInt(1600 - 1400) + 1400;
        if (price < computerPrice) {
            p += 1;
            EventClass.priceNotification(price);
        }
        setPrice(price);
    }

    //converts the string containing order object information into an order object
    public OrderClass decoder(String orderobj) {
        String[] orderInfo = orderobj.split("/");
        OrderClass OrderObj = new OrderClass();

        OrderObj.setSenderID(orderInfo[0]);
        OrderObj.setCardNo(Integer.parseInt(orderInfo[1]));
        OrderObj.setQuantity(Integer.parseInt(orderInfo[2]));

        return OrderObj;
    }
}
