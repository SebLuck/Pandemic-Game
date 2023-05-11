import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class City {

    private static final String cityMapFileName= "fullMap.txt";

    static int numberCities;

    static int numberConnections;

    static String[] cities; //Cities
    static String[] diseaseColor;

  //Number of disease cubes in the associated city.
    static int[][] diseaseCubes; 
  //The connections via offset in the cities array.
    static  int[][] connections; 


    /* Open the city file, allocate the space for the cities and connections,
     * then read the cities, and then read the connections. */
    static void readCityGraph() {
        //Open the file and read it.
        try {
            File fileHandle = new File(cityMapFileName);
            Scanner mapFileReader = new Scanner(fileHandle);

            //Read the number of cities and allocate variables.
            numberCities = mapFileReader.nextInt();
            //Read the rest of the line after the int
            String data = mapFileReader.nextLine(); 
            //Allocate the cities array
            cities = new String[numberCities]; 
            diseaseCubes = new int[numberCities][4];
            diseaseColor = new String[numberCities];
            ResearchStation.researchStation = new boolean[numberCities];

            //Read the number of connections and allocate variables.
            numberConnections = mapFileReader.nextInt();
            //Read the rest of the line after the int.
            data = mapFileReader.nextLine();  
            connections = new int[2][numberConnections];

            //Read cities.
            readCities(numberCities,mapFileReader);
            //Read connections.
            readConnections(numberConnections,mapFileReader);

            mapFileReader.close();
        }

        catch (FileNotFoundException e) {
            System.out.println("An error occurred reading the city graph.");
            e.printStackTrace();
        }
    }

    //Read the specified number of cities.
    static void readCities(int numCities, Scanner scanner) {
        /*A loop reading cities in.  It assumes the file is text with 
         * the last character of the line being the last letter of the city
         *  name.*/
        for (int cityNumber = 0; cityNumber < numCities; cityNumber++) {
            String cityName = scanner.nextLine();
            cities[cityNumber] = cityName;
            // Initialized researchStation array of boolean.
            if(Objects.equals(cityName, "Atlanta")){
                ResearchStation.researchStation[cityNumber] = true;
            }else{
                ResearchStation.researchStation[cityNumber] = false;
            }
            // Initialized diseaseColor array.
            if (cityNumber < 12) {
                diseaseColor[cityNumber] = "Blue";
            } else if (cityNumber < 24) {
                diseaseColor[cityNumber] = "Yellow";
            } else if (cityNumber < 36) {
                diseaseColor[cityNumber] = "Black";
            } else {
                diseaseColor[cityNumber] = "Red";
            }
            Action.initAction();
            Disease.initDisease();
            ResearchStation.initResearchStationArray();
            PlayerCard.initPlayerCard();
            InfectionCard.initInfectionCard();

        }
    }

    //Read the specified number of connections.
    static void readConnections(int numConnections, Scanner scanner) {
        /*A simple loop reading connections in.  It assumes the file 
         * is text with the last character of the line being the last
         *  letter of the city name.  The two cities are separated by 
         *  a ";" with no spaces */
        for (int i = 0; i < numConnections; i++) {
            String connectionName = scanner.nextLine();
            String[] cityName = connectionName.split(";");
            int firstCityOffset = getCityOffset(cityName[0]);
            int secondCityOffset = getCityOffset(cityName[1]);
            connections[0][i] = firstCityOffset;
            connections[1][i] = secondCityOffset;
        }
    }

    /* Loop through the city array, and return the offset of the cityName
     *  parameter in that array.  Return -1 if the cityName is not in the
     *   array. */
     static int getCityOffset(String cityName) {
        for (int cityNumber = 0; cityNumber < numberCities; cityNumber++) {
            if (cityName.compareTo(cities[cityNumber]) == 0)
                return cityNumber;
        }
        return -1;
    }

    /*Give the player their current city name and how many cubes there are.*/
    static void cubesCurrentCity(int currentUser, int userLocation){
        System.out.println(GameBoard.userNames[currentUser] + " is in " + 
        		City.cities[userLocation] + ".");
        String color;
        int noDiseaseCounter = 0;
        for(int i = 0; i < City.diseaseCubes[userLocation].length; i++){
            color = Disease.getColorByIndex(i);
            if(City.diseaseCubes[userLocation][i] == 1){
                System.out.println("There is " + 
                City.diseaseCubes[userLocation][i] + " "+  color +
                " cube left.");
            }else if (City.diseaseCubes[userLocation][i] > 1){
                System.out.println("There are " +
                City.diseaseCubes[userLocation][i] + " "+  color +
                " cubes left.");
            }else{
                noDiseaseCounter++;
            }
        }
        if(noDiseaseCounter == 4){
            System.out.println("There is no disease cubes in " + 
            		City.cities[userLocation] + ".");
        }
    }
}
