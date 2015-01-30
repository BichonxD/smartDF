package reseauSimple.transporteur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;

public class TransporteurAgent extends AbstractAgent
{
	// Constantes
	private static final long serialVersionUID = 1L;
	private static final int CAPACITE_MAX = 50;
	private static final int CAPACITE_MIN = 15;
	private static final int PRIX_MAX = 10;
	private static final int PRIX_MIN = 5;

	// Variables statiques
	private static int currentID = 0;

	// Caractéristiques du transporteur
	private final int idTransporteur = getNextID();
	private final int prixKWhTransporteur = (int) (Math.random() * (PRIX_MAX - PRIX_MIN) + PRIX_MIN);
	
	// Variables propres
	private AID proprietaire = null;
	private int capaciteTransporteur;
	private int argentTransporteur = 0;
	private boolean transporteurOfficiel = false;
	private TransporteurBehaviourMsgListenerAllPhases tansporteurBehaviourMsg;
	
	protected void setup()
	{	
		
		if(getArguments().length == 1)
		{
			try
			{
				proprietaire = (AID) getArguments()[0];
			}
			catch(ClassCastException e)
			{
				transporteurOfficiel = true;
			}
		}
		else
			transporteurOfficiel = true;
		
		if(transporteurOfficiel)
		{
			setServiceName("transporteur-officiel");
			capaciteTransporteur = Integer.MAX_VALUE;
		}
		else
		{
			setServiceName("transporteur");
			capaciteTransporteur =  (int) (Math.random() * (CAPACITE_MAX - CAPACITE_MIN) + CAPACITE_MIN);
		}
		super.setup();
		
		System.out.println("Creation d'un nouveau transporteur :\n" + toString());
		
		addBehaviour(new TransporteurBehaviourMsgListenerDemandeInfos(this));
		
		addBehaviour(new TransporteurBehaviourHorlogeListener(this));
		tansporteurBehaviourMsg = new TransporteurBehaviourMsgListenerAllPhases(this);
		addBehaviour(tansporteurBehaviourMsg);
	}
	
	public AID getFournisseurID()
	{
		return proprietaire == null ? null : proprietaire;
	}
	
	public int getCapaciteTransporteur()
	{
		return capaciteTransporteur;
	}
	
	public int getPrixKWhTransporteur()
	{
		return prixKWhTransporteur;
	}
	
	public int getArgentTransporteur()
	{
		return argentTransporteur;
	}
	
	public void setArgentTransporteur(int argentTransporteur)
	{
		this.argentTransporteur = argentTransporteur;
	}
	
	public static int getNextID()
	{
		return currentID++;
	}
	
	public boolean isTransporteurOfficiel()
	{
		return transporteurOfficiel;
	}
	
	public TransporteurBehaviourMsgListenerAllPhases getTansporteurBehaviourMsg()
	{
		return tansporteurBehaviourMsg;
	}
	
	@Override
	public String toString()
	{
		String ret = "Transporteur " + idTransporteur + " nommé : " + getName() + "\n";
		if(transporteurOfficiel)
		{
			ret += "\tC'est un Transporteur Officiel.\n";
			ret += "\t\tCapacité de transport illimitée.\n";
		}
		else
		{
			ret += "\tSon propriétaire est le Producteur " + proprietaire + "\n";
			ret += "\t\tCapacité de transport = " + capaciteTransporteur + " kWh\n";
		}
		ret += "\tPrix de vente du kWh = " + prixKWhTransporteur + " €/kWh\n";
		ret += "\tArgent = " + argentTransporteur + " €\n";
		return ret;
	}
	
}
