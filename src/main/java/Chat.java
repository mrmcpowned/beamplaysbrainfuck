import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.chat.BeamChat;
import pro.beam.api.resource.chat.events.IncomingMessageEvent;
import pro.beam.api.resource.chat.events.UserJoinEvent;
import pro.beam.api.resource.chat.methods.AuthenticateMessage;
import pro.beam.api.resource.chat.methods.ChatSendMethod;
import pro.beam.api.resource.chat.replies.AuthenticationReply;
import pro.beam.api.resource.chat.replies.ReplyHandler;
import pro.beam.api.resource.chat.ws.BeamChatConnectable;
import pro.beam.api.services.impl.ChatService;
import pro.beam.api.services.impl.UsersService;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.robot.RobotBuilder;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Chat {

    static BeamChatConnectable chatConnectable;
    static pro.beam.interactive.robot.Robot beamBot;

    public static void main(String[] args) throws Exception {
        JPanel panel = new JPanel();
        JLabel labelUsername = new JLabel("Enter the beam account username:");
        JTextField user = new JTextField(10);
        JLabel labelPass = new JLabel("Enter the account password:");
        JPasswordField pass = new JPasswordField(10);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(labelUsername);
        panel.add(user);
        panel.add(labelPass);
        panel.add(pass);
        String[] options = new String[]{"Cancel", "OK"};
        int option = JOptionPane.showOptionDialog(null, panel, "The title",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[1]);
        if(option == 0) System.exit(0);
        String stringPass = new String(pass.getPassword());
        String userText = user.getText();

//        int beamID = (int) new JSONObject(readUrl("https://beam.pro/api/v1/channels/" + userText + "?fields=id")).get("id");
        BeamAPI beam = new BeamAPI();
        BeamUser userBot = beam.use(UsersService.class).login(userText, stringPass).get();

        BeamChat chat = beam.use(ChatService.class).findOne(userBot.channel.id).get();
        chatConnectable = chat.connectable(beam);

        try {
            if (chatConnectable.connect()) {
                chatConnectable.send(AuthenticateMessage.from(userBot.channel, userBot, chat.authkey), new ReplyHandler<AuthenticationReply>() {
                    public void onSuccess(AuthenticationReply reply) {
                        chatConnectable.send(ChatSendMethod.of("Connection established. BeamPlaysProgramming is Online."));
                    }
                    public void onFailure(Throwable var1) {
                        var1.printStackTrace();
                    }
                });
            }

            chatConnectable.on(IncomingMessageEvent.class, event -> {
                if (event.data.message.message.get(0).text.startsWith("!ping")) {
                    chatConnectable.send(ChatSendMethod.of(String.format("@%s PONG!",event.data.userName)));
                }
                if (!event.data.userName.toLowerCase().equals("beamplaysprogramming")) {
                    chatConnectable.send(ChatSendMethod.of(String.format("%s", event.data.message.message.get(0).text)));
                }
            });

            chatConnectable.on(UserJoinEvent.class, event -> {
                chatConnectable.send(ChatSendMethod.of(
                        String.format("Hi %s! I'm pingbot! Write !ping and I will pong back!",
                                event.data.username)));
            });


           beamBot = new RobotBuilder()
                    .username(userText)
                    .password(stringPass)
                    .channel(userBot.channel.id).build(beam).get();

            /*beamBot.on(Protocol.Report.class, report -> {

                if (report.getScreenCount() > 0) {
                    Protocol.Coordinate coordMean = report.getScreen(0).getCoordMean();
                    if (!Double.isNaN(coordMean.getX()) && !Double.isNaN(coordMean.getY())) {
                        controller.mouseMove(
                                ((int) ((3840 * coordMean.getX()))),
                                ((int) ((2160 * coordMean.getY())))
                        );

                        System.out.println("Mean Coordinates X: " + coordMean.getX());
                        System.out.println("Mean Coordinates Y: " + coordMean.getY());
                    }

                }

                if (report.getJoystickCount() > 0) {
                    Protocol.Coordinate coordMean = report.getJoystick(0).getCoordMean();
                    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
                    if (!Double.isNaN(coordMean.getX()) && !Double.isNaN(coordMean.getY())) {
                        controller.mouseMove(
                                ((int) (mousePosition.getX() + (150 * coordMean.getX()))),
                                ((int) (mousePosition.getY() + (150 * coordMean.getY())))
                        );

                        System.out.println("Mean JoyCoord Coordinates X: " + coordMean.getX());
                        System.out.println("Mean JoyCoord Coordinates Y: " + coordMean.getY());
                    }

                }

                if (report.getTactileCount() > 0){
                    for (int i = 0; i < report.getTactileCount(); i++) {
                        if (report.getTactile(i).hasHolding()) {
                            System.out.println("Tactile ID: " + report.getTactile(i).getId());;
                            System.out.println("Tactile # Holding: " + report.getTactile(i).getHolding());;
                            System.out.println("Tactile Press Frequency: " + report.getTactile(i).getPressFrequency());;
                            System.out.println("Tactile Release Frequency: " + report.getTactile(i).getReleaseFrequency());;
                            System.out.println("Tactile Has Release Frequency: " + report.getTactile(i).hasReleaseFrequency());;
                            System.out.println("Tactile Has Press Frequency: " + report.getTactile(i).hasPressFrequency());;
                            System.out.println("Tactile Has Holding: " + report.getTactile(i).hasHolding());;
                        }
                    }
                }

            });*/
//            KeyPressListener keys = new KeyPressListener();
//            MouseMoveListener mice = new MouseMoveListener();
            beamBot.on(Protocol.Report.class, new KeyPressListener());

//            beamBot.on(Protocol.Report.class, new MouseMoveListener());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}