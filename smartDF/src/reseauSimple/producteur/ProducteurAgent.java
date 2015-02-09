package reseauSimple.producteur;

import java.util.ArrayList;

import reseauSimple.global.AbstractAgent;
import reseauSimple.transporteur.TransporteurAgent;
import jade.core.AID;

public class ProducteurAgent extends AbstractAgent
{
	// Constantes
	private static final long serialVersionUID = 1L;
	private static final int PRIX_FABRICATION_MAX = 10;
	private static final int PRIX_FABRICATION_MIN = 5;
	
	// Variables statiques
	private static int currentID = 0;
	
	// Caractéristiques du producteur
	private final int idProducteur = getNextID();
	
	// Variables propres
	private int prixFournisseur = (int) (Math.random() * (PRIX_FABRICATION_MAX - PRIX_FABRICATION_MIN) + PRIX_FABRICATION_MIN + TransporteurAgent.getPrixMax() + 5);
	private int argentDispo = 0;
	private int electriciteAFournir = 0;
	private int prixTransporteur = 10000;
	private int prixFabrication = (PRIX_FABRICATION_MIN + PRIX_FABRICATION_MAX) / 2;
	private ArrayList<AID> clientsFournisseur = new ArrayList<AID>();
	private ArrayList<AID> transportsFournisseur = new ArrayList<AID>();
	
	protected void setup()
	{
		setServiceName("producteur");
		super.setup();

		System.out.println("Creation d'un nouveau producteur :\n" + toString());
		
		addBehaviour(new ProducteurBehaviourHorlogeListener(this));
		addBehaviour(new ProducteurBehaviourMsgListenerDemandeInfos(this));
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
	
	public static int getPrixFabricationMax() {
		return PRIX_FABRICATION_MAX;
	}

	public static int getPrixFabricationMin() {
		return PRIX_FABRICATION_MIN;
	}

	public int getPrixFabrication() {
		return prixFabrication;
	}

	public void setPrixFabrication(int prixFabrication) {
		this.prixFabrication = prixFabrication;
	}
	
}
