import java.util.*;

public class PlayerCard {
    static Stack<String> playerDeck;
    static ArrayList<String> player1Card;
    static ArrayList<String> player2Card;
    static  Stack<String> discardCard;
    /*Method to initialize the arraylist and stack of the class
    * Player Card.*/
    public static void initPlayerCard(){
        playerDeck = new Stack<>();
        for(int i = 0; i < City.cities.length; i++ ){
            playerDeck.push((City.cities[i]) + "," +
        (City.diseaseColor[i]) + ",Card");
        }
        playerDeck.push("Government Grant");
        playerDeck.push("One Quiet Night");
        playerDeck.push("Airlift");
        playerDeck.push("Resilient Population");
        player1Card = new ArrayList<>();
        player2Card = new ArrayList<>();
        discardCard = new Stack<>();
    }
    /* This method will add all epidemic cards to the player deck after each
     * player has drawn 4 cards. The infection deck is divided into 4 piles.
     * These 4 piles are represented as 4 stacks here. The deck has 44 cards
     * here. These 44 cards are divided, therefore, the 4 stacks will have 11
     * cards each. 1 epidemic card will be added to each pile stack, and then
     * they will be shuffled and added to the player deck, which was 
     * cleared before.
     */
    public static void addEpidemicCard(){
        Stack<String> pile1 = new Stack<>();
        Stack<String> pile2 = new Stack<>();
        Stack<String> pile3 = new Stack<>();
        Stack<String> pile4 = new Stack<>();
        for(int i = 0; i < playerDeck.size(); i++){
            if(i < 11){
                pile1.push(playerDeck.get(i));
            } else if (i < 22) {
                pile2.push(playerDeck.get(i));
            }else if (i < 33) {
                pile3.push(playerDeck.get(i));
            }else{
                pile4.push(playerDeck.get(i));
            }
        }
        pile1.add("Epidemic Card");
        Collections.shuffle(pile1);
        pile2.add("Epidemic Card");
        Collections.shuffle(pile2);
        pile3.add("Epidemic Card");
        Collections.shuffle(pile3);
        pile4.add("Epidemic Card");
        Collections.shuffle(pile4);
        playerDeck.clear();
        playerDeck.addAll(pile1);
        playerDeck.addAll(pile2);
        playerDeck.addAll(pile3);
        playerDeck.addAll(pile4);
    }

    /* Check if the player deck is empty. If it is empty, the discardCard stack
     * is added to the playerDeck stack. Then the discardCard stack is cleared,
     *  and the player deck is shuffled.*/
    public static void checkEmpty(){
        if(playerDeck.isEmpty()){
            System.out.println("Player deck is empty.");
            System.out.println("Add all cards from the discard pile "
            		+ "to the player deck.");
            System.out.println("Shuffle the Player Deck...");
            if(!discardCard.isEmpty()) {
                playerDeck.addAll(discardCard);
                discardCard.clear();
                Collections.shuffle(playerDeck);	
            }
        }
    }

    //Method that makes each player draw 4 cards at the start of the game.
    public static void drawStart(){
        System.out.println("Each player has to draw 4 cards now.");
        System.out.println("It is " + GameBoard.userNames[0] +
        		"'s turn to draw." );
        System.out.println();
        menuDrawPlayerDeck();
        for(int i = 0; i < 4; i++){
            drawCardStart(player1Card);
        }
        System.out.println();
        System.out.println("It is " + GameBoard.userNames[1] + 
        		"'s turn to draw." );
        System.out.println();
        menuDrawPlayerDeck();
        for(int i = 0; i < 4; i++){
            drawCardStart(player2Card);
        }

    }

    /*Method that will draw a card from the player deck at the start.*/
    public static void drawCardStart(ArrayList<String> playerCard){
        String card = playerDeck.pop();
        System.out.println(card + " was drawn.");
        playerCard.add(card);
        if((Objects.equals(card, "Government Grant") &&
                !EventCard.governmentGrantDesc) ||
                (Objects.equals(card, "One Quiet Night")
                        && !EventCard.oneQuietNightDesc) ||
                (Objects.equals(card, "Airlift") &&
                        !EventCard.airliftDesc) ||
                (Objects.equals(card, "Resilient Population")
                        && !EventCard.resilientPopulationDesc)){
            EventCard.explainEventCard(playerCard);
            GameBoard.continueToMenu();
        }
        GameBoard.sleepGame(1000);
    }

