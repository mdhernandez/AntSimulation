package antsimulation;

import java.util.Random;
public class SoldierAnt extends Ant
{
	
	public SoldierAnt(int ID, Square sq, SimulationController controller) 
	{
		super(ID, sq, 3650, controller);
	}
	
	public Square move() 
	{
		int tempX, tempY, nextX, nextY;
		Square tempSquare = this.getLocation();
		Square nextSquare = this.getLocation();
		
		//marks the starting position of the soldier
		int startX = tempSquare.getPosX();
		int startY = tempSquare.getPosY();
		
		//if a bala is detected this determines where the soldier ant must move to be on the same square
		OrderedPair possibleLocation = senseBala();
		tempX = possibleLocation.getX();
		tempY = possibleLocation.getY();
		nextX = startX + tempX;
		nextY = startY + tempY;
		
		//if no bala ant is detected the soldier moves randomly within the boundaries of the board
		if(tempX ==0 && tempY==0) 
		{
			Random rng = new Random();
			
			boolean inBounds = false;
			
			while(!inBounds) 
			{
				
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
						nextSquare = this.getController().getSquare(nextX, nextY);
						if(nextSquare.isVisible()) 
						{
							inBounds = true;
						}
					}
					
					else {nextX = 0; nextY = 0;}
				
			}
		}
		
		this.getLocation().removeAnt(this.getID());
		tempSquare = this.getController().getSquare(nextX, nextY);
		tempSquare.addAnt(this);
		this.setLocation(tempSquare);
		return tempSquare;//soldier's new location is returned for any further processing
	}
	
	public OrderedPair senseBala() 
	{
		Square temp = this.getLocation();
		
		OrderedPair relativeLocation = new OrderedPair(0, 0);//0,0 indicates no balas in adjacent squares
		int startX = temp.getPosX();
		int startY = temp.getPosY();
		
		/*
		 * adjacent square check block
		*/
		
		//left
		if(startX > 0) {
			//cardinal left
			temp = this.getController().getSquare(startX - 1, startY);
			if(temp.hasBalas() && temp.isVisible()) 
			{
				relativeLocation.set(-1, 0);
				return relativeLocation;
			}
			
			//left-up
			if(startY > 0) 
			{
				temp = this.getController().getSquare(startX - 1, startY - 1);
				if(temp.hasBalas() && temp.isVisible()) 
				{
					relativeLocation.set(-1, -1);
					return relativeLocation;
				}
				
			}
			
			//left-down
			if (startY < 26) 
			{
				temp = this.getController().getSquare(startX - 1, startY + 1);
				if(temp.hasBalas() && temp.isVisible()) 
				{
					relativeLocation.set(-1, 1);
					return relativeLocation;
				}
			}
		}
		//cardinal down
		if(startY < 26) 
		{
			temp = this.getController().getSquare(startX, startY + 1);
			if(temp.hasBalas() && temp.isVisible()) 
			{
				relativeLocation.set(0, 1);
				return relativeLocation;
			}
		}
		
		//cardinal up
		if(startY > 0) 
		{
			temp = this.getController().getSquare(startX , startY - 1);
			if(temp.hasBalas() && temp.isVisible()) 
			{
				relativeLocation.set(0, -1);
				return relativeLocation;
			}
		}
		
		//right
		if(startX < 26) 
		{
			temp = this.getController().getSquare(startX + 1, startY);
			if(temp.hasBalas() && temp.isVisible()) 
			{
				relativeLocation.set(1, 0);
				return relativeLocation;
			}
			
			//right-up
			if(startY > 0) 
			{
				temp = this.getController().getSquare(startX + 1, startY - 1);
				if(temp.hasBalas() && temp.isVisible()) 
				{
					relativeLocation.set(1, -1);
					return relativeLocation;
				}
			}
			
			//right-down
			if(startY < 26) 
			{
				temp = this.getController().getSquare(startX + 1, startY + 1);
				if(temp.hasBalas() && temp.isVisible()) 
				{
					relativeLocation.set(1, 1);
					return relativeLocation;
				}
			}
		}
		
		return relativeLocation;
	}
	
	public Integer attack() 
	{
		Square tempSquare = this.getLocation();
		BalaAnt tempAnt = tempSquare.killBala();
		tempAnt.setAlive(false);//signifies that the ant is dead and will be removed
		tempSquare.removeBala(tempAnt.getID());
		return tempAnt.getID();
	}
	
	
	public boolean update() 
	{
		
		//prevents a dead ant from taking another turn
		if(!this.isAlive()) 
		{
			
			this.getLocation().removeAnt(this.getID());
			this.getController().addToMarkedAnts(this.getID());
			return false;
		}
		
		if(this.getLocation().hasBalas()) 
		{
			this.setMode(1);
		}
		
		
		switch(this.getMode()) 
		{
		case 0:
			this.move();
			break;
		case 1:{
			
			//the soldier has a 1/2 chance of attacking successfully
			
			Random rng = new Random();
			rng.setSeed(System.currentTimeMillis());//consider removing
			int attackChance = rng.nextInt(2);
			
			switch(attackChance) 
			{
			//the attack missed
			case 0:
				
				break;
			//the attack succeeded
			case 1:
				
				int deadID = attack();
				
				this.getController().addToMarkedAnts(deadID);
			}
			break;
		}
		}
			
		this.setLifeSpan(this.getLifeSpan() - 1);
		if(this.getLifeSpan() < 1) 
		{
			this.setAlive(false);
			
		}
		
		if(!this.isAlive()) 
		{
			this.getLocation().removeAnt(this.getID());
			this.getController().addToMarkedAnts(this.getID());
			
			return false;
		}
		
		//mode is set to 0 by default
		this.setMode(0);
		return true;
	}
}
