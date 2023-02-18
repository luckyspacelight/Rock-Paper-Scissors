package rockpaperscissors;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

public class User {

    private String name;
    private int rating;

    public User(String name, int rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int earnedPoints) throws FileNotFoundException {
        this.rating = this.getRating() + earnedPoints;

        // Path for the CHECK
        //Path pathInputFile = Path.of("rating.txt");
        //Path pathTempFile = Path.of("temp.txt");

        // LOCAL Path
        Path pathInputFile = Path.of("Rock-Paper-Scissors/task/src/rockpaperscissors/rating.txt");
        Path pathTempFile = Path.of("Rock-Paper-Scissors/task/src/rockpaperscissors/temp.txt");


        File inputFile = pathInputFile.toFile();
        File tempFile = pathTempFile.toFile();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String currentLine;
            int count = 0;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(" ");
                String username = parts[0];
                if (username.equals(this.name)) {
                    currentLine = username + " " + this.rating;
                }
                writer.write(count != 0 ? System.getProperty("line.separator") : "");
                writer.write(currentLine);
                count++;
            }
            reader.close();
            writer.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }







    }
}
