import java.sql.SQLOutput;
import java.util.*;

public class InfectionCard {
    static int infectionRate;
    static Stack<String> infectionDeck;

    static  Stack<String> discardCard;

    static int outbreakNumber;
    static int infectionRatePosition;

    static boolean[] outbreakList;
    /* Method that will initialize all the variables in the InfectionCard
     * class. All city cards will be added to the infection deck in a for
     * loop. The array of boolean outbreakList will be initialized to keep
     * track of which city had an outbreak in a given turn.*/
    public static void initInfectionCard(){
        outbreakNumber = 0;
        infectionRate = 2;
        infectionRatePosition = 1;
        infectionDeck = new Stack<>();
        for(int i = 0; i < City.cities.length; i++ ){
            infectionDeck.push((City.cities[i]) + "," + (City.diseaseColor[i])
            		+ ",Card");
        }

        discardCard = new Stack<>();
        outbreakList = new boolean[City.numberCities];
        for(int i = 0; i < City.numberCities; i++){
            outbreakList[i] = false;
        }
    }


    /* Method that adds 18 disease cubes at the start of the game. There are
     * 3 for loop. The first one will add 3 disease cubes in each 3 cities.
     * The second one will add 2 disease cubes in each 3 cities. Finally, the
     * third one will add 1 disease cube to each of the 3 cities.*/
    static void infectCities() {
        int diseaseNum;
        System.out.println("Infect cities by drawing 9 cards in the"
        		+ " infection deck.");
        System.out.println();
        InfectionCard.menuDrawInfectionDeck();
        for(int i = 0; i < 3; i++){
            diseaseNum = 3;
            infectCityWithDisease(diseaseNum);
            System.out.println();
            GameBoard.sleepGame(1000);
        }
        for(int i = 0; i < 3; i++){
            diseaseNum = 2;
            infectCityWithDisease(diseaseNum);
            System.out.println();
            GameBoard.sleepGame(1000);
        }
        for(int i = 0; i < 3; i++){
            diseaseNum = 1;
            infectCityWithDisease(diseaseNum);
            System.out.println();
            GameBoard.sleepGame(1000);
        }
    }
    /* This method will place disease cubes in cities. The diseaseNum parameter
     * is the number of disease cubes that are going to be placed in a city.
     * The card drawn from the infection deck is added to the discard pile, 
     * which is the stack discardCard here.*/
    static void infectCityWithDisease(int diseaseNum){
        String card;
        card = InfectionCard.infectionDeck.pop();
        System.out.println("The " + card + " was drawn.");
        InfectionCard.discardCard.push(card);
        String[] splitCard = card.split(",");
        int cityToInfect = City.getCityOffset(splitCard[0]);
        int colorIndex = Disease.getColorIndex(splitCard[1]);
        Disease.diseaseCounter += diseaseNum;
        Disease.addDiseaseColorCounter(colorIndex, diseaseNum);
        City.diseaseCubes[cityToInfect][colorIndex] +=  diseaseNum;
        if(diseaseNum > 1) {
            System.out.println(diseaseNum+ " "+ splitCard[1] + " cubes have been"
            		+ " added to " + splitCard[0]);

        }else {
            System.out.println(diseaseNum+ " "+ splitCard[1] + " cube has been"
            		+ " added to " + splitCard[0]);

        }

    }

    /* Check if the infection deck is empty. Add the card from the stack
     * discardCard to the infectionDeck stack. Shuffle the infectionDeck
     * stack.*/
    public static void checkEmpty(){
        if(infectionDeck.isEmpty()){
            System.out.println("Infection deck is empty.");
            System.out.println("Add all cards from the discard pile to the"
            		+ " infection deck.");
            System.out.println("Shuffle the Infection Deck...");
            infectionDeck.addAll(discardCard);
            discardCard.clear();
            Collections.shuffle(infectionDeck);
        }
    }
    
