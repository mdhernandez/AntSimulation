package antsimulation;

import java.util.Stack;
import java.util.Random;

public class ForagerAnt extends Ant
{
	
	private boolean carryingFood;
	private Stack<OrderedPair> stepsFromNest;
	private OrderedPair secondPrevMove;
	

	public ForagerAnt(int ID, Square location, SimulationController controller) 
	{
		super(ID, location, 3650, controller);
		this.carryingFood = false;
		this.stepsFromNest = new Stack<OrderedPair>();
		this.secondPrevMove = new OrderedPair(0, 0);
	}
	
	//detects and returns two adjacent spaces with the highest and second highest pheromone amounts
	//and returns the two directions coded as an ordered pair with the following encoding scheme...
	//0-left 1-leftup 2-leftdown 3- down 4- up 5- right, 6- rightup, 7-rightdown
	//-1 is returned as one of the numbers if there is no valid and applicable adjacent square
	public OrderedPair sensePheromone() 
	{
		
		Square currentSquare = this.getLocation();
		Square temp;
		int currentPheromones = currentSquare.getPheromones();
		OrderedPair choices = new OrderedPair(-1, 0);
		int startX = currentSquare.getPosX();
		int startY = currentSquare.getPosY();
		
		if(startX > 0) {
			//cardinal left
			temp = this.getController().getSquare(startX - 1, startY);
			if(temp.isVisible() && (temp.getPheromones() > currentPheromones)) 
			{
				choices.setY(choices.getX());
				choices.setX(0);
			}
			
			//left-up
			if(startY > 0) 
			{
				temp = this.getController().getSquare(startX - 1, startY - 1);
				if(temp.isVisible() && (temp.getPheromones() > currentPheromones))
				{
					choices.setY(choices.getX());
					choices.setX(1);
				}
				
			}
			
			//left-down
			if (startY < 26) 
			{
				temp = this.getController().getSquare(startX - 1, startY + 1);
				if(temp.isVisible() && (temp.getPheromones() > currentPheromones)) 
				{
					choices.setY(choices.getX());
					choices.setX(2);
				}
			}
		}
		
		//cardinal down
		if(startY < 26) 
		{
			temp = this.getController().getSquare(startX, startY + 1);
			if(temp.isVisible() && (temp.getPheromones() > currentPheromones)) 
			{
				choices.setY(choices.getX());
				choices.setX(3);
			}
		}
		
		//cardinal up
		if(startY > 0) 
		{
			temp = this.getController().getSquare(startX , startY - 1);
			if(temp.isVisible() && (temp.getPheromones() > currentPheromones)) 
			{
				choices.setY(choices.getX());
				choices.setX(4);
			}
		}
		
		//right
		if(startX < 26) 
		{
			temp = this.getController().getSquare(startX + 1, startY);
			if(temp.isVisible() && (temp.getPheromones() > currentPheromones))
			{
				choices.setY(choices.getX());
				choices.setX(5);
			}
			
			//right-up
			if(startY > 0) 
			{
				temp = this.getController().getSquare(startX + 1, startY - 1);
				if(temp.isVisible() && (temp.getPheromones() > currentPheromones)) 
				{
					choices.setY(choices.getX());
					choices.setX(6);
					
				}
			}
			
			//right-down
			if(startY < 26) 
			{
				temp = this.getController().getSquare(startX + 1, startY + 1);
				if(temp.isVisible() && (temp.getPheromones() > currentPheromones)) 
				{
					choices.setY(choices.getX());
					choices.setX(7);
					
				}
			}
		}
		
		return choices;	
	}
	
	
	
	public int randomMove() 
	{
		Random rng = new Random();
		 return rng.nextInt(8);
	}
	
	public OrderedPair movementChoice(int direction) 
	{
		//0-left 1-leftup 2-leftdown 3- down 4- up 5- right, 6- rightup, 7-rightdown
		int tempX = 0;
		int tempY = 0;;
		
		switch(direction) 
		{
		case 0:
			tempX = -1;
			tempY = 0;
			break;
		case 1:
			tempX = -1;
			tempY = -1;
			break;
		case 2:
			tempX = -1;
			tempY = 1;
			break;
		case 3:
			tempX = 0;
			tempY = 1;
			break;
		case 4:
			tempX = 0;
			tempY = -1;
			break;
		case 5:
			tempX = 1;
			tempY = 0;
			break;
		case 6:
			tempX = 1;
			tempY = -1;
			break;
		case 7:
			tempX = 1;
			tempY = 1;
			break;
		}
		OrderedPair ret = new OrderedPair(tempX, tempY);
		return ret;
		
	}
	
	//used to reverse a previous step for return-to-nest mode
	public OrderedPair negate(OrderedPair original) 
	{
		int tempX = original.getX() * -1;
		int tempY = original.getY() * -1;
		return new OrderedPair(tempX, tempY);
	}
	
