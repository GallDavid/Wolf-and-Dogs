import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Game implements ActionListener {

    final int tableSize = 8;
    private final JPanel window = new JPanel(new BorderLayout(20, 2));
    private JButton[][] fields = new JButton[tableSize][tableSize];
    private JPanel table;
    private JButton wolf = getWolfButton();
    private JButton dog1 = getDogButton();
    private JButton dog2 = getDogButton();
    private JButton dog3 = getDogButton();
    private JButton dog4 = getDogButton();

    private static int lastDogRow;

    private static String lastMessage = "";

    // to store all the black fields, key is the index, where they lay
    private static  Map<Integer, JButton> blackFields = new HashMap<>();

    // list of indexes where blacks lay
    private static ArrayList<Integer> blackList =  new ArrayList<Integer>();

    // collection of the four dogs, keyed with indexes.
    private static  Map<Integer, JButton> dogs = new HashMap<>();

    // self-explanatory
    private static boolean isDogSelected;

    // Index of selected dog. If no dog is selected, it will be -1
    private static int selectedDogIndex;

    // Index of selected wolf
    private static int selectedWolfIndex;

    // For deciding weather game is started, are dogs set ?
    private static boolean isDogsSet;

    private static boolean isGameOver = false;

    private static boolean isWolfSet = false;
    private static boolean isWolfSelected = false;

    private boolean isGameStarted;
    static boolean isReadyToStart;

    // possible (yellow) moves of the selected dog or wolf
    private static ArrayList<Integer> possibleMoves = new ArrayList<>();;


    private static final String columns = "ABCDEFGH";
    private static boolean isPlayer1DogToMove = false;
    private static boolean isPlayer2WolfToMove = false;

    Game() {
        isDogsSet = false;
        isGameStarted = false;
        isReadyToStart = false;
        Gui();
    }

    private static void  highlightPossibleMoves(ArrayList<Integer> possibleMoves){
        for (int counter = 0; counter < possibleMoves.size(); counter++) {
            int buttonIndex = possibleMoves.get(counter);
            JButton button = blackFields.get(buttonIndex);
            button.setBackground(Color.YELLOW);
        }
    }

    JButton getDogButton() {
        final JButton button = new JButton();
        try {
            Image img = ImageIO.read(new FileInputStream("./src/main/resources/kutya.jpg"));
            button.setIcon(new ImageIcon(img));
            button.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    highlightDogs(false);
                    for (Map.Entry<Integer, JButton> entry : dogs.entrySet()) {
                        Integer dogKey = entry.getKey();
                        JButton value = entry.getValue();
                        if (value.equals(button) && isWolfSet && isPlayer1DogToMove){
                            isDogSelected = true;
                            selectedDogIndex = dogKey;
                            button.setBackground(Color.GREEN);
                            possibleMoves = getPossibleMovesOfIndexForDog(dogKey);
                            putYellowsBackToBlack(true);
                            highlightPossibleMoves(possibleMoves);
                        }
                        else {
                            System.out.println("PLAYER 2 ( WOLF ) is next. PLease chose a black field to place wolf.");
                        }
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return button;
    }

    private ArrayList<Integer> getPossibleMovesOfIndexForDog(Integer dogIndex) {
        ArrayList<Integer> poss = new ArrayList<>();
        int left = tableSize - 1;
        int right = tableSize + 1;

        int firstPossibleMove = dogIndex - right;
        if (firstPossibleMove > 0 && firstPossibleMove < 63 && blackList.contains(firstPossibleMove))
        poss.add(firstPossibleMove);

        int secondPossibleMove = dogIndex - left;
        if (secondPossibleMove > 0 && secondPossibleMove < 63 && blackList.contains(secondPossibleMove))
        poss.add(secondPossibleMove);

        return poss;
    }

    private ArrayList<Integer> getPossibleMovesOfIndexForWolf(Integer wolfIndex) {
        ArrayList<Integer> poss = new ArrayList<>();

        int firstPossibleMove = wolfIndex - 7;
        if (firstPossibleMove > 0 && firstPossibleMove < 63 && blackList.contains(firstPossibleMove) && !dogs.containsKey(firstPossibleMove))
            poss.add(firstPossibleMove);

        int secondPossibleMove = wolfIndex -9;
        if (secondPossibleMove > 0 && secondPossibleMove < 63 && blackList.contains(secondPossibleMove) && !dogs.containsKey(secondPossibleMove))
            poss.add(secondPossibleMove);

        int thirdPossibleMove = wolfIndex + 7;
        if (thirdPossibleMove > 0 && thirdPossibleMove < 63 && blackList.contains(thirdPossibleMove) && !dogs.containsKey(thirdPossibleMove))
            poss.add(thirdPossibleMove);

        int fourthPossibleMove = wolfIndex + 9;
        if (fourthPossibleMove > 0 && fourthPossibleMove < 63 && blackList.contains(fourthPossibleMove) && !dogs.containsKey(fourthPossibleMove))
            poss.add(fourthPossibleMove);

        return poss;
    }

    JButton getWolfButton() {
        JButton button = new JButton();

        try {
            Image img = ImageIO.read(new FileInputStream("./src/main/resources/wolf.jpg"));
            button.setIcon(new ImageIcon(img));
            button.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    possibleMoves = getPossibleMovesOfIndexForWolf(selectedWolfIndex);
                    System.out.println("Possible moves " + possibleMoves);
                    System.out.println("Selectedwolf " + selectedWolfIndex);
                    System.out.println("clicked WOLF");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return button;
    }

    public JButton createBlackButton(final int i, final int j){
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        JButton button = new JButton();
        button.setMargin(buttonMargin);
        ImageIcon icon = new ImageIcon(new BufferedImage
                (64, 64, BufferedImage.TYPE_INT_ARGB));
        button.setIcon(icon);
        button.setText(columns.substring(j, j + 1) + Integer.toString(i + 1));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                int index = getIndexFromCoord(i, j);
                updateReadyToStart();
                // Wolf to be set
                if (!isWolfSet && isDogsSet && blackFields.containsKey(index)) {
                    setWolf(index);
                    isPlayer1DogToMove = true;
                    isPlayer2WolfToMove = false;
                    selectedWolfIndex = index;
                    highlightDogs(true);
                }
                // Dog move to be done

                if (isReadyToStart && !isGameOver) {
                    if (possibleMoves.contains(index) && isDogSelected && isWolfSet && isPlayer1DogToMove && !isPlayer2WolfToMove) {
                        makeADogmove(selectedDogIndex, index);

                        if (!wolfCanMove()) {
                            System.out.println("Wolf lost");
                            isGameOver = true;
                            wolf.setBackground(Color.RED);
                        } else if (
                                (selectedWolfIndex == 56 ||
                                        selectedWolfIndex == 58 ||
                                        selectedWolfIndex == 60 ||
                                        selectedWolfIndex == 56) && isPlayer2WolfToMove) {
                            lastMessage = "Player 2 ( WOLF ) WON.";
                            highlightDogsToRed();
                            System.out.println("Farkas nyert");
                            isGameOver = true;
                        }
                        lastMessage = "Player 2 ( WOLF ) is next to move.";
                        displayInfo("Player 1 ( Dog ) moved from " + selectedDogIndex + " to " + index);
                        possibleMoves = getPossibleMovesOfIndexForWolf(selectedWolfIndex);
                    } else if (possibleMoves.contains(index) && isWolfSelected && isPlayer2WolfToMove) {
                        if (wolfCanMove()) {
                            makeAWolfmove(index);
                            lastMessage = "Player 1 ( DOG ) is next to move. Select a dog and move.";
                        } else {
                            lastMessage = "Player 1 ( DOG ) is next to move. Select a dog and move.";
                        }
                        displayInfo("Player 2 ( Wolf ) moved from " + selectedWolfIndex + " to " + index);
                    }
                    if (!isWolfSelected) {
                        putYellowsBackToBlack(true);
                        highlightDogs(true);
                    }
                }
            }else {
                    highlightDogsToRed();
                }
                }
        });

        return button;
    }

    private void highlightDogsToRed() {
        dog1.setBackground(Color.RED);
        dog2.setBackground(Color.RED);
        dog3.setBackground(Color.RED);
        dog4.setBackground(Color.RED);
    }

    private boolean wolfCanMove() {
        possibleMoves = getPossibleMovesOfIndexForWolf(selectedWolfIndex);

        System.out.println("BEFORE " + possibleMoves.toString());
        for (int i = 0; i < possibleMoves.size(); i++) {
            System.out.println("actual ind " +  possibleMoves.get(i));
            if (dogs.containsKey(possibleMoves.get(i))) {
                System.out.println("Removing " + possibleMoves.get(i) );
                possibleMoves.remove(i);
            }
        }
        System.out.println(possibleMoves.toString());
        return isPlayer2WolfToMove && !possibleMoves.isEmpty();
    }

    private void makeAWolfmove(int index) {

        if(index == 56 || index == 58 || index == 60 || index == 62 ) {
            isGameOver = true;
            highlightDogsToRed();
        }

        putBackToBlack(index);
        putField(wolf, index);
        putYellowsBackToBlack(true);
        isPlayer2WolfToMove = false;
        highlightField(wolf, false);
        selectedWolfIndex = index;
        highlightDogs(true);
        isWolfSelected = false;
        isPlayer1DogToMove = true;
    }

    public JButton createWhiteButton(int i, int j){
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        JButton button = new JButton();
        button.setMargin(buttonMargin);
        ImageIcon icon = new ImageIcon(new BufferedImage
                (64, 64, BufferedImage.TYPE_INT_ARGB));
        button.setIcon(icon);
        button.setText(columns.substring(j, j + 1) + Integer.toString(i + 1));
        button.setBackground(Color.WHITE);
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("clicked WHITE");
            }
        });

        return button;
    }

    public final void Gui() {
        window.setBorder(new EmptyBorder(8, 8, 8, 8));

        table = new JPanel(new GridLayout(8, 8));
        table.setBorder(new LineBorder(Color.BLACK));
        window.add(table);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button;
                if (isWhiteButton(i, j)) {
                    button = createWhiteButton(i, j);
                } else {
                    button = createBlackButton(i, j);
                    int index = getIndexFromCoord(i, j);
                    blackFields.put(index, button);
                    blackList.add(index);

                }
                fields[i][j] = button;
                table.add(button);
                button.addActionListener(this);
            }

        }
        this.setDogs();
        System.out.println("GUI STOOD UP");
    }

    private boolean isWhiteButton(int i, int j) {
        return   (i % 2 == 1 && j % 2 == 1) || (j % 2 == 0 && i % 2 == 0);
    }

    public final JComponent getGui() {
        return window;
    }

    int getIndexFromCoord(int i, int j) {
        return (i * 8) + (j );
    }

    public void actionPerformed(ActionEvent e) {
    }

    private void updateReadyToStart() {
        isReadyToStart = isDogsSet && isWolfSet;
    }

    private void makeADogmove(int selectedDogInd, int index) {
        JButton dog = dogs.get(selectedDogInd);
        putBackToBlack(index);
        putField(dog, index);
        putYellowsBackToBlack(true);
        isPlayer2WolfToMove = true;

        highlightField(dog, false);
        highlightField(wolf, true);
        dogs.remove(selectedDogInd);
        selectedDogIndex = -1;
        ArrayList<Integer> possibleMoves = getPossibleMovesOfIndexForWolf(selectedWolfIndex);
        highlightPossibleMoves(possibleMoves);
        isWolfSelected = true;
        isDogSelected = false;
        dogs.put(index, dog);
        isPlayer1DogToMove = false;
        isPlayer2WolfToMove = true;


    }

    private void highlightField(JButton dog, boolean set) {
        if (set) {
            dog.setBackground(Color.GREEN);
        }
        else {
            dog.setBackground(null);
        }
    }


    private void highlightDogs(boolean b) {
        if (b) {
            dog1.setBackground(Color.CYAN);
            dog2.setBackground(Color.cyan);
            dog3.setBackground(Color.CYAN);
            dog4.setBackground(Color.CYAN);
        }
        else {
            dog1.setBackground(null);
            dog2.setBackground(null);
            dog3.setBackground(null);
            dog4.setBackground(null);
        }
    }

    private static void putYellowsBackToBlack(boolean b) {
        if (b) {
            for (int i = 0; i < blackFields.size(); i++) {
                for (Map.Entry<Integer, JButton> entry : blackFields.entrySet()) {
                    Integer k = entry.getKey();
                    JButton button = entry.getValue();
                    button.setBackground(Color.BLACK);
                }
            }
        }
        else {

        }
    }

    public void setDogs() {
        putField(dog1, 56);
        dogs.put(56, dog1);
        putField(dog2, 58);
        dogs.put(58, dog2);
        putField(dog3, 60);
        dogs.put(60, dog3);
        putField(dog4, 62);
        dogs.put(62, dog4);
        isDogsSet = true;
    }

    public void setWolf(int i) {
        putField(wolf, i);
        isWolfSet = true;
    }

    private void putField(JButton dog, int i) {
        table.remove(i);
        table.add(dog, i);
        table.revalidate();
        table.repaint();
    }

    private void putBackToBlack(int i) {
        if (isWolfSelected) {
            JButton black = blackFields.get(selectedWolfIndex);
            table.remove(selectedWolfIndex);
            table.add(black, selectedWolfIndex);
        }
        if (isDogSelected) {
            JButton black = blackFields.get(selectedDogIndex);
            table.remove(selectedDogIndex);
            table.add(black, selectedDogIndex);
        }

        table.revalidate();
        table.repaint();
    }

    public void displayInfo(String i){
        StringBuilder sb = new StringBuilder();
        sb.append("\n====================================");
        sb.append("\n STARTING: " + i);
        sb.append("\ndogsAreSet = " + isDogsSet);
        sb.append("\nwolfIsSet = " + isWolfSet);
        sb.append("\nisReadyToStart = " + isReadyToStart);
        sb.append("\nplayer1DogToMove  = " + isPlayer1DogToMove);
        sb.append("\nplayer2WolfToMove  = " + isPlayer2WolfToMove);
        sb.append("\nisDogSelected  = " + isDogSelected);
        sb.append("\nselectedDogIndex  = " + selectedDogIndex);
        sb.append("\nisWolfSelected  = " + isWolfSelected);
        sb.append("\nselectedWolfIndex  = " + selectedWolfIndex);
        sb.append("\npossibleMoves  = " + possibleMoves);

        sb.append("\n END OF : " + i);
        sb.append("\n====================================");
        System.out.println(sb.toString());
    }
}
