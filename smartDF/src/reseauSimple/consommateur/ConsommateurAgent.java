package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;

public class ConsommateurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	
	private int besoin;
	private boolean consommateurProducteur;
	private int capaciteProducteur;
	private AID fournisseurID;
	private int prixfournisseur;
	private int dureeRenouvellement = 12;
	
	protected void setup()
	{
		besoin = (int) getArguments()[0];
		consommateurProducteur = (boolean) getArguments()[1];
		capaciteProducteur = (int) getArguments()[2];
		
		if(consommateurProducteur)
			setServiceName("consommateurProducteur");
		else
			setServiceName("consommateur");
		super.setup();
		
		System.out.println("Hello World! My name is " + getLocalName());
		
		fournisseurID = null;
		prixfournisseur = 0;
		
		addBehaviour(new ConsommateurBehaviourHorlogeListener());
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
	
}
