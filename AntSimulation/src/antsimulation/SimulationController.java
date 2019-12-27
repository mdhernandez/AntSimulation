package antsimulation;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.ArrayList;

public class SimulationController implements SimulationEventListener
{
	private Square[][] board;
	private boolean setUp = false;//signifies if a test condition has been set-up
	private int turn;//tracks global turns
	private int day;//tracks global days
	private int nextAntID;//stores the ID to be assigned to the next ant that is spawned
	private boolean antsCanSpawn;//allows certain test set-ups to disable new ants from spawning
	private boolean clean;//used as a go-ahead for a simulation to run
	private ColonyView view;
	AntSimGUI gui;//for changing the time display on a turn-by-tun basis
	
	LinkedHashMap <Integer, Ant> ants;
	ArrayList<Integer> antsToKill;
	QueenAnt queen;
		
	public boolean canSpawn() {return this.antsCanSpawn;};
	
	//default constructor for initialization
	public SimulationController() 
	{
		this.turn = 1;//the simulation begins at turn 1
		this.day = 1;//the simulation begins on day 1
		this.clean = false;
	}
	
	public SimulationController(ColonyView colony, AntSimGUI access) 
	{
		this();
		
		this.view = colony;
		this.gui = access;
		
		//board initialization;
		board = new Square[27][27];
		for(int i = 0; i < 27; i++) 
		{
			for(int j = 0; j < 27; j++) 
			{
				board[i][j] = new Square(i, j);
				ColonyNodeView temp = new ColonyNodeView();
				this.view.addColonyNodeView(temp, i, j);
				board[i][j].setNodeView(temp);
				temp.setID(i + ", " + j);
			}
		}
		//hashmap initialization
		this.ants = new LinkedHashMap<Integer, Ant>();
		this.antsToKill = new ArrayList<>();
		
		//spawns queen in the center of the map
		ColonyNodeView tempQueen = board[13][13].getNodeView();
		board[13][13].setQueenSquare(true);
		board[13][13].setVisibility(true);
		this.queen = new QueenAnt(board[13][13], this);//queen initialization
		tempQueen.setQueen(true);
		tempQueen.showQueenIcon();
		tempQueen.showNode();
		
		this.nextAntID = 1;//queen is assigned an ID of 0 automatically
		this.ants = new LinkedHashMap<>();//initializes the ants hash map		
	}
	
	//resets the simulation to be set-up and run again
	public void cleanSlate() 
	{
		for(int i = 0; i < 27; i++) 
		{
			for(int j = 0; j < 27; j++) 
			{
				board[i][j].clear();
			}
		}
		this.turn = 1;
		this.day = 1;
		this.nextAntID = 1;
		this.ants.clear();
		this.antsToKill.clear();
		this.gui.setTime("Day " + this.day + ", Turn" + this.turn%10);
	}
	
	//retrieval, set, and increment operations on nextID variable
	public Integer getNextID() {return this.nextAntID;}
	public void setNextID(Integer next) {this.nextAntID = next;}
	public void incrementNextID() {this.nextAntID++;}
	
	public void addToAntHash(Ant nextAnt) 
	{
		this.ants.put(nextAnt.getID(), nextAnt);
	}
	public void addToMarkedAnts(Integer ID) 
	{
		this.antsToKill.add(ID);
	}
	
	//implements bala spawning mechanics
	public void balaSpawn() 
	{
		Random rng = new Random();
		int spawnChance = rng.nextInt(100);
		
		//balas have a 3/100 chance to spawn
		if(spawnChance < 3) 
		{
			
			BalaAnt tempBala = new BalaAnt(this.nextAntID, this.board[0][0], this);
			this.ants.put(tempBala.getID(), tempBala);
			this.nextAntID++;//increments ID of next ant
		}
	}
	
	//Sets up the standard configuration where the queen, 4 scout ants, 10 soldier ants, and 50 forager ants are spawned initially
	//and new ants can spawn
	public void normalSetUp() 
	{
		if(!this.clean || this.setUp) 
		{
			cleanSlate();
			this.clean = true;
		}
		
		queen.setAlive(true);
		this.antsCanSpawn = true;
		Square start = board[13][13];
		ColonyNodeView temp = start.getNodeView();
		int i;//loop variable;
		
		//scout initialization loop
		for(i=0; i < 4; i++) 
		{
			ScoutAnt scTemp = new ScoutAnt(this.nextAntID, start, this);
			this.ants.put(this.nextAntID, scTemp);
			this.nextAntID++;//increments ID to ensure ID uniqueness
		}
		
		//soldier initialization loop
		for(i = 0; i < 10; i++) 
		{
			SoldierAnt sTemp = new SoldierAnt(this.nextAntID, start, this);
			this.ants.put(this.nextAntID, sTemp);
			this.nextAntID++;//increments ID to ensure ID uniqueness
		}
		
		//forager initialization loop
		for(i = 0; i < 50; i++) 
		{
			ForagerAnt fTemp = new ForagerAnt(this.nextAntID, start, this);
			this.ants.put(this.nextAntID, fTemp);
			start.addAnt(fTemp);
			this.nextAntID++;//increments ID to ensure ID uniqueness
		}
		
		//start.setAllAnts(4, 10, 50);
		temp.setForagerCount(50);
		temp.setScoutCount(4);
		temp.setSoldierCount(10);
		temp.showForagerIcon();
		temp.showScoutIcon();
		temp.showSoldierIcon();
		start.setFood(1000);
		temp.setFoodAmount(1000);
		temp.setBalaCount(0);
		temp.setPheromoneLevel(0);
		setUp = true;
	}
	
