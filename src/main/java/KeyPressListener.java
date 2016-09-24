import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;

import java.awt.*;
import java.io.IOException;

public class KeyPressListener implements EventListener<Protocol.Report> {
    // This Robot is an AWT Robot, not a Beam one. It knows how to press keys on the keyboard.
    protected Robot keyboard;
    private boolean sendUpdate = false;

    // Basic no-args constructor, nothing special here...
    public KeyPressListener() {
        try {
            this.keyboard = new Robot();
        } catch (AWTException ignored) {
        }
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
//            System.out.println("Button #" + buttonID + " Pressed");
                // Otherwise, trigger an AWT key press with the same keycode.
//            this.keyboard.keyPress(buttonID);
                System.out.println(pressCount + " Are pressing the button");
//                System.out.println(tactile.getHolding() + " Are supposedly pressing the button");
                System.out.println("Button #" + buttonID + " Pressed");

                //In order to send back a visual response of buttons being pressed, we need to create a generic builder
                Protocol.ProgressUpdate.Builder build = Protocol.ProgressUpdate.newBuilder();
                //Then, we need to create an input specific builder
                Protocol.ProgressUpdate.TactileUpdate.Builder tacbuilder = Protocol.ProgressUpdate.TactileUpdate.newBuilder();
                //**Then**, within that builder, we apply properties that we want set.
                //NOTE: We can't actually get the state of the UI itself, and the properties we apply are persistent
                tacbuilder
                        .setId(buttonID)
                        .setFired(true)
                        .setProgress(1);

                if (buttonID == 13) {
                    //You could send a message via chat when a button is pressed
//                    Chat.chatConnectable.send(ChatSendMethod.of(
//                            String.format("You pressed button UP which is button %s", buttonID)));
                    //For this specific button (Build Button), we've added a global cooldown of 30s, represented in milliseconds
                    tacbuilder.setCooldown(30000);
                }
                build.addTactile(tacbuilder);
                Protocol.ProgressUpdate pu = build.build();

                try {
                    Chat.beamBot.write(pu);
                    System.out.println("Sent visual update");
                    //This stuff could probably be placed into a method/class
                    tacbuilder.setFired(false).setProgress(0);
                    build.addTactile(tacbuilder);
                    pu = build.build();
                    Chat.beamBot.write(pu);
                } catch (IOException ex) {
                    System.err.println("Failed to send packet.");
                    ex.printStackTrace();
                }

            }
        }

    }
}