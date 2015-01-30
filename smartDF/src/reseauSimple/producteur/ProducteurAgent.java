package reseauSimple.producteur;

import java.util.ArrayList;
import reseauSimple.global.AbstractAgent;
import jade.core.AID;

public class ProducteurAgent extends AbstractAgent
{
	// Constantes
	private static final long serialVersionUID = 1L;
	private static final int PRIX_MAX = 15;
	private static final int PRIX_MIN = 10;
	
	// Variables statiques
	private static int currentID = 0;
	
	// Caractéristiques du producteur
	private final int idProducteur = getNextID();
	
	// Variables propres
	private int prixFournisseur = (int) (Math.random() * (PRIX_MAX - PRIX_MIN) + PRIX_MIN);
	private int argentDispo = 0;
	private int electriciteAFournir = 0;
	private int prixTransporteur = 10000;
	private ArrayList<AID> clientsFournisseur = new ArrayList<AID>();
	private ArrayList<AID> transportsFournisseur = new ArrayList<AID>();
	
	protected void setup()
	{
		setServiceName("producteur");
		super.setup();

		System.out.println("Creation d'un nouveau producteur :\n" + toString());
		
		addBehaviour(new ProducteurBehaviourHorlogeListener(this));
		addBehaviour(new ProducteurBehaviourMsgListenerObservateur(this));
	}
	
	public int getIdProducteur()
	{
		return idProducteur;
	}
	
	public int getPrixFournisseur()
	{
		return prixFournisseur;
	}
	
	public void setPrixFournisseur(int prixFournisseur)
	{
		this.prixFournisseur = prixFournisseur;
	}
	
	public int getArgentFournisseur()
	{
		return argentDispo;
	}
	
	public void setArgentFournisseur(int argentDispo)
	{
		this.argentDispo = argentDispo;
	}
	
	public ArrayList<AID> getClientsFournisseur()
	{
		return clientsFournisseur;
	}
	
	public void addClientsFournisseur(AID fournisseurID)
	{
		if(!clientsFournisseur.contains(fournisseurID))
		{
			clientsFournisseur.add(fournisseurID);
		}
		else
		{
			System.out.println("Le client est déjà présent dans la base!");
		}
	}
	
	public void removeClientsFournisseur(AID fournisseurID)
	{
		if(clientsFournisseur.contains(fournisseurID))
		{
			clientsFournisseur.remove(fournisseurID);
		}
		else
		{
			System.out.println("Le client n'est pas présent dans la base");
		}
	}
	
	public int getElectriciteAFournir()
	{
		return electriciteAFournir;
	}
	
	public void setElectriciteAFournir(int electriciteAFournir)
	{
		this.electriciteAFournir = electriciteAFournir;
	}
	
	public ArrayList<AID> getTransportsFournisseur()
	{
		return transportsFournisseur;
	}
	
	public void setTransportsFournisseur(ArrayList<AID> transportsFournisseur)
	{
		this.transportsFournisseur = transportsFournisseur;
	}
	
	public static int getNextID()
	{
		return currentID++;
	}
	
	@Override
	public String toString()
	{
		String ret = "Producteur " + idProducteur + " nommé : " + getName() + "\n";
		ret += "\tPrix = " + prixFournisseur + " €/kWh\n";
		ret += "\tPossède : " + argentDispo + " €\n";
		ret += "\tDoit fournir : " + electriciteAFournir + " kWh\n";
		return ret;
	}

	public int getPrixTransporteur() {
		return prixTransporteur;
	}

	public void setPrixTransporteur(int prixTransporteur) {
		this.prixTransporteur = prixTransporteur;
	}
	
}
