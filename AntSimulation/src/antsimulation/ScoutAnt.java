package antsimulation;
import java.util.Random;
public class ScoutAnt extends Ant
{
	public ScoutAnt(int ID, Square sq, SimulationController controller) 
	{
		super(ID, sq, 3650, controller);
	}
	
	public Square move() 
	{
		Square nextSquare;
		
		int xChange = 0;
		int yChange = 0;
		
		int startX = this.getLocation().getPosX();
		int startY = this.getLocation().getPosY();
		
		int tempX = 0;
		int tempY = 0;
		
		boolean inBounds = false;
		
		while(!inBounds) {
			Random rng = new Random();
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
				inBounds = true;
				
			}
			
		}
		
		this.getLocation().removeAnt(this.getID());
		nextSquare = this.getController().getSquare(tempX, tempY);
		nextSquare.addAnt(this);
		this.setLocation(nextSquare);
		nextSquare.setVisibility(true);
		
		return nextSquare;
	}
	
	//turn-by-turn update method
	public boolean update() 
	{
		//if the ant was killed it does not take a turn
		if(!this.isAlive()) 
		{
			this.getLocation().removeAnt(this.getID());
			this.getController().addToMarkedAnts(this.getID());
			return false;
		}
		
		
		Square temp = this.move();
		if(temp.isVisible() && (!temp.getWasRevealed())) 
		{
			temp.reveal();
			temp.setWasRevealed(true);
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
		return true;
	}
}
