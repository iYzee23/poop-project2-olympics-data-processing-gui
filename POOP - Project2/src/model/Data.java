package model;

public class Data {
	
	private static Data instance;
	
	private Data() {}
	
	public static Data getInstance() {
		if(instance == null) {
			instance = new Data();
		}
		return instance;
	}
	
	public native void addDiscipline(String disciplineName, String type);
	public native void addCountry(String countryName);
	public native void addGames(String gamesName, String city);
	public native void addSport(String sportName);
	public native void addMedal(String type, String gamesName);
	public native int addTeam();
	public native void addAthlete(int id, String name, String gender, int age, int height, int weight);

	public native int countCompetitors(String country, String year, String type, String sport, String medal);

	public native void addSportDiscipline(String sport, String discipline);
	public native void addGamesCompetitor(String games, int id);
	public native void addCountryCompetitor(String country, int id);
	
	public native void addCompetitorMedals(int competitorId, String discipline, String gamesMedal);
	public native void addCompetitorDiscipline(int competitorId, String games, String discipline);
	public native void addCompetitorCountry(int competitorId, String country, String gamesMedal);
	
	public native void addTeamAthlete(int teamId, int athleteId);
	
	public native void deleteData();
	
	public native int numofDisciplines(String yearSeason);
	public native double avgHeight(String yearSeason);
	public native double avgWeight(String yearSeason);
	
}
