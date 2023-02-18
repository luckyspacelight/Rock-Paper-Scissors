package rockpaperscissors;

public class Main {
    public static void main(String[] args) {

        boolean inGame = true;
        String userName = Handler.greetUser();
        final User currentUser = Handler.findUser(userName);
        String options = Handler.askForOptions();

        while (inGame) {
            String userInput = Handler.takeUserInput();
            if (userInput.equals("!exit")) {
                inGame = false;
                System.out.println("Bye!");
            } else if (userInput.equals("!rating")){
                System.out.println("Your rating: " + currentUser.getRating());
            } else {
                if (options.isEmpty() || options == null) {
                    Handler.outputLineResult(userInput, currentUser);
                } else {
                    Handler.outputLineResultAdvanced(userInput, options);
                }
            }
        }

    }
}
