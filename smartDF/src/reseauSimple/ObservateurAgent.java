
package reseauSimple;

import javax.swing.JFrame;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ObservateurAgent extends Agent
{
	private static final long serialVersionUID = 1L;
	JFrame frame = null;
	ObservateurBehaviourRecuInfoProd recupInfosProd = null;
	
	protected void setup()
	{
		frame = new JFrame();
		
		recupInfosProd = new ObservateurBehaviourRecuInfoProd();
		addBehaviour(recupInfosProd);
		
		// Inscription de l'agent dans l'annuaire
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("observateur");
		sd.setType("observateur");
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		} catch (FIPAException e)
		{
			e.printStackTrace();
			System.err.println("Erreur d'inscription de l'observateur " + getAID() + " dans l'annuaire.");
		}
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
}
