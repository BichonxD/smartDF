package reseauSimple.observateur;

import reseauSimple.global.AbstractAgent;

public class ObservateurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	
	private ObservateurGUI myGUI;
	
	protected void setup()
	{
		setServiceName("observateur");
		super.setup();
		
		System.out.println("Hello World! My name is " + getLocalName() + ".");
		
		// Create and show the GUI
		myGUI = new ObservateurGUI(this);
		myGUI.setVisible(true);
		
		// Ajout d'un listener pour récupérer les réponses aux messages envoyés en continu
		addBehaviour(new ObservateurBehaviourMsgListenerAllPhases());
	}
	
	public ObservateurGUI getMyGUI()
	{
		return myGUI;
	}
	
}
