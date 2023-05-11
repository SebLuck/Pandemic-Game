import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
/*Class that have different methods based on the functionality of the roles. 
 * It has a 2D array of boolean named preventDisease, which is from the role
 *  medic. This array will prevent the disease of a color from being added to 
 *  a city. There is an array named playerRoles that will help to identify 
 *  which user has which role. There are 3 boolean variables that will help to
 *   determine which role has already been chosen. This will prevent the player
 *    from choosing the same role.*/
public class Roles {
    static boolean[][] preventDisease;
    static String[] playerRoles = {"", ""};

    static  boolean scientist;
    static  boolean researcher;
    static  boolean medic;
    /*Method that will initialize all the global variables
    * of the class Roles*/
    static void initRoles(){
        playerRoles[0] = "";
        playerRoles[1] = "";
        scientist = false;
        researcher = false;
        medic = false;
        Roles.preventDisease = new boolean[City.numberCities][4];
        for(int j = 0; j < City.numberCities; j++){
            for(int k = 0; k < 4; k++){
                Roles.preventDisease[j][k] = false;
            }
        }
    }

    /*Method that will be called when the user has started the game. 
     * The two players have to choose their roles. The array playerRoles
     *  will determine which role the player has based on the index. 
     *  For example, if player 1 has chosen Scientist, it will store Scientist
     *   in index 0 of that array.*/
    static void chooseRole() {
        for(int i = 0; i < 2; i++) {
            String roleMenu = "";
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose your role " + GameBoard.userNames[i]
            		+ ".");
            System.out.println("1 - Scientist.");
            System.out.println("2 - Researcher.");
            System.out.println("3 - Medic.");
            System.out.println("Enter a number: ");
            roleMenu = scanner.nextLine();
            while (!Objects.equals(roleMenu, "1") &&
            		!Objects.equals(roleMenu, "2") &&
                    !Objects.equals(roleMenu, "3") ) {
                System.out.println("Please enter a valid number: ");
                roleMenu = scanner.nextLine();
            }
            if (roleMenu.equals("1") && !scientist) {
                playerRoles[i] = "Scientist";
                printRoleDetail(i);
                scientist = true;
            }else if(roleMenu.equals("1") && scientist){
                System.out.println("The Scientist role has already been taken"
                		+ " by " + GameBoard.userNames[0]);
                i--;
            }
            if (roleMenu.equals("2") && !researcher) {
                playerRoles[i] = "Researcher";
                printRoleDetail(i);
                researcher = true;

            } else if(roleMenu.equals("2") && researcher){
                System.out.println("The Researcher role has already been"
                		+ " taken by " + GameBoard.userNames[0]);
                i--;
            }
            if(roleMenu.equals("3") && !medic) {
                playerRoles[i] = "Medic";
                printRoleDetail(i);
                medic = true;
            }else if(roleMenu.equals("3") && medic) {
                System.out.println("The Medic role has already been taken by "
            + GameBoard.userNames[0]);
                i--;
            }
        }

    }

    /*Method which will print the role of the player which is
        currently playing.*/
    static void printRoleDetail(int currentUser) {
        if(Objects.equals(Roles.playerRoles[currentUser], "Scientist")){
            System.out.println();
            System.out.println("Your role is the Scientist.");
            System.out.println("You need only 4 cards of the same color to"
            		+ " perform the Discover cure action.");
        }
        if (Objects.equals(Roles.playerRoles[currentUser], "Researcher")) {
            System.out.println();
            System.out.println("Your role is the Researcher.");
            System.out.println("You may give any 1 of your City cards when"
            		+ " you share knowledge.");
            System.out.println("It does not need to match your city.");
            System.out.println("A player who shares knowledge with you on"
            		+ " their turn can take any 1 of your city cards.");
        }
        if(Objects.equals(Roles.playerRoles[currentUser], "Medic")){
            System.out.println();
            System.out.println("Your role is the Medic.");
            System.out.println("You can remove all cubes of one color when"
            		+ " doing Treat Disease.");
            System.out.println("You can automatically remove cubes of "
            		+ "cured disease from the city you are in");
            System.out.println("and prevent them from being placed "
            		+ "there.");
        }
    }

