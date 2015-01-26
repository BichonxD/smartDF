package reseauSimple.observateur;

import reseauSimple.consommateur.ConsommateurBehaviourAskPrixProducteur;
import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ObservateurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	GlobalSearchBehaviour chercheurAnnuaireProducteur = null;
	ConsommateurBehaviourAskPrixProducteur demandeurPrixProducteurs = null;
	ObservateurBehaviourMsgListener recuperateurPrixProducteurs = null;
	ObservateurBehaviourAskArgentProducteur recuperateurArgentProducteurs = null;
	ObservateurBehaviourAskNbClientProducteur recuperateurNbClientProducteurs = null;
	private ObservateurGUI myGUI;
	
	protected void setup()
	{
		setServiceName("observateur");
		super.setup();
		
		System.out.println("Hello World! My name is " + getLocalName() + ".");
		
		// Create and show the GUI
		myGUI = new ObservateurGUI(this);
		myGUI.setVisible(true);
		
		// Ajout d'un behaviour de récupération de l'annuaire toutes les minutes
		DFAgentDescription dfdRecherche = new DFAgentDescription();
		ServiceDescription sdRecherche = new ServiceDescription();
		sdRecherche.setName("producteur");
		sdRecherche.setType("producteur");
		dfdRecherche.addServices(sdRecherche);
		chercheurAnnuaireProducteur = new GlobalSearchBehaviour(this, 6000, dfdRecherche);
		addBehaviour(chercheurAnnuaireProducteur);
		
		// Ajout d'un behaviour de demande des tarifs de tous les producteurs executé toutes les minutes
		demandeurPrixProducteurs = new ConsommateurBehaviourAskPrixProducteur(this, 1000);
		addBehaviour(demandeurPrixProducteurs);
		
		// Ajout d'un behaviour de demande des tarifs de tous les producteurs executé toutes les minutes
		recuperateurArgentProducteurs = new ObservateurBehaviourAskArgentProducteur(this, 1000);
		addBehaviour(recuperateurArgentProducteurs);
		
		// Ajout d'un behaviour de demande des tarifs de tous les producteurs executé toutes les minutes
		recuperateurNbClientProducteurs = new ObservateurBehaviourAskNbClientProducteur(this, 1000);
		addBehaviour(recuperateurNbClientProducteurs);
		
		// Ajout d'un behaviour de récupération des tarifs de tous les producteurs executé toutes les secondes
		recuperateurPrixProducteurs = new ObservateurBehaviourMsgListener();
		addBehaviour(recuperateurPrixProducteurs);
	}
	
	public ObservateurGUI getMyGUI()
	{
		return myGUI;
	}
	
}
