/*Author: Cody Fuller
 *Course: CSE-598*/

import java.util.Random;
import java.util.concurrent.Semaphore;

public class MultiCellBuffer {
    private static int n = 2;
    private static int items = 0;
    private static Semaphore _sem = new Semaphore(n);
    private static String[] buffer = new String[n];
    private static Random random = new Random();

    //get the number of cells for Main to use to initialize buffer
    public static int GetNumCells()
    {
        return n;
    }
    //change the value of a cell in the buffer and change event in evbuffer
    public synchronized static void setOneCell(String value) throws InterruptedException {
        _sem.acquire();
        //sleep if buffer is full
        if (items == 2) Thread.sleep(random.nextInt(1000 - 200) + 200);
        for (int i = 0; i < n; i++){
                if (buffer[i] == "")
                {
                    buffer[i] = value;
                    items += 1;
                    break;
                }
        }
        _sem.release();
    }

    //get and return the value of a cell in the buffer
    public synchronized static String getOneCell() throws InterruptedException {
        String value = "";
        _sem.acquire();
        //sleep if buffer is empty
        if (items == 0) Thread.sleep(random.nextInt(1000 - 200) + 200);
        for (int i = 0; i < n; i++)
        {
            if (buffer[i] != "")
            {
                value = buffer[i];
                buffer[i] = "";
                items += 1;
                break;
            }
        }
        _sem.release();
        return value;
    }
}
