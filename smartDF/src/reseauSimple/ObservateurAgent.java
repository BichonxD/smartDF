package reseauSimple;

import javax.swing.JFrame;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ObservateurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	JFrame frame = null;
	GlobalSearchBehaviour recupInfosProd = null;
	
	protected void setup()
	{
		setServiceName("observateur");
		super.setup();
		
		frame = new JFrame();
		
		
		// On cherche tous les producteurs
		DFAgentDescription dfdRecherche = new DFAgentDescription();
		ServiceDescription sdRecherche = new ServiceDescription();
		sdRecherche.setName("producteur");
		sdRecherche.setType("producteur");
		dfdRecherche.addServices(sdRecherche);
		recupInfosProd = new GlobalSearchBehaviour(dfdRecherche);
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
		}catch(FIPAException e)
		{
			e.printStackTrace();
			System.err.println("Erreur d'inscription de l'observateur " + getAID() + " dans l'annuaire.");
		}
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	// Put agent clean-up operations here
	protected void takeDown()
	{
		// Printout a dismissal message
		System.out.println("Buyer-agent" + getAID().getName() + "terminating.");
	}
}
