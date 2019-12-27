package antsimulation;

public class AntDriver 
{
	//initializes the GUI and creates an event listener
	public static void main(String[] args) 
	{
		AntSimGUI simGui = new AntSimGUI();
		ColonyView antColony = new ColonyView(27, 27);
		simGui.initGUI(antColony);
		simGui.setTime("Day 0, Turn 0");
		SimulationController control = new SimulationController(antColony, simGui);
		simGui.addSimulationEventListener(control);
		
	}
}