    /* Draw infection cards based on the infection rate. Call the method
     * addDiseaseCube to add the disease that matches the color of the card
     * drawn from the infection deck. Then check if the infection deck is
     * empty by calling the method checkEmpty().*/
    public static void drawTurn(){
        String card;
        if(!EventCard.skipInfectCity){
            menuDrawInfectionDeck();
            for(int i = 0; i < infectionRate; i++){
                card = infectionDeck.pop();
                discardCard.add(card);
                addDiseaseCube(card);
                checkEmpty();
            }
        }else{
            //If a player has used the One Quiet Night Card.
            EventCard.skipInfectCity = false;
        }
    }
    /* This method will perform the epidemic event functionality. It will start
     * by drawing a card from the bottom of the infection deck. This card is 
     * then added to the discard pile. The discard pile is then shuffled. Place
     * the discard pile cards on top of the infection deck by adding all the
     * discardCard stack to the infectionDeck stack.After this, it will call a
     * method that will add the three disease cubes to the city. If the disease
     * has already been eradicated, no disease cube will be added.*/
    public static void drawEpidemicEvent(){
        String card;
        System.out.println("Draw bottom card of the infection deck and add 3"
        		+ " disease cubes to the city.");
        menuDrawInfectionDeck();
        card = infectionDeck.remove(0);
        System.out.println("The " + card + " has been drawn.");
        System.out.println();
        GameBoard.sleepGame(1000);
        discardCard.push(card);
        System.out.println("Shuffle the discard pile of the infection deck.");
        System.out.println();
        Collections.shuffle(discardCard);
        GameBoard.sleepGame(1000);
        infectionDeck.addAll(discardCard);
        System.out.println("Add all the cards in the discard pile at the top "
        		+ "of the infection deck");
        System.out.println();
        GameBoard.sleepGame(1000);
        discardCard.clear();
        String[] splitCard = card.split(",");
        int cityIndex = City.getCityOffset(splitCard[0]);
        int colorIndex = Disease.getColorIndex(splitCard[1]);
        if(((Objects.equals(splitCard[1], "Blue") && 
        		!Disease.blueDiseaseEradicated) ||
                (Objects.equals(splitCard[1], "Yellow") && 
                		!Disease.yellowDiseaseEradicated) ||
                (Objects.equals(splitCard[1], "Black") && 
                		!Disease.blackDiseaseEradicated) ||
                (Objects.equals(splitCard[1], "Red") &&
                		!Disease.redDiseaseEradicated)) &&
                (!Roles.preventDisease[cityIndex][colorIndex])
        ){
            addCubeEpidemicEvent(card);
        }
    }
    /* This method will be called when the player draws a card from the 
     * infection deck. It will check which color the card matches to add
     * a disease cube of that color, and it will check if the disease of
     * that color has been eradicated or not. If it has been eradicated,
     * the disease cube will not be added to a city. The method will also
     * check if there are already 3 disease cubes in a city before adding a
     * disease cube to call the outbreak method.*/
    public static void addDiseaseCube(String card){
        System.out.println();
        System.out.println(GameBoard.userNames[GameBoard.currentUser] +
        		" has drawn the " + card + " in the infection deck");
        GameBoard.sleepGame(1000);
        String[] splitCard = card.split(",");
        int cityToInfect = City.getCityOffset(splitCard[0]);
        int colorIndex = Disease.getColorIndex(splitCard[1]);
        if(((Objects.equals(splitCard[1], "Blue") &&
        		!Disease.blueDiseaseEradicated) ||
                (Objects.equals(splitCard[1], "Yellow") && 
                		!Disease.yellowDiseaseEradicated) ||
                (Objects.equals(splitCard[1], "Black") &&
                		!Disease.blackDiseaseEradicated) ||
                (Objects.equals(splitCard[1], "Red") && 
                		!Disease.redDiseaseEradicated)) &&
                        (!Roles.preventDisease[cityToInfect][colorIndex])
        ){
            System.out.println("1 "+ splitCard[1] + " cube has been added to "
        + splitCard[0]);
            if(City.diseaseCubes[cityToInfect][colorIndex] == 3){
                outbreak(splitCard[0], cityToInfect, colorIndex);
            }else{
                City.diseaseCubes[cityToInfect][colorIndex]++;
                Disease.diseaseCounter++;
                Disease.addDiseaseColorCounter(colorIndex, 1);
                System.out.println(splitCard[0] + " has now " +
                City.diseaseCubes[cityToInfect][colorIndex] + " cube(s)");
            }
        }
    }
    /* This method will be called when a player will have to draw one card at
     * the bottom of the infection deck from the epidemic event. It will add 3
     * disease cubes to the corresponding city on the infection card. If the
     * city has at least 1 disease cube of that color, it will use the outbreak
     * method.*/
    public static void addCubeEpidemicEvent(String card){
        String[] splitCard = card.split(",");
        int cityToInfect = City.getCityOffset(splitCard[0]);
        int colorIndex = Disease.getColorIndex(splitCard[1]);
        Disease.diseaseCounter += (3 - City.diseaseCubes
        		[cityToInfect][colorIndex]);
        Disease.addDiseaseColorCounter(colorIndex, (3 - 
        		City.diseaseCubes[cityToInfect][colorIndex]));
        City.diseaseCubes[cityToInfect][colorIndex] += 3;
        System.out.println("3 " +  splitCard[1]+ " cubes have been added to "
        + splitCard[0]);
        if(City.diseaseCubes[cityToInfect][colorIndex] > 3){
            City.diseaseCubes[cityToInfect][colorIndex] = 3;
            System.out.println();
            outbreak(splitCard[0], cityToInfect, colorIndex);
        }else{
            System.out.println(splitCard[0] + " has now " +
        City.diseaseCubes[cityToInfect][colorIndex] + " cube(s)");
        }
        System.out.println();
    }
    /* This method will be called when an epidemic card is drawn to check
     * if the infection rate is greater than 3 or 5. If it is, it will update
     * the infection rate.*/
    public static void updateInfectionRate(){
        if(infectionRatePosition > 3 && infectionRate == 2){
            System.out.println("Infection rate goes from 2 to 3.");
            System.out.println("Players have to draw 3 infection cards"
            		+ " each turn now.");
            infectionRate = 3;
        }
        if (infectionRatePosition > 5 && infectionRate == 3) {
            System.out.println("Infection rate goes from 3 to 4.");
            System.out.println("Players have to draw 4 infection cards"
            		+ " each turn now.");
            infectionRate = 4;
        }
    }