	//has the ant move to a random adjacent square that is both visible and in the bounds of the board
	public Square makeRandomMove() 
	{
		Square nextSquare;
		
		int xChange = 0;
		int yChange = 0;
		
		int startX = this.getLocation().getPosX();
		int startY = this.getLocation().getPosY();
		
		int tempX = 0;
		int tempY = 0;
		
		boolean inBounds = false;
		Random rng = new Random();
		Random breaker = new Random();
		while(!inBounds) {
			
			int direction = rng.nextInt(8);
		
			//decides which direction the ant will try to move in
			switch(direction) 
			{
			//left-up
			case 0:
				xChange = -1;
				yChange = -1;
				break;
			
			//left
			case 1:
				xChange = -1;
				yChange = 0;
				break;
		
			//left-down
			case 2:
				xChange = -1;
				yChange = 1;
				break;
		
			//down	
			case 3:
				xChange = 0;
				yChange = 1;
				break;
			
			//right-down	
			case 4:
				xChange = 1;
				yChange = 1;
				break;
			
			//right	
			case 5:
				xChange = 1;
				yChange = 0;
				break;
			
			//right-up	
			case 6:
				xChange = 1;
				yChange = -1;
				break;
			
			//up	
			case 7:
				xChange = 0;
				yChange = -1;
				break;
			}
			
			tempX = startX + xChange;
			tempY = startY + yChange;
			
			//the loop is ended if the move is within the bounds of the board
			if(tempX >= 0 && tempX < 27 && tempY >= 0 && tempY < 27) 
			{
				
				nextSquare = this.getController().getSquare(tempX, tempY);
				if(nextSquare.isVisible()) 
				{
					inBounds = true;
				}
				else 
				{
					
					tempX = 0;
					tempY = 0;
					
					
					
					int tie = breaker.nextInt(2);
					switch(tie) 
					{
					case 0:
						direction += 1;
						if(direction >= 8) 
						{
							direction-=8;
						}
						break;
					case 1:
						direction -= 1;
						if(direction < 0) 
						{
							direction+=8;
						}
						
					}
					
				}
			}
			else 
			{
				int tie = breaker.nextInt(2);
				switch(tie) 
				{
				case 0:
					direction += 1;
					if(direction >= 8) 
					{
						direction-=8;
					}
					break;
				case 1:
					direction -= 1;
					if(direction < 0) 
					{
						direction+=8;
					}
					
				}
			}
			
		}
		
		this.getLocation().removeAnt(this.getID());
		nextSquare = this.getController().getSquare(tempX, tempY);
		nextSquare.addAnt(this);
		this.setLocation(nextSquare);
		
		if(this.stepsFromNest.isEmpty()) 
		{
			this.secondPrevMove = new OrderedPair(0, 0);
		}
		else 
		{
			this.secondPrevMove = this.stepsFromNest.peek();
		}
		
		OrderedPair temp = new OrderedPair(xChange, yChange);
		
		this.stepsFromNest.push(temp);
		return nextSquare;
	}
	
	//main movement method for executing both movement modes
	public void move() 
	{
		switch(this.getMode()) 
		{
		//forage mode
		case 0:
		{
			boolean randomMoveMade = false;
			Square thisSquare = this.getLocation();
			Square nextSquare;
			int startX = thisSquare.getPosX();
			int startY = thisSquare.getPosY();
			int nextX = 0;
			int nextY = 0;
			int direction = 0;
			OrderedPair nextMove = new OrderedPair(0, 0);
			OrderedPair possibleLocations = new OrderedPair(0,0);//test
			
			//0-left 1-leftup 2-leftdown 3- down 4- up 5- right, 6- rightup, 7-rightdown -1 - no movement
			
			//if all directions have the same amount of pheromones as the current square
			if(possibleLocations.getX() == 0) 
			{
				
				nextSquare = this.makeRandomMove();
				randomMoveMade = true;
				
			}
			
			
			else if((!this.stepsFromNest.isEmpty()) && this.movementChoice(possibleLocations.getX()).equals(this.negate(this.stepsFromNest.peek()))) 
			{
				if(this.movementChoice(possibleLocations.getY()).equals(this.negate(this.secondPrevMove))) 
				{
					
					nextSquare = this.makeRandomMove();//should prevent infinite loops
					randomMoveMade = true;
				}
				
				else 
				{
					direction = possibleLocations.getY();
				}
			}
			
			else 
			{
				direction = possibleLocations.getX();
			}
			if(!randomMoveMade) 
			{
				nextMove = this.movementChoice(direction);
				
				if(this.stepsFromNest.isEmpty()) 
				{
					this.secondPrevMove = new OrderedPair(0, 0);
				}
				else 
				{
					this.secondPrevMove = this.stepsFromNest.peek();
				}
				
				this.stepsFromNest.push(nextMove);
				nextX = startX + nextMove.getX();
				nextY = startY + nextMove.getY();
				nextSquare = this.getController().getSquare(nextX, nextY);
				this.setLocation(nextSquare);
				thisSquare.removeAnt(this.getID());
				nextSquare.addAnt(this);
			}
			
			
			if(this.getLocation().getFood() > 0 && !this.getLocation().hasQueen()) 
			{
				this.carryingFood = true;
				this.setMode(1);
			}
			break;
		}
				
		//return-to-nest mode
		case 1:
		{
			if(!this.stepsFromNest.isEmpty()) {
				OrderedPair tempPair;
				Square tempSquare = this.getLocation();
				tempPair = this.stepsFromNest.pop();
				int nextX = tempSquare.getPosX() +(tempPair.getX() * - 1);
				int nextY = tempSquare.getPosY() + (tempPair.getY() * - 1);
				
				tempSquare.removeAnt(this.getID());
				tempSquare = this.getController().getSquare(nextX, nextY);
				this.setLocation(tempSquare);
				tempSquare.addAnt(this);
				
				//if the square the forager moves to is not the queens square, it deposits 10 units of pheromones
				if(!tempSquare.hasQueen()) 
				{
					tempSquare.setPheromones(tempSquare.getPheromones() + 10);
				}
				
				//if the square is the queens square it deposits the food
				else  
				{
					tempSquare.setFood(tempSquare.getFood() + 1);
					this.carryingFood = false;
					this.setMode(0);
				}
			}
		}
			
		}
	}
	
	public boolean hasFood() 
	{
		return this.carryingFood;
	}
	
	//turn-by-turn update method
	public boolean update() 
	{
		if(!this.isAlive()) 
		{
			this.getLocation().removeAnt(this.getID());
			this.getController().addToMarkedAnts(this.getID());
			return false;
		}
		
		this.move();
		
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
		return true;
	}
}
