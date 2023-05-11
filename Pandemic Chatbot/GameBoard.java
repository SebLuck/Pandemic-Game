import javax.swing.*;
import java.util.Objects;
import java.util.Scanner;

public class GameBoard {

    static Scanner shellInput;   
    private static boolean shellOpen = false;

    //The constants for the commands.
    static final int QUIT = 0;
    static final int PRINT_ACTIONS = 1;
    static final int PRINT_PLAYER_CARD = 2;
    static final int PRINT_LOCATION = 3;
    static final int PRINT_CONNECTIONS = 4;
    static final int PRINT_DISEASES = 5;
    static final int PRINT_CURED_DISEASE_NUM = 6;
    static final int PRINT_CITIES = 7;
    static final int PRINT_ADJACENT_CITIES = 8;
    static final int PRINT_MATE_CARD = 9;
    static final int PRINT_RESEARCH_STATION = 10;
    static final int PRINT_ROLE_DETAIL = 11;
    static final int PRINT_ALL_CITIES_WITH_3_CUBES = 12;


    static final String[] userNames = {"Al","Bob"};

    static final int NUMBER_USERS = 2;
    static int currentUser;
  //These are the users' location that can change.
    static int[] userLocation = {0, 0}; 



    static void initGameBoard(){
        currentUser = 0;
        userLocation[0] = 0;
        userLocation[1] = 0;
    }

    //Return the integer command.
    static int getUserInput() {
        boolean gotReasonableInput = false;
        int processedUserInput = -1;

        //Open up the scanner if it's not already open.
        if (!shellOpen) {
            shellInput = new Scanner(System. in);
            shellOpen = true;
        }
        printGameMenu();
        /*Loop until the user types in a command that is named. 
         * It may not be a valid move.*/
        while (!gotReasonableInput) {
            String userInput = shellInput.nextLine();
            System.out.println("The user typed:"+ userInput);
            //Translate the user's input to an integer.
            processedUserInput = processUserInput(userInput);
            if (processedUserInput >= 0)
                gotReasonableInput = true;
            else
                System.out.println("Please enter a valid number: ");
        }
        return processedUserInput;
    }

    //Print out all possible user actions.
    private static void printGameMenu() {
        System.out.println("-------------Menu--------------");
        System.out.println("1 - Perform an action.");
        System.out.println("2 - Print Player Cards.");
        System.out.println("3 - Print the current location.");
        System.out.println("4 - Display all connections.");
        System.out.println("5 - Print infected cities and the number of"
        		+ " cities infected.");
        System.out.println("6 - Print the number of diseases cured.");
        System.out.println("7 - Print all cities.");
        System.out.println("8 - Print all adjacent cities.");
        if(GameBoard.currentUser == 0){
            System.out.println("9 - Print " + userNames[1] + " Player Cards.");
        }else{
            System.out.println("9 - Print " + userNames[0] + " Player Cards.");
        }
        System.out.println("10 - Print all research stations.");
        System.out.println("11 - Print role detail.");
        System.out.println("12 - Print all cities with 3 "
        		+ "disease cubes.");
        System.out.println ("0 - Quit.");
        System.out.println("Enter a number: ");
    }

    //Get the users input and translate it to the constants.
    private static int processUserInput(String inputString) {
        if (inputString.compareTo("0") == 0)
            return QUIT;
        else if (inputString.compareTo("1") == 0)
            return PRINT_ACTIONS;
        else if (inputString.compareTo("2") == 0)
            return PRINT_PLAYER_CARD;
        else if (inputString.compareTo("3") == 0)
            return PRINT_LOCATION;
        else if (inputString.compareTo("4") == 0)
            return PRINT_CONNECTIONS;
        else if (inputString.compareTo("5") == 0)
            return PRINT_DISEASES;
        else if ((inputString.compareTo("6") == 0))
            return PRINT_CURED_DISEASE_NUM;
        else if ((inputString.compareTo("7") == 0)) {
            return PRINT_CITIES;
        }else if ((inputString.compareTo("8") == 0)) {
            return PRINT_ADJACENT_CITIES;
        } else if ((inputString.compareTo("9") == 0)) {
            return PRINT_MATE_CARD;
        }else if ((inputString.compareTo("10") == 0)) {
            return PRINT_RESEARCH_STATION;
        } else if ((inputString.compareTo("11") == 0)) {
            return PRINT_ROLE_DETAIL;
        }  else if ((inputString.compareTo("12") == 0)) {
            return PRINT_ALL_CITIES_WITH_3_CUBES;
        }else
            return -1;
    }

