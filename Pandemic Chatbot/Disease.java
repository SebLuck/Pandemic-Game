import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Disease {
    /*These boolean variables will be set to true if the disease
    * is cured.*/
    static boolean blueDiseaseCured;
    static boolean yellowDiseaseCured;
    static boolean blackDiseaseCured;
    static boolean redDiseaseCured;

    static boolean blueDiseaseEradicated;
    static boolean yellowDiseaseEradicated;
    static boolean blackDiseaseEradicated;
    static boolean redDiseaseEradicated;

    static int diseaseCounter;

    static int curedDiseaseNum;
    static int eradicatedDiseaseNum;

    static int blueDiseaseCounter;
    static int yellowDiseaseCounter;
    static int blackDiseaseCounter;
    static int redDiseaseCounter;

    /*Method to initialize all the variables of the Disease Class*/
    static void initDisease(){
        blueDiseaseCured = false;
        yellowDiseaseCured = false;
        blackDiseaseCured = false;
        redDiseaseCured = false;
        blueDiseaseEradicated = false;
        yellowDiseaseEradicated = false;
        blackDiseaseEradicated = false;
        redDiseaseEradicated = false;
        curedDiseaseNum = 0;
        diseaseCounter = 0;
        eradicatedDiseaseNum = 0;
        blueDiseaseCounter = 0;
        yellowDiseaseCounter = 0;
        blackDiseaseCounter = 0;
        redDiseaseCounter = 0;
    }

    /*Display the cities with 3 disease cubes to the user.*/
    static void displayCityWith3Cubes(){
        String color;
        int counter = 0;
        for(int i = 0; i < City.diseaseCubes.length; i++){
            for(int j = 0; j < City.diseaseCubes[i].length; j++){
                if(City.diseaseCubes[i][j] == 3){
                    color = Disease.getColorByIndex(j);
                    System.out.println("There are 3 " + color + " cubes in "
                    + City.cities[i] + "!!!");
                    counter++;
                }
            }
        }
        if (counter == 0){
            System.out.println("There are no cities with 3 disease cubes.");
        }
    }

    /*This method will be called when the player will want to display
    * how many disease cubes there are in each city. */
    static void printInfectedCities() {
        for (int i = 0;  i < City.numberCities; i ++) {
            for(int j = 0; j < 4; j++){
                if (City.diseaseCubes[i][j] > 0) {
                    System.out.println(City.cities[i] + " has " +
                            City.diseaseCubes[i][j] + " " + getColorByIndex(j) 
                            + " cube(s).");
                }
            }
        }
        System.out.println("There are " + diseaseCounter + " "
        		+ "disease cube(s) on the board.");
        System.out.println("There are " + blueDiseaseCounter + ""
        		+ " blue cube(s) on the board");
        System.out.println("There are " + yellowDiseaseCounter + " "
        		+ "yellow cube(s) on the board");
        System.out.println("There are " + blackDiseaseCounter + " "
        		+ "black cube(s) on the board");
        System.out.println("There are " + redDiseaseCounter + ""
        		+ " red cube(s) on the board");
    }
    
    /* This method will allow the user to cure a disease if he/she has 5 player
     * cards of the same color and is at a research station. There will be an
     * if statement to check which disease the player can cure.
    */
    static void cureDisease(ArrayList<String> playerCard){
        int counterBlue = 0;
        int counterYellow = 0;
        int counterBlack = 0;
        int counterRed = 0;
        for(int i = 0; i < playerCard.size(); i++){
            String[] splitColor = playerCard.get(i).split(",");
            if(splitColor.length == 1){
                continue;
            }
            if(Objects.equals(splitColor[1], "Blue")){
                counterBlue++;
            } else if (Objects.equals(splitColor[1], "Yellow")) {
                counterYellow++;
            } else if (Objects.equals(splitColor[1], "Black")) {
                counterBlack++;
            } else {
                counterRed++;
            }
        }
        if((counterBlue >= 5 || counterYellow >= 5 || counterBlack >= 5 || 
           counterRed >= 5) && (ResearchStation.researchStation
                [GameBoard.userLocation[GameBoard.currentUser]])){
            boolean colorDiseaseCured;
            if(counterBlue >= 5){
                colorDiseaseCured = Disease.blueDiseaseCured;
                cureDiseaseColor(playerCard, colorDiseaseCured,
                        "Blue");
            }else if(counterYellow >= 5){
                colorDiseaseCured = Disease.yellowDiseaseCured;
                cureDiseaseColor(playerCard, colorDiseaseCured,
                        "Yellow");
            }else if(counterBlack >= 5){
                colorDiseaseCured = Disease.blackDiseaseCured;
                cureDiseaseColor(playerCard, colorDiseaseCured,
                        "Black");
            }else {
                colorDiseaseCured = Disease.redDiseaseCured;
                cureDiseaseColor(playerCard, colorDiseaseCured,
                        "Red");
            }
        } else if (!ResearchStation.researchStation[GameBoard.userLocation
                                         [GameBoard.currentUser]]) {
            System.out.println("You need to be in a research station to cure"
            		+ " a disease.");
            Action.printAction();
        } else{
            System.out.println("You need 5 player cards of the same color to"
            		+ " cure a disease.");
            Action.printAction();
        }

    }
    
    /* If the player has 5 blue player cards, it will consume the five cards
     * and mark the disease as cured in the game. If the player has more than
     * 5 player cards of the same color, the player will choose which card to
     * keep. curedDiseaseNum variable will be incremented by 1. */
    private static void cureDiseaseColor(ArrayList<String> playerCard,
                                         boolean colorDiseaseCured,
                                         String color) {
        if(!colorDiseaseCured){
            int counterColor = 0;
            if(Objects.equals(color, "Blue")){
                blueDiseaseCured = true;
            } else if (Objects.equals(color, "Yellow")) {
                yellowDiseaseCured = true;
            } else if (Objects.equals(color, "Black")) {
                blackDiseaseCured = true;
            } else if (Objects.equals(color, "Red")) {
                redDiseaseCured = true;
            }
            curedDiseaseNum++;
            ArrayList<String> newPlayerCard = new ArrayList<>();
            ArrayList<String> dropPlayerCard = new ArrayList<>();
            for(int i = 0; i < playerCard.size(); i++){
                String[] splitColor = playerCard.get(i).split(",");
                if(splitColor.length == 1){
                    newPlayerCard.add(playerCard.get(i));
                    continue;
                }
                if(!Objects.equals(splitColor[1], color)){
                    newPlayerCard.add(playerCard.get(i));
                }else{
                    counterColor++;
                    if(counterColor >= 6){
                        dropPlayerCard.add(playerCard.get(i));
                        chooseCardToKeep(dropPlayerCard,
                                newPlayerCard);
                    }else {
                        dropPlayerCard.add(playerCard.get(i));
                    }
                }
            }
            playerCard.clear();
            playerCard.addAll(newPlayerCard);
            System.out.println("The " + color + " disease has been cured. ");
            System.out.println("You can now remove all " + color
                    + " cubes from a city when using the remove disease"
                    + " action.");
            GameBoard.continueToMenu();
            Action.actionDone();
        }else{
            System.out.println("The " + color + " disease has already been"
                    + " cured.");
            Action.printAction();
        }
    }

    /* This method will be called when the player will have to choose which
     * card to keep when the player tries to cure a disease but has more than
     * 5 cards of the same color. The dropCard ArrayList will store all the
     * cards of the same color, and the newCard ArrayList will store the card
     * the player will keep. The numCard integer will represent the number of
     * cards of the same color the player has.*/
    static void chooseCardToKeep(ArrayList<String> dropCard, 
    		ArrayList<String> newCard){
        Scanner scanner = new Scanner(System.in);
        String keepMenu = "";
        System.out.println("Choose which card you want to keep:");
        for(int i = 0; i < dropCard.size(); i++){
            System.out.println((i+1) + ". " + dropCard.get(i));
        }
         if(Objects.equals(Roles.playerRoles[GameBoard.currentUser],
        		 "Scientist")){
            System.out.println("Enter a number: ");
            keepMenu = scanner.nextLine();
            while (!Objects.equals(keepMenu, "1") && 
            		(!Objects.equals(keepMenu, "2"))
                    && !Objects.equals(keepMenu, "3") && 
                    !Objects.equals(keepMenu, "4")
                    && !Objects.equals(keepMenu, "5")) {
                System.out.println("Please enter a valid number: ");
                keepMenu = scanner.nextLine();
            }
        }else{
             System.out.println("Enter a number: ");
             keepMenu = scanner.nextLine();
             while (!Objects.equals(keepMenu, "1") && 
            		 (!Objects.equals(keepMenu, "2"))
                     && !Objects.equals(keepMenu, "3") &&
                     !Objects.equals(keepMenu, "4")
                     && !Objects.equals(keepMenu, "5") &&
                     !Objects.equals(keepMenu, "6")) {
                 System.out.println("Please enter a valid number: ");
                 keepMenu = scanner.nextLine();
             }
         }
        newCard.add(dropCard.get(Integer.parseInt(keepMenu) - 1));
        dropCard.remove(Integer.parseInt(keepMenu) - 1);
    }

    /*This method will display how many diseases has been cured so
    far in the game.*/
    static void printDiseaseCured(){
        if(!blueDiseaseCured && !yellowDiseaseCured && !blackDiseaseCured 
        		&& !redDiseaseCured){
            System.out.println("There are no disease cured.");
        }else{
            if(curedDiseaseNum == 1){
                System.out.println("There are " + curedDiseaseNum
                		+ " disease cured:");
            }else{
                System.out.println("There are " + curedDiseaseNum 
                		+ " diseases cured:");
            }
            if (blueDiseaseCured){
                System.out.println("Blue Disease Cured.");
            }
            if (yellowDiseaseCured) {
                System.out.println("Yellow Disease Cured.");
            }
            if (blackDiseaseCured) {
                System.out.println("Black Disease Cured.");
            }
            if (redDiseaseCured){
                System.out.println("Red Disease Cured.");
            }
        }
    }
    
    /*Method that returns an index based on the color.*/
    static int getColorIndex(String color){
        if(Objects.equals(color, "Blue")){
            return 0;
        } else if (Objects.equals(color, "Yellow")) {
            return 1;
        } else if (Objects.equals(color, "Black")) {
            return 2;
        }else{
            return 3;
        }
    }
    
    /*Method that returns a color based on the index.*/
    static String getColorByIndex(int colorIndex){
        if(colorIndex == 0){
            return "Blue";
        } else if (colorIndex == 1) {
            return "Yellow";
        } else if (colorIndex == 2) {
            return "Black";
        }else{
            return "Red";
        }
    }
    
    /* Method which adds a certain amount to the disease color counter 
     * when a city gets diseases added to it.*/
    static void addDiseaseColorCounter(int colorIndex, int amount){
        if(colorIndex == 0){
            blueDiseaseCounter += amount;
        } else if (colorIndex == 1) {
            yellowDiseaseCounter += amount;
        } else if (colorIndex == 2) {
            blackDiseaseCounter += amount;
        }else{
            redDiseaseCounter += amount;
        }
    }
    
    /* Method which subtracts a certain amount from the disease color counter
     *  when a city gets diseases removed from it.*/
    static void subtractDiseaseColorCounter(int colorIndex, int amount){
        if(colorIndex == 0){
            blueDiseaseCounter -= amount;
        } else if (colorIndex == 1) {
            yellowDiseaseCounter -= amount;
        } else if (colorIndex == 2) {
            blackDiseaseCounter -= amount;
        }else{
            redDiseaseCounter -= amount;
        }
    }

    /*Method that will check if a disease has been eradicated or not.*/
    static void checkDiseaseEradicated(){
        if(blueDiseaseCounter == 0 && !blueDiseaseEradicated && 
        		blueDiseaseCured){
            System.out.println("All blue disease cubes have been eradicated.");
            blueDiseaseEradicated = true;
        }
        if (yellowDiseaseCounter == 0 && !yellowDiseaseEradicated && 
        		yellowDiseaseCured) {
            System.out.println("All yellow disease cubes have been"
            		+ " eradicated.");
            yellowDiseaseEradicated = true;
        }
        if (blackDiseaseCounter == 0 && !blackDiseaseEradicated && 
        		blackDiseaseCured) {
            System.out.println("All black disease cubes have been "
            		+ "eradicated.");
            blackDiseaseEradicated = true;
        }
        if (redDiseaseCounter == 0 && !redDiseaseEradicated && 
        		redDiseaseCured){
            System.out.println("All red disease cubes have been eradicated.");
            redDiseaseEradicated = true;
        }
    }

    /* This method will check if the player has the right amount of cards
     *  of the same color to cure a disease.*/
    static void checkCureDisease(ArrayList<String> playerCard, String role, 
    		int userLocation){
        System.out.println();
        int counterBlue = 0;
        int counterYellow = 0;
        int counterBlack = 0;
        int counterRed = 0;
        // Check how many cards of the same color the current player have.
        for(int i = 0; i < playerCard.size(); i++){
            String[] splitColor = playerCard.get(i).split(",");
            if(splitColor.length == 1){
                continue;
            }
            if(Objects.equals(splitColor[1], "Blue")){
                counterBlue++;
            } else if (Objects.equals(splitColor[1], "Yellow")) {
                counterYellow++;
            } else if (Objects.equals(splitColor[1], "Black")) {
                counterBlack++;
            } else {
                counterRed++;
            }
        }
        /* Tell the player to go use the cure disease action when he/she
         * has the right amount of cards of the same color.*/
        if(Objects.equals(role, "Scientist")){
            if((counterBlue >= 4 && !Disease.blueDiseaseCured) ||
                    (counterYellow >= 4 && !Disease.yellowDiseaseCured)||
                    (counterBlack >= 4 && !Disease.blackDiseaseCured) ||
                    (counterRed >= 4 && !Disease.redDiseaseCured)){
                if(ResearchStation.researchStation[userLocation]){
                    System.out.println("You have 4 cards of the same color and"
                    		+ " you are in a research station.");
                    System.out.println("Use the discover a cure action to cure"
                    		+ " one disease.");
                    System.out.println();
                }else{
                    System.out.println("You have 4 cards of the same color. "
                    		+ "Try to go in a research station to cure a "
                    		+ "disease.");
                    System.out.println();
                    ResearchStation.printAllResearchStation();
                }
                GameBoard.sleepGame(1000);
            }
        }else{
            if((counterBlue >= 5 && !Disease.blueDiseaseCured) ||
                    (counterYellow >= 5 && !Disease.yellowDiseaseCured)||
                    (counterBlack >= 5 && !Disease.blackDiseaseCured) ||
                    (counterRed >= 5 && !Disease.redDiseaseCured)){
                if(ResearchStation.researchStation[userLocation]){
                    System.out.println("You have 5 cards of the same color and"
                    		+ " you are in a research station.");
                    System.out.println("Use the discover a cure action to cure"
                    		+ " one disease.");
                    System.out.println();
                }else{
                    System.out.println("You have 5 cards of the same color. "
                    		+ "Try to go in a research station to cure a"
                    		+ " disease.");
                    System.out.println();
                    ResearchStation.printAllResearchStation();
                }
                GameBoard.sleepGame(1000);
            }
        }
    }

}
