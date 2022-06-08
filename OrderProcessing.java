/*Author: Cody Fuller
 *Course: CSE-598*/

public class OrderProcessing implements Runnable{
    private OrderClass orderObj;
    volatile boolean exit = false;

    /*validates credit card and calculates total cost and sends
      confirmation to store*/
    @Override
    public void run()
    {
        while (!exit) {
            ComputerMaker computerMaker = new ComputerMaker();

            boolean validCardNum = false;
            int cardNum = orderObj.getCardNo();
            int quantity = orderObj.getQuantity();

            if (cardNum >= 0000 && cardNum < 9999) validCardNum = true;
            if (!validCardNum) System.out.println("Invalid credit card number.");
            if (validCardNum) {
                float totalCharge = (float) (((computerMaker.getPrice() * quantity)
                        * 6.5) + 29.99);
                String name = orderObj.getSenderID();
                EventClass.sendConfirmation(name, Float.toString(totalCharge));
            }
            exit = true;
        }
    }

    //used to get order object when decoding string to order object
    public void SetOrderObj(OrderClass OrderObjIn)
    {
        orderObj = OrderObjIn;
    }
}


