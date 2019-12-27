package antsimulation;

import java.util.Random;

public class BalaAnt extends Ant
{
	
	public BalaAnt(int ID, Square sq, SimulationController controller) 
	{
		super(ID, sq, 3650, controller);
		sq.addBala(this);
	}
	
	public Square move() 
	{
		int tempX, tempY, nextX, nextY;
		Square tempSquare = this.getLocation();
		int startX = tempSquare.getPosX();
		int startY = tempSquare.getPosY();
		
		nextX = 0;
		nextY = 0;
		tempX = 0;
		tempY = 0;
		
		boolean inBounds = false;
		while(!inBounds) 
		{
			
				//next move is chosen randomly
				Random rng = new Random();
				int direction = rng.nextInt(8);
				
				switch(direction) 
				{
				//left-up
				case 0:
					tempX = -1;
					tempY = -1;
					break;
				
				//left
				case 1:
					tempX = -1;
					tempY = 0;
					break;
			
				//left-down
				case 2:
					tempX = -1;
					tempY = 1;
					break;
			
				//down	
				case 3:
					tempX = 0;
					tempY = 1;
					break;
				
				//right-down	
				case 4:
					tempX = 1;
					tempY = 1;
					break;
				
				//right	
				case 5:
					tempX = 1;
					tempY = 0;
					break;
				
				//right-up	
				case 6:
					tempX = 1;
					tempY = -1;
					break;
				
				//up	
				case 7:
					tempX = 0;
					tempY = -1;
					break;
				}
				
				nextX = startX + tempX;
				nextY = startY + tempY;
				
				if(nextX >= 0 && nextX < 27 && nextY >=0 && nextY < 27) 
				{
					inBounds = true;
				}
				
				
		}
		this.getLocation().removeBala(this.getID());
		tempSquare = this.getController().getSquare(nextX, nextY);
		tempSquare.addBala(this);
		this.setLocation(tempSquare);
		return tempSquare;
	}
	
	//returns true if attack is successful, false otherwise
	public Integer attack() 
	{
		Square tempSquare = this.getLocation();
		
		if(tempSquare.hasQueen()) 
		{
			int chance = tempSquare.antNumber() + 1;
			Random rng = new Random();
			int result = rng.nextInt(chance);
			if(result == 0) 
			{
				this.getController().queen.setAlive(false);
				System.out.println("Simulation over: queen killed by Bala");
				return 0;
			}
		}
		Ant tempAnt = tempSquare.getAnt();
		tempAnt.setAlive(false);
		tempSquare.removeAnt(tempAnt.getID());
		return tempAnt.getID();
	}
	
	public boolean update() 
	{
		
		//prevents a dead ant from taking another turn
		if(!this.isAlive())
		{
			this.getController().addToMarkedAnts(this.getID());
			this.getLocation().removeBala(this.getID());
			return false;
		}
		
		
		Square tempSquare = this.getLocation();
		if(tempSquare.getHasAnts() || tempSquare.hasQueen()) 
		{
			this.setMode(1);
		}
		
		switch(this.getMode()) 
		{
		case 0:
			
			this.move();
			break;
		case 1:{
			//the bala has a 1/2 chance of attacking successfully
			Random rng = new Random();
			int attackChance = rng.nextInt(2);
			
			switch(attackChance) 
			{
			//the attack missed
			case 0:
				break;
			case 1:
				int deadAntID = attack();
				this.getController().addToMarkedAnts(deadAntID);
			}
		}
		}
		
		//checks if the ant died of old age and marks it for removal if so
		this.setLifeSpan(this.getLifeSpan() - 1);
		if(this.getLifeSpan() < 1) 
		{
			this.setAlive(false);
		}
		
		if(!this.isAlive()) 
		{
			this.getLocation().removeBala(this.getID());
			this.getController().addToMarkedAnts(this.getID());
			return false;
		}
		
		//mode is set to 0 by default
		this.setMode(0);
		return true;
	}
}
