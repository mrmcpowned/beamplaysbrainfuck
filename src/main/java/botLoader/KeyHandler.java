package botLoader;

import pro.beam.interactive.net.packet.Protocol;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Here we're actually handling what happens when a key is pressed.
 */
public class KeyHandler {
    protected Robot keyboard;
    private int keyID;
    private KeyValues keyEnum;

    public KeyHandler(int keyID) {
        this.keyID = keyID;
        /**
         * Since we can't go equating ints to enums, we need to pull an enum
         * using our int
         */
        keyEnum = KeyValues.values()[keyID];
        /***
         * Now we create the robot that'll manage teh inputs for us
         */
        try {
            this.keyboard = new Robot();
        } catch (AWTException ignored) {
        }
        /***
         * Finally, we run out helper method which will actually sort through the switch
         * and update the UI
         */
        runHandler();
    }


    /**
     * As a workaround for default parameters, I just create a "super override" which will apply the defaults for me
     *
     * @param asciiKey The KeyCode for the key we wish to press
     * @param modified Whether or not we choose to modify the key, with the default modifier being SHIFT
     */
    private void executePress(int asciiKey, boolean modified) {
        executePress(asciiKey, modified, KeyEvent.VK_SHIFT);
    }

    /**
     * Executes the given key along with 1 modifier
     *
     * @param asciiKey The KeyCode for the key we wish to press
     * @param modified Whether or not we choose to modify the key
     * @param modifier The keycode of the modifier key to be used, e.g. CTRL
     */
    private void executePress(int asciiKey, boolean modified, int modifier) {
        if (modified) {
            keyboard.keyPress(modifier);
        }
        keyboard.keyPress(asciiKey);
        keyboard.keyRelease(asciiKey);
        if (modified) {
            keyboard.keyRelease(modifier);
        }
    }

    private Protocol.ProgressUpdate makeUpdate(Protocol.ProgressUpdate.TactileUpdate.Builder builder) {
        //In order to send back a visual response of buttons being pressed, we need to create a generic builder
        Protocol.ProgressUpdate.Builder build = Protocol.ProgressUpdate.newBuilder();
        //then we add the input specific builder to the general builder
        build.addTactile(builder);
        //finally we create a proper progress update
        Protocol.ProgressUpdate pu = build.build();
        //Then, we just ship it
        return pu;
    }

    /**
     * Our handler which will execute all the required functions to get a button pressed and visually updated on the UI
     */
    private void runHandler() {

        //Then, we need to create an input specific builder
        Protocol.ProgressUpdate.TactileUpdate.Builder tacbuilder = Protocol.ProgressUpdate.TactileUpdate.newBuilder();
        //**Then**, within that builder, we apply properties that we want set.
        //NOTE: We can't actually get the state of the UI itself, and the properties we apply are persistent
        tacbuilder
                .setId(keyID)
                .setFired(true)
                .setProgress(1);
        System.out.println("Button " + keyEnum.toString() + " was pressed");
        /***
         * All these values are fairly self explanatory.
         */
        switch (keyEnum) {
            case UP:
                executePress(38, false);
                break;
            case DOWN:
                executePress(40, false);
                break;
            case LEFT:
                executePress(37, false);
                break;
            case RIGHT:
                executePress(39, false);
                break;
            case BACKSPACE:
                executePress(8, false);
                break;
            case COMMA:
                executePress(KeyEvent.VK_COMMA, false);
                break;
            case PERIOD:
                executePress(KeyEvent.VK_PERIOD, false);
                break;
            case LBRACKET:
                executePress(KeyEvent.VK_OPEN_BRACKET, false);
                break;
            case RBRACKET:
                executePress(KeyEvent.VK_CLOSE_BRACKET, false);
                break;
            case LTHAN:
                executePress(KeyEvent.VK_COMMA, true);
                break;
            case GTHAN:
                executePress(KeyEvent.VK_PERIOD, true);
                break;
            case PLUS:
                executePress(KeyEvent.VK_EQUALS, true);
                break;
            case MINUS:
                executePress(KeyEvent.VK_MINUS, false);
                break;
            case BUILD:
                executePress(120, false);
                tacbuilder.setCooldown(60000);
                break;
            case STOP:
                executePress(KeyEvent.VK_F2, true, KeyEvent.VK_CONTROL);
                break;
            default:
                System.out.println("Unknown button of ID " + keyID);
        }
        BotLoader.keypressStats.addCount(keyID);
        BotLoader.keylog.outputToFile(keyEnum.toString());


        try {
            //I've simplified the building process here by creating a method that handles the
            //ProgressUpdate creation.
            BotLoader.beamBot.write(makeUpdate(tacbuilder));
            /**This one resets the state of the button on the UI
             * We also reuse the tacbuilder object, since it's only use is for being a template.
             * And, you know, "reduce, reuse, recycle"
             */
            BotLoader.beamBot.write(makeUpdate(tacbuilder.setFired(false).setProgress(0)));
        } catch (IOException ex) {
            System.err.println("Failed to send packet.");
            ex.printStackTrace();
        }


    }
}
