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
	private AID[] annuairePersoTransporteurOfficiel;
	private AID[] annuairePersoOptionnel;
	private ArrayList<Behaviour> listCyclicBehaviour = new ArrayList<Behaviour>();
	
	// Informations sur le consommateur
	static public final int CONSOMMATEUR_BESOIN_DEMANDE = 100;
	static public final int CONSOMMATEUR_BESOIN_REPONSE = 101;

	// Informations sur les phases de l'horloge
	static public final int HORLOGE_PHASE_NEGOCIATION = 110;
	static public final int HORLOGE_PHASE_FACTURATION = 111;
	static public final int HORLOGE_PHASE_DEPARTAGE = 112;

	// Informations sur le producteur
	static public final int PRODUCTEUR_PRIX_DEMANDE = 120;
	static public final int PRODUCTEUR_PRIX_REPONSE = 121;
	static public final int PRODUCTEUR_PRIX_CHANGEMENT = 122;
	static public final int PRODUCTEUR_ABONNEMENT = 123;
	static public final int PRODUCTEUR_DESABONNEMENT = 124;
	static public final int PRODUCTEUR_ARGENT_DEMANDE = 125;
	static public final int PRODUCTEUR_ARGENT_REPONSE = 126;
	static public final int PRODUCTEUR_NBCLIENT_DEMANDE = 127;
	static public final int PRODUCTEUR_NBCLIENT_REPONSE = 128;

	// Facturation pour le producteur
	static public final int PRODUCTEUR_FACTURATION_DEMANDE = 130;
	static public final int PRODUCTEUR_FACTURATION_REPONSE = 131;

	// Constantes pour le producteur en phase departage
	static public final int PRODUCTEUR_AMENDE = 140;

	// Informations sur le transporteur
	static public final int TRANSPORTEUR_CAPACITE_DEMANDE = 150;
	static public final int TRANSPORTEUR_CAPACITE_REPONSE = 151;
	static public final int TRANSPORTEUR_PRIX_DEMANDE = 152;
	static public final int TRANSPORTEUR_PRIX_REPONSE = 153;
	static public final int TRANSPORTEUR_ARGENT_DEMANDE = 154;
	static public final int TRANSPORTEUR_ARGENT_REPONSE = 155;
	static public final int TRANSPORTEUR_FACTURATION_DEMANDE = 156;
	static public final int TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE = 157;
	static public final int TRANSPORTEUR_FACTURATION_REPONSE_NEGATIVE = 158;
	static public final int TRANSPORTEUR_ENCAISSE_PAIEMENT = 159;
	static public final int TRANSPORTEUR_ENVOI_ARGENT = 160;
	
	
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
	
	public void setAnnuairePersoTransporteurOfficiel(AID[] annuairePersoTransporteurOfficiel)
	{
		this.annuairePersoTransporteurOfficiel = annuairePersoTransporteurOfficiel;
	}
	
	public AID[] getAnnuairePersoTransporteurOfficiel()
	{
		return annuairePersoTransporteurOfficiel;
	}
	
	public void setAnnuairePersoOptionnel(AID[] annuairePersoOptionnel)
	{
		this.annuairePersoOptionnel = annuairePersoOptionnel;
	}
	
	public AID[] getAnnuairePersoOptionnel()
	{
		return annuairePersoOptionnel;
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
