import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class EventCard {
    static boolean skipInfectCity;
    static boolean governmentGrantDesc;
    static boolean oneQuietNightDesc;
    static boolean airliftDesc;
    static boolean resilientPopulationDesc;
    static void eventCardInit(){
        skipInfectCity = false;
        governmentGrantDesc = false;
        oneQuietNightDesc = false;
        airliftDesc = false;
        resilientPopulationDesc = false;
    }
    /* This method is the functionality of the event card Government Grant.
     * When the player uses it, it will ask the user in which city he/she
     * wants to build a research station.*/
    static void governmentGrantCard(int userIndex){
        String userInput = "";
        Scanner scanner =  new Scanner(System.in);
        System.out.println(GameBoard.userNames[userIndex] + 
        		" has used the Government Grant card.");
        boolean moved = false;
        while (!moved) {
            System.out.println("In which city you want to add a research"
            		+ " station. ");
            System.out.println("Type a city name: ");
            userInput = scanner.nextLine();
            int cityToMoveTo = City.getCityOffset(userInput);
            while (cityToMoveTo == -1) {
                System.out.println(userInput + " is not a valid city. "
                		+ "Try one of these:");
                GameBoard.printCities();
                System.out.println("Type a city name: ");
                userInput = scanner.nextLine();
                cityToMoveTo = City.getCityOffset(userInput);
            }
            if (ResearchStation.researchStation[cityToMoveTo]) {
                System.out.println("This city has already a research "
                		+ "station.");
                ResearchStation.printAllResearchStation();
                continue;
            }
            ResearchStation.researchStation[cityToMoveTo] = true;
            System.out.println("You have built a research station in "
            + City.cities[cityToMoveTo] + ".");
            GameBoard.continueToMenu();
            ResearchStation.researchStationNum++;
            ResearchStation.researchStationCity.add(City.cities[cityToMoveTo]);
            moved = true;
        }
    }
    /* Method for the One Quiet Night card functionality It will just set the
     * boolean variable skipInfectCity to true. This will prevent the player
     * from drawing an infection card for 1 turn.*/
    static void oneQuietNightCard(int userIndex){
        System.out.println(GameBoard.userNames[userIndex] + 
        		" has used the One Quiet Night card.");
        System.out.println("There will be no infection cards that will be"
        		+ " drawn when your turn ends.");
        GameBoard.continueToMenu();
        skipInfectCity = true;
    }
    /* Method for the functionality of the Airlift card. It will ask the
     * player to type a city, which will then send the other player to
     * that city.*/
    static void airliftCard(int userIndex){
        String userInput = "";
        Scanner scanner =  new Scanner(System.in);
        System.out.println(GameBoard.userNames[userIndex] + 
        		" has used the Airlift card.");
        int otherUser = 0;
        if(userIndex == 0){
            otherUser = 1;
        }
        boolean moved = false;
        while (!moved) {
            System.out.println("Which city do you want to move " +
        GameBoard.userNames[otherUser] + " to?");
            System.out.println("Type a city name:");
            userInput = scanner.nextLine();
            int cityToMoveTo = City.getCityOffset(userInput);
            while (cityToMoveTo == -1) {
                System.out.println(userInput + " is not a valid city. "
                		+ "Try one of these.");
                GameBoard.printCities();
                System.out.println("Type a city name: ");
                userInput = scanner.nextLine();
                cityToMoveTo = City.getCityOffset(userInput);
            }
            System.out.println(GameBoard.userNames[otherUser] + 
            " has moved from " + City.cities[GameBoard.userLocation[otherUser]]
            + " to " + City.cities[cityToMoveTo] + ".");
            GameBoard.continueToMenu();
            GameBoard.userLocation[otherUser] = cityToMoveTo;
            moved = true;
        }

    }
    /* Method for the functionality of the Resilient Population Card. It will
     * ask the user to choose which card to remove from the infection discard
     * pile. */
    static void resilientPopulationCard(int userIndex){
        Scanner scanner = new Scanner(System.in);
        String cardMenu = "";
        System.out.println(GameBoard.userNames[userIndex] + 
        		" has used the Resilient Population Card.");
        System.out.println("Which card do you want to remove from the"
        + " infection discard pile (the card will not be drawn again):");
        for (int i = 0; i < InfectionCard.discardCard.size(); i++){
            System.out.println((i+1) + ". " + 
        InfectionCard.discardCard.get(i));
        }
        System.out.println("Enter a number:");
        cardMenu = scanner.nextLine();
        int cardNum = getDeckOffset(cardMenu,InfectionCard.discardCard);
        while (cardNum == -1){
            System.out.println("Please enter the right number: ");
            cardMenu = scanner.nextLine();
            cardNum = getDeckOffset(cardMenu, InfectionCard.discardCard);
        }

        String card = InfectionCard.discardCard.remove(cardNum);
        System.out.println("The " + card + " from the infection discard pile"
        		+ " has been remove.");
        GameBoard.continueToMenu();
    }
    /*This will get the index of a stack when the user will use
    * the event card resilient Population.*/
    static int getDeckOffset(String cardNum, Stack<String> deck){
        if(!cardNum.matches(".*\\d.*")){
            return -1;
        }else {
            for (int i = 0; i < deck.size(); i++) {
                if ((i+1) == Integer.parseInt(cardNum))
                    return i;
            }
        }
        return -1;
    }
    /*Explain the event card when a player has one in their cards.*/
    static void explainEventCard(ArrayList<String> playerCard){
        for(int i = 0; i < playerCard.size(); i++){
            if(Objects.equals(playerCard.get(i), "Government Grant") && 
            		!EventCard.governmentGrantDesc){
                System.out.println("The Government Grant card allows you"
                		+ " to build a research in any city.");
                System.out.println("Try to use this card when you have 5 cards"
                		+ " of the same color and you are far away from a "
                		+ "research station.");
                System.out.println("You may use it to go into another research"
                		+ " station by building the research station in your"
                		+ " current city and fly to another one.");
                System.out.println();
                EventCard.governmentGrantDesc = true;
            }
            if (Objects.equals(playerCard.get(i), "One Quiet Night") &&
                    !EventCard.oneQuietNightDesc) {
                System.out.println("This card can prevent you from drawing"
                		+ " infection cards when your turn ends.");
                System.out.println("You may use it to prevent outbreaks. "
                		+ "For example, if there is a city that has 3 disease"
                		+ " cubes and you are not able to treat the disease "
                		+ "cubes in that city.");
                System.out.println();
                EventCard.oneQuietNightDesc = true;
            }
            if (Objects.equals(playerCard.get(i), "Airlift") &&
                    !EventCard.airliftDesc) {
                System.out.println("This card allows you to send a player to "
                		+ "another city.");
                System.out.println("This can be useful when the other player "
                		+ "has 5 cards of the same color and is not in a "
                		+ "research station.");
                System.out.println("With this card, you can send the player to"
                		+ " a city with a research station to make the other"
                		+ " player cure the disease.");
                System.out.println("You may also use it to send a player to a"
                		+ " city with 3 disease cubes to prevent outbreaks.");
                System.out.println();
                EventCard.airliftDesc = true;
            }
            if(Objects.equals(playerCard.get(i), "Resilient Population") &&
                    !EventCard.resilientPopulationDesc){
                System.out.println("This card allows you to remove a card from"
                		+ " the infection discard pile.");
                System.out.println("The city that matches that card will no "
                		+ "longer receive a disease cube when drawing cards.");
                System.out.println("The city will only receive disease cubes"
                		+ " if there is an outbreak in the nearby cities.");
                System.out.println("Use this card before an epidemic card is"
                		+ " drawn.");
                System.out.println();
                EventCard.resilientPopulationDesc = true;
            }
        }
    }

    /*This method will help the player who has the oneQuietNight card to use
     *  it wisely. */
    static void oneQuietNightUse(ArrayList<String> playerCard){
        // Boolean variable to know if the user has the card or not.
        boolean havaOneQuietNight = false;
        // Loop through the player cards to know if the card is in or not.
        for(int i = 0; i < playerCard.size(); i++){
            if (Objects.equals(playerCard.get(i), "One Quiet Night")) {
                havaOneQuietNight = true;
                break;
            }
        }

        if(havaOneQuietNight){
            if(InfectionCard.infectionRate == 3){
                System.out.println();
                System.out.println("The infection rate is at 3!!!");
                System.out.println("You may use the One Quiet Night event card"
                + " to reduce the number of disease cubes that are added.");
                System.out.println("You will not draw infection cards at the"
                		+ " end of your turn when using this event card.");
                GameBoard.continueToMenu();
            } else if (InfectionCard.outbreakNumber >= 6) {
                System.out.println();
                System.out.println((8-InfectionCard.outbreakNumber) + " more"
                		+ " outbreaks and you lose!!!");
                System.out.println("You may use the One Quiet Night event card"
                		+ " to prevent outbreak.");
                System.out.println("You will not draw infection cards at the"
                		+ " end of your turn when using this event card.");
                GameBoard.continueToMenu();
            }
        }

    }
}
