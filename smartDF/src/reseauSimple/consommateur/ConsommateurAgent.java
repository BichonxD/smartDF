package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;

public class ConsommateurAgent extends AbstractAgent
{
	// Constantes
	private static final long serialVersionUID = 1L;
	private static final int BESOIN_MAX = 20;
	private static final int BESOIN_MIN = 5;
	private static final int PROD_MIN = 1;
	private static final int RENOUVELLEMENT_MAX = 15;
	private static final int RENOUVELLEMENT_MIN = 5;
	
	// Variables statiques
	private static int currentID = 0;
	
	// Caractéristiques du consommateur
	private final int idConsommateur = getNextID();
	private final int besoin = (int) (Math.random() * (BESOIN_MAX - BESOIN_MIN) + BESOIN_MIN);
	private final boolean consommateurProducteur = (((int) (Math.random() * 100))%2 == 0);
	private final int capaciteProducteur = consommateurProducteur ? (int) (Math.random() * (besoin - 4 - PROD_MIN) + PROD_MIN) : 0;
	private final int dureeRenouvellement = (int) (Math.random() * (RENOUVELLEMENT_MAX - RENOUVELLEMENT_MIN) + RENOUVELLEMENT_MIN);
	
	// Variables propres
	private AID fournisseurID;
	private int prixfournisseur;
	private boolean aEteFacture = false;
	
	protected void setup()
	{
		if(consommateurProducteur)
			setServiceName("consommateurProducteur");
		else
			setServiceName("consommateur");
		super.setup();
		
		System.out.println("Creation d'un nouveau consommateur :\n" + toString());
		
		fournisseurID = null;
		prixfournisseur = 0;
		
		addBehaviour(new ConsommateurBehaviourHorlogeListener(this));
	}
	
	public int getBesoin()
	{
		return besoin;
	}
	
	public boolean isConsommateurProducteur()
	{
		return consommateurProducteur;
	}
	
	public int getCapaciteProducteur()
	{
		return capaciteProducteur;
	}
	
	public AID getFournisseurID()
	{
		return fournisseurID;
	}
	
	public void setFournisseurID(AID fournisseurID)
	{
		this.fournisseurID = fournisseurID;
	}
	
	public int getPrixfournisseur()
	{
		return prixfournisseur;
	}
	
	public void setPrixfournisseur(int prixfournisseur)
	{
		this.prixfournisseur = prixfournisseur;
	}
	
	public int getDureeRenouvellement()
	{
		return dureeRenouvellement;
	}

	public boolean isaEteFacture() {
		return aEteFacture;
	}

	public void setaEteFacture(boolean aEteFacture) {
		this.aEteFacture = aEteFacture;
	}
	
	public static int getNextID()
	{
		return currentID++;
	}
	
	@Override
	public String toString()
	{
		String ret = "Consommateur " + idConsommateur + " nommé : " + getName() + "\n";
		ret += "\tBesoins = " + besoin + " kWh\n";
		if(consommateurProducteur)
		{
			ret += "\tC'est un Consommateur-Producteur : \n";
			ret += "\t\tProduction = " + capaciteProducteur + " kWh\n";
		}
		ret += "\tCherche un meilleur producteur tous les " + dureeRenouvellement + " tours.\n";
		return ret;
	}
	
}
