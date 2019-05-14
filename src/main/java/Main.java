import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.Thread.sleep;

public class Main {
    public static View view = new View("WatchDog");

    public static void main(String[] args) {
        /*EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main.view = new View("WatchDog");
            }
        });*/

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                new WatchDog();
            }
        });
        thread1.start();

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                CPU.activeProcessor = new CPU();
                CPU.activeProcessor.run();
            }
        });
        thread2.start();
    }

    static void spoilCPU(){
        CPU.activeProcessor.spoil();
    }
}
