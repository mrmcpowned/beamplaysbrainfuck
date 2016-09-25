package botLoader;

import java.util.Arrays;
import java.util.Observable;

/**
 * This class will hold stats data about keypresses
 */
public class KeyStats extends Observable {
    /**
     * Before the revelation of using an array, I had separate values for each button.
     * Then I realized the button IDs are enumerable, so I can just add them to an array and call them
     * without any issue.
     */
    private long pressStats[] =  new long[KeyValues.values().length];
    private String currentOutput;


    public KeyStats(){
        Arrays.fill(pressStats, 0);
    }

    /**
     * Add to the current count of presses which starts at 0
     * @param KeyID ID of the key to increment the count for
     */
    synchronized public void addCount(int keyID){
        pressStats[keyID]++;
        System.out.println("Key " + keyID + " incremented to " + pressStats[keyID]);
        /**
         * Since this object is being watched, we need to notify observers of a change
         */
        currentOutput = currentKeyStats();
        setChanged();
        notifyObservers(currentOutput);
    }

    /**
     * Retrieves the current count of the submited ID
     * @param keyID ID of the key to get the count for
     * @return Count of presses for the selected keyID
     */
    public long getCount(int keyID){
        return pressStats[keyID];
    }

    /**
     * Returns a multi-lined string with current totals of presses for all of the buttons
     * @return Multi-lined string of total button presses
     */
    public String currentKeyStats(){
        String stringStats = "";
        for (int i = 0; i < pressStats.length; i++){
            stringStats += KeyValues.values()[i] + " Presses: " + pressStats[i] + " \n";
        }
        return stringStats;

    }
}
