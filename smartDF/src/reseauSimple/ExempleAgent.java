package reseauSimple;

public class ExempleAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	
	protected void setup()
	{
		setServiceName("exempleAgent");
		super.setup();
	}
	
	// Put agent clean-up operations here
	protected void takeDown()
	{
		// Printout a dismissal message
		System.out.println("L'ExempleAgent " + getAID().getName() + " is terminating.");
		super.takeDown();
	}
	
}
