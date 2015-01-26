package reseauSimple.observateur;

import reseauSimple.consommateur.ConsommateurBehaviourAskPrixProducteur;
import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ObservateurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	
	private ObservateurBehaviourGetAnnuaireProducteurEtTransporteur chercheurAnnuaire = null;
	ConsommateurBehaviourAskPrixProducteur demandeurPrixProducteurs = null;
	ObservateurBehaviourMsgListener recuperateurPrixProducteurs = null;
	ObservateurBehaviourAskArgentProducteur recuperateurArgentProducteurs = null;
	ObservateurBehaviourAskNbClientProducteur recuperateurNbClientProducteurs = null;
	private ObservateurGUI myGUI;
	private AID[] AnnuairePersoProducteur;
	private AID[] AnnuairePersoTransporteur;
	
	protected void setup()
	{
		setServiceName("observateur");
		super.setup();
		
		System.out.println("Hello World! My name is " + getLocalName() + ".");
		
		// Create and show the GUI
		myGUI = new ObservateurGUI(this);
		myGUI.setVisible(true);
		
		// Récupération des annuaires nécessaires
		chercheurAnnuaire = new ObservateurBehaviourGetAnnuaireProducteurEtTransporteur();
		addBehaviour(chercheurAnnuaire);
		
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
	
	public AID[] getAnnuairePersoProducteur()
	{
		return AnnuairePersoProducteur;
	}
	
	public AID[] getAnnuairePersoTransporteur()
	{
		return AnnuairePersoTransporteur;
	}
	
}
