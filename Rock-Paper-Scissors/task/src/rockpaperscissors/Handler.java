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
        //Path path = Path.of("rating.txt");

        // LOCAL Path
        Path path = Path.of("Rock-Paper-Scissors/task/src/rockpaperscissors/rating.txt");

        User[] users = Handler.getUsersFromFile(path);

        boolean isFound = false;
        for (User user : users) {
            if (user.getName().equals(name)) {
                isFound = true;
                return user;
            }
        }
        if (!isFound) {
            try {
                return createUser(name, path);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public static String chooseTurnResult() {
        Random random = new Random();
        int rndNumber = random.nextInt(3);
        String turnResult = rndNumber == 0  ? "DRAW" : rndNumber == 1 ? "WIN" : "LOSS";
        return turnResult;
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

    public static void outputLineResultAdvanced(String options) {
        System.out.println("play with the advanced version");
        String[] optionsList = options.split(",");
        for (int i = 0; i < optionsList.length; i++) {
            optionsList[i] = optionsList[i].trim();
            System.out.println(optionsList[i]);
        }

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
        addNewUserToFile(path, name, 0);
        return user;
    }

    private static void addNewUserToFile(Path path, String name, int rating) throws IOException {
        //File file = new File(pathFileName);
        File file = path.toFile();
        FileWriter writer = new FileWriter(file, true);
        writer.write("\n" + name + " " + rating);
        writer.close();
    }

    public static User[] getUsersFromFile(Path path) {
        List<User> userList = new ArrayList<>();

        //File file = new File(fileName);
        File file = path.toFile();
        try(Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                String name = parts[0];
                int rating = Integer.parseInt(parts[1]);
                userList.add(new User(name, rating));
            }

        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + path.toString());
        }

        // Convert the list to an array and return it
        return userList.toArray(new User[userList.size()]);
    }

}
