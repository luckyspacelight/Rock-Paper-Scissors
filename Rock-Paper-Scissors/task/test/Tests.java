import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Tests extends StageTest<String> {
    String[] cases = new String[]{"rock",
            "paper",
            "scissors"};
    int loses = 0;
    int wins = 0;
    int draws = 0;
    String fileName = "rating.txt";
    int startScore = 350;
    String userName = "Bob";

    CheckResult checkInvalidInput(String reply, String attach) {
        if (!reply.toLowerCase().contains("invalid"))
            return CheckResult.wrong("Looks like your program doesn't handle invalid inputs correctly.\n" +
                    "You should print 'Invalid input' if the input can't be processed.");
        return CheckResult.correct();
    }

    CheckResult checkValidInputs(String reply, String attach) {
        int results = 0;
        int attachInt = Integer.parseInt(attach);
        for (String s : reply.toLowerCase().split("\n")) {
            if (s.contains("sorry"))
                results++;
            if (s.contains("draw"))
                results++;
            if (s.contains("well done"))
                results++;
        }
        if (results != attachInt) {
            return CheckResult.wrong(String.format("Not enough results of the games were printed!\n" +
                            "Tried to input %s actions and got %s results of the games.\n" +
                            "Perhaps your program did not run enough games. Is it set up correctly to loop " +
                            "until the user inputs ‘!exit’? \nAlso, make sure you print the result of the " + 
                            "game in the correct format after each valid input!",
                    attach, results));
        }
        return CheckResult.correct();
    }

    CheckResult checkResults(String reply, String attach) {
        for (String s : reply.toLowerCase().split("\n")) {
            if (s.contains("sorry"))
                loses++;
            else if (s.contains("draw"))
                draws++;
            else if (s.contains("well done"))
                wins++;
        }
        CheckResult wrongRandomize = CheckResult.wrong(String.format(
                "The results of the games: %s wins, %s draws and %s loses\n" +
                        "The game is too easy to win. Is the computer being too predictable? " +
                        "The number of wins, draws and loses should be approximately the same.\n" +
                        "Perhaps you don't use the random module to choose random option.\n" +
                        "Also, make sure you output the results of " +
                        "the games the same way as was stated in the examples!\n" +
                        "If you are sure that you use the random module, try to rerun the tests!\n",
                wins, draws, loses));

        if (loses < 20)
            return wrongRandomize;
        if (draws < 20)
            return wrongRandomize;
        if (wins < 20)
            return wrongRandomize;

        return CheckResult.correct();

    }

    public List<TestCase<String>> generate() {
        String[] validInputCases = {String.format(
                "%s\n\nrock\npaper\nscissors\npaper\nscissors\nrock\npaper\nscissors\n!exit", userName),
                String.format(
                        "%s\n\nscissors\nscissors\nscissors\n!exit", userName)};
        String[] invalidInputCases = {String.format("%s\n\nrock\npaper\npaper\nscissors\nblabla\n!exit", userName),
                String.format(
                        "%s\n\nrock\ninvalid\n!exit", userName),
                String.format(
                        "%s\n\nrock\nrock\nrock\nrock-n-roll\n!exit", userName)};
        List<TestCase<String>> tests = new ArrayList<>();
        //Cases that checks multiple input
        for (String input : validInputCases) {
            TestCase<String> testCase = new TestCase<>();
            testCase.setCheckFunc(this::checkValidInputs);
            testCase.setAttach(String.valueOf(input.split("\n").length - 3));
            testCase.setInput(input);
            testCase.addFile(fileName, String.format(
                    "%s %s\nJane 200\nAlex 400", userName, startScore));
            tests.add(testCase);
        }
        //Cases that check invalid input
        for (String input : invalidInputCases) {
            TestCase<String> testCase = new TestCase<>();
            testCase.setCheckFunc(this::checkInvalidInput);
            testCase.setInput(input);
            testCase.addFile(fileName, String.format("%s %s\nJane 200\nAlex 400", userName, startScore));
            tests.add(testCase);
        }
        //Cases that check using random module
        String longInput = String.format("%s\n\n", userName) + "rock\n".repeat(100) +
                "!exit";
        TestCase<String> testCase = new TestCase<>();
        testCase.setCheckFunc(this::checkResults);
        testCase.setAttach("rock");
        testCase.setInput(longInput);
        testCase.addFile(fileName, String.format("%s %s\nJane 200\nAlex 400", userName, startScore));
        tests.add(testCase);
        //Case that checks score
        String[] temp = longInput.split("\n");
        temp[temp.length - 2] = "!rating";
        StringBuilder sb = new StringBuilder();
        for (String t : temp) {
            sb.append(t);
            sb.append("\n");
        }
        longInput = sb.toString();
        testCase = new TestCase<>();
        testCase.setCheckFunc(this::checkFile);
        testCase.setAttach("rock");
        testCase.setInput(longInput);
        testCase.addFile(fileName, String.format("%s %s\nJane 200\nAlex 400", userName, startScore));
        tests.add(testCase);
        //Case that check advanced options
        String[] options =
                ("rock,gun,lightning,devil,dragon,water,air,paper," +
                        "sponge,wolf,tree,human,snake,scissors,fire").split(
                        ",");
        sb = new StringBuilder();
        sb.append(String.format(
                "%s\nrock,gun,lightning,devil,dragon,water,air,paper," +
                        "sponge,wolf,tree,human,snake,scissors,fire\n", userName));
        Random r = new Random(System.nanoTime());
        for (int i = 0; i < 20; i++) {
            sb.append(String.format("%s\n", options[r.nextInt(options.length)]));
        }
        sb.append("!rating\n!exit");
        String advancedInput = sb.toString();
        testCase = new TestCase<>();
        testCase.setCheckFunc(this::checkAdvanced);
        testCase.setAttach(advancedInput);
        testCase.setInput(advancedInput);
        testCase.addFile(fileName, String.format("%s %s\nJane 200\nAlex 400", userName, startScore));
        tests.add(testCase);
        return tests;

    }

    CheckResult checkAdvanced(String reply, String attach) {
        List<String> attachArray = Arrays.stream(attach.split("\n")).collect(Collectors.toList());
        for (int i = 0; i < 2; i++) {
            attachArray.remove(0);
            attachArray.remove(attachArray.size() - 1);
        }
        List<String> options = Arrays.stream(
                ("rock,gun,lightning,devil,dragon,water,air," +
                        "paper,sponge,wolf,tree,human,snake,scissors,fire")
                        .split(",")).collect(Collectors.toList());

        if (!reply.toLowerCase().contains("okay, let's start"))
            return CheckResult.wrong(
                    "There is no \"Okay, let's start\" message in the output!");

        boolean isGameStarted = false;
        int i = 0;

        for (String line : reply.split("\n")) {
            if (line.toLowerCase().contains("okay, let's start")) {
                isGameStarted = true;
                continue;
            }
            if (!isGameStarted)
                continue;
            if (i == attachArray.size())
                break;
            String inp = attachArray.get(i);
            int index = options.indexOf(inp);
            List<String> temp = new ArrayList<>();
            for (int j = index + 1; j < options.size(); j++)
                temp.add(options.get(j));
            for (int j = 0; j < index; j++)
                temp.add(options.get(j));
            int half = options.size() / 2;
            List<String> lose = new ArrayList<>();
            for (int j = 0; j < half; j++)
                lose.add(temp.get(j));
            List<String> win = new ArrayList<>();
            for (int j = half; j < temp.size(); j++)
                win.add(temp.get(j));

            String lineLower = line.toLowerCase();
            if (!(lineLower.contains("draw") || lineLower.contains("sorry") || lineLower.contains("well done")))
                return CheckResult.wrong(String.format(
                        "This answer seems to be wrong:\"%s\". \n" +
                                "The game did not respond on a valid option with " +
                                "a correctly formatted win, lose, or draw result.\n", line));

            boolean optionsMentions = false;
            for (String opt : options)
                if (lineLower.contains(opt)) {
                    optionsMentions = true;
                    break;
                }
            if (!optionsMentions)
                return CheckResult.wrong(String.format(
                        "This answer seems to be wrong: \"%s\".\n" +
                                "This answer does not contain the name of a valid " +
                                "option chosen by the computer.\n", line));
            if (lineLower.contains("well done")) {
                boolean finded = false;
                for (String option : win) {
                    if (lineLower.contains(option)) {
                        finded = true;
                        break;
                    }
                }
                if (!finded)
                    return CheckResult.wrong("Wrong win!");
            } else if (lineLower.contains("draw") && !lineLower.contains(inp))
                return CheckResult.wrong("Wrong draw!");
            else if (lineLower.contains("sorry")) {
                boolean found = false;
                for (String option : lose) {
                    if (lineLower.contains(option)) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    return CheckResult.wrong("Wrong lose!");
            }
            i += 1;
        }
        return CheckResult.correct();

    }

    CheckResult checkFile(String reply, String attach) {
        if (!reply.toLowerCase().contains("enter your name"))
            return CheckResult.wrong(
                    "Seems like you did not offer the user to input their name. " +
                            "Your program should output \"Enter your name:\" before the start of the game.\n");
        if (!reply.toLowerCase().contains(String.format("hello, %s", userName).toLowerCase()))
            return CheckResult.wrong(
                    "Seems like you did not greet the user. " +
                            "Your program should output \"Hello, <user_name>\"\n");
        for (String line : reply.split("\n")) {
            String lowerLine = line.toLowerCase();
            if (lowerLine.contains("well done") && !lowerLine.contains("scissors"))
                return CheckResult.wrong(String.format(
                        "Wrong result of the game:\n> rock\n%s\nRock can only beat scissors!", line));
            else if (lowerLine.contains("draw") && !lowerLine.contains("rock"))
                return CheckResult.wrong(String.format(
                        "Wrong result of the game:\n> rock\n%s\n" +
                                "The game ends with a draw only when user " +
                                "option and computer choose the same option", line));
            else if (lowerLine.contains("sorry") && !lowerLine.contains("paper"))
                return CheckResult.wrong(String.format(
                        "Wrong result of the game:\n> rock\n%s\nOnly paper can beat rock!", line));
        }
        draws = 0;
        loses = 0;
        wins = 0;
        for (String s : reply.toLowerCase().split("\n")) {
            if (s.contains("sorry")) {
                loses++;
            } else if (s.contains("draw")) {
                draws++;
            } else if (s.contains("well done"))
                wins++;
        }
        int correctPoints = startScore + wins * 100 + draws * 50;
        if (!reply.contains(String.valueOf(correctPoints)))
            return CheckResult.wrong(
                    "Looks like you incorrectly calculated the player's score!\n" +
                            "Make sure that you took into account the " +
                            "user's initial score written in the file.\n" +
                            "For each draw, add 50 point to the score. " +
                            "For each user's win, add 100 to his/her score.\n" +
                            "In case the user loses, don't change the score. ");
        return CheckResult.correct();

    }
}
