package antsimulation;

import java.util.Random;

public class QueenAnt extends Ant{
	
	public QueenAnt(Square sq, SimulationController controller) 
	{
		super(0, sq, 73000, controller);//queen has a lifespan of 73000 turns or 20 years
		
	}
		
	public boolean eatFood() 
	{
		int foodAmount = this.getLocation().getFood();
		
		if(foodAmount < 0) 
		{
			return false;
		}
		
		Square temp = this.getLocation();
		temp.setFood(foodAmount - 1);
		temp.render();
		return true;
	}
	
	public boolean update() 
	{
		
		//if the queen died of starvation or was killed by a Bala ant
		if(!this.isAlive())
		{
			
			return false;
		}
		
		this.getController().canSpawn();
		
		//if it is the first turn or ten turns have passed a new ant is spawned by the queen
		if(this.getController().canSpawn()) 
		{
			if(this.getTurn() % 10 == 1) 
			{
				//a pseudorandom number between 0 and 3 inclusive is generated
				Random rng = new Random();
				int randomNum = rng.nextInt(3);
				SimulationController control = this.getController();
				Ant temp;
				
				//
				// Each case represents a 1/4 chance of a certain type of ant being spawned
				// There is a 1/2 chance of a forager ant being spawned so either case 0 or case 1 corresponds to that event
				// case 2 represents a scout ant being spawned
				// case 3 represent a soldier ant being spawned
				//
				switch(randomNum) 
				{
				case 0:
				case 1:
					temp = new ForagerAnt(control.getNextID(), control.getSquare(13, 13), control);
					control.addToAntHash(temp);
					control.setNextID(control.getNextID()+1);
					break;
				case 2:
					temp = new ScoutAnt(control.getNextID(), control.getSquare(13, 13), control);
					control.addToAntHash(temp);
					control.setNextID(control.getNextID()+1);
					break;
				case 3:
					temp = new SoldierAnt(control.getNextID(), control.getSquare(13, 13), control);
					control.addToAntHash(temp);
					control.setNextID(control.getNextID()+1);
					break;
				}
			}
		}
		
		//turn and lifespan updates
		this.setLifeSpan(this.getLifeSpan() - 1);
		this.incrementTurn();
		
		return true;
	}
}
