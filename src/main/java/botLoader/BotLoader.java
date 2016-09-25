package botLoader;

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
import java.util.concurrent.ExecutionException;

public class BotLoader {

    static BeamChatConnectable chatConnectable;
    static pro.beam.interactive.robot.Robot beamBot;
    static KeyStats keypressStats;
    static KeyOut keylog = new KeyOut();

    public static void main(String[] args) throws Exception {
        /**
         * So, basically this huuuuge area of code is just so I can get a
         * JFrame to appear with a text and password field.
         * This pretty much makes this app usable for any beam account.
         */
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
        if (option == 0) System.exit(0);
        String stringPass = new String(pass.getPassword());
        String userText = user.getText();

        /**
         * Here we're creating a new Beam Object, which we'll use to build a user login and botLoader login
         */
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
                    chatConnectable.send(ChatSendMethod.of(String.format("@%s PONG!", event.data.userName)));
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


            /**
             * Here we're using an Object observer to update the stats file.
             * Since we have a function that's used to modify the
             */
            keypressStats = new KeyStats();
            StatFileWriter writeStats = new StatFileWriter(keypressStats.currentKeyStats());
            keypressStats.addObserver(writeStats);

            beamBot = new RobotBuilder()
                    .username(userText)
                    .password(stringPass)
                    .channel(userBot.channel.id).build(beam).get();

            /*
             * We basically pass all of the report data to the botLoader.KeyPressListener
             */
            beamBot.on(Protocol.Report.class, new KeyPressListener());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}