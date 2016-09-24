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
            double pressCount = tactile.getPressFrequency();
            int buttonID = tactile.getId();
//            Protocol.ProgressUpdate.Builder build = Protocol.ProgressUpdate.newBuilder();
//            Protocol.ProgressUpdate.TactileUpdate.Builder tacbuilder = Protocol.ProgressUpdate.TactileUpdate.newBuilder();

//            System.out.println(fired + " hasfired before keycheck");
//            System.out.println(tacbuilder.hasFired()+ " hasfired before keycheck");
//            System.out.println(tacbuilder.getFired() + " getfired before keycheck");
//            System.out.println(build.getTactileList().get(0).hasFired() + " hasfired before keycheck for build");
//            System.out.println(build.getTactileList().get(0).getFired() + " getfired before keycheck for build");
            // If the key is not pressed down, skip it.
//            System.out.println("Button #" + buttonID + " Pressed");
            if (pressCount > 0) {
//                System.out.println(fired + " getfired AFTER keycheck");
                // Otherwise, trigger an AWT key press with the same keycode.
//            this.keyboard.keyPress(buttonID);
                System.out.println(pressCount + " Are pressing the button");
//                System.out.println(tactile.getHolding() + " Are supposedly pressing the button");
                System.out.println("Button #" + buttonID + " Pressed");
//                Protocol.Report.Users.Builder users = Protocol.Report.Users.newBuilder();
//                int userData = users.getActive();
//                System.out.println(userData + " Active users");
//                int numCurrent = users.getActive();
//                System.out.println(numCurrent + " Users pressed buttons");

                Protocol.ProgressUpdate.Builder build = Protocol.ProgressUpdate.newBuilder();
                Protocol.ProgressUpdate.TactileUpdate.Builder tacbuilder = Protocol.ProgressUpdate.TactileUpdate.newBuilder();
                tacbuilder
                        .setId(buttonID)
                        .setFired(true)
                        .setProgress(1);
                if (buttonID == 13) {
//                    Chat.chatConnectable.send(ChatSendMethod.of(
//                            String.format("You pressed button UP which is button %s", buttonID)));
                    tacbuilder.setCooldown(30000);
                }
//                System.out.println(fired + " getfired");
                build.addTactile(tacbuilder);
                Protocol.ProgressUpdate pu = build.build();

                try {
                    Chat.beamBot.write(pu);
                    System.out.println("Sent visual update");
                    tacbuilder.setFired(false).setProgress(0);
                    build.addTactile(tacbuilder);
                    pu = build.build();
                    Chat.beamBot.write(pu);
                } catch (IOException ex) {
                    System.err.println("Failed to send packet.");
                    ex.printStackTrace();
                }

//                System.out.println("Added button");
            }
        }

    }
}