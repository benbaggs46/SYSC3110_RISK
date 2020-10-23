public class BoardConstructor {

    private String filename;

    public void setMap(String filename){
        this.filename = filename;
    }

    public Board createMap() {
        Board board = new Board();
        Continent northAmerica = new Continent("North America");
        northAmerica.setArmyBonus(5);

        Territory alaska = new Territory("Alaska");
        Territory alberta = new Territory("Alberta");
        Territory ontario = new Territory("Ontario");
        Territory quebec = new Territory("Quebec");
        Territory westernUnitedStates = new Territory("Western United States");
        Territory easternUnitedStates = new Territory("Eastern United States");
        Territory centralAmerica = new Territory("Central America");
        Territory northwestTerritories = new Territory("Northwest Territories");
        Territory greenland = new Territory("Greenland");

        northAmerica.addTerritory(alaska);
        northAmerica.addTerritory(alberta);
        northAmerica.addTerritory(ontario);
        northAmerica.addTerritory(quebec);
        northAmerica.addTerritory(westernUnitedStates);
        northAmerica.addTerritory(easternUnitedStates);
        northAmerica.addTerritory(centralAmerica);
        northAmerica.addTerritory(northwestTerritories);
        northAmerica.addTerritory(greenland);

        board.joinTerritories(alaska, alberta);
        board.joinTerritories(alaska, northwestTerritories);
        board.joinTerritories(alberta, northwestTerritories);
        board.joinTerritories(alberta, westernUnitedStates);
        board.joinTerritories(alberta, ontario);
        board.joinTerritories(quebec, ontario);
        board.joinTerritories(greenland, ontario);
        board.joinTerritories(quebec, easternUnitedStates);
        board.joinTerritories(quebec, greenland);
        board.joinTerritories(northwestTerritories, greenland);
        board.joinTerritories(westernUnitedStates, ontario);
        board.joinTerritories(easternUnitedStates, ontario);
        board.joinTerritories(westernUnitedStates, centralAmerica);
        board.joinTerritories(easternUnitedStates, centralAmerica);

        return board;
    }
}
