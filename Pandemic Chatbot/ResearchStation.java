import java.util.ArrayList;
import java.util.Objects;

public class ResearchStation {
    static int researchStationNum = 1;
    static ArrayList<String> researchStationCity;
    static boolean[] researchStation;
    /* Method that initializes the array of researchStationCity. This array
     * will store all the cities that have research stations. A research
     * station is added to Atlanta by default.*/
    static void initResearchStationArray(){
        researchStationCity = new ArrayList<>();
        researchStationCity.add(City.cities[0]);
    }
	
    /*This will be called every time the user returns to the menu to check if
     * there is a research station in the current city.*/
    static void researchStationCurrentCity(){
        if(researchStation[GameBoard.userLocation
                                [GameBoard.currentUser]]){
            System.out.println("There is a research station in the city"
            		+ " you're in.");
            System.out.println();
        }
    }
    
    /*Method that prints all the research stations that the player can fly to.*/
    static void printResearchStation(){
        System.out.println("Research Station in: ");
        int counterResearchStation = 0;
        for(int i = 0; i < researchStation.length; i++){
            if(researchStation[i] && 
              (GameBoard.userLocation[GameBoard.currentUser] != i)){
                counterResearchStation++;
                System.out.println( (counterResearchStation)+". " + City.cities[i]);
            }
        }
    }
    
    /*Method that will print all the research stations on the board.*/
    static void printAllResearchStation(){
        System.out.println("Research Station in: ");
        int counterResearchStation = 0;
        for(int i = 0; i < researchStation.length; i++){
            if(researchStation[i]){
                counterResearchStation++;
                System.out.println( (counterResearchStation)+". " + City.cities[i]);
            }
        }
    }
    
    /* This method is called when a player uses the shuttle flight action.
     * It will check if the player is in a research station or not and if
     * there are at least 2 research stations on the game board. If none of
     * these requirements are met, it will send the player back to the menu
     * with an error message. If the requirements are met, it will call
     * flyToResearchStationMove to perform the action.*/
    static void flyToResearchStation(){
        if(GameBoard.currentUser == 0){
            if(researchStation[GameBoard.userLocation[0]]){
                if(researchStationNum > 1){
                    flyToResearchStationMove(GameBoard.userLocation[0]);
                }else{
                    System.out.println("There is only 1 research station"
                    + " on the board. There has to be at least two research "
                    + "stations to be able to fly from one to another.");
                    Action.printAction();
                }
            }else{
                System.out.println("There is no research station in that "
                		+ "city.");
                Action.printAction();
            }
        }else{
            if(researchStation[GameBoard.userLocation[1]]){
                if(researchStationNum > 1){
                    flyToResearchStationMove(GameBoard.userLocation[1]);
                }else{
                    System.out.println("There is only 1 research station"
                    + " on the board. There has to be at least two research "
                    + "stations to be able to fly from one to another.");
                    Action.printAction();
                }
            }else{
                System.out.println("There is no research station in that "
                		+ "city.");
                Action.printAction();
            }
        }
    }
    
    /* This method will perform the shuttle flight action. It will allow 
     * the user to fly from one research station to another. It will first
     * tell the user to type a city that has a research station. If the user
     * input does not match a research station, it asks the user to try again
     * by printing all cities with a research station that does not match the
     * current city.*/
    static void flyToResearchStationMove(int userLocation){
        boolean moved = false;
        while (!moved) {
            System.out.println ("Which research station you'd like to "
            		+ "move?");
            System.out.println("Type 0 to return to the action menu.");
            printResearchStation();
            System.out.println("Type one of the cities names above:");
            String userInput = GameBoard.shellInput.nextLine();
            if(Objects.equals(userInput, "0")){
            	Action.printAction();
            	return;
            }
            int cityToMoveTo = 
            		getResearchCityOffset(userInput, userLocation);

            if (cityToMoveTo == -1) {
                System.out.println(userInput + " is not a valid city. "
                		+ "Try one of these:");
                for(int i = 0; i < researchStation.length; i++){
                    if(researchStation[i] && 
                      (GameBoard.userLocation[GameBoard.currentUser] != i)){
                        System.out.println(City.cities[i]);
                    }
                }
            }

            //If research station moves the user, if not print an error.
            else {
                System.out.println("The user has moved from " +
                City.cities[GameBoard.userLocation[GameBoard.currentUser]]
                + " to " + City.cities[cityToMoveTo] + ".");
                GameBoard.userLocation[GameBoard.currentUser] = cityToMoveTo;
                moved = true;
                GameBoard.continueToMenu();
                Action.actionDone();
            }
        }
    }
    
    /* This method will return an index of a research station's city. If the
     * city name entered by the user has a research station on the board that
     * is not the current city, it will return the index of that city entered
     * by the user. If it does not match, it will return -1.*/
    static int getResearchCityOffset(String cityName, int userLocation) {
        for (int i = 0; i < City.cities.length; i++) {
            if (cityName.compareTo(City.cities[i]) == 0 && researchStation[i]
                    && !cityName.equals(City.cities[userLocation]))
                return i;
        }
        return -1;
    }
    
    /* Method that will perform the build research station action. It will 
     * check if a research station has already been built in the city on the
     * card and if the user is in the city that matches the card. If these two
     * requirements are met, the research station will be built.*/
    static void createResearchStation(String card, int playerCardIndex, ArrayList<String> playerCard){
        if(Objects.equals(City.cities[GameBoard.userLocation
                                      [GameBoard.currentUser]], card)){
            if(researchStation[GameBoard.userLocation[GameBoard.currentUser]]){
                System.out.println("There is already a research station in"
                		+ " this city.");
                Action.cardAction(card, playerCardIndex);
            } else if (researchStationNum == 6) {
                System.out.println("There is 6 research stations "
                		+ "on the board.");
                System.out.println("You can't add more than 6.");
                Action.cardAction(card, playerCardIndex);
            } else{
                playerCard.remove(playerCardIndex);
                researchStation[GameBoard.userLocation[GameBoard.currentUser]]
                		= true;
                System.out.println("You have built a research station in "
                + card + ".");
                researchStationNum++;
                researchStationCity.add(City.cities
                		[GameBoard.userLocation[GameBoard.currentUser]]);
                GameBoard.continueToMenu();
                Action.actionDone();
            }
        }else{
            System.out.println("You have to be in " + card + 
            		" to build a research station using this card.");
            Action.cardAction(card, playerCardIndex);
        }
    }
    
}
