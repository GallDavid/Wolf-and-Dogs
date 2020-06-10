import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BorderL implements ActionListener {

    public static final String startText = "Start";
    public static final String restartText = "Restart";
    public static final String player1moves = "Player 1 is next ( Dogs ).";
    public static final String player2moves = "Player 2 is next ( Wolf ).";
    public static final String setWolfText = "Set the Wolf, Player 2 ! Pick any free black field.";

    public static Figures figures;

    public BorderL() {

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

        figures = new Figures();

        pane.add(startButton, BorderLayout.PAGE_START);

        //Make the center component big, since that's the
        //typical usage of BorderLayout.
        JButton button = new JButton("TABLE GAME HERE");
        button.setPreferredSize(new Dimension(500, 500));
        pane.add(figures.getGui(), BorderLayout.CENTER);
        figures.displayInfo("AFTER GUI SETUP");
        JTextArea player1Info=new JTextArea("   DOGS - PLAYER 1  ");
        pane.add(player1Info, BorderLayout.LINE_START);

        infoArea=new JTextArea("INFO");
        infoArea.setBounds(100,30, 1000,2000);
        pane.add(infoArea, BorderLayout.PAGE_END);
   //     button = new JButton("Long-Named Button 4 (PAGE_END)");
    //    pane.add(button, BorderLayout.PAGE_END);

        JTextArea player2Info=new JTextArea("   WOLF - PLAYER 2  ");
       // button = new JButton("5 (LINE_END)");
        pane.add(player2Info, BorderLayout.LINE_END);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {

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

    public static void main(String[] args) {
        BorderL bl = new BorderL();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        System.out.println("clicked by " + button.getText() + " " + ((JButton) e.getSource()).getName());

        if (((JButton) e.getSource()).getText().equals(startText)){
            startGame();
        }
    }

    public void reactionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        System.out.println("clicked by " + button.getText() + " " + ((JButton) e.getSource()).getName());

        if (((JButton) e.getSource()).getText().equals(restartText)){
            restartGame();
        }
    }

    private void restartGame() {
        this.gameHasStarted = false;
        this.restartButton.setText(restartText);

        this.figures.setDogs();
        this.setInfo("Set the Wolf, Player 2 ! Pick any free black field.");

    }

    private void setUpBoard() {
        this.startButton.setText(setWolfText);
        this.figures.setDogs();
        System.out.println(this.figures.dogIndexesToString());
        this.setInfo(setWolfText);
    }

    private void startGame() {
        this.gameHasStarted = true;
        this.startButton.setText("Started");
        this.figures.setDogs();
        this.setInfo("Set the Wolf, Player 2 ! Pick any free black field.");
    }

    private void setInfo(String s) {
        this.infoArea.setText(player2moves + " " + s);
    }

}