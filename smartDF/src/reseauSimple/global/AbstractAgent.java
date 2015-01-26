package reseauSimple.global;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public abstract class AbstractAgent extends Agent
{
	private static final long serialVersionUID = 1L;
	private String serviceName = "defaultService";
	private AID[] annuairePerso;
	private ArrayList<Behaviour> listCyclicBehaviour = new ArrayList<Behaviour>();
	
	static public final int PRIX_PRODUCTEUR_DEMANDE = 100;
	static public final int PRIX_PRODUCTEUR_REPONSE = 101;
	static public final int PRIX_PRODUCTEUR_ABONNEMENT = 102;
	static public final int PRIX_PRODUCTEUR_DESABONNEMENT = 103;
	static public final int PRIX_PRODUCTEUR_CHANGEMENT = 104;
	
	static public final int FACTURATION_PRODUCTEUR_DEMANDE = 110;
	static public final int FACTURATION_PRODUCTEUR_REPONSE = 111;

	static public final int HORLOGE_PHASE_NEGOCIATION = 120;
	static public final int HORLOGE_PHASE_FACTURATION = 121;
	static public final int HORLOGE_PHASE_DEPARTAGE = 122;
	
	static public final int AMENDE_PRODUCTEUR = 130;
	
	protected void setup()
	{
		// Inscription de l'agent dans l'annuaire
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(serviceName);
		sd.setType(serviceName);
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
	
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	
	public void setAnnuairePerso(AID[] annuairePerso)
	{
		this.annuairePerso = annuairePerso;
	}
	
	public AID[] getAnnuairePerso()
	{
		return annuairePerso;
	}
	
	public ArrayList<Behaviour> getListCyclicBehaviour()
	{
		return listCyclicBehaviour;
	}
	
	// Put agent clean-up operations here
	protected void takeDown()
	{
		// Deregister from the yellow pages
		try
		{
			DFService.deregister(this);
		}catch(FIPAException fe)
		{
			fe.printStackTrace();
		}
	}
	
}
