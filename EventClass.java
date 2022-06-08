/*Author: Cody Fuller
 *Course: CSE-598*/

import java.util.LinkedList;
import java.util.List;

public class EventClass {
    private static final List<Store> stores = new LinkedList<>();

    public static void addStore(Store store){
        stores.add(store);
    }

    public static void sendConfirmation(String name, String price){
        for (Store store : stores) {
            if (store.getStoreName().equals(name)){
                store.orderConfirmation(name, price);
            }
        }
    }

    public static void priceNotification(int price){
        for (Store store : stores) {
            store.lowerPrice(price);
        }
    }
}