    /* This method will be called when one player is done with its turn. 
     * The player will draw 2 player cards. If the player has more than
     * 7 cards, he/she will have to replace one card. The card that was
     * removed from the player's cards will be added to the discardCard stack.
     */
    public static void drawTurn(){
        String card;
        menuDrawPlayerDeck();

        ArrayList<String> playerCard;
        if(GameBoard.currentUser == 0){
            playerCard = player1Card;
        }else{
            playerCard = player2Card;
        }


        for(int i = 0; i < 2; i++) {
            card = playerDeck.pop();
            /* Boolean variable to check if the player has drawn an event card
             * for the first time. */
            boolean drawEventCard =
                    Objects.equals(card, "Government Grant") ||
                            Objects.equals(card, "One Quiet Night") ||
                            Objects.equals(card, "Airlift") ||
                            Objects.equals(card, "Resilient Population");
            // Check if the player's cards have more than 7 cards.
            if (playerCard.size() >= 7 &&
                    !Objects.equals(card, "Epidemic Card")) {
                System.out.println(GameBoard.userNames[GameBoard.currentUser] +
                        " has drawn the " + card + " in the player deck.");
                replaceCard(playerCard, card,
                        GameBoard.userNames[GameBoard.currentUser]);
                System.out.println();
                if(drawEventCard){
                    EventCard.explainEventCard(playerCard);
                    GameBoard.continueToMenu();
                }
                GameBoard.sleepGame(1000);
                checkEmpty();
            } else {
                // If the player draws an epidemic card.
                if (Objects.equals(card, "Epidemic Card")) {
                    System.out.println(GameBoard.userNames
                            [GameBoard.currentUser] + " has drawn an"
                            + " epidemic card!!!!!!!!!!");
                    GameBoard.sleepGame(500);
                    if(InfectionCard.infectionRatePosition != 7){
                        InfectionCard.infectionRatePosition++;
                        System.out.println("The infection marker on the"
                                + " infection rate track goes from position " +
                                (InfectionCard.infectionRatePosition - 1) +
                                " to " + InfectionCard.infectionRatePosition
                                + ".");
                        GameBoard.sleepGame(500);
                        InfectionCard.updateInfectionRate();
                        GameBoard.sleepGame(500);
                    }
                    InfectionCard.drawEpidemicEvent();
                    checkEmpty();
                } else {
                    playerCard.add(card);
                    System.out.println();
                    System.out.println(
                            GameBoard.userNames[GameBoard.currentUser] +
                            " has drawn the " + card + " in the"
                            + " player deck.");
                    if(drawEventCard){
                        EventCard.explainEventCard(playerCard);
                        GameBoard.continueToMenu();
                    }
                    GameBoard.sleepGame(1000);
                    checkEmpty();
                }
            }
        }
    }

