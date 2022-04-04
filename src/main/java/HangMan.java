/*
Player guess letters one at a time for a random secret word
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class HangMan {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Path filePath = Paths.get(System.getProperty("user.dir")+"\\src\\main\\resources\\words.txt");
    private static final String msg_continueOrQuitGame = "Do you want to play again? (yes or no)";
    private static ArrayList<String> misLetrLst = new ArrayList<>();
    private static ArrayList<String> goodLetrLst = new ArrayList<>();
    private static Map<Integer, String> wordHashMap = new HashMap<>();
    private static final String words = "car,elephant,house,computer,infrastructure,fish,man,universe,television," +
            "school,boat,candy,boy,running,walking,basketball,hospital,union,university,watermelon";

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
        int idxSize = 0, idxLetr = 0, numTry = 0, tryCount = 0, randNum = 0;
        boolean breakOut;
        //Create and update words dictionary file
        createWordsFile();
        updateWordsFile();
        //Read dictionary file into list
        List<String> wordLst = Arrays.asList(readWordsFile().split(","));
        //Build hashmap
        buldWordHashMap(wordLst);
        //Delete dictionary file
        deleteWordsFile();

        //Start game
        try {
            do {
                breakOut = false; //reset for each game
                System.out.println(greetMsg + banner);
                System.out.println("Missed letters: ");
                randNum = getRandomInt();
                word = generateWord(randNum);
                numTry = word.length() + 2; //number of tries is length of word plus 2
                tryCount = numTry;
                System.out.println("Secret word: " + word); //print out for testing purpose

                //clear lsts for each game
                goodLetrLst.clear();
                misLetrLst.clear();

                //initialize good guess lst for each game
                for (int i = 0; i < word.length(); i++) {
                    goodLetrLst.add("_");
                }

                //Print blanks for guess
                System.out.println(lstToString(goodLetrLst));

                //loop through guess tries
                for (int i = 0; i < numTry; i++) {
                    //get user guess
                    guess = getGuess(tryCount);
                    tryCount -= 1;
                    //get # of match(es) for each guess
                    idxSize = idxLstLetrMatch(word, guess).size();
                    if (idxSize > 0) {
                        //add to goodLetrLst, with one or more matches
                        for (int j = 0; j < idxSize; j++) {
                            idxLetr = idxLstLetrMatch(word, guess).get(j); //get index from word for the guess
                            buldGoodLetrLst(guess, idxLetr); //fill blank with matching guess
                            if (j == idxSize - 1) //condition to print successfully guessed word, part or whole so far with blank(s) filled
                                System.out.println(lstToString(goodLetrLst));
                            if (word.equals(lstToString(goodLetrLst))) { //chk if won
                                System.out.println("Yes! The secret word is \"" + lstToString(goodLetrLst) + "\"! You have won!");
                                breakOut = true;
                                break;
                            } else {
                                continue;
                            }
                        }
                    } else { //no match for guess
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
        } catch (Exception e) {
            System.out.println("Something went wrong, here is the stacktrace");
            e.printStackTrace();
        } finally {
            System.out.println("Goodbye, see you again!");
        }
    }

    //Method: create words.txt
    public static void createWordsFile() {
        try {
            Files.createFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method: update words.txt
    public static void updateWordsFile() {
        try {
            Files.writeString(filePath, words);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method: read words.txt
    public static String readWordsFile() {
        String retStr = "";
        try {
            retStr = Files.readString(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            retStr = "";
        } finally {
            return retStr;
        }
    }

    //Method: delete words.txt
    public static void deleteWordsFile() {
        try {
            Files.delete(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method: get a word from word hashmap/dictionary with a random key: num
    public static String generateWord(int num) {
        String word = "";
        for (int key: wordHashMap.keySet()
             ) {
            word = wordHashMap.get(num);
        }
        return word;
    }

    //Method: build word hashmap/dictionary
    public static void buldWordHashMap(List<String> lst) {
        for (int i=1; i<=lst.size(); i++) {
            wordHashMap.put(i, lst.get(i-1));
        }
    }

    //Method: generate random number as hashmap key
    public static int getRandomInt() {
        return ThreadLocalRandom.current().nextInt(1, 20);
    }

    //Method: get user guess/input
    public static String getGuess(int tryLeft) {
        final String askGuessMsg = "Guess a letter. You have ";
        String usrGuess = "";
        System.out.print(askGuessMsg+String.valueOf(tryLeft)+ " try => ");
        usrGuess = scanner.next();
        while (!chkUsrGuess(usrGuess)) {
            System.out.print("Invalid input, please enter one single letter => ");
            usrGuess = scanner.next();
        }
        return usrGuess;
    }

    //Method: build missed letters/wrong guesses list
    public static void buldMisLetrLst(String str) {
        misLetrLst.add(str);
    }

    //Method: validate user input
    public static boolean chkUsrGuess(String str) {
        return (str.length() == 1) && (str.toLowerCase().matches("[a-z]")) ? true : false;
    }

    //Method: build list for successfully guessed word so far, part or whole
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

    //Method: build list of index(es) for letters successfully guessed
    //can be one or more occurrences
    public static ArrayList<Integer> idxLstLetrMatch(String word, String str) {
        ArrayList<Integer> idxLst = new ArrayList<>();
        int i = word.indexOf(str);
        while(i >= 0) {
            idxLst.add(i);
            i = word.indexOf(str, i+1);
        }
        return idxLst;
    }

    //Method: get user input whether continue playing or exit
    public static boolean continueOrQuit() {
        System.out.println(msg_continueOrQuitGame);
        String ans = scanner.next();
        return !ans.equals("no");
    }
}