	//Sets up a configuration where only the queen is spawned initially and no new ants can spawn
	public void queenSetUp() 
	{
		
		if(!this.clean|| this.setUp) 
		{
			cleanSlate();
			this.clean = true;
		}
		
		queen.setAlive(true);
		this.antsCanSpawn = false;
		Square start = board[13][13];
		ColonyNodeView temp = start.getNodeView();
		start.setFood(1000);
		temp.setForagerCount(0);
		temp.setScoutCount(0);
		temp.setSoldierCount(0);
		temp.setBalaCount(0);
		temp.setFoodAmount(1000);
		temp.setPheromoneLevel(0);
		this.setUp = true;
	}
	
	//Sets up a configuration where only the queen is spawned initally and new ants can spawn
	public void queenAltSetUp() 
	{
		if(!this.clean|| this.setUp) 
		{
			cleanSlate();
			this.clean = true;
		}
		
		queen.setAlive(true);
		this.antsCanSpawn = true;
		Square start = board[13][13];
		ColonyNodeView temp = start.getNodeView();
		start.setFood(1000);
		temp.setForagerCount(0);
		temp.setScoutCount(0);
		temp.setSoldierCount(0);
		temp.setBalaCount(0);
		temp.setFoodAmount(1000);
		temp.setPheromoneLevel(0);
		this.setUp = true;
	
	}
	
	//Sets up a configuration where only the queen and one scout ant are spawned initially and no new ants can spawn
	public void scoutSetUp() 
	{
		
		if(!this.clean || this.setUp) 
		{
			cleanSlate();
			this.clean = true;
		}
		queen.setAlive(true);
		this.antsCanSpawn = false;
		Square start = board[13][13];
		ColonyNodeView temp = start.getNodeView();
		this.ants.put(this.nextAntID, new ScoutAnt(this.nextAntID, board[13][13], this));
		this.nextAntID++;
		start.setFood(4000);
		temp.setForagerCount(0);
		temp.setScoutCount(1);
		temp.showScoutIcon();
		temp.setSoldierCount(0);
		temp.setBalaCount(0);
		temp.setFoodAmount(4000);
		temp.setPheromoneLevel(0);
		this.setUp = true;
	}
	
	//Sets up a configuration where only the queen, one soldier, and one bala is spawned initially and no new ants can spawn
	public void soldierSetUp() 
	{
		if(!this.clean || this.setUp) 
		{
			cleanSlate();
			this.clean = true;
		}
		queen.setAlive(true);
		this.antsCanSpawn = false;
		Square start = board[13][13];
		ColonyNodeView temp = start.getNodeView();
		this.ants.put(this.nextAntID, new SoldierAnt(this.nextAntID, board[13][13], this));
		this.nextAntID++;
		this.ants.put(this.nextAntID, new BalaAnt(this.nextAntID, board[13][12], this));
		this.nextAntID++;
		start.setFood(4000);
		temp.setForagerCount(0);
		temp.setScoutCount(0);
		temp.setSoldierCount(1);
		temp.showSoldierIcon();
		
		temp.setBalaCount(0);
		temp.setFoodAmount(4000);
		temp.setPheromoneLevel(0);
		sectionReveal();
		this.setUp = true;
		
	}
	
	//Sets up a configuration where only the queen ant one forager is spawned initially and no new ants can spawn
	public void foragerSetUp() 
	{
		if(!this.clean || this.setUp) 
		{
			
			cleanSlate();
			this.clean = true;
		}
		queen.setAlive(true);
		this.antsCanSpawn = false;
		Square start = board[13][13];
		ColonyNodeView temp = start.getNodeView();
		this.ants.put(this.nextAntID, new ForagerAnt(this.nextAntID, board[13][13], this));
		this.nextAntID++;
		
		start.setFood(1000);
		temp.setForagerCount(1);
		temp.setScoutCount(0);
		temp.setSoldierCount(0);
		temp.showForagerIcon();
		temp.setBalaCount(0);
		temp.setFoodAmount(1000);
		temp.setPheromoneLevel(0);
		
		sectionReveal();
		this.setUp = true;
	}
	