    /*Method that will be called when one of the players has more than 7 cards.
     *  It will allow the user to choose which card to remove from the 7.*/
    public static void replaceCard(ArrayList<String> playerCard, String card, 
    		String username){
        Scanner scanner = new Scanner(System.in);
        String replaceNum = "";
        int userIndex;
        if(Objects.equals(username, "Al")){
            userIndex = 0;
        }else {
            userIndex = 1;
        }
        System.out.println( username + "'s deck is full. Remove one of the "
        		+ "following cards or use 1 event card.");
        System.out.println("Card that will be added: " + card);
        System.out.println(username + " Player Cards:");
        for (int i = 0; i < playerCard.size(); i++){
            System.out.println((i+1) + ". " + playerCard.get(i));
        }
        ArrayList<String> eventCardArray = new ArrayList<>();
        /* add all the event cards from the player cards to the arraylist
        eventCardArray*/
        for(int i = 0; i < playerCard.size(); i++){
            if(Objects.equals(playerCard.get(i), "Government Grant") ||
                    Objects.equals(playerCard.get(i), "One Quiet Night") ||
                    Objects.equals(playerCard.get(i), "Airlift")  ||
                    Objects.equals(playerCard.get(i), "Resilient Population")){
                eventCardArray.add(playerCard.get(i));
            }
        }
        /* Check if the arraylist eventCardArray is empty to allow the user
          used an event card or not. */
        if(!eventCardArray.isEmpty()){
            if(eventCardArray.size() == 1){
                System.out.println("0 - Use the " + eventCardArray.get(0)
                + " card.");
            }else {
                System.out.println("0 - Use 1 event card.");
            }
            System.out.println("Enter a number: ");
            replaceNum = scanner.nextLine();
            while (!Objects.equals(replaceNum, "1") &&
            		(!Objects.equals(replaceNum, "2"))
                    && !Objects.equals(replaceNum, "3")
                    && !Objects.equals(replaceNum, "4")
                    && !Objects.equals(replaceNum, "5")
                    && !Objects.equals(replaceNum, "6")
                    && !Objects.equals(replaceNum, "7") 
                    && !Objects.equals(replaceNum, "0") ) {
                System.out.println("Please enter a valid number: ");
                replaceNum = scanner.nextLine();
            }
        }else{
            System.out.println("Enter a number: ");
            replaceNum = scanner.nextLine();
            while (!Objects.equals(replaceNum, "1") && 
            		(!Objects.equals(replaceNum, "2"))
                    && !Objects.equals(replaceNum, "3") 
                    && !Objects.equals(replaceNum, "4")
                    && !Objects.equals(replaceNum, "5") 
                    && !Objects.equals(replaceNum, "6")
                    && !Objects.equals(replaceNum, "7")) {
                System.out.println("Please enter a valid number: ");
                replaceNum = scanner.nextLine();
            }
        }
        //
        if(replaceNum.equals("0")){
            String eventCardMenu = "";
            String eventCard;
            /* If there is more than 1 event card in the player's cards,
            * allow the player to choose which card to use.*/
            if(eventCardArray.size() > 1){
                for (int i = 0; i < eventCardArray.size(); i++){
                    System.out.println((i+1) + ". " + eventCardArray.get(i));
                }
                System.out.println ("Enter a number: ");
                eventCardMenu = scanner.nextLine();

                int cardNum = getCardOffset(eventCardMenu, eventCardArray);
                while (cardNum == -1){
                    System.out.println("Please enter a right number: ");
                    eventCardMenu = scanner.nextLine();
                    cardNum = getCardOffset(eventCardMenu, eventCardArray);
                }
                eventCard = eventCardArray.get(cardNum);
            }else{
                eventCard = eventCardArray.get(0);
            }
            // Check which eventCard the user has chosen.
            if(Objects.equals(eventCard, "Government Grant")){
            	if(ResearchStation.researchStationNum == 6) {
                    System.out.println("There is 6 research stations on the board.");
                    System.out.println("You can't add more than 6.");
            		replaceCard(playerCard,  card,  username);
            	}else{
                    EventCard.governmentGrantCard(userIndex);
                    discardCard.push(eventCard);
                    playerCard.add(card);
                    playerCard.remove(eventCard);
            	}
            } else if (Objects.equals(eventCard, "One Quiet Night")) {
                EventCard.oneQuietNightCard(userIndex);
                discardCard.push(eventCard);
                playerCard.add(card);
                playerCard.remove(eventCard);
            } else if (Objects.equals(eventCard, "Airlift")) {
                int otherUser = 0;
                if(userIndex == 0){
                     otherUser = 1;
                }
                String acceptMenu;
                System.out.println(GameBoard.userNames[userIndex] +
                		" want to send " + GameBoard.userNames[otherUser]
                        + " to another city.");
                System.out.println("1. Accept");
                System.out.println("0. Decline");
                System.out.println("Enter a number");
                acceptMenu = scanner.nextLine();
                while(!Objects.equals(acceptMenu, "1") && 
                		!Objects.equals(acceptMenu, "0")){
                    System.out.println("Please enter a right number");
                    acceptMenu = scanner.nextLine();
                }
                if(acceptMenu.equals("1")){
                    EventCard.airliftCard(userIndex);
                    discardCard.push(eventCard);
                    playerCard.add(card);
                    playerCard.remove(eventCard);
                }else{
                    replaceCard(playerCard,  card,  username);
                }
            } else {
                if(InfectionCard.discardCard.isEmpty()){
                    System.out.println("There is no card in the infection"
                    		+ " discard pile.");
                    replaceCard(playerCard,  card,  username);
                }else{
                    EventCard.resilientPopulationCard(userIndex);
                    discardCard.push(eventCard);
                    playerCard.add(card);
                    playerCard.remove(eventCard);
                }
            }
        }else{
            discardCard.push(playerCard.get(Integer.parseInt(replaceNum) - 1));
            playerCard.set((Integer.parseInt(replaceNum) - 1), card);
        }

    }
    /*Method that will print the cards of the current user.*/
    public static void printPlayerCard(){
        if(GameBoard.currentUser == 0){
            System.out.println(GameBoard.userNames[0] + " Player Cards:");
            for (int i = 0; i < player1Card.size(); i++){
                System.out.println((i+1) + ". " + player1Card.get(i));
            }
        }else{
            System.out.println(GameBoard.userNames[1] + " Player Cards:");
            for (int i = 0; i < player2Card.size(); i++){
                System.out.println((i+1) + ". " + player2Card.get(i));
            }
        }
    }
    /*Method when the player will use a player card.
    * The player will get a menu to choose between his/her cards. */
    public static void useCard(){
        String cardMenu = "";
        Scanner scanner = new Scanner(System.in);
        String card;
        while(!cardMenu.equals("0")){
            System.out.println("Choose your card: ");
            ArrayList<String> playerCard;
            int currentUser;
            int otherUser;
            if(GameBoard.currentUser == 0){
                playerCard = player1Card;
                currentUser = 0;
                otherUser = 1;
                if(player1Card.isEmpty()){
                    break;
                }
                for (int i = 0; i < player1Card.size(); i++){
                    System.out.println((i+1) + ". " + player1Card.get(i));
                }
            }else{
                playerCard = player2Card;
                currentUser = 1;
                otherUser = 0;

                if(player2Card.isEmpty()){
                    break;
                }
                for (int i = 0; i < player2Card.size(); i++){
                    System.out.println((i+1) + ". " + player2Card.get(i));
                }
            }
            System.out.println("0 - Return to the action menu.");
            System.out.println ("Enter a number: ");
            cardMenu = scanner.nextLine();
            if(Objects.equals(cardMenu, "0")){
            	Action.printAction();
                continue;
            }
            int cardNum = getCardOffset(cardMenu, playerCard);
            while (cardNum == -1){
                System.out.println("Please enter a right number: ");
                cardMenu = scanner.nextLine();
                if(Objects.equals(cardMenu, "0")){
                    break;
                }
                cardNum = getCardOffset(cardMenu, playerCard);
            }
            if(cardNum == -1){
                continue;
            }
            card = playerCard.get(cardNum);
            if(Objects.equals(card, "Government Grant")){
                if(ResearchStation.researchStationNum == 6){
                    System.out.println("There is 6 research stations "
                    		+ "on the board.");
                    System.out.println("You can't add more than 6.");
                    useCard();
                }else{
                    EventCard.governmentGrantCard(currentUser);
                    discardCard.push(card);
                    playerCard.remove(cardNum);
                }
            } else if (Objects.equals(card, "One Quiet Night")) {
                EventCard.oneQuietNightCard(currentUser);
                discardCard.push(card);
                playerCard.remove(cardNum);
            } else if (Objects.equals(card, "Airlift")) {
                String acceptMenu;
                System.out.println(GameBoard.userNames[currentUser] +
                        " wants to send " + GameBoard.userNames[otherUser]
                        + " to another city.");
                System.out.println("1 - Accept.");
                System.out.println("0 - Decline.");
                System.out.println("Enter a number:");
                acceptMenu = scanner.nextLine();
                while(!Objects.equals(acceptMenu, "1") &&
                        !Objects.equals(acceptMenu, "0")){
                    System.out.println("Please enter a right number:");
                    acceptMenu = scanner.nextLine();
                }
                if(acceptMenu.equals("1")){
                    EventCard.airliftCard(currentUser);
                    discardCard.push(card);
                    playerCard.remove(cardNum);
                }else if (acceptMenu.equals("0")) {
                	useCard();
                }
            } else if (Objects.equals(card, "Resilient Population")) {
                if(InfectionCard.discardCard.isEmpty()){
                    System.out.println("There is no card in the infection"
                            + " discard pile.");
                    useCard();
                }else{
                    EventCard.resilientPopulationCard(currentUser);
                    discardCard.push(card);
                    playerCard.remove(cardNum);
                }
            }else{
                Action.cardAction(card, cardNum);
            }
            cardMenu = "0";
        }

    }
    /*This method will print the card of the other player that is not
    * its turn.*/
    static void printMateCard(){
        if(GameBoard.currentUser == 0){
            System.out.println(GameBoard.userNames[1] + " Player Cards:");
            for (int i = 0; i < player2Card.size(); i++){
                System.out.println((i+1) + ". " + player2Card.get(i));
            }
        }else{
            System.out.println(GameBoard.userNames[0] + " Player Cards:");
            for (int i = 0; i < player1Card.size(); i++){
                System.out.println((i+1) + ". " + player1Card.get(i));
            }
        }
    }

    /*This will get the index of the player card arraylist that the player 
     * wants to give or take.*/
    static int getCardOffset(String cardNum, ArrayList<String> playerCard){
        if(!cardNum.matches(".*\\d.*")){
            return -1;
        }else {
            for (int i = 0; i < playerCard.size(); i++) {
                if ((i+1) == Integer.parseInt(cardNum))
                    return i;
            }
        }
        return -1;
    }
    // Method that will be call when the player will draw player cards.
    public static void menuDrawPlayerDeck(){
        Scanner scanner = new Scanner(System.in);
        String confirm = "";
        System.out.println("Type 1 to draw the player cards: ");
        confirm = scanner.nextLine();
        while(!Objects.equals(confirm, "1")){
            System.out.println("Type 1 to draw the player cards: ");
            confirm = scanner.nextLine();
        }
    }
}
