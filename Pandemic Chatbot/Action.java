import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Action {

    static int actionPoint;

    static void initAction(){
        actionPoint = 4;
    }
    /*This method is called when the user chooses to perform an action.*/
    static void printAction() {
        String actionMenu = "";
        Scanner scanner = new Scanner(System.in);
        while(!actionMenu.equals("0")){
            System.out.println ("Choose your action:");
            System.out.println ("1 - Drive (Move to adjacent city).");
            if(Objects.equals(Roles.playerRoles[GameBoard.currentUser],
            		"Medic")){
                System.out.println("2 - Treat Disease (Remove all infection "
                		+ "cube(s) in the current city).");
            }else{
                System.out.println("2 - Treat Disease (Remove infection cube"
                		+ " in the current city).");
            }
            System.out.println("3 - Use 1 Player Card (Move to a city, build "
            		+ "research station, move anywhere or use 1 event card).");
            if(Objects.equals(Roles.playerRoles[GameBoard.currentUser],
            		"Scientist")){
                System.out.println("4 - Discover a cure (Cure a disease with 4"
                		+ " Player Cards of the same color).");
            }else{
                System.out.println("4 - Discover a cure (Cure a disease with "
                		+ "5 Player Cards of the same color).");
            }
            System.out.println("5 - Shuttle Flight (Fly from a Research "
            		+ "Station to another one).");
            if(GameBoard.currentUser == 0){
                System.out.println("6 - Share Knowledge. (Give or take one "
                		+ "card from " + GameBoard.userNames[1] + ")");
            }else{
                System.out.println("6 - Share Knowledge. (Give or take one "
                		+ "card from " + GameBoard.userNames[0] + ")");
            }
            System.out.println("0 - Return to menu.");
            System.out.println ("Enter a number: ");
            actionMenu = scanner.nextLine();
            while (!Objects.equals(actionMenu, "1") && 
            		(!Objects.equals(actionMenu, "2"))
                    && !Objects.equals(actionMenu, "3") && 
                    !Objects.equals(actionMenu, "4")
                    && !Objects.equals(actionMenu, "5") &&
                    !Objects.equals(actionMenu, "6") &&
                    !Objects.equals(actionMenu, "0")) {
                System.out.println("Please enter a valid number: ");
                actionMenu = scanner.nextLine();
            }
            if(actionMenu.equals("1")){
                moveUser();
                actionMenu = "0";
            } else if (actionMenu.equals("2")) {
                if (removeCube()) actionDone();
                actionMenu = "0";
            } else if (actionMenu.equals("3")) {
                PlayerCard.useCard();
                actionMenu = "0";
            }else if (actionMenu.equals("4")) {
                if(GameBoard.currentUser == 0){
                    if(Objects.equals(Roles.playerRoles[GameBoard.currentUser],
                    		"Scientist")){
                        Roles.cureDiseaseScientist(PlayerCard.player1Card);
                    }else{
                        Disease.cureDisease(PlayerCard.player1Card);
                    }
                }else{
                    if(Objects.equals(Roles.playerRoles[GameBoard.currentUser],
                    		"Scientist")){
                        Roles.cureDiseaseScientist(PlayerCard.player2Card);
                    }else {
                        Disease.cureDisease(PlayerCard.player2Card);
                    }
                }
                actionMenu = "0";
            }else if (actionMenu.equals("5")) {
                ResearchStation.flyToResearchStation();
                actionMenu = "0";
            } else if (actionMenu.equals("6")) {
                if(Roles.researcher){
                    Roles.shareCardResearcher();
                }else{
                    shareCard();
                }
                actionMenu = "0";
            }
        }
    }

    /*Ask the user where to move, get the city, and if valid, 
     * move the user's location.*/
    private static void moveUser() {
        boolean moved = false;
        while (!moved) {

            GameBoard.printAdjacentCities();
            System.out.println ("Type the city you'd like to move.");
            System.out.println("Type 0 to return to the action menu.");
            String userInput = GameBoard.shellInput.nextLine();
            int cityToMoveTo = City.getCityOffset(userInput);

            if (Objects.equals(userInput, "0")){
                moved = true;
                printAction();
                continue;
            }

            if (cityToMoveTo == -1) {
                System.out.println(userInput + " is not a valid city. "
                		+ "Try one of these:");
            }

            //If adjacent move the user, if not print an error.
            else if (GameBoard.citiesAdjacent(GameBoard.userLocation
            		[GameBoard.currentUser],cityToMoveTo)) {
                System.out.println("The user has moved from " +
                        City.cities[GameBoard.userLocation
                                    [GameBoard.currentUser]] + " to " +
                        City.cities[cityToMoveTo] + ".");
                GameBoard.userLocation[GameBoard.currentUser] = cityToMoveTo;
                actionDone();
                moved = true;
            }
            else {
                System.out.println ("You can't move to " + userInput + ".  Try"
                		+ " one of these:");
            }
        }

    }
    
    /*This method is called every time a player performs an action. If the 
     * player's action is 0, his/her turn ends, and draw 2 player cards and 
     * some infection cards. The next player can then start playing. The action
     *  point is reset to 4 each time a player's turn ends.*/
    static void actionDone() {
        actionPoint--;
        /*If statement to check if the medic is in the game or not. It will
         *  check which disease has been cured to remove all cubes of that
         *   disease from the city where the medic is.*/
        if(Roles.medic){
            int cityIndex;
            if(Objects.equals(Roles.playerRoles[0], "Medic")){
                cityIndex = GameBoard.userLocation[0];
            }else{
                cityIndex = GameBoard.userLocation[1];
            }
            if(Disease.blueDiseaseCured){
                Roles.cureMedic(cityIndex, 0);
            }
            if(Disease.yellowDiseaseCured){
                Roles.cureMedic(cityIndex, 1);
            }
            if(Disease.blackDiseaseCured){
                Roles.cureMedic(cityIndex, 2);
            }
            if(Disease.redDiseaseCured){
                Roles.cureMedic(cityIndex, 3);
            }
        }
        // Check if a disease has been eradicated or not.
        Disease.checkDiseaseEradicated();
        if(actionPoint == 0){
            System.out.println();
            System.out.println(GameBoard.userNames[GameBoard.currentUser] 
            		+ "'s turn has ended.");
            System.out.println();
            /* Reset all indexes of the outbreakList array to false when
             * the turn of a player ends so that an outbreak can occur again
             * in a city that has already had one.*/
            for(int i = 0; i < City.numberCities; i++){
                InfectionCard.outbreakList[i] = false;
            }
            PlayerCard.drawTurn();
            InfectionCard.drawTurn();

            GameBoard.currentUser++;
            GameBoard.currentUser%= GameBoard.NUMBER_USERS;
            actionPoint = 4;

            System.out.println();

            GameBoard.sleepGame(1000);
            System.out.println("It's now " + GameBoard.userNames
            		[GameBoard.currentUser]  +"'s turn. (" + 
            		Roles.playerRoles[GameBoard.currentUser] + ")");

            if(InfectionCard.outbreakNumber > 1){
                System.out.println("There are " + InfectionCard.outbreakNumber
                		+ " outbreaks on the board!!!");
            }else{
                System.out.println("There is " + InfectionCard.outbreakNumber
                		+ " outbreak on the board.");
            }
            GameBoard.sleepGame(1000);

            InfectionCard.preventOutbreak();

            if(Roles.researcher){
                if(Objects.equals(Roles.playerRoles[0], "Researcher")){
                    Roles.researcherGiveCardToCure(PlayerCard.player1Card,
                    		PlayerCard.player2Card, 0, 1);
                }else if(Objects.equals(Roles.playerRoles[1], "Researcher")){
                    Roles.researcherGiveCardToCure(PlayerCard.player2Card,
                    		PlayerCard.player1Card, 1, 0);
                }
            }

            if(GameBoard.currentUser == 0){
                EventCard.oneQuietNightUse(PlayerCard.player1Card);
            }else{
                EventCard.oneQuietNightUse(PlayerCard.player2Card);
            }
        }
    }

    /* Remove a cube from the current location. If there's not, return false
     * for an error. If the disease in that cube has been cured, remove all
     * disease cubes from the city. If there are more than two diseases in
     * the same city, the player can choose which cube to remove. */
     static boolean removeCube() {
        int currentUserLocation = GameBoard.userLocation
        		[GameBoard.currentUser];
         Scanner scanner = new Scanner(System.in);
        String removeString = "";
        boolean rightColor = false;
        ArrayList<String> colorArray = new ArrayList<>();
         for(int i = 0; i < 4; i++){
             if(City.diseaseCubes[currentUserLocation][i] > 0){
                 colorArray.add(Disease.getColorByIndex(i));
             }
         }
         /* If the arraylist colorArray is empty, this means there is no
          *  disease cube in the current city.*/
         if(colorArray.isEmpty()){
             System.out.println("The space you're on has no disease cubes.");
             printAction();
             return false;
         }
         /*If there is only one disease cube of a color in a city.*/
         if(colorArray.size() == 1){
             int colorIndex = Disease.getColorIndex(colorArray.get(0));
             if((Disease.blueDiseaseCured ||
                     Objects.equals(Roles.playerRoles[GameBoard.currentUser],
                             "Medic")) && colorIndex == 0){
                 removeAllDiseaseCube(currentUserLocation, colorIndex,
                         "blue");
             } else if ((Disease.yellowDiseaseCured ||
                     Objects.equals(Roles.playerRoles[GameBoard.currentUser],
                             "Medic")) && colorIndex == 1) {
                 removeAllDiseaseCube(currentUserLocation, colorIndex,
                         "yellow");
             } else if ((Disease.blackDiseaseCured ||
                     Objects.equals(Roles.playerRoles[GameBoard.currentUser],
                             "Medic")) && colorIndex == 2) {
                 removeAllDiseaseCube(currentUserLocation, colorIndex,
                         "black");
             } else if ((Disease.redDiseaseCured ||
                     Objects.equals(Roles.playerRoles[GameBoard.currentUser],
                             "Medic")) && colorIndex == 3) {
                 removeAllDiseaseCube(currentUserLocation, colorIndex,
                         "red");
             }else {
                 City.diseaseCubes[currentUserLocation][colorIndex]--;
                 Disease.diseaseCounter--;
                 Disease.subtractDiseaseColorCounter(colorIndex, 1);
             }
             if(City.diseaseCubes[currentUserLocation][colorIndex] > 1){
                 System.out.println("There are " + 
             City.diseaseCubes[currentUserLocation][colorIndex]
                         + " " + colorArray.get(0) + " cubes left");
             }else{
                 System.out.println("There is " +
             City.diseaseCubes[currentUserLocation][colorIndex]
                         + " " + colorArray.get(0) + " cube left");
             }
             return true;
         }
         System.out.println("Which disease cube to remove:");
         for(int i = 0; i < colorArray.size(); i++){
             int colorIndex = Disease.getColorIndex(colorArray.get(i));
             System.out.println(colorArray.get(i) + " - " +
             City.diseaseCubes[currentUserLocation][colorIndex] + 
             " disease cube(s).");
         }
         System.out.println("Type a color: ");
         removeString  = scanner.nextLine();
         // Check if the user has entered a right color.
         for(int i = 0; i < colorArray.size(); i++){
             if (Objects.equals(removeString, colorArray.get(i))) {
                 rightColor = true;
                 break;
             }
         }
         /*Validation to make sure the user entered the right color.*/
         while(!rightColor){
             System.out.println("Please enter a right color: ");
             for(int i = 0; i < 4; i++){
                 if(City.diseaseCubes[currentUserLocation][i] > 0){
                     int colorIndex = Disease.getColorIndex(colorArray.get(i));
                     System.out.println(colorArray.get(i) + " - " +
                     City.diseaseCubes[currentUserLocation][colorIndex] +
                             " disease cube(s).");
                 }
             }
             System.out.println("Type the color: ");
             removeString  = scanner.nextLine();
             for(int i = 0; i < colorArray.size(); i++){
                 if (Objects.equals(removeString, colorArray.get(i))) {
                     rightColor = true;
                     break;
                 }
             }
         }
         boolean colorDiseaseCured;
         if(Objects.equals(removeString, "Blue")){
             colorDiseaseCured = Disease.blueDiseaseCured;
             chooseDiseaseToRemove(colorDiseaseCured, currentUserLocation,
                     0, "blue");
             return true;
         } else if (Objects.equals(removeString, "Yellow")) {
             colorDiseaseCured = Disease.yellowDiseaseCured;
             chooseDiseaseToRemove(colorDiseaseCured, currentUserLocation,
                     1, "yellow");
             return true;
         } else if (Objects.equals(removeString, "Black")) {
             colorDiseaseCured = Disease.blackDiseaseCured;
             chooseDiseaseToRemove(colorDiseaseCured, currentUserLocation,
                     2, "black");
             return true;
         }else{
             colorDiseaseCured = Disease.redDiseaseCured;
             chooseDiseaseToRemove(colorDiseaseCured, currentUserLocation,
                     3, "red");
             return true;
         }
    }

    /*Method that will allow the user to choose which disease to treat
    * if there are different diseases in one city.*/
    static void chooseDiseaseToRemove(boolean colorDiseaseCured,
                                      int currentUserLocation, int colorIndex,
                                      String color){
        if(colorDiseaseCured ||
                Objects.equals(Roles.playerRoles[GameBoard.currentUser],
                "Medic")){
            removeAllDiseaseCube(currentUserLocation, colorIndex, color);
        }else{
            City.diseaseCubes[currentUserLocation][colorIndex]--;
            Disease.diseaseCounter--;
            Disease.subtractDiseaseColorCounter(colorIndex, 1);
        }
        if(City.diseaseCubes[currentUserLocation][colorIndex] > 1){
            System.out.println("There are " +
                    City.diseaseCubes[currentUserLocation][colorIndex]
                    + " " + color + " cubes left");
        }else{
            System.out.println("There is " +
                    City.diseaseCubes[currentUserLocation][colorIndex]
                    + " " + color + " cube left");
        }
    }
    
    /*Method that will remove all disease cubes in a city.*/
    static void removeAllDiseaseCube(int currentUserLocation, int colorIndex,
                                     String color){
        Disease.subtractDiseaseColorCounter(colorIndex, City.diseaseCubes
                [currentUserLocation][colorIndex]);
        Disease.diseaseCounter -= City.diseaseCubes
                [currentUserLocation][colorIndex];
        City.diseaseCubes[currentUserLocation][colorIndex] = 0;
        System.out.println("Remove all " + color + " cubes in " + City.cities
                [GameBoard.userLocation[GameBoard.currentUser]]);
    }

    /* This method is called when the current player has accepted to give/take
     * the card from the other player. It will ask the other player to accept
     * or not. If the other player accepts, it will call another method
     * called shareKnowledge.*/
    static void acceptShare(String card, int playerCardIndex, int whichUser){
        Scanner scanner = new Scanner(System.in);
        String tradeMenu = "";
        if(GameBoard.currentUser == 0){
            if(whichUser == 0){
                System.out.println(GameBoard.userNames[0] + 
                		" wants to give " + card + ".");
            }else{
                System.out.println(GameBoard.userNames[0] + 
                		" wants to take " + card + ".");
            }

        }else{
            if(whichUser == 1){
                System.out.println(GameBoard.userNames[1] + 
                		" wants to give " + card + ".");
            }else{
                System.out.println(GameBoard.userNames[1] + 
                		" wants to take " + card + ".");
            }
        }
        System.out.println("1 - Accept.");
        System.out.println("0 - Decline.");
        System.out.println("Enter a number: ");
        tradeMenu  = scanner.nextLine();
        while (!Objects.equals(tradeMenu, "1") &&
        		!Objects.equals(tradeMenu, "0")){
            System.out.println("Please enter a valid number: ");
            tradeMenu = scanner.nextLine();
        }
        if(Objects.equals(tradeMenu, "1")){
            shareKnowledge(playerCardIndex, whichUser);
            Action.actionDone();
        }else {
        	printAction();
        }
    }
    
    /* This method will allow the user to give and take a card from another
     * user. It will first check which user has the card that matches the
     * city they are in. If it is the first player, the integer whichUser
     * will be 0. If all the requirements are met, the current player will
     * have to confirm if he/she wants to give or take the card from the
     * other player. If the player chooses yes, the acceptShare method 
     * will be called.*/
    static void shareCard(){
        Scanner scanner = new Scanner(System.in);
        String shareMenu = "";
        String shareCard = "";
        int whichUser = 0;
        int playerCardIndex = 0;
        for(int i = 0; i < PlayerCard.player1Card.size(); i++){
            String[] splitCard = PlayerCard.player1Card.get(i).split(",");
            if(Objects.equals(City.cities[GameBoard.userLocation[0]], 
            		splitCard[0])){
                shareCard = PlayerCard.player1Card.get(i);
                playerCardIndex = i;
                break;
            }
        }
        for(int i = 0; i < PlayerCard.player2Card.size(); i++){
            String[] splitCard = PlayerCard.player2Card.get(i).split(",");
            if(Objects.equals(City.cities[GameBoard.userLocation[1]],
            		splitCard[0])){
                shareCard = PlayerCard.player2Card.get(i);
                playerCardIndex = i;
                whichUser = 1;
                break;
            }
        }
        if(GameBoard.userLocation[0] != GameBoard.userLocation[1]){
            if(GameBoard.currentUser == 0){
                System.out.println(GameBoard.userNames[1] +
                		" is not in the same city as you.");
                
            }else{
                System.out.println(GameBoard.userNames[0] + 
                		" is not in the same city as you.");
            }
            printAction();
        }else if(Objects.equals(shareCard, "")){
            System.out.println("There is no player card that matches the " +
                    "current city in both player's deck. ");
            System.out.println("If you want to share a card, one of the " +
                    "player card from one of the player's deck has to match" +
                    " the current city.");
            printAction();
        }else{
            if(GameBoard.currentUser == 0){
                if(whichUser == 0){
                    System.out.println("Give " + shareCard + " to " + 
                GameBoard.userNames[1] + "?");
                }else{
                    System.out.println("Take " + shareCard + " from " + 
                GameBoard.userNames[1] + "?");
                }
            }else{
                if(whichUser == 1){
                    System.out.println("Give " + shareCard + " to " + 
                GameBoard.userNames[0] + "?");
                }else{
                    System.out.println("Take " + shareCard + " from " + 
                GameBoard.userNames[0] + "?");
                }
            }
            System.out.println("1 - Yes");
            System.out.println("0 - No");
            System.out.println("Enter a number: ");
            shareMenu  = scanner.nextLine();
            while (!Objects.equals(shareMenu, "1") &&
            		!Objects.equals(shareMenu, "0")) {
                System.out.println("Please enter a valid number: ");
                shareMenu = scanner.nextLine();
            }
            if(shareMenu.equals("1")){
                acceptShare(shareCard, playerCardIndex, whichUser);
            } else {
            	printAction();
            }
        }
    }
    
    /*This method is called when the two players accept to share their
    * cards. It will check which player wants to give or take cards.
    */
    static void shareKnowledge(int playerCardIndex, int whichUser){
        String shareCard;
        if (GameBoard.currentUser == 0) {
            if(whichUser == 0){
                shareCard = PlayerCard.player1Card.remove(playerCardIndex);
                if (PlayerCard.player2Card.size() >= 7) {
                    PlayerCard.replaceCard(PlayerCard.player2Card, shareCard,
                    		GameBoard.userNames[1]);
                }else{
                    PlayerCard.player2Card.add(shareCard);
                }
                System.out.println("You have given the " + shareCard + " to Bob.");
            }else{
                shareCard = PlayerCard.player2Card.remove(playerCardIndex);
                if (PlayerCard.player1Card.size() >= 7) {
                    PlayerCard.replaceCard(PlayerCard.player1Card, shareCard, 
                    		GameBoard.userNames[0]);
                }else{
                    PlayerCard.player1Card.add(shareCard);
                }
                System.out.println("You have taken the " + shareCard + " from Bob.");
            }
        } else {
            if(whichUser == 1){
                shareCard = PlayerCard.player2Card.remove(playerCardIndex);
                if (PlayerCard.player1Card.size() >= 7) {
                    PlayerCard.replaceCard(PlayerCard.player1Card, shareCard, 
                    		GameBoard.userNames[0]);
                }else{
                    PlayerCard.player1Card.add(shareCard);
                }
                System.out.println("You have given the " + shareCard + " to Al.");
            }else{
                shareCard = PlayerCard.player1Card.remove(playerCardIndex);
                if (PlayerCard.player2Card.size() >= 7) {
                    PlayerCard.replaceCard(PlayerCard.player2Card, shareCard, 
                    		GameBoard.userNames[1]);
                }else{
                    PlayerCard.player2Card.add(shareCard);
                }
                System.out.println("You have taken the " + shareCard + " from Al.");
            }
        }
        GameBoard.continueToMenu();
    }
    
    /* Method that will be called when the user chooses to use one of their
     * cards. It will print a menu for the user to choose which action to 
     * perform with the card.*/
    static void cardAction(String card, int playerCardIndex){
        String actionCardMenu = "";
        Scanner scanner = new Scanner(System.in);
        String[] splitCard = card.split(",");
        while(!actionCardMenu.equals("0")){
            System.out.println("Choose your action to do with the card:");
            System.out.println("1 - Direct Flight (Move to " + splitCard[0] +
            		").");
            System.out.println("2 - Build Research Station.");
            System.out.println("3 - Charter Flight (Go to any city).");
            System.out.println("0 - Return to the player card menu.");
            System.out.println("Enter a number: ");
            actionCardMenu = scanner.nextLine();
            while (!Objects.equals(actionCardMenu, "1") &&
            		(!Objects.equals(actionCardMenu, "2"))
                    && (!Objects.equals(actionCardMenu, "3")) && 
                    (!Objects.equals(actionCardMenu, "0"))) {
                System.out.println("Please enter a valid number: ");
                actionCardMenu = scanner.nextLine();
            }
            if(Objects.equals(actionCardMenu, "1")){
                moveCard(splitCard[0], playerCardIndex);
                actionCardMenu = "0";
                GameBoard.continueToMenu();
                actionDone();
            }  else if (Objects.equals(actionCardMenu, "2")) {
            	if(GameBoard.currentUser == 0) {
            		ResearchStation.createResearchStation(splitCard[0], playerCardIndex, PlayerCard.player1Card);
            	}else {
            		ResearchStation.createResearchStation(splitCard[0], playerCardIndex, PlayerCard.player2Card);
            	}
                actionCardMenu = "0";

            } else if (Objects.equals(actionCardMenu, "3")) {
                charterFlight(splitCard[0], playerCardIndex);
                actionCardMenu = "0";
            }else {
            	PlayerCard.useCard();
            }
        }
    }
    /*This method will perform the charter flight action, which allows the user
     * to fly anywhere on the board by consuming a city card that matches the
     * current city of the user. If the user is in the city that is on the
     * card, the method moveToAnyCity is called. */
     static void charterFlight(String card, int playerCardIndex) {
         int cityIndex = City.getCityOffset(card);
        if(GameBoard.userLocation[GameBoard.currentUser] == cityIndex){
            moveToAnyCity(card, playerCardIndex);
        }else{
            System.out.println("To perform this action, the city on the card"
            		+ " used has to match the current location.");
            System.out.println("Your current location is: " +  
            		City.cities[GameBoard.userLocation
            		            [GameBoard.currentUser]]);
            System.out.println("The city on the card used is: " + card);
            System.out.println("You have to move to " + card +
            		" to perform this action.");
            cardAction(card, playerCardIndex);
        }
    }
    /* This method will allow the user to fly anywhere on the map. It will ask
     * the user to type a city name that is not the current city. If the user
     * types a wrong city, all cities except the current city will be printed
     * to the user and ask him/her to enter an input again.*/
    static void moveToAnyCity(String card, int playerCardIndex){
        boolean moved = false;
        while (!moved) {
            System.out.println ("Type the city you'd like to move.");
            System.out.println("Type 0 to return to the action card menu.");
            String userInput = GameBoard.shellInput.nextLine();
            int cityToMoveTo = City.getCityOffset(userInput);
            if (Objects.equals(userInput, "0")){
                moved = true;
                cardAction(card, playerCardIndex);
                continue;
            }
            if (cityToMoveTo == -1) {
                System.out.println(userInput + " is not a valid city. "
                		+ "Try one of these:");
                for (int i = 0; i < City.numberCities; i++) {
                    if(i != GameBoard.userLocation[GameBoard.currentUser]){
                        System.out.println(City.cities[i]);
                    }
                }
            } else if(cityToMoveTo == GameBoard.userLocation
            		[GameBoard.currentUser]){
                System.out.println("You are already in that city."
                		+ " Try one of these:");
                for (int i = 0; i < City.numberCities; i++) {
                    if(i != GameBoard.userLocation[GameBoard.currentUser]){
                        System.out.println(City.cities[i]);
                    }
                }
            }else{
                if(GameBoard.currentUser == 0){
                    PlayerCard.player1Card.remove(playerCardIndex);
                }else{
                    PlayerCard.player2Card.remove(playerCardIndex);
                }
                System.out.println(GameBoard.userNames[GameBoard.currentUser] 
                		+ " has used the " + card + " Card.");
                System.out.println(GameBoard.userNames[GameBoard.currentUser] 
                + " has moved from " +
                City.cities[GameBoard.userLocation[GameBoard.currentUser]] 
                + " to " + City.cities[cityToMoveTo] + ".");
                GameBoard.userLocation[GameBoard.currentUser] = cityToMoveTo;
                GameBoard.continueToMenu();
                actionDone();
                moved = true;
            }
        }
    }
    /* Method that will be called when a player performs the direct
     * flight action. It will allow the user to consume a card and go
     * to the city on that card.*/
    static void moveCard(String card, int playerCardIndex){
        if(GameBoard.currentUser == 0){
            PlayerCard.player1Card.remove(playerCardIndex);
        }else{
            PlayerCard.player2Card.remove(playerCardIndex);
        }
        String[] splitCard = card.split(",");
        System.out.println(GameBoard.userNames[GameBoard.currentUser] 
        		+ " has used the " + card + " Card.");
        int cityToMoveTo = City.getCityOffset(splitCard[0]);
        System.out.println(GameBoard.userNames[GameBoard.currentUser] 
        		+ " has moved from " +
                City.cities[GameBoard.userLocation[GameBoard.currentUser]] 
                		+ " to " +
                City.cities[cityToMoveTo] + ".");
        GameBoard.userLocation[GameBoard.currentUser] = cityToMoveTo;

    }

}
