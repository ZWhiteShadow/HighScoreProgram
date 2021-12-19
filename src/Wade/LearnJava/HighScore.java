package Wade.LearnJava;

// Modified from this code https://stackoverflow.com/questions/12810460/joptionpane-input-dialog-box-program/12810518

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class HighScore {


    private final JTextField[] JTextFieldScoresArray;
    private final JTextField[] JTextFieldPlayersArray;
    private String[] sortedPlayersArray;
    private String[] unsortedPlayersArray;
    private int[] unsortedScoresArray;
    private int[] scoresArray;
    private int numberOfPlayers;
    private int[] newScoresArray;
    boolean[] isErrorPlayersArray;
    boolean[] isErrorScoresArray;
    ArrayList<Integer> correctList;
    String[] smallPlayersArray;
    int[] smallScoresArray;
    boolean useSmallArrays = false;


    public HighScore(boolean firstRun) {
        while (true) {
            UIManager.put("OptionPane.minimumSize",new Dimension(375,100));

            if (firstRun) { //https://stackoverflow.com/questions/24970176/joptionpane-handling-ok-cancel-and-x-button
                int n = JOptionPane.showOptionDialog(new JFrame(),  "This program ranks players by score",
                        "Welcome to the program", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, new Object[]{"Ok"}, JOptionPane.YES_OPTION);
                if (n == JOptionPane.CLOSED_OPTION) {
                    quit();
                }
            }
            String choice = JOptionPane.showInputDialog(null,"How Many Players 2-80: ","Input needed:", JOptionPane.QUESTION_MESSAGE);
            try {
                numberOfPlayers = Integer.parseInt(choice);
                if ((numberOfPlayers < 2) || (numberOfPlayers > 80)) {
                    JOptionPane.showMessageDialog(null, "Please enter number between 2-80", "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    break;
                }
            } catch (Exception e) {
                if (choice == null) {
                    quit();
                } else {
                    int n = JOptionPane.showOptionDialog(new JFrame(), "Please enter a whole positive whole number",
                            "Error!", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
                            null, new Object[]{"Ok"}, JOptionPane.YES_OPTION);
                    if (n == JOptionPane.CLOSED_OPTION) {
                        quit();
                    }
                }
            }
            firstRun = false;
        }

        JTextFieldScoresArray = new JTextField[numberOfPlayers]; // initialize array with length of number of players
        JTextFieldPlayersArray = new JTextField[numberOfPlayers];
        sortedPlayersArray = new String[numberOfPlayers];
        unsortedPlayersArray = new String[numberOfPlayers];
        unsortedScoresArray = new int[numberOfPlayers];
        scoresArray = new int[numberOfPlayers];
        newScoresArray = new int[numberOfPlayers];
        isErrorPlayersArray = new boolean[numberOfPlayers];
        isErrorScoresArray =  new boolean[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            JTextFieldScoresArray[i] = new JTextField(10);
            JTextFieldPlayersArray[i] = new JTextField(10);
        }
        if (firstRun) {
            JOptionPane.showMessageDialog(null,
                    "Please fill out all "  + numberOfPlayers + " boxes:\n\n" +
                            "Players: Letters only\n" +
                            "Scores: Whole, positive numbers, with no commas.\n",
                    "Please read. Instructions:", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private boolean quit() {
        int confirm = JOptionPane.showConfirmDialog(null, "Would you like to keep using the program?", "Important Question:",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            return true;
        }

        if (confirm == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "See you soon!", "End of Program", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
        return false;
    }

    private void displayGUI() {

        while (true) {
            Object[] options = {"Submit", "Different Number of Players"};
            int selection = JOptionPane.showOptionDialog(null, getPanel(), "Enter Name And Scores To Rank: ",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, null);
            if (selection == JOptionPane.CLOSED_OPTION) {
                quit();
            }
            if (selection == 1) {
                SwingUtilities.invokeLater(() -> new HighScore(false).displayGUI());
                break;
            }
            if (selection == 0) {
                boolean isError = false;

                String[] message = new String[8];
                int[] count = new int[8];
                for (int i = 0; i < count.length; i++) {
                    message[i] = "";
                    count[i] = 0;
                }

                correctList = new ArrayList<Integer>();

                for (int i = 0; i < numberOfPlayers; i++) {

                    String scoresText = JTextFieldScoresArray[i].getText();
                    String playersText = JTextFieldPlayersArray[i].getText();

                    JTextFieldScoresArray[i].setBackground(new Color(204, 255, 204)); // light green
                    JTextFieldPlayersArray[i].setBackground(new Color(204, 255, 204)); // light green
                    isErrorPlayersArray[i] = false;
                    isErrorScoresArray[i] = false;

                    JTextField green = new JTextField();
                    green.setBackground(new Color(204, 255, 204));

                    try {
                        scoresArray[i] = Integer.parseInt(scoresText);
                    } catch (Exception e) {
                        JTextFieldScoresArray[i].setBackground(new Color(255, 204, 204)); // light pink
                        //Idiot-proof messages
                        if (scoresText.equals("")) {
                            count[0] += 1;
                            message[0] = " had been left blank.\n";
                            System.setProperty("lightYellow", "#fee6c8");
                            JTextFieldScoresArray[i].setBackground(new Color(255, 255, 204)); //light yellow
                        }
                        if (scoresText.contains(",")) {
                            message[1] = " had a comma.\n";
                            count[1] += 1;
                        }
                        if (scoresText.contains("-")) {
                            message[2] = " had a minus sign.\n";
                            count[2] += 1;
                        }
                        if (scoresText.contains(".")) {
                            message[3] = " had a period.\n";
                            count[3] += 1;
                        }
                        if (scoresText.matches("-?\\d+(\\.\\d+)?")) {
                            message[4] = " had a large number.\n";
                            count[4] += 1;
                        }
                        if ((!scoresText.contains(",")) &&
                                (!scoresText.equals("")) &&
                                (!scoresText.contains("-")) &&
                                (!scoresText.contains(".")) &&
                                (!scoresText.matches("-?\\d+(\\.\\d+)?"))) {

                            message[5] = " was only text.\n";
                            count[5] += 1;
                        }
                        isError = true;
                        isErrorScoresArray[i] = true;
                    }
                    if (playersText.length() > 10) {
                        playersText = playersText.substring(0, 10);
                    }

                    unsortedPlayersArray[i] = playersText; // First 15

                    if (playersText.equals("")) {
                        message[6] = " were blank.\n";
                        count[6] += 1;
                        isError = true;
                        isErrorPlayersArray[i] = true;
                        JTextFieldPlayersArray[i].setBackground(new Color(255, 255, 204)); //light yellow
                    } else if (!playersText.matches("^[a-zA-Z\\s]*$")){ // letters and spaces only
                        message[7] = " were not letters only.\n";
                        count[7] += 1;
                        isError = true;
                        isErrorPlayersArray[i] = true;
                        JTextFieldPlayersArray[i].setBackground(new Color(255, 204, 204)); // light pink

                    }

                    if ((isErrorPlayersArray[i] == false) && (isErrorScoresArray[i] == false)) {
                        correctList.add(i);
                    }
                }

                StringBuilder fullMessage = new StringBuilder();

                if ((count[0] + count[1] + count[2] + count[3] + count[4] + count[5]) > 0) {
                    fullMessage.append("Number Field(s):\n");
                }
                for (int i = 0; i < 6; i++) {
                    if (count[i] != 0) {
                        message[i] = count[i] + message[i];
                    }
                    fullMessage.append(message[i]);
                }
                if ((count[6] + count[7]) > 0) {
                    fullMessage.append("\nPlayer Field(s):\n");
                    if (count[6] != 0) {
                        message[6] = count[6] + message[6];
                    }
                    if (count[7] != 0) {
                        message[7] = count[7] + message[7];
                    }
                }
                fullMessage.append(message[6]).append(message[7]);

                if ((correctList.size() > 1) && (correctList.size() != numberOfPlayers)) {
                    int understand = JOptionPane.showConfirmDialog(null, "You have completed " + correctList.size() +
                                    " out of " + numberOfPlayers + " players.\n\n Do you want to rank only these?\n\n", "You have partially completed the form:",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (understand == JOptionPane.YES_OPTION) {
                        smallPlayersArray = new String[correctList.size()];
                        smallScoresArray = new int[correctList.size()];
                        for (int i = 0; i < correctList.size(); i++) {
                            String tempText = JTextFieldPlayersArray[correctList.get(i)].getText(); // change to length of ten
                            smallPlayersArray[i] = tempText.substring(0, 10); //store new length
                            smallScoresArray[i] = Integer.parseInt(JTextFieldScoresArray[correctList.get(i)].getText());
                        }
                        useSmallArrays = true;
                        break;
                    }
                }


                if (isError) {
                    int understand = JOptionPane.showConfirmDialog(null, "The Following Error(s) were found in the program:\n\n" +
                                    "Will you correct the following?\n\n" + fullMessage, "Welcome to \"Advanced Error Reporting\" ©2021",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if ((understand == JOptionPane.CLOSED_OPTION) || (understand == JOptionPane.NO_OPTION)) {
                        quit();
                    }
                } else {
                    break;
                }
            }
        }

        if (useSmallArrays) {
            scoresArray = smallScoresArray;
            unsortedPlayersArray = smallPlayersArray;
            numberOfPlayers = correctList.size();
            sortedPlayersArray = new String[correctList.size()];
        }

        unsortedScoresArray = scoresArray.clone(); //Keep a copy of the original using clone otherwise would change with sort
        Arrays.sort(scoresArray); // change original array and sort in order - lowest to highest
        newScoresArray = scoresArray.clone(); //Created due to duplicates

        for (int i = 0; i < numberOfPlayers; i++) {
            for (int j = 0; j < numberOfPlayers; j++) {
                if (unsortedScoresArray[i] == scoresArray[j]) {
                    sortedPlayersArray[j] = unsortedPlayersArray[i]; // assign player name to empty array
                    unsortedScoresArray[i] = -1;  //solves duplicate issues
                    scoresArray[j] = -2; //solves duplicate issues
                }
            }
        }

        int scoreRank = 0;

        StringBuilder rankList = new StringBuilder();

        for (int player = numberOfPlayers - 1; player >= 0; player--) {
            scoreRank++;

            rankList.append(scoreRank).append(": ").append(sortedPlayersArray[player]).append(" ").append(newScoresArray[player]).append("\n");

            //If a players score is the same as the next players score don't count up
            // for instance 1st wade 500, 2nd  max 300  2nd bob 300 3rd fred 100
            //Like olympics if there is a tie then both people get that ranking

            try {
                if ((newScoresArray[player] == newScoresArray[player - 1])) {
                    scoreRank--;
                }
            } catch (Exception e) {
                //
            }
        }
        JOptionPane.showMessageDialog(null, rankList.toString(), "Your Players Ranked: ", JOptionPane.PLAIN_MESSAGE);
        if (quit()) {
            SwingUtilities.invokeLater(() -> new HighScore(false).displayGUI());
        }
    }

    private JPanel getPanel() {
        JPanel basePanel = new JPanel();
        //basePanel.setLayout(new BorderLayout(5, 5));
        basePanel.setOpaque(true);
        basePanel.setBackground(Color.gray);
        JPanel centerPanel = new JPanel();

        int coll = 1;

        if (numberOfPlayers > 5){
            coll += 1;
        }
        if (numberOfPlayers > 20){
            coll += 1;
        }
        if (numberOfPlayers > 45){
            coll += 1;
        }

        int rows = numberOfPlayers / coll;
        if (numberOfPlayers % (rows * coll) > 0) {
            rows++;
        }

        centerPanel.setLayout(new

                GridLayout(rows, 1, 5, 5));
        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setOpaque(true);
        centerPanel.setBackground(Color.WHITE);
        for (
                int i = 0;
                i < JTextFieldScoresArray.length; i++) {
            JPanel group = new JPanel();
            JLabel mLabel = new JLabel("Player: ");
            group.add(mLabel);
            group.setBackground(Color.lightGray);

            group.add(JTextFieldPlayersArray[i]);
            JLabel mLabel2 = new JLabel("Score: ");
            group.add(mLabel2);
            group.add(JTextFieldScoresArray[i]);
            centerPanel.add(group);
        }
        basePanel.add(centerPanel);
        return basePanel;
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> new HighScore(true).displayGUI());
    }
}