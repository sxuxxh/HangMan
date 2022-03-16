/*
Player guess letters one at a time to match a auto-generated/hidden word
 */
import java.util.ArrayList;
import java.util.Scanner;

public class HangMan {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final String greetMsg = "HANGMAN\n";
        final String banner = "+---+\n" +
                "    |\n" +
                "    |\n" +
                "    |\n" +
                "    ===\n";
        final String misLetrMsg = "Missed letters: %s\n%s";

        System.out.println(greetMsg+banner);
    }

    //Method:
    public static String generateWord() {
        return "dog";
    }

    //Method:
    public static String getGuess() {
        final String askGuessMsg = "Guess a letter";
        System.out.println(askGuessMsg);
        return scanner.next();
    }

    //Method:
    public static ArrayList<String> buldMisStr (String str) {
        ArrayList<String> misLetrLst = new ArrayList<>();
        misLetrLst.add(str);
        return misLetrLst;
    }

    //Method:
    public static ArrayList<String> buldDisplStr(String str) {
        ArrayList<String> displLetrLst = new ArrayList<>();
        displLetrLst.add(str);
        return displLetrLst;
    }

    //Method:
    public static String strInterpol(String mainStr, String misStr, String displStr) {
        return String.format(mainStr, misStr, displStr);
    }
}