		//reveals a section of the map for testing purposes for configurations without scout ants
		public void sectionReveal() 
		{
			int i= 0;
			int j = 0;
			
			int rt = 3;
			
			int startInt = 13 - rt;
			
			Square start = board[startInt][startInt];
			for(i = 0; i < rt * rt; i++) 
			{
				for(j = 0; j < rt * rt; j++) 
				{
					start = board[startInt + i][startInt + j];
					if(!start.hasQueen()) 
					{
						start.reveal();
					}
					
				}
			}
		}
	
	//returns a reference to the square found at a certain position on the board
	public Square getSquare(int posX, int posY) 
	{
		return this.board[posX][posY];
	}

	//updates the pheromone concentration on each square and renders the board
	public void updateBoard() 
	{
		int tempPheromones;
		for(int i = 0; i < 27; i++) 
		{
			for(int j = 0; j < 27; j++) 
			{
				tempPheromones = board[i][j].getPheromones();
				if(tempPheromones > 0) 
				{
					board[i][j].setPheromones(tempPheromones/2);
					board[i][j].render();
				}
			}
		}
	}

	//executes turn-specific updates to the simulation
	public void update() 
	{
		
		this.updateBoard();
		this.gui.setTime("Day " + this.day + ", Turn" + ((this.turn%10)+1));
		
		//the queen takes her turn first
		this.queen.update();
		
		//update loop for each ant in the simulation other than the queen
		if(!ants.isEmpty()) 
		{
			ants.forEach((key, temp)->
			{
				
				if(temp instanceof ScoutAnt) 
				{
					temp.update();
				}
				
				else if(temp instanceof SoldierAnt) 
				{
					temp.update();
				}
				
				else if(temp instanceof ForagerAnt) 
				{
					 temp.update();
				}
				
				else if(temp instanceof BalaAnt) 
				{
					temp.update();
				}
				
				//ends the simulation if the queen is killed
				if(!this.queen.isAlive()) {return;}
				
			});
		}
		
		
		turn++;
		
		if(turn % 10 == 0) 
		{
			day++;
		}
				
		//end-of-turn ant removal loop
		for(int i = 0; i < this.antsToKill.size(); i++) 
		{
			if(!this.ants.isEmpty()) 
			{
				this.ants.remove(this.antsToKill.get(i));
				
			}
		}
		
		this.antsToKill.clear();
		
		//bala spawning
		if(antsCanSpawn) 
		{
			Random rng = new Random();
			
			//balas have 3/100 chance to spawn at the top left edge of the colony
			int balaChance = rng.nextInt(100);
			switch(balaChance) 
			{
			case 0:
			case 1:
			case 2:
				this.ants.put(this.nextAntID, new BalaAnt(this.nextAntID, board[0][0], this));
				this.nextAntID++;
				break;
			}
		}
		//board[13][13].render();
	}
	
	//handles button presses in the GUI with the appropriate actions
	public void simulationEventOccurred(SimulationEvent simEvent)
	{
		if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT) 
		{
			//set up simulation for normal operation
			this.normalSetUp();
			
		}
		
		else if(simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT) 
		{
			//set up simulation for queen testing
			this.queenSetUp();
			
		}
		
		else if(simEvent.getEventType() == SimulationEvent.ALTERNATIVE_QUEEN_EVENT) 
		{
			this.queenAltSetUp();
		}
		
		else if(simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT) 
		{
			//set up simulation for scout testing
			this.scoutSetUp();
			
		}
		
		else if(simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT) 
		{
			//set up simulation for forager testing
			this.foragerSetUp();
			
		}
		
		else if(simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT) 
		{
			//set up simulation for soldier testing
			this.soldierSetUp();
			
		}
		
		else if(simEvent.getEventType() == SimulationEvent.RUN_EVENT) 
		{
			//run the simulation continuously
			if(!this.setUp||!this.clean)
			{
				//prevents simulations from overlapping one another
			}
			
			else 
			{
				
				//operational instructions
				while(queen.isAlive()) 
				{
					if(!queen.eatFood()) 
					{
						this.turn--;//compensation
						System.out.println("Simulation over: queen starved at turn: " + this.turn);
						queen.setAlive(false);
						
					}
					
					this.update();
					
					
				}
			}
			this.setUp = false;
			this.clean = false;
		}
	
		
		else if(simEvent.getEventType() == SimulationEvent.STEP_EVENT) 
		{
			
			//run the next turn of the simulation
			if(!this.setUp )
			{
				//prevents simulations from overlapping one another
			}
			
			else 
			{
				if(!queen.eatFood()) 
				{
					this.turn--;//compensation
					System.out.println("Simulation over: queen starved at turn: " + this.turn);
					queen.setAlive(false);
					
				}
				
				//operational instructions
				this.update();
				this.clean = false;
			}
		}
		
		//if a problem occurred
		else 
		{
			System.out.println("Error Occurred");
			
		}
	}
}
