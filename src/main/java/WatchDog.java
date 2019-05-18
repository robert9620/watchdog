import static java.lang.Thread.sleep;

public class WatchDog {
    public static int timer = 10;

    public WatchDog(){
        run();
    }
    synchronized void run(){
        while(true){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(timer <= 0){
                Thread thread2 = new Thread(new Runnable() {
                    public void run() {
                        CPU.activeProcessor = new CPU();
                        CPU.activeProcessor.run();
                    }
                });
                timer = 10;
                thread2.start();
            } else{
                System.out.println("watchdog czeka");
                timer--;
            }
            Main.view.setCounter(timer);
        }
    }
}
