package botLoader;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

/**
 * This class handles writing to a file for the stats.
 */
public class StatFileWriter implements Observer {
    private String statFileName = "TotalStats.txt";
    private String statOutput;

    public StatFileWriter(String input){
        statOutput = input;
        writeToFile(statFileName);
    }

    synchronized private void writeToFile(String path){
        PrintWriter output;
        try {
            output = new PrintWriter(path);
            output.println(statOutput);
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mandatory to implement observer
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
//        System.out.println("Object " + o + " notified with argument " + arg);
        statOutput = arg.toString();
        writeToFile(statFileName);
    }
}
