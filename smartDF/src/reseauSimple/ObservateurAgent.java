package reseauSimple;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ObservateurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	GlobalSearchBehaviour chercheurAnnuaireProducteur = null;
	GlobalBehaviourAskPrixProducteur recuperateurPrixProducteurs = null;
	private ObservatorGUI myGUI;
	
	protected void setup()
	{
		setServiceName("observateur");
		super.setup();
		
		// Create and show the GUI
		myGUI = new ObservatorGUI(this);
		myGUI.setVisible(true);
		
		// Ajout d'un behaviour de récupération de l'annuaire toutes les minutes
		DFAgentDescription dfdRecherche = new DFAgentDescription();
		ServiceDescription sdRecherche = new ServiceDescription();
		sdRecherche.setName("producteur");
		sdRecherche.setType("producteur");
		dfdRecherche.addServices(sdRecherche);
		chercheurAnnuaireProducteur = new GlobalSearchBehaviour(this, 60000, dfdRecherche);
		addBehaviour(chercheurAnnuaireProducteur);
		
		
	}
	
	// Put agent clean-up operations here
	protected void takeDown()
	{
		// Printout a dismissal message
		System.out.println("Buyer-agent" + getAID().getName() + "terminating.");
		
		// Close the GUI
		myGUI.dispose();
	}
}
