public class Main {

    public static void main(String[] args) {

        final GameContainer gameContainer = new GameContainer();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gameContainer.createAndShowGUI();
            }
        });
    }
}