/*Method that will be called when the Scientist uses the cure disease action.
 *  It will check if the player has 4 cards of the same color.*/
    static void cureDiseaseScientist(ArrayList<String> playerCard) {
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
        if((counterBlue >= 4 || counterYellow >= 4 || counterBlack >= 4 || 
           counterRed >= 4) && (ResearchStation.researchStation
        		   [GameBoard.userLocation[GameBoard.currentUser]])){
            boolean colorDiseaseCured;
            if(counterBlue >= 4){
                colorDiseaseCured = Disease.blueDiseaseCured;
                cureDiseaseColorScientist(playerCard, colorDiseaseCured,
                        "Blue");
            }else if(counterYellow >= 4){
                colorDiseaseCured = Disease.yellowDiseaseCured;
                cureDiseaseColorScientist(playerCard, colorDiseaseCured,
                        "Yellow");
            }else if(counterBlack >= 4){
                colorDiseaseCured = Disease.blackDiseaseCured;
                cureDiseaseColorScientist(playerCard, colorDiseaseCured,
                        "Black");
            }else {
                colorDiseaseCured = Disease.redDiseaseCured;
                cureDiseaseColorScientist(playerCard, colorDiseaseCured,
                        "Red");
            }
        } else if (!ResearchStation.researchStation[GameBoard.userLocation
                                         [GameBoard.currentUser]]) {
            System.out.println("You need to be in a research station to "
            		+ "cure a disease.");
            Action.printAction();
        } else{
            System.out.println("You need 4 player cards of the same color "
            		+ "to cure a disease.");
            Action.printAction();
        }
    }
    /* If the player has 4 blue player cards, it will consume the five cards
     *  and mark the disease as cured in the game. If the player has more than
     *   4 player cards of the same color, the player will choose which card
     *    to keep. curedDiseaseNum variable will be incremented by 1. */
    private static void cureDiseaseColorScientist(ArrayList<String> playerCard,
                                                  boolean colorDiseaseCured,
                                                  String color) {
        if(!colorDiseaseCured){
// Counter for the cards of the same color as the disease that is being cured.
            int counterColor = 0;
            if(Objects.equals(color, "Blue")){
                Disease.blueDiseaseCured = true;
            } else if (Objects.equals(color, "Yellow")) {
                Disease.yellowDiseaseCured = true;
            } else if (Objects.equals(color, "Black")) {
                Disease.blackDiseaseCured = true;
            } else if (Objects.equals(color, "Red")) {
                Disease.redDiseaseCured = true;
            }
            Disease.curedDiseaseNum++;
            // Arraylist to store all the cards which will not be consumed.
            ArrayList<String> newPlayerCard = new ArrayList<>();
            // Arraylist to store all the cards which will be consumed.
            ArrayList<String> dropPlayerCard = new ArrayList<>();
            for(int i = 0; i < playerCard.size(); i++){
                String[] splitColor = playerCard.get(i).split(",");
                // If it is an Event card.
                if(splitColor.length == 1){
                    newPlayerCard.add(playerCard.get(i));
                    continue;
                }
                if(!Objects.equals(splitColor[1], color)){
                    newPlayerCard.add(playerCard.get(i));
                }else{
                    counterColor++;
                    if(counterColor >= 5){
                        dropPlayerCard.add(playerCard.get(i));
                        Disease.chooseCardToKeep(dropPlayerCard,
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

    /*Method that will be called if there is a researcher in the game and the
     *  user chooses to use the action share knowledge. It asks the user first
     *  to choose if he/she wants to take a card from or give one to the
     *  player. If the player is a researcher and wants to give a player
     *  a card, the researcher will not have to be in the same city as the
     *  other player, and the player card that he/she wants to give will
     *  not have to match the city they are in. However, if the other
     *  player wants to give a card to the researcher, both players will
     *  have to be in the same city, and the card that he/she wants to
     *  give has to match the city they are in.*/
    static void shareCardResearcher(){
        Scanner scanner = new Scanner(System.in);
        String shareMenu = "";
        String shareCard = "";
        String share = "";
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
        int toUser;
        int currentUser;
        ArrayList<String> currentPlayerCard;
        ArrayList<String> otherPlayerCard;
        if(GameBoard.currentUser == 0){
            toUser = 1;
            currentUser = 0;
            currentPlayerCard = PlayerCard.player1Card;
            otherPlayerCard = PlayerCard.player2Card;
        }else{
            toUser = 0;
            currentUser = 1;
            currentPlayerCard = PlayerCard.player2Card;
            otherPlayerCard = PlayerCard.player1Card;
        }

        System.out.println("Choose your action: ");
        if(Objects.equals(playerRoles[currentUser], "Researcher")){
            System.out.println("1 - Give " + GameBoard.userNames[toUser] +
                    " any of your card." );
            System.out.println("2 - Take the " +
                    City.cities[GameBoard.userLocation[currentUser]] +
                    " card from " + GameBoard.userNames[toUser] + ".");
            System.out.println("0 - Return to action menu.");
            System.out.println("Enter a number: ");
            share = scanner.nextLine();
            while (!Objects.equals(share, "1") &&
                    !Objects.equals(share, "2") &&
                    !Objects.equals(share, "0")) {
                System.out.println("Please enter a valid number: ");
                share = scanner.nextLine();
            }
            if(share.equals("1")){
                researcherGive(otherPlayerCard, currentPlayerCard, toUser,
                		currentUser);
            } else if (share.equals("2")) {
                if(GameBoard.userLocation[currentUser] != 
                		GameBoard.userLocation[toUser]){
                    System.out.println(GameBoard.userNames[toUser] + " is not"
                            + " in the same city as you.");
                    Action.printAction();
                }else if(Objects.equals(shareCard, "")){
                    System.out.println("There is no player card"
                            + " that matches the " +
                            "current city in " + GameBoard.userNames[toUser]
                            +"'s cards. ");
                    System.out.println("If you want to take a card from "+
                            GameBoard.userNames[toUser] +", the " + 
                    		"card that you want"
                            + " has to match the current city.");
                    Action.printAction();
                } else if (whichUser == currentUser) {
                    System.out.println("You already have the card.");
                    PlayerCard.printPlayerCard();
                    Action.printAction();
                } else{
                    System.out.println("Take " + shareCard + " from " +
                            GameBoard.userNames[toUser] + "?");
                    System.out.println("1 - Yes");
                    System.out.println("0 - No");
                    System.out.println("Enter a number: ");
                    shareMenu  = scanner.nextLine();
                    while (!Objects.equals(shareMenu, "1")
                            && !Objects.equals(shareMenu, "0")) {
                        System.out.println("Please enter a valid number:");
                        shareMenu = scanner.nextLine();
                    }
                    if(shareMenu.equals("1")){
                        Action.acceptShare(shareCard, playerCardIndex,
                                whichUser);
                    }else {
                    	Action.printAction();
                    }
                }
            }else {
            	Action.printAction();
            }
        }else{
            System.out.println("1 - Give " + GameBoard.userNames[toUser] +
                    " the " + City.cities[GameBoard.userLocation[currentUser]] 
                    		+ " card.");
            System.out.println("2 - Take any card from " +
                    GameBoard.userNames[toUser] + ".");
            System.out.println("0 - Return to action menu.");
            System.out.println("Enter a number: ");
            share = scanner.nextLine();
            while (!Objects.equals(share, "1") &&
                    !Objects.equals(share, "2") &&
                    !Objects.equals(share, "0")) {
                System.out.println("Please enter a valid number: ");
                share = scanner.nextLine();
            }
            if(share.equals("1")){
                if(GameBoard.userLocation[currentUser] != 
                		GameBoard.userLocation[toUser]){
                    System.out.println(GameBoard.userNames[toUser] + " is not "
                            + "in the same city as you.");
                    Action.printAction();
                }else if(Objects.equals(shareCard, "")){
                    System.out.println("There is no player card that matches"
                            + " the current city in your cards. ");
                    System.out.println("If you want to give a card to "+
                            GameBoard.userNames[toUser] +", the " +
                            "card that you want to give has to match"
                            + " the current city.");
                    Action.printAction();
                } else if (whichUser == toUser) {
                    System.out.println(GameBoard.userNames[toUser] +
                            " has already the " +
                            City.cities[GameBoard.userLocation[toUser]]
                            		+ " card.");
                    PlayerCard.printPlayerCard();
                    Action.printAction();
                } else{
                    System.out.println("Take " + shareCard + " from " +
                            GameBoard.userNames[toUser] + "?");
                    System.out.println("1 - Yes");
                    System.out.println("0 - No");
                    System.out.println("Enter a number: ");
                    shareMenu  = scanner.nextLine();
                    while (!Objects.equals(shareMenu, "1") &&
                            !Objects.equals(shareMenu, "0")) {
                        System.out.println("Please enter a valid number:");
                        shareMenu = scanner.nextLine();
                    }
                    if(shareMenu.equals("1")){
                        Action.acceptShare(shareCard, playerCardIndex,
                                whichUser);
                    }else {
                    	Action.printAction();
                    }
                }
            } else if (share.equals("2")) {
                takeResearcher(currentPlayerCard, otherPlayerCard, toUser, 
                		currentUser);
            }else {
            	Action.printAction();
            }
        }


    }

    /* This function will be called if the researcher has chosen to give 
     * the other player a card. It will take 4 parameters, which will be
     * two arraylists that represent the player cards that will receive the
     * card and the player cards that will give the card. The two integers
     * are toUser, which is the user that will receive the card, and 
     * currentUser, which is the user that is currently giving the card.*/
    private static void takeResearcher(ArrayList<String> addPlayerCard, 
    								   ArrayList<String> removePlayerCard,
                                       int toUser, int currentUser) {
        Scanner scanner = new Scanner(System.in);
        String cardMenu = "";
        System.out.println("Which card do you want to take from "
        + GameBoard.userNames[toUser] + ":");
        for(int i = 0; i < removePlayerCard.size(); i++){
            System.out.println((i+1) + ". " + removePlayerCard.get(i));
        }
        System.out.println("0 - Cancel.");
        System.out.println("Enter a number:");
        cardMenu = scanner.nextLine();
        if(Objects.equals(cardMenu, "0")) {
        	Action.printAction();
        	return;
        }
        int cardNum = PlayerCard.getCardOffset(cardMenu, removePlayerCard);
        while (cardNum == -1){
            System.out.println("Please enter the right number: ");
            cardMenu = scanner.nextLine();
            cardNum = PlayerCard.getCardOffset(cardMenu, removePlayerCard);
        }
        String addCard = removePlayerCard.remove(cardNum);
        if (addPlayerCard.size() >= 7) {
            PlayerCard.replaceCard(addPlayerCard, addCard, 
            		GameBoard.userNames[currentUser]);
        }else{
            addPlayerCard.add(addCard);
        }
        System.out.println(GameBoard.userNames[currentUser] + 
        		" has taken " + addCard + " from " +
                GameBoard.userNames[toUser]);
        GameBoard.continueToMenu();
        Action.actionDone();
    }

    /*This function will be called if a player has chosen to take a card
     *  from the researcher's cards.*/
    static void researcherGive(ArrayList<String> addPlayerCard, 
    						   ArrayList<String> removePlayerCard,
                               int toUser, int currentUser) {
        Scanner scanner = new Scanner(System.in);
        String cardMenu = "";
        System.out.println("Which card do you want to give to "
        + GameBoard.userNames[toUser] + ":");
        for(int i = 0; i < removePlayerCard.size(); i++){
            System.out.println((i+1) + ". " + removePlayerCard.get(i));
        }
        System.out.println("0 - Cancel.");
        System.out.println("Enter a number:");
        cardMenu = scanner.nextLine();
        if(Objects.equals(cardMenu, "0")) {
        	Action.printAction();
        	return;
        }
        int cardNum = PlayerCard.getCardOffset(cardMenu, removePlayerCard);
        while (cardNum == -1){
            System.out.println("Please enter a right number: ");
            cardMenu = scanner.nextLine();
            cardNum = PlayerCard.getCardOffset(cardMenu, removePlayerCard);
        }
        String addCard = removePlayerCard.remove(cardNum);
        if (addPlayerCard.size() >= 7) {
            PlayerCard.replaceCard(addPlayerCard, addCard, 
            		GameBoard.userNames[toUser]);
        }else{
            addPlayerCard.add(addCard);
        }
        System.out.println(GameBoard.userNames[currentUser] + " has given "
        			+ addCard + " to " +
        			GameBoard.userNames[toUser]);
        GameBoard.continueToMenu();
        Action.actionDone();
    }

    /* This function is called every time an action is taken when a disease
     * has been cured. It will remove all disease cubes from the city where
     * the medic is currently located if the disease has been cured, and it
     *  will also prevent the disease from being added to that city. If there
     *   is no disease cube that was cured where the medic is, it will just
     *    prevent that disease that was cured from being added to that city.*/
    static void cureMedic(int cityIndex, int colorIndex){
        String color = Disease.getColorByIndex(colorIndex);
        if(City.diseaseCubes[cityIndex][colorIndex] == 0){
            if(!preventDisease[cityIndex][colorIndex]){
                System.out.println(color + " cubes will no longer be added to "
            + City.cities[cityIndex] + ".");
                preventDisease[cityIndex][colorIndex] = true;
            }
        }else{
            if(!preventDisease[cityIndex][colorIndex]){
                preventDisease[cityIndex][colorIndex] = true;
                Disease.subtractDiseaseColorCounter(colorIndex, 
                		City.diseaseCubes[cityIndex][colorIndex]);
                Disease.diseaseCounter -= 
                		City.diseaseCubes[cityIndex][colorIndex];
                City.diseaseCubes[cityIndex][colorIndex] = 0;
                System.out.println("All " + color + " cubes were removed from "
                + City.cities[cityIndex] + ".");
                System.out.println(color + " cubes will no longer be added to "
                + City.cities[cityIndex] + ".");
            }
        }
    }

    /* This method will be called if there is a researcher in the game. 
     * It will help the player who has that role to play it wisely. 
     * For example, by suggesting the player to give 2 blue cards to the 
     * other player so that he/she can cure a disease.*/
    static void researcherGiveCardToCure(ArrayList<String> researcherCard, 
    		ArrayList<String> playerCard, int researcherIndex
            , int playerIndex){
        int blueCounterResearcher = 0;
        int yellowCounterResearcher = 0;
        int blackCounterResearcher = 0;
        int redCounterResearcher = 0;
        int blueCounter = 0;
        int yellowCounter = 0;
        int blackCounter = 0;
        int redCounter = 0;
        /*For loop to know how many cards of the same color the 
         * researcher have.*/
        for(int i = 0; i < researcherCard.size(); i++){
            String[] splitColor = researcherCard.get(i).split(",");
            if(splitColor.length == 1){
                continue;
            }
            if(Objects.equals(splitColor[1], "Blue")){
                blueCounterResearcher++;
            } else if (Objects.equals(splitColor[1], "Yellow")) {
                yellowCounterResearcher++;
            } else if (Objects.equals(splitColor[1], "Black")) {
                blackCounterResearcher++;
            } else {
                redCounterResearcher++;
            }
        }
        /*For loop to know how many cards of the same color the other 
         * player have.*/
        for(int i = 0; i < playerCard.size(); i++){
            String[] splitColor = playerCard.get(i).split(",");
            if(splitColor.length == 1){
                continue;
            }
            if(Objects.equals(splitColor[1], "Blue")){
                blueCounter++;
            } else if (Objects.equals(splitColor[1], "Yellow")) {
                yellowCounter++;
            } else if (Objects.equals(splitColor[1], "Black")) {
                blackCounter++;
            } else {
                redCounter++;
            }
        }
        /*Add the two counter of the same color from the two player's card
         *  for each color.*/
        int blueCardNum = blueCounterResearcher + blueCounter;
        int yellowCardNum = yellowCounterResearcher + yellowCounter;
        int blackCardNum = blackCounterResearcher + blackCounter;
        int redCardNum = redCounterResearcher + redCounter;
        // The number of cards of the same color required to cure a disease.
        int cureNum = 5;
        if(Roles.scientist) {
            cureNum = 4;
        }
        // Check if the diseases have already been cured or not.
        if(!Disease.blueDiseaseCured){
            whichColorToCure(researcherIndex, playerIndex, 
            		blueCounterResearcher, blueCounter, "blue", 
            		blueCardNum, cureNum);
        }
        if (!Disease.yellowDiseaseCured) {
            whichColorToCure(researcherIndex, playerIndex, 
            		yellowCounterResearcher, yellowCounter,
            		"yellow", yellowCardNum, cureNum);
        }
        if (!Disease.blackDiseaseCured) {
            whichColorToCure(researcherIndex, playerIndex,
            		blackCounterResearcher, blackCounter,
            		"black", blackCardNum, cureNum);
        }
        if(!Disease.redDiseaseCured){
            whichColorToCure(researcherIndex, playerIndex, 
            		redCounterResearcher, redCounter, "red",
            		redCardNum, cureNum);
        }

    }
    /* This method will take the parameters researcherIndex and playerIndex 
     * to know which player is the researcher and which is not. 
     * colorResearchNum and colorPlayerNum is the number of cards of the same
     * color the researcher and the other player have. colorCardNum is the
     * total number of the same color in the 2 player cards.*/
    static void whichColorToCure(int researcherIndex, int playerIndex,
    								int colorResearchNum, int colorPlayerNum,
                                 String color, int colorCardNum, int cureNum){
        /* If the researcher and the other player have 1 to 3 cards of the
         * same color and the colorCardNum is greater than the number of cards
         * required to cure a disease, then it will advise the researcher 
         * to give the other player some cards or, if it is the other player's
         * turn, to take some cards from the researcher to cure the disease.*/
        boolean colorRangeResearcher = colorResearchNum >= 1 && 
        		colorResearchNum <= 3;
        boolean colorRange;
        if(Roles.scientist){
            colorRange = colorPlayerNum >= 1 && colorPlayerNum <= 3;
        }else{
            colorRange = colorPlayerNum >= 1 && colorPlayerNum <= 4;
        }
        int cardNumToGive = cureNum - colorPlayerNum;
        if(colorCardNum >= cureNum && colorRangeResearcher && colorRange) {
            if (GameBoard.currentUser == researcherIndex) {
                System.out.println();
                System.out.println("You have " + colorResearchNum + " " + 
                color + " cards.");
                System.out.println(GameBoard.userNames[playerIndex] + " has "
                + colorPlayerNum + " " + color + " cards.");
                System.out.println();
                System.out.println("You may give " + 
                GameBoard.userNames[playerIndex] + " " + cardNumToGive
                        + " of your " + color + " cards so that he can cure"
                        		+ " the disease.");
                System.out.println("You have to use the share knowledge"
                		+ " action.");
                System.out.println("You have to use this action " +
                		cardNumToGive + " time(s).");
                System.out.println();
                GameBoard.continueToMenu();
            } else {
                System.out.println();
                System.out.println("You have " + colorPlayerNum + " " + color 
                		+ "  cards.");
                System.out.println(GameBoard.userNames[researcherIndex] + 
                		" has " + colorResearchNum + " " + color + " cards.");
                System.out.println();
                System.out.println("You may take " + cardNumToGive + " " + 
                color + " cards from " + GameBoard.userNames[researcherIndex]
                        + " so that you can cure the disease.");
                System.out.println("You have to use the share knowledge"
                		+ " action.");
                System.out.println("You have to use this action " +
                		cardNumToGive + " time(s).");
                System.out.println();
                GameBoard.continueToMenu();
            }
        }
    }


}
