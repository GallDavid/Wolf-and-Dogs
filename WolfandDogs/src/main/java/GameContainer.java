import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameContainer implements ActionListener {

    public static final String startText = "Start";
    public static final String restartText = "Restart";
    public static final String player1moves = "Player 1 is next ( Dogs ).";
    public static final String player2moves = "Player 2 is next ( Wolf ).";
    public static final String setWolfText = "Set the Wolf, Player 2 ! Pick any free black field.";

    public static Game game;

    public GameContainer() {

        startButton = new JButton(startText);
        startButton.setName("startButton");
        startButton.addActionListener(this);
        restartButton = new JButton(restartText);
        restartButton.setName("restartButton");
        restartButton.addActionListener(this);
    }

    private boolean gameHasStarted = false;
    private boolean isGameOver = false;

    public static boolean RIGHT_TO_LEFT = false;
    public static JButton startButton;
    public static JButton restartButton;
    public static JTextArea infoArea;


    public static void addComponentsToPane(Container pane) {

        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }

        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }

        game = new Game(); //everything in the table

        pane.add(startButton, BorderLayout.PAGE_START);

        //Make the center component big, since that's the
        //typical usage of BorderLayout.
        pane.add(game.getGui(), BorderLayout.CENTER);
        game.displayInfo("AFTER GUI SETUP");
        JTextArea player1Info = new JTextArea("   DOGS - PLAYER 1  ");
        pane.add(player1Info, BorderLayout.LINE_START);

        infoArea = new JTextArea("");
        infoArea.setBounds(100, 30, 1000, 2000);
        pane.add(infoArea, BorderLayout.PAGE_END);

        JTextArea player2Info = new JTextArea("   WOLF - PLAYER 2  ");
        pane.add(player2Info, BorderLayout.LINE_END);
    }

    static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("Dogs and Wolf");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        //Use the content pane's default BorderLayout. No need for
        //setLayout(new BorderLayout());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        System.out.println("clicked by " + button.getText() + " " + ((JButton) e.getSource()).getName());

        if (((JButton) e.getSource()).getText().equals(startText)) {
            startGame();
        }
    }

    private void restartGame() {
        this.gameHasStarted = false;
        this.restartButton.setText(restartText);

        this.game.setDogs();

    }

    private void startGame() {
        this.gameHasStarted = true;
        this.startButton.setText("Started");
    }
}