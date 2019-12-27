package antsimulation;

public class OrderedPair 
{
	private int xValue;
	private int yValue;
	
	public void setX(int x) {this.xValue = x;}
	public int getX() {return this.xValue;}
	public void setY(int y) {this.yValue = y;}
	public int getY() {return this.yValue;}
	
	public OrderedPair(int x, int y) 
	{
		this.xValue = x;
		this.yValue = y;
	}
	
	public void set(int x, int y) 
	{
		this.xValue = x;
		this.yValue = y;
	}
	
	public boolean equals(OrderedPair temp) 
	{
		if((temp.getX() == this.getX()) && (this.getY() == temp.getY())) 
		{
			return true;
		}
		return false;
	}
	
}
