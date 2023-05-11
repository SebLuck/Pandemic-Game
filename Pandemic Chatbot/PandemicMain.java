import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

/* This is the main method of the pandemic game, where the user will have the
 * option to start the game and quit. Before the game starts, some methods
 * will be called to make the game setup. The readCityGraph method will read
 * the file, which has all the cities and connections. All the connections 
 * will be made. The players will then choose their role. The Player Deck 
 * and Infection Deck will be shuffled. The cities will be infected by 
 * calling the method infectCities. Each player will draw cards at the 
 * start. After this, the epidemic cards will be added to the player deck
 * by calling the method addEpidemicCard.*/

public class PandemicMain {
	static boolean gameDone = false;
	public static void main(String[] args) {
		String mainMenu = "";
		Scanner scanner = new Scanner(System.in);
		while (!mainMenu.equals("0")) {
			System.out.println("Pandemic Game");
			System.out.println("1 - Start Game.");
			System.out.println("0 - Exit Game.");
			System.out.println("Enter a number: ");
			mainMenu = scanner.nextLine();
			while (!Objects.equals(mainMenu, "1") && 
					!Objects.equals(mainMenu, "0")) {
				System.out.println("Please enter a valid number: ");
				mainMenu = scanner.nextLine();
			}
			if(mainMenu.equals("1")){
				// Initialize the location of the user.
				GameBoard.initGameBoard();
				// Get the cities and their connections.
				City.readCityGraph();
				Roles.initRoles();
				// Make the players choose their roles.
				Roles.chooseRole();
				EventCard.eventCardInit();
				// Shuffle the player deck.
				Collections.shuffle(PlayerCard.playerDeck);
				// Shuffle the infection deck.
				Collections.shuffle(InfectionCard.infectionDeck);
				// Infect cities by drawing 9 cards from the infection deck.
				InfectionCard.infectCities();
				// Each player draws 4 cards from the player deck.
				PlayerCard.drawStart();
				// Add the epidemic cards in the player deck.
				PlayerCard.addEpidemicCard();
				System.out.println();
				if(Roles.researcher){
					if(Objects.equals(Roles.playerRoles[0], "Researcher")){
						Roles.researcherGiveCardToCure(PlayerCard.player1Card,
								PlayerCard.player2Card, 0, 1);
					}else if(Objects.equals(Roles.playerRoles[1], 
							"Researcher")){
						Roles.researcherGiveCardToCure(PlayerCard.player2Card,
								PlayerCard.player1Card, 1, 0);
					}
				}
				while (!gameDone) {
					if(InfectionCard.outbreakNumber >= 8){
						System.out.println();
						System.out.println("You lose! There are at least "
								+ "8 outbreaks on the game board.");
						System.out.println();
						break;
					} else if (Disease.curedDiseaseNum == 4) {
						System.out.println();
						System.out.println("Congratulation! You won! All "
								+ "diseases have been cured!");
						System.out.println();
						break;
					} else if (Disease.diseaseCounter == 0) {
						System.out.println();
						System.out.println("Congratulation! You won! All "
								+ "disease cubes have been eradicated!");
						System.out.println();
						break;
					} else if (Disease.redDiseaseCounter > 24 ||
							Disease.yellowDiseaseCounter > 24 ||
							Disease.blackDiseaseCounter > 24 || 
							Disease.blueDiseaseCounter > 24 ) {
						System.out.println();
						System.out.println("You lose! You have run out of"
								+ " disease cubes.");
						System.out.println();
						break;
					}
					if(GameBoard.currentUser == 0){
						City.cubesCurrentCity(0, GameBoard.userLocation[0]);
						Disease.checkCureDisease(PlayerCard.player1Card,
							Roles.playerRoles[0],GameBoard.userLocation[0]);
					}else{
						City.cubesCurrentCity(1, GameBoard.userLocation[1]);
						Disease.checkCureDisease(PlayerCard.player2Card,
							Roles.playerRoles[1],GameBoard.userLocation[1]);
					}
					ResearchStation.researchStationCurrentCity();
					if(Action.actionPoint > 1){
						System.out.println("You have " + Action.actionPoint +
								" action points left.");
					}else{
						System.out.println("You have " + Action.actionPoint +
								" action point left.");
					}
					int userInput = GameBoard.getUserInput();
					gameDone = GameBoard.processUserCommand(userInput);

				}
				gameDone = false;

			}
		}
	}
}