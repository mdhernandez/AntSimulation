package antsimulation;

import java.util.LinkedHashMap;
import java.util.Random;

public class Square 
{
	private boolean hasAnts;
	private boolean visibility;
	private int food;
	private int pheromones;
	private boolean containsQueen;
	private int numScouts;
	private int numSoldiers;
	private int numForagers;
	private int numBalas;
	private boolean hasBeenRevealed;
	private int positionX;
	private int positionY;
	private ColonyNodeView nodeView;
	
	private LinkedHashMap<Integer, ScoutAnt> scouts;
	private LinkedHashMap<Integer, SoldierAnt> soldiers;
	private LinkedHashMap<Integer, BalaAnt> balas;
	private LinkedHashMap<Integer, Ant> ants;
	
	public boolean getHasAnts() {return !this.ants.isEmpty();}
	public void setHasAnts(boolean isAnts) {this.hasAnts = isAnts;}
	public boolean hasQueen() {return this.containsQueen;}
	
	public int getFood() {return this.food;}
	public void setFood(int remaining) {this.food = remaining;}
		
	public int getForagerCount() {return this.numForagers;}
	public int getScoutCount() {return this.numScouts;}
	public int getSoldierCount() {return this.numSoldiers;}
	public int getBalaCount() {return this.numBalas;}
	
	public void incrementForager(int amount) {this.numForagers += amount;}
	public void incrementScout(int amount) {this.numScouts += amount;}
	public void incrementSoldier(int amount) {this.numSoldiers += amount;}
	public void incrementBalas(int amount) {this.numBalas += amount;}
	
	public void setVisibility(boolean vis) {this.visibility = vis;}
	public void setQueenSquare(boolean queenPresent) {this.containsQueen = queenPresent;}
	public int getPosX() {return this.positionX;}
	public int getPosY() {return this.positionY;}
	public boolean isVisible() {return this.visibility;}
	public int getPheromones() {return this.pheromones;}
	public void setPheromones(int pAmount) {this.pheromones = pAmount;}
	public ColonyNodeView getNodeView() {return this.nodeView;}
	public void setNodeView(ColonyNodeView colony) {this.nodeView = colony;}
	public boolean getWasRevealed() {return this.hasBeenRevealed;}
	public void setWasRevealed(boolean rev) {this.hasBeenRevealed = rev;}
	
	//sets the number of ants that start on a given space
	public void setAllAnts(int s, int sc, int f) 
	{
		this.numScouts = sc;
		this.numSoldiers = s;
		this.numForagers = f;
	}
	
	public void addSoldier(SoldierAnt nextSoldier) 
	{
		Integer temp = nextSoldier.getID();
		this.soldiers.put(temp, nextSoldier);
	}
	
	public boolean hasBalas() 
	{
		if(this.balas.isEmpty()) 
		{
			return false;
		}
		
		return true;
	}
	
	public int antNumber() 
	{
		return this.ants.size();
	}
	
	public void addScout (ScoutAnt nextScout) 
	{
		this.scouts.put(nextScout.getID(), nextScout);
	}
	
	//returns true if there is 1 or more ants on this square, false otherwise.
	public boolean hasAntsUpdate() 
	{
		if(this.containsQueen) 
		{
			return true;
		}
		
		if (this.hasAnts)
		{
			return true;
		}
		
		return false;
	}
	
	public BalaAnt killBala() 
	{
		//return balas.poll();
		BalaAnt toKill;
		toKill = balas.get(balas.entrySet().iterator().next().getKey());
		return toKill;
	}
	
	public void addBala(BalaAnt temp) 
	{
		this.balas.put(temp.getID(), temp);
		this.numBalas++;
		this.render();
	}
	
	public void removeBala(Integer ID) 
	{
		this.balas.remove(ID);
		this.numBalas--;
		this.render();
	
	}
	
	public Ant getAnt() 
	{
		Ant toKill;
		toKill = ants.get(ants.entrySet().iterator().next().getKey());
		return toKill;
	}
	
	public void addAnt(Ant toAdd) 
	{
		this.ants.put(toAdd.getID(), toAdd);
		if(toAdd instanceof ScoutAnt) {this.numScouts++;}
		else if(toAdd instanceof SoldierAnt) {this.numSoldiers++;}
		else if(toAdd instanceof ForagerAnt) {this.numForagers++;} 
		this.render();
	}
	
	public void removeAnt(Integer ID) 
	{
		Ant toRem = this.ants.remove(ID);
		if(toRem instanceof ScoutAnt) 
		{
			this.numScouts--;
		}
		else if(toRem instanceof SoldierAnt) 
		{
			this.numSoldiers--;
			
		}
		else if(toRem instanceof ForagerAnt) 
		{
			this.numForagers--;
			
		} 
		
		this.render();
	}
	
	public void reveal() 
	{
		this.visibility = true;
		Random rng = new Random();
		int foodChance = rng.nextInt(4);
		
		//there is a 1/4 chance the square will contain food
		if(foodChance == 0 && !this.hasQueen()) 
		{
			//the amount of food on the square is randomly selected between 500 and 1000 units
			int foodAmt = rng.nextInt(501);
			this.setFood(foodAmt + 500);
		}
		
		
		this.render();
	}
	
	public void render() 
	{
		this.nodeView.setForagerCount(this.numForagers);
		this.nodeView.setScoutCount(this.numScouts);
		this.nodeView.setBalaCount(this.numBalas);
		this.nodeView.setSoldierCount(this.numSoldiers);
		this.nodeView.setFoodAmount(this.food);
		this.nodeView.setPheromoneLevel(this.pheromones);
		this.icons();
		if(this.visibility)
		this.nodeView.showNode();
	}
	
	public void icons() 
	{
		if(numScouts > 0) {this.nodeView.showScoutIcon();}
		else {this.nodeView.hideScoutIcon();}
		if(numForagers > 0) {this.nodeView.showForagerIcon();}
		else {this.nodeView.hideForagerIcon();}
		if(numSoldiers > 0) {this.nodeView.showSoldierIcon();}
		else {this.nodeView.hideSoldierIcon();}
		if(numBalas > 0) {this.nodeView.showBalaIcon();}
		else {this.nodeView.hideBalaIcon();}
	}
	
	
	public Square(int x, int y) 
	{
		this.positionX = x;
		this.positionY = y;
		this.visibility = false;//square initially starts off unrevealed
		this.hasAnts = false;
		this.numBalas = 0;
		this.numForagers = 0;
		this.numScouts = 0;
		this.numSoldiers = 0;
		this.ants = new LinkedHashMap<>();
		this.balas = new LinkedHashMap<>();
		this.hasBeenRevealed = false;
		
	}
	
	//used to reset the simulation
	public void clear() 
	{
		//this.antsToRemove = new ArrayList<Integer>();
		this.hasAnts = false;
		this.numBalas = 0;
		this.numForagers = 0;
		this.numScouts = 0;
		this.numSoldiers = 0;
		this.setFood(0);
		this.setPheromones(0);
		this.ants = new LinkedHashMap<>();
		this.balas = new LinkedHashMap<>();
		this.hasBeenRevealed = false;
		ColonyNodeView temp = this.getNodeView();
		temp.setBalaCount(0);
		temp.setSoldierCount(0);
		temp.setScoutCount(0);
		temp.setForagerCount(0);
		temp.setFoodAmount(0);
		temp.setPheromoneLevel(0);
		this.icons();
		
		if(!this.containsQueen) 
		{
			this.visibility = false;
			temp.hideNode();
		}
		this.render();
	}
}
