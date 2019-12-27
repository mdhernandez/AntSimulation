package antsimulation;

public abstract class Ant 
{
	private int antID;//ID for retrieval and processing of specific ants
	private int lifeSpan;//measured in turns where 1 day equals 10 turns
	private int mode;//determines the actions available to the ant for the current turn
	private Square location;//stores a reference to the square the ant currently occupies
	private boolean lifeStatus;//tracks whether an ant has died
	private int turn;//the amount of turns the ant has been alive
	private SimulationController controllerAccess;//allows ants to directly influence the game as a whole
	
	//getter and setter methods
	public int getID() {return this.antID;}
	public SimulationController getController(){return this.controllerAccess;};
	public void setID(int ID) {this.antID = ID;}
	public int getLifeSpan() {return this.lifeSpan;}
	public void setLifeSpan(int life) {this.lifeSpan = life;}
	public int getMode() {return this.mode;}
	public void setMode(int theMode) {this.mode = theMode;}
	public void incrementTurn() {this.turn++;}
	public int getTurn() {return this.turn;}
	public boolean isAlive() {return this.lifeStatus;}
	public void setAlive(boolean life) {this.lifeStatus = life;}
	public Square getLocation() {return this.location;}
	public void setLocation(Square myLocation) {this.location = myLocation;}
	
	//main processing method to be implemented by each ant subclass
	public abstract boolean update();
	
	//constructor for ant subclasses to invoke
	public Ant(int ID, Square sq, int life, SimulationController controller) 
	{
		this.antID = ID;
		this.location = sq;
		this.lifeSpan = life;
		this.turn = 1;
		this.lifeStatus = true;
		this.mode = 0;
		this.controllerAccess = controller;
	}
}
