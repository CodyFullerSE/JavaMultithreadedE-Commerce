/*Author: Cody Fuller
 *Course: CSE-598*/

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int N = 5;
        ComputerMaker computerMaker = new ComputerMaker();
        Thread computerMakerThread = new Thread(new Thread(computerMaker));

        //initialize buffer
        for (int i = 0; i < MultiCellBuffer.GetNumCells(); i++)
        {
            MultiCellBuffer.setOneCell("");
        }

        computerMakerThread.start();
        Thread[] stores = new Thread[5];

        for (int i = 0; i < N; i++)
        {
            Store storeThread = new Store();
            storeThread.setStoreName("Store" + ((i + 1)));
            EventClass.addStore(storeThread);
            stores[i] = new Thread(new Thread(storeThread));
            stores[i].setName("Store" + ((i + 1)));
            stores[i].start();
        }

        while (computerMakerThread.isAlive()) { }
        Store.exit = true;
    }
}
