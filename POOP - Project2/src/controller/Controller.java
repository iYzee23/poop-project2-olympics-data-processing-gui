package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import ds.DataPair;
import ds.DataTriplet;
import ds.Pair;
import model.Data;
import view.MainWindow;

public class Controller {
	
	private MainWindow myView;
	
	public Controller(MainWindow myView) {
		this.myView = myView;
	}
	
	private HashSet<String> countries = new HashSet<String>();
	private HashSet<String> yearSeasons = new HashSet<String>();
	
	// formiranje parova [drzava; broj takmicara]
	public ArrayList<Pair> performPie(String sport, String year, String eventType, String medal) {
		//dohvati podatke
		ArrayList<Pair> result = new ArrayList<Pair>();
		Data data = Data.getInstance();
		for(String it: countries) {
			int tmp = data.countCompetitors(it, year, eventType, sport, medal);
			result.add(new Pair(it, tmp));
		}
		Collections.sort(result, new Comparator<Pair>() {
		    public int compare(Pair p1, Pair p2) {
		        return p2.num - p1.num;
		    }
		});
		
		//izracunaj uglove za svaku drzavu
		int tmp = data.countCompetitors("", year, eventType, sport, medal);
		if(tmp==0) return null;
			
		ArrayList<Pair> param = new ArrayList<Pair>();
		int cnt = 0, sumAngles = 0, angle = 0;
		
		for(Pair p: result) {
			if(cnt == 9) {
				param.add(new Pair("Other", 360 - sumAngles));
				return param;
			}
			angle = (int)((p.num/(double)tmp)*360);
			if(angle + sumAngles > 360) {
				param.add(new Pair("Other", 360-sumAngles));
				return param;
			}
			param.add(new Pair(p.country, angle));
			sumAngles += angle;
			cnt++;
			if(angle == 0) {
				param.remove(param.size()-1);
				break;
			}
			if(angle<10) {
				param.get(param.size()-1).num = 360 - (sumAngles - angle);
				param.get(param.size()-1).country = "Other";
				return param;
				
//				param.remove(param.size()-1);
//				param.get(param.size()-1).num = 360 - (sumAngles - param.get(param.size()-1).num - angle);
//				param.get(param.size()-1).country = "Other";
//				return param;
			}
		}
		//poravnaj uglove na 360 u sumi, poslednji ugao se malo poveca
		param.get(param.size()-1).num = 360 - (sumAngles - param.get(param.size()-1).num);
		return param;
	}
	
	// formiranje trojki [vrednost; godina; leto/zima]
	public ArrayList<DataTriplet> performXY(String yearFrom, String yearTo, int option) {//option odlucuje sta se poziva od one tri funkcije
		if(yearFrom.isEmpty() || yearFrom.isEmpty()) return null;
		int from = Integer.parseInt(yearFrom);
		int to = Integer.parseInt(yearTo);
		if(from>to) return null;
		ArrayList<DataTriplet> dps = new ArrayList<DataTriplet>();
		for(String tmp: yearSeasons) {
			int tmpYear = Integer.parseInt(tmp.split(" ", 2)[0]);
			String tmpSeason = tmp.split(" ", 2)[1];
			if(tmpYear>=from && tmpYear<=to) {
				switch(option) {
				case 0:
					dps.add(new DataTriplet(Data.getInstance().numofDisciplines("" + tmp), tmpYear, tmpSeason));
					break;
				case 1:
					dps.add(new DataTriplet(Data.getInstance().avgHeight("" + tmp), tmpYear, tmpSeason));
					break;
				case 2:
					dps.add(new DataTriplet(Data.getInstance().avgWeight("" + tmp), tmpYear, tmpSeason));
					break;
				default:
					break;
				}
			}
		}
		return dps;
	}
	