    //Handle the user's commands.
    static boolean processUserCommand(int userInput) {

        if (userInput == QUIT)
            return true;
        else if (userInput == PRINT_LOCATION) {
            printUserLocations();
            continueToMenu();
        }
        else if (userInput == PRINT_CITIES) {
            printCities();
            continueToMenu();
        }
        else if (userInput == PRINT_CONNECTIONS)
            printConnections();
        else if (userInput == PRINT_ADJACENT_CITIES) {
            printAdjacentCities();
            continueToMenu();
        }
        else if (userInput == PRINT_DISEASES) {
            Disease.printInfectedCities();
            continueToMenu();
        }
        else if (userInput == PRINT_ACTIONS)
            Action.printAction();
        else if (userInput == PRINT_PLAYER_CARD) {
            PlayerCard.printPlayerCard();
            continueToMenu();
        } else if (userInput == PRINT_CURED_DISEASE_NUM) {
            Disease.printDiseaseCured();
            continueToMenu();
        } else if (userInput == PRINT_MATE_CARD) {
            PlayerCard.printMateCard();
            continueToMenu();
        }else if (userInput == PRINT_RESEARCH_STATION) {
            ResearchStation.printAllResearchStation();
            continueToMenu();
        } else if (userInput == PRINT_ROLE_DETAIL) {
            Roles.printRoleDetail(currentUser);
            continueToMenu();
        } else if (userInput == PRINT_ALL_CITIES_WITH_3_CUBES) {
            Disease.displayCityWith3Cubes();
            continueToMenu();
        }
        return false;
    }

    //Print out all the users' locations.
     static void printUserLocations() {
        System.out.println("The current user is " + userNames[currentUser]);
        for (int i = 0; i < NUMBER_USERS; i++) {
            int printUserLocation = userLocation[i];

            System.out.println (userNames[i] + " is in " + 
            City.cities[printUserLocation]);
        }
    }

    //Print out the list of all the cities.
    static void printCities() {
        System.out.println(City.numberCities + " Cities.");
        for (int i = 0; i < City.numberCities; i++){
            System.out.println(City.cities[i]);
        }
    }

    //Display an interface with the connection of all the cities.
    static void printConnections() {
        ImageIcon image = new ImageIcon("map.png");
        JLabel label = new JLabel();
        label.setIcon(image);
        label.setHorizontalTextPosition(JLabel.CENTER); 
        label.setVerticalTextPosition(JLabel.TOP); 
        label.setVerticalAlignment(JLabel.CENTER); 
        label.setHorizontalAlignment(JLabel.CENTER); 

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(label);
        frame.pack();
        frame.setResizable(false);
    }

    //Print out the cities adjacent to the userLocation.
    static void printAdjacentCities() {
        for (int i = 0; i < City.numberCities; i++) {
            if (citiesAdjacent(userLocation[currentUser],i)) {
                System.out.println(City.cities[i]);
            }
        }
    }
    //Look through the connections and see if the city numbers are in them. 
     static boolean citiesAdjacent(int city1,int city2) {
        for (int i = 0; i < City.numberConnections; i ++) {
            if ((City.connections[0][i] == city1) &&
                    (City.connections[1][i] == city2))
                return true;
//Need to check both ways A to B and B to A as only one connection is stored.
            else if ((City.connections[0][i] == city2) &&
                    (City.connections[1][i] == city1))
                return true;
        }
        return false;
    }
    // Method to sleep the game for a certain time.
    static void sleepGame(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // handle the exception if the thread is interrupted while sleeping
            System.out.println("Error! Thread interrupted.");
        }
    }
    // Method to ask the user to continue by typing 1.
    static void continueToMenu(){
        Scanner scanner = new Scanner(System.in);
        String confirm = "";
        System.out.println("Type 1 to continue: ");
        confirm = scanner.nextLine();
        while(!Objects.equals(confirm, "1")){
            System.out.println("Type 1 to continue: ");
            confirm = scanner.nextLine();
        }
    }
}
