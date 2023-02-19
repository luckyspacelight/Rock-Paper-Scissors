package rockpaperscissors;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static rockpaperscissors.Gesture.*;

public class Handler {

    final static int WIN_POINTS = 100;
    final static int DRAW_POINTS = 50;

    public static String greetUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        System.out.printf("Hello, %s%n", userName);
        return userName.trim();
    }

    public static User findUser(String name) {


        // Path for the CHECK
        Path path = Path.of("rating.txt");

        // LOCAL Path
        //Path path = Path.of("Rock-Paper-Scissors/task/src/rockpaperscissors/rating.txt");

        User[] users = Handler.getUsersFromFile(path);

        for (User user : users) {
            if (user.getName().equals(name)) {
                //isFound = true;
                return user;
            }
        }

        try {
            return createUser(name, path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static String chooseTurnResult() {
        Random random = new Random();
        int rndNumber = random.nextInt(3);
        return rndNumber == 0  ? "DRAW" : rndNumber == 1 ? "WIN" : "LOSS";
    }

    public static String chooseComputerGesture(String[] optionsList) {
        Random random = new Random();
        int rndNumber = random.nextInt(optionsList.length);

        for (int i = 0; i < optionsList.length; i++) {
            if (rndNumber == i) {
                return optionsList[i];
            }
        }
        throw new IllegalArgumentException("Invalid computer response");
    }

    public static void outputLineResult(String userInput, User currentUser) {

        try{
            Gesture chosenGesture = Gesture.fromString(userInput);
            String matchResult = chooseTurnResult();

            // Points management
            int earnedPoints = 0;
            if (matchResult.equals("WIN")) {
                earnedPoints = WIN_POINTS;
            } else if (matchResult.equals("DRAW")) {
                earnedPoints = DRAW_POINTS;
            }
            if (earnedPoints !=0) {
                currentUser.setRating(earnedPoints);
            }

            String message = "";
            switch (chosenGesture) {
                case SCISSORS -> {
                    if (matchResult.equals("LOSS")) {
                        message = "Sorry, but the computer chose " + SCISSORS.getLosesAgainst();
                    } else if (matchResult.equals("WIN")) {
                        message = "Well done. The computer chose " + SCISSORS.getWinsAgainst() + " and failed";
                    } else {
                        message = "There is a draw (" + chosenGesture.getName() + ")";
                    }
                }
                case ROCK -> {
                    if (matchResult.equals("LOSS")) {
                        message = "Sorry, but the computer chose " + ROCK.getLosesAgainst();
                    } else if (matchResult.equals("WIN")) {
                        message = "Well done. The computer chose " + ROCK.getWinsAgainst() + " and failed";
                    } else {
                        message = "There is a draw (" + chosenGesture.getName() + ")";
                    }
                }
                case PAPER -> {
                    if (matchResult.equals("LOSS")) {
                        message = "Sorry, but the computer chose " + PAPER.getLosesAgainst();
                    } else if (matchResult.equals("WIN")) {
                        message = "Well done. The computer chose " + PAPER.getWinsAgainst() + " and failed";
                    } else {
                        message = "There is a draw (" + chosenGesture.getName() + ")";
                    }
                }
            }

            System.out.println(message);

        } catch(IllegalArgumentException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void outputLineResultAdvanced(String userInput, String options, User currentUser) {

        // Creat the Option List
        String[] optionsList = options.split(",");
        for (int i = 0; i < optionsList.length; i++) {
            optionsList[i] = optionsList[i].trim();
        }

        try {
            String chosenGesture = checkInput(optionsList, userInput);
            String pcChoice = chooseComputerGesture(optionsList);
            getTheResult(optionsList, chosenGesture, pcChoice, currentUser);
        } catch(IllegalArgumentException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void getTheResult(String[] optionsList, String chosenGesture,
                                      String computerChoice, User currentUser) throws FileNotFoundException {

        int defeatedNumber = (optionsList.length - 1) / 2;

        // Find UserInput position
        //int userGesturePosition = 0;
        int distanceBetweenChoices = 0;
        int earnedPoints;


        for (int i = 0; i < optionsList.length; i++) {
            if (chosenGesture.equals(optionsList[i])) {
                //userGesturePosition = i;
                for (int j = i, count = 0; true; j++, count++) {
                    if (computerChoice.equals(optionsList[j])) {
                        distanceBetweenChoices = count;
                        break;
                    }
                    // To run the for loop again...
                    j = j == optionsList.length - 1 ? -1 : j;
                }
                break;
            }
        }

        if (distanceBetweenChoices == 0) {
            System.out.println("There is a draw (" + computerChoice + ")");
            earnedPoints = DRAW_POINTS;
            currentUser.setRating(earnedPoints);
            //return "DRAW";
        } else if (distanceBetweenChoices <= defeatedNumber) {
            System.out.println("Sorry, but the computer chose " + computerChoice);
            //return "LOSS";
        } else {
            System.out.println("Well done. The computer chose " +  computerChoice + " and failed");
            earnedPoints = WIN_POINTS;
            currentUser.setRating(earnedPoints);
            //return "WIN";
        }

    }

    public static String checkInput(String[] optionsList, String userInput) {
        for (String s : optionsList) {
            if (userInput.equals(s)) {
                return userInput;
            }
        }
        throw new IllegalArgumentException("Invalid input");
    }

    public static String takeUserInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String askForOptions(){
        Scanner scanner = new Scanner(System.in);
        String options = scanner.nextLine();
        System.out.println("Okay, let's start");
        return options;
    }

    public static User createUser(String name, Path path) throws IllegalArgumentException, IOException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        User user = new User(name, 0);
        addNewUserToFile(path, name);
        return user;
    }

    private static void addNewUserToFile(Path path, String name) throws IOException {
        //File file = new File(pathFileName);
        File file = path.toFile();
        FileWriter writer = new FileWriter(file, true);
        writer.write("\n" + name + " " + 0);
        writer.close();
    }

    public static User[] getUsersFromFile(Path path) {
        List<User> userList = new ArrayList<>();

        File file = path.toFile();
        try(Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                String name = parts[0];
                int rating = Integer.parseInt(parts[1]);
                userList.add(new User(name, rating));
            }

        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + path);
        }

        // Convert the list to an array and return it
        //return userList.toArray(new User[userList.size()]);
        return userList.toArray(new User[0]);
    }

}
