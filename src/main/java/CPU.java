import java.awt.*;
import java.util.*;
import java.io.*;

import static java.lang.Thread.sleep;

public class CPU {
    public static CPU activeProcessor;
    private boolean error = false;
    private static int pc = 0;
    private static int sp = 1000;
    private static int ir = 0;
    private static int ac = 0;
    private static int x, y;
    private static int instructionsToInterrupt;
    private static int instructionsSinceInterrupt = 0;
    private static boolean exitFlag = false;
    private static boolean kernelModeFlag = false;
    private static Scanner input;
    private static PrintWriter output;

    public CPU(){
        Main.view.setCPUWorksColor(Color.GREEN);
        Main.view.setRestarted(pc);
        pc++;
        System.out.println("Odpalenie procesora nr: " + pc);
    }

    synchronized void run() {
        while(true){
            try {
                sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!error){
                WatchDog.timer = 10;
                Main.view.setCounter(WatchDog.timer);
                System.out.println("Zrestartowanie odliczania w watchdog przez procesor numer: " + pc);
            }
        }
    }

    void spoil(){
        error = true;
        Main.view.setCPUWorksColor(Color.RED);
    }

    public CPU(InputStream memInputStream, OutputStream memOutputStream, int instructionsToInterrupt) {
        this.input = new Scanner(memInputStream);
        this.output = new PrintWriter(memOutputStream);
        this.instructionsToInterrupt = instructionsToInterrupt;
    }

    public static void writeToMemory(int address, int data) {
        output.println("w:" + address + "," + data);
        output.flush();
    }

    public static int readFromMemory(int address) {
        // make a request to the memory process
        output.println("r:" + address + ",-1");
        output.flush();

        // get the response
        String response = input.nextLine();

        try {
            return Integer.parseInt(response);
        } catch (Exception e) {
            // print the exception
            System.err.println(response);

            // kill the memory process
            output.println("e:0,-1");

            // exit
            System.exit(0);
        }

        return -1;
    }

    public static void fetchIntoIR() {
        ir = readFromMemory(pc++);
    }

    public static int pop() {
        return readFromMemory(sp++);
    }

    public static void push(int element) {
        writeToMemory(--sp, element);
    }

    public static void enterKernelMode(int handlerAddr) {
        // System.out.println("entering kernel mode!");
        // update kernel mode flags
        kernelModeFlag = true;
        output.println("k:0,0"); // tell memory to enter kernel mode
        output.flush();

        // push registers onto to the stack
        writeToMemory(1999, sp);
        writeToMemory(1998, pc);

        // update pc and sp for system memory
        pc = handlerAddr;
        sp = 1998;
    }

    public static void exitKernelMode() {
        // restore sp and pc
        pc = pop();
        sp = pop();

        // update kernel mode flags
        kernelModeFlag = false;
        output.println("u:0,0"); // tell memory to exit kernel mode
        output.flush();
    }

    public static void startCPU() {
        int instructionsSinceInterrupt = 0;

        while (true) {
            if (instructionsSinceInterrupt < instructionsToInterrupt) { // interrupt timer hasn't expired
                // don't interrupt
                fetchIntoIR();
                processInstruction(ir);

                // only update timer in user mode to prevent interrupts in kernel mode
                if (!kernelModeFlag) {
                    instructionsSinceInterrupt++;
                }
            } else { // interrupt timer expired
                instructionsSinceInterrupt = 0;
                enterKernelMode(1000);
            }
        }
    }

    public static void processInstruction(int instrNum) {
        switch (instrNum) {
            case 1:
                fetchIntoIR();
                ac = ir;
                break;
            case 2:
                fetchIntoIR();
                ac = readFromMemory(ir);
                break;
            case 3:
                fetchIntoIR();
                ac = readFromMemory(readFromMemory(ir));
                break;
            case 4:
                fetchIntoIR();
                ac = readFromMemory(ir + x);
                break;
            case 5:
                fetchIntoIR();
                ac = readFromMemory(ir + y);
                break;
            case 6:
                ac = readFromMemory(sp + x);
                break;
            case 7:
                fetchIntoIR();
                writeToMemory(ir, ac);
                break;
            case 8:
                ac = 1 + (int)(99*Math.random()); // Math.random() yields a random num between 0 and 99, so add 1
                break;
            case 9:
                fetchIntoIR();
                int port = ir;

                // System.out.println("got port: " + port + ", ac: " + ac);
                if (port == 1) {
                    System.out.print(ac);
                } else if (port == 2) {
                    System.out.print((char) ac);
                } else {
                    // invalid port. wat do
                }
                break;
            case 10:
                ac += x;
                break;
            case 11:
                ac += y;
                break;
            case 12:
                ac -= x;
                break;
            case 13:
                ac -= y;
                break;
            case 14:
                x = ac;
                break;
            case 15:
                ac = x;
                break;
            case 16:
                y = ac;
                break;
            case 17:
                ac = y;
                break;
            case 18:
                sp = ac;
                break;
            case 19:
                ac = sp;
                break;
            case 20:
                fetchIntoIR();
                pc = ir;
                break;
            case 21:
                fetchIntoIR();

                if (ac == 0) {
                    pc = ir;
                }
                break;
            case 22:
                fetchIntoIR();

                if (ac != 0) {
                    pc = ir;
                }
                break;
            case 23:
                fetchIntoIR();
                push(pc);
                pc = ir;
                break;
            case 24:
                pc = pop();
                break;
            case 25:
                x++;
                break;
            case 26:
                x--;
                break;
            case 27:
                push(ac);
                break;
            case 28:
                ac = pop();
                break;
            case 29:
                enterKernelMode(1500);
                break;
            case 30:
                exitKernelMode();
                break;
            case 50:
                output.println("e:0,-1"); // tell memory to exit
                output.flush();
                System.exit(0); // exit cpu
                break;
            default:
                // throw exception?
                break;
        }
    }
}				