	// parsiranje, ali ovo ne moze da se implementira u Cpp
	// tj verovatno moze, ali je ovde ideja da se koriste BufferedReader, FileReader, File, Stream, Pattern, Mather, lambda fje
	// zato u Data (kod mene OlimpicGames) dodajemo metode koje omogucavaju da se nakon parsiranja popune podaci iz Cpp
	// ove metode zatim koristimo kao native i uvozimo preko .dll bibliteke
	public void read(String year) {
		
		HashMap<Integer, TreeSet<String > > newAthletes = new HashMap<Integer, TreeSet<String > >();
		HashMap<Integer, TreeSet<String > > gamesDisAth = new HashMap<Integer, TreeSet<String > >();
		HashMap<Integer, TreeMap<String, Integer> > discHash = new HashMap<Integer, TreeMap<String, Integer> >();//integer u mapi broji pojave - zamena za multiset
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("data/events.txt")));
			Stream<String> s = br.lines();
			
			Pattern pattern = Pattern.compile("(" + year + "[^!]*)!([^!]*)!([^!]*)!([^!]*)!([^!]*)!([^!]*)!([^!]*)!([^!]*)");
			Data data = Data.getInstance();
			
			s.forEach((line)->{
				
				Matcher m = pattern.matcher(line);
				if(m.matches()) {
					
					String games = m.group(1);
					String city = m.group(2);
					String sport = m.group(3);
					String discipline = m.group(4);
					String type = m.group(5);
					String country = m.group(6);
					String ids = m.group(7);
					String medal = m.group(8);
					
					countries.add(country);
					yearSeasons.add(games);
					
					data.addDiscipline(discipline, type);
					data.addCountry(country);
					data.addGames(games, city);
					data.addSport(sport);
					data.addSportDiscipline(sport, discipline);
					data.addMedal(games, medal);
					
					if (ids.charAt(0) != '[') {
						TreeSet<String> it = newAthletes.get(Integer.parseInt(ids));
						
						data.addGamesCompetitor(games, Integer.parseInt(ids));
						data.addCountryCompetitor(country, Integer.parseInt(ids));
						
						if (it == null) {
							TreeSet<String> set = new TreeSet<String>();
							set.add(discipline + "!" + ((!medal.isEmpty()) ? games + medal : ""));// proveri ovo jel dobro odradi
							newAthletes.put(Integer.parseInt(ids), set);
						}
						else {
							it.add(discipline + "!" + ((!medal.isEmpty()) ? games + medal : ""));
						}
						
						TreeSet<String> itd = gamesDisAth.get(Integer.parseInt(ids));
						if (itd == null) {
							TreeSet<String> set = new TreeSet<String>();
							set.add(games + "!" + discipline);
							gamesDisAth.put(Integer.parseInt(ids), set);
						}
						else {
							itd.add(games + "!" + discipline);
						}
						if (!medal.isEmpty()) {
							TreeMap<String, Integer> ith = discHash.get(Integer.parseInt(ids));
							if (ith == null) {
								TreeMap<String, Integer> mp = new TreeMap<String, Integer>();
								mp.put(country + "!" + games+medal, 1);
								discHash.put(Integer.parseInt(ids), mp);
							}
							else {
								Integer cnt = ith.get(country + "!" + games+medal);
								if(cnt == null) {
									ith.put(country + "!" + games+medal, 1);
								}
								else {
									ith.put(country + "!" + games+medal, cnt + 1);
								}
							}
						}
					}
					else {
						int competitorId = data.addTeam();
						
						data.addCompetitorMedals(competitorId, discipline, (!medal.isEmpty()) ? games + medal : "");
						data.addCompetitorDiscipline(competitorId, games, discipline);
						data.addCompetitorCountry(competitorId, country, games+medal);
						
						data.addGamesCompetitor(games, competitorId);
						data.addCountryCompetitor(country, competitorId);

						ids = ids.substring(0, ids.length() - 1);
						ids = ids.substring(1);
						
						
						Pattern rxi = Pattern.compile("'([^']+)',? ?");//proveri
						Matcher matcher = rxi.matcher(ids);

						StringBuilder result = new StringBuilder();//proveri sta ce ti ovo
						while (matcher.find()) {
							
							int id = Integer.parseInt(matcher.group(1));
							data.addTeamAthlete(competitorId, id);
							
							//hesiranje atlete
							TreeSet<String> it = newAthletes.get(id);
							if (it == null) {
								TreeSet<String> set = new TreeSet<String>();
								set.add("!");//pazi obrati paznju
								newAthletes.put(id, set);
							}
						}
					}
				}
			});
		br.close();
		} catch (FileNotFoundException e) {
			System.err.println("Fajl nije pronadjen...");
		} catch (IOException e) {
		}
		
		//athletes.txt parsiranje
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("data/athletes.txt")));
			Stream<String> s = br.lines();
			
			Pattern pattern = Pattern.compile("([^!]*)!([^!]*)!([^!]*)!([^!]*)!([^!]*)!([^!]*)");
			Data data = Data.getInstance();

			s.forEach((line)->{
				
				Matcher m = pattern.matcher(line);
				if(m.matches()) {
					
					int id = Integer.parseInt(m.group(1));
					TreeSet<String> it = newAthletes.get(id);
					if (it == null) return; //kao continue
					
					String name = m.group(2);
					String gender = Character.toString(m.group(3).charAt(0));
					int age = (m.group(4).equals("NA")) ? 0 : Integer.parseInt(m.group(4));//pazi mora equals
					int height = (m.group(5).equals("NA")) ? 0 : (int)Double.parseDouble(m.group(5));
					int weight = (m.group(6).equals("NA")) ? 0 : (int)Double.parseDouble(m.group(6));
					
					data.addAthlete(id, name, gender, age, height, weight);
					
					for(String val: it) {
						String[] arr0 = val.split("!", 2); //ovde je stojalo bez dvojke proveri
						data.addCompetitorMedals(id, arr0[0], arr0[1]);
					}
					
					TreeSet<String> itd = gamesDisAth.get(id);
					if (itd != null) {
						for(String val: itd) {
							String[] arr1 = val.split("!", 2);
							data.addCompetitorDiscipline(id, arr1[0], arr1[1]);
						}
					} 
					TreeMap<String, Integer> ite = discHash.get(id);
					if (ite != null) {
						for(Map.Entry<String, Integer> entry : ite.entrySet()) {
							String[] arr2 = entry.getKey().split("!", 2);
							int cnt = entry.getValue();
							while(cnt>0) {
								data.addCompetitorCountry(id, arr2[0], arr2[1]);
								cnt--;
							}
						}
						
					}
				}
			});
		br.close();
		} catch (FileNotFoundException e) {
			System.err.println("Fajl nije pronadjen...");
		} catch (IOException e) {
		}
		
	}
	
}
