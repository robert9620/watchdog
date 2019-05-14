import java.util.*;
import java.io.*;

public class Memory {
    private static int[] mem = new int[2000];
    private static boolean kernelMode = false;
    private static final int USER_BOUNDARY = 999;

    // set up the input file and listen for commands via the input stream
    public static void main(String[] args) {
        try {
            // setup memory with the given input file
            setupMemory(args[0]);

            // read input from input stream
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) { // process each line as they come in
                String line = scanner.nextLine(); // will be of the form "[r/w/k/u/e]:[int],[int]
                String commandFlag = line.split(":")[0];
                int address = Integer.parseInt(line.split(":")[1].split(",")[0]);
                int data = Integer.parseInt(line.split(":")[1].split(",")[1]);

                if (commandFlag.equals("r")) { // read
                    System.out.println(read(address));
                } else if (commandFlag.equals("w")) { // write
                    write(address, data);
                } else if (commandFlag.equals("k")) { // enter kernel mode
                    kernelMode = true;
                } else if (commandFlag.equals("u")) { // exit kernel mode
                    kernelMode = false;
                } else if (commandFlag.equals("e")) { // exit memory process
                    System.exit(0);
                } else { // unexpected command
                    throw new IllegalArgumentException("Invalid command " + commandFlag + ":" + address + "," + data);
                }
            }
        } catch (FileNotFoundException e) { // caught when input file is invalid or could not be opened
            System.out.println("Memory (FileNotFoundException): " + e);
            System.exit(0);
        } catch (IllegalArgumentException e) { // caught when there is a bad command flag
            System.out.println("Memory (IllegalArgumentException): " + e);
            System.exit(0);
        } catch (SecurityException e) { // caught when a memory violation is attempted
            System.out.println("Memory (SecurityException): " + e);
            System.exit(0);
        } catch (Exception e) { // anything else
            System.out.println("Memory (misc Exception): " + e);
            System.exit(0);
        }
    }

    // initialize memory with the given input file
    public static void setupMemory(String inputFile) throws FileNotFoundException {
        int elementIndex = 0;
        File file = new File(inputFile);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) { // read the input file line by line
            String line = scanner.nextLine().split(" ")[0]; // read everything up to a space

            try { // attempt to read a line
                if (line.indexOf(".") != -1) { // change load address marker read
                    elementIndex = Integer.parseInt(line.substring(1, line.length()));
                } else {
                    mem[elementIndex++] = Integer.parseInt(line);
                }
            } catch (Exception e) { // caught a blank line
                elementIndex--; // back up
            }
        }
    }

    // return mem[address] if address is good, otherwise throw exception
    public static int read(int address) throws SecurityException {
        if ((!kernelMode && address <= USER_BOUNDARY) || kernelMode) { // no issues accessing this address
            return mem[address];
        } else { // attempted to read a system address from user mode
            throw new SecurityException("Blocked an attempt to read system memory from user mode");
        }
    }

    // set mem[address] = data if address is good, otherwise throw exception
    public static void write(int address, int data) throws SecurityException {
        if ((!kernelMode && address <= USER_BOUNDARY) || kernelMode) { // no issues accessing this address
            mem[address] = data;
        } else { // attempted to write to a system address from user mode
            throw new SecurityException("Blocked an attempt to write to system memory from user mode");
        }
    }
}