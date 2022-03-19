/*
Player guess letters one at a time to match a auto-generated/hidden word
 */
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class HangMan {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String msg_continueOrQuitGame = "Do you want to play again? (yes or no)";
    private static ArrayList<String> misLetrLst = new ArrayList<>();
    private static ArrayList<String> goodLetrLst = new ArrayList<>();
    private static Map<Integer, String> wordHashMap = new HashMap<>();

    public static void main(String[] args) {
        final String greetMsg = "HANGMAN\n";
        final String banner = "+---+\n" +
                "    |\n" +
                "    |\n" +
                "    |\n" +
                "    ===\n";
        final String misLetrMsg = "Missed letters: %s\n%s";
        String word = "";
        String guess = "";
        int idxSize = 0, idxLetr = 0, numTry = 0, randNum = 0;
        boolean breakOut;

        buldWordHashMap();
        //Start game
        do {
            breakOut = false;
            System.out.println(greetMsg + banner);
            System.out.println("Missed letters: ");
            randNum = getRandomInt();
            word = generateWord(randNum);
            numTry = word.length()+2;
            System.out.println("Secret word: " + word);

            //clear lsts
            goodLetrLst.clear();
            misLetrLst.clear();

            //initialize good lst
            for (int i=0; i<word.length(); i++) {
                goodLetrLst.add("_");
            }

            //Print blanks for guess
            System.out.println(lstToString(goodLetrLst));
            //loop through guess tries based on the length of the word
            for (int i=0; i<word.length()+2; i++) {
                //get user guess
                guess = getGuess(numTry);
                numTry -= 1;
                idxSize = idxLstLetrMatch(word, guess).size();
                if (idxSize > 0) {
                    //add to goodLetrLst, with potential multiple occurrences
                    for (int j=0; j<idxSize; j++) {
                        idxLetr = idxLstLetrMatch(word, guess).get(j);
                        buldGoodLetrLst(guess, idxLetr);
                        if (j==idxSize-1)
                            System.out.println(lstToString(goodLetrLst));
                        if (word.equals(lstToString(goodLetrLst))) {
                            System.out.println("Yes! The secret word is \"" + lstToString(goodLetrLst) + "\"! You have won!");
                            breakOut = true;
                            break;
                        } else {
                            continue;
                        }
                    }
                } else {
                    if (chkAlreadyGuess(misLetrLst, guess)) {
                        System.out.println("You have already guessed that letter. Choose again.");
                        continue;
                    }
                    //add to miss letrs
                    buldMisLetrLst(guess);
                    System.out.println("Missed letters: " + lstToString(misLetrLst));
                }
                if (breakOut == true) {
                    break;
                }
            }
            if (breakOut == false)
                System.out.println("Game over, you lost!!");
        } while (continueOrQuit());
        System.out.println("Goodbye, see you again!");
    }

    //Method: get a word from a hashmap collection/dictionary with a random hashmap key
    public static String generateWord(int num) {
        String word = "";
        for (int key: wordHashMap.keySet()
             ) {
            word = wordHashMap.get(num);
        }
        return word;
    }

    //Method: build word dictionary as hashmap
    public static void buldWordHashMap() {
        wordHashMap.put(1, "car");
        wordHashMap.put(2, "elephant");
        wordHashMap.put(3, "house");
        wordHashMap.put(4, "computer");
        wordHashMap.put(5, "infrastructure");
        wordHashMap.put(6, "fish");
        wordHashMap.put(7, "man");
        wordHashMap.put(8, "universe");
        wordHashMap.put(9, "television");
        wordHashMap.put(10, "school");
        wordHashMap.put(11, "boat");
        wordHashMap.put(12, "candy");
        wordHashMap.put(13, "boy");
        wordHashMap.put(14, "running");
        wordHashMap.put(15, "walking");
        wordHashMap.put(16, "basketball");
        wordHashMap.put(17, "hospital");
        wordHashMap.put(18, "union");
        wordHashMap.put(19, "university");
        wordHashMap.put(20, "watermelon");
    }

    //Method: generate random number for hashmap key
    public static int getRandomInt() {
        return ThreadLocalRandom.current().nextInt(1, 20);
    }

    //Method: get valid user input
    public static String getGuess(int numTry) {
        final String askGuessMsg = "Guess a letter. You have ";
        String usrGuess = "";
        System.out.print(askGuessMsg+String.valueOf(numTry)+ " try => ");
        usrGuess = scanner.next();
        while (!chkUsrGuess(usrGuess)) {
            System.out.print("Invalid input, please enter one single letter => ");
            usrGuess = scanner.next();
        }
        return usrGuess;
    }

    //Method: build missed letters list
    public static void buldMisLetrLst(String str) {
        misLetrLst.add(str);
    }

    //Method: validate user input format
    public static boolean chkUsrGuess(String str) {
        return str.length() == 1 ? true : false;
    }

    //Method: build letter list matching the word for guessing
    public static void buldGoodLetrLst(String str, int idx) {
        goodLetrLst.remove(idx);
        goodLetrLst.add(idx, str);
    }

    //Method: check if letter already been input as guess
    public static boolean chkAlreadyGuess(ArrayList<String> lst, String str) {
        return lst.contains(str) ? true : false ;
    }

    //Method: convert list to string
    public static String lstToString(ArrayList<String> lst) {
        String retStr = "";
        for (int i=0; i<lst.size(); i++) {
            retStr += lst.get(i);
        }
        return retStr;
    }

    //Method: format string
    public static String strInterpol(String mainStr, String str) {
        return String.format(mainStr, str);
    }

    //Method: determine letter index in word
    public static int idxLetrMatch(String word, String str) {
        return word.indexOf(str);
    }

    //Method: return integer list for indexes for a matching letter
    // with more than one occurrences
    public static ArrayList<Integer> idxLstLetrMatch(String word, String str) {
        ArrayList<Integer> idxLst = new ArrayList<>();
        int i = word.indexOf(str);
        while(i >= 0) {
            idxLst.add(i);
            i = word.indexOf(str, i+1);
        }
        return idxLst;
    }

    //Method: obtain user input if wanting to continue playing game or exit
    public static boolean continueOrQuit() {
        System.out.println(msg_continueOrQuitGame);
        String ans = scanner.next();
        return !ans.equals("no");
    }
}