    /* This method will be called if a disease cube is added to a city with 3
     * disease cubes. It will start by adding 1 to the outbreakNumber and
     * marking an outbreak in the city by setting the outbreakList with the
     * city index to true. It will then loop through all the cities and 
     * connections to check which city has a connection with the city that has
     * an outbreak.*/
    public static void outbreak( String infectedCity, int cityIndex,
    		int colorIndex){
        if(!outbreakList[cityIndex]){
            outbreakNumber++;
            outbreakList[cityIndex] = true;
            System.out.println("Outbreak occurs in " + infectedCity + "!!!");
            GameBoard.sleepGame(1000);
            for(int i = 0; i < City.numberCities; i++){
                for(int j = 0; j < City.numberConnections; j++){
                    if ((City.connections[0][j] == cityIndex) &&
                            (City.connections[1][j] == i) &&
                            !Roles.preventDisease[i][colorIndex]){
                        spreadDisease(i, colorIndex);
                    }else if ((City.connections[0][j] == i) &&
                            (City.connections[1][j] == cityIndex) &&
                            !Roles.preventDisease[i][colorIndex]){
                        spreadDisease(i, colorIndex);
                    }
                }
            }
        }
    }

    /* Each connected city will get 1 disease cube added to it. If one of the
     * connected cities has 3 cubes of that color. It will cause another
     * outbreak. It will then make a recursive call to the outbreak method. */
    private static void spreadDisease(int cityIndex, int colorIndex) {
        String color = Disease.getColorByIndex(colorIndex);
        System.out.println("Spread 1 " + color + " cube to " +
                City.cities[cityIndex] + ".");
        GameBoard.sleepGame(500);

        if (City.diseaseCubes[cityIndex][colorIndex] == 3) {
            outbreak(City.cities[cityIndex], cityIndex, colorIndex);
        } else {
            City.diseaseCubes[cityIndex][colorIndex]++;
            System.out.println(City.cities[cityIndex] + " has now " +
                    City.diseaseCubes[cityIndex][colorIndex] + " cube(s)");
            GameBoard.sleepGame(500);
            Disease.diseaseCounter++;
            Disease.addDiseaseColorCounter(colorIndex, 1);
        }
    }

    // Method that will be called when the player has to draw infection cards.
    public static void menuDrawInfectionDeck(){
        Scanner scanner = new Scanner(System.in);
        String confirm = "";
        System.out.println("Type 1 to draw the infection card(s): ");
        confirm = scanner.nextLine();
        while(!Objects.equals(confirm, "1")){
            System.out.println("Type 1 to draw the infection card(s): ");
            confirm = scanner.nextLine();
        }
    }

    /* Method that will be called every time a player's turn ends. It will keep
     * track of all the cities that have 3 disease cubes and whether the cards
     * of those cities are still in the player deck. If this is the case, it
     * will warn the user to go to those cities and prevent outbreaks there.*/
    static void preventOutbreak(){
        int counter = 0;
        for(int i = 0; i < InfectionCard.infectionDeck.size(); i++){
            String[] splitCard = InfectionCard.infectionDeck.get(i).split(",");
            int cityIndex = City.getCityOffset(splitCard[0]);
            int colorIndex = Disease.getColorIndex(splitCard[1]);
            if(City.diseaseCubes[cityIndex][colorIndex] == 3){
                counter++;
                System.out.println("You may go to " + splitCard[0] +
                		" to prevent a potential outbreak!!!");
                GameBoard.sleepGame(500);
            }
        }
        if(counter == 1){
            System.out.println("There is a chance that you will draw a card in"
            		+ " the infection deck that matches that city.");
            GameBoard.continueToMenu();
        } else if (counter > 1) {
            System.out.println("There is a chance that you will draw a card in"
            		+ " the infection deck that matches those cities.");
            GameBoard.continueToMenu();
        }
    }


}
