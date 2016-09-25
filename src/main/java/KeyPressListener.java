import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;

import java.awt.*;


public class KeyPressListener implements EventListener<Protocol.Report> {
    // This Robot is an AWT Robot, not a Beam one. It knows how to press keys on the keyboard.
    protected Robot keyboard;
    protected VisualAid aid = new VisualAid();

    // Basic no-args constructor, nothing special here...
    public KeyPressListener() {

    }

    // The handler method. This method is invoked when a new Report message is received over the wire.
    // It takes some information given in the Report message and does something with it (in this case,
    // it moves the mouse across the screen.
    @Override
    public void handle(Protocol.Report report) {
        // Iterate through each tactile key-press in the report's tactile list, and preform actions
        // based on the state of a particular TactileInfo instance.
        for (Protocol.Report.TactileInfo tactile : report.getTactileList()) {
            //Assign a variable to whatever the number of users currently pressing this key is
            double pressCount = tactile.getPressFrequency();
            //Assign the ID of the current button
            int buttonID = tactile.getId();
            // If the key is not pressed down, skip it.
            if (pressCount > 0) {
                System.out.println(pressCount + " press during this frame");
                System.out.println("Button #" + buttonID + " Pressed");
                new KeyHandler(buttonID);
                if (buttonID == 13) {
                    aid.update();
                }

            }
        }

    }
}