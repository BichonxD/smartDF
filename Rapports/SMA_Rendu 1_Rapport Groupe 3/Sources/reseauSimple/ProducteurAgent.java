package reseauSimple;

import java.util.ArrayList;

import jade.core.AID;

public class ProducteurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<AID> clientsFournisseur = new ArrayList<AID>();
	private int prixFournisseur;
	private int argentDispo = 0;
	
	protected void setup()
	{
		setServiceName("producteur");
		super.setup();
		
		prixFournisseur = (int) getArguments()[0];
		
		System.out.println("Hello World! My name is " + getLocalName());
		
		addBehaviour(new ProducteurBehaviourFactureClient(this, 1000));
		addBehaviour(new ProducteurBehaviourMsgListener());
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
		}else
		{
			System.out.println("Le client est déjà présent dans la base!");
		}
	}
	
	public void removeClientsFournisseur(AID fournisseurID)
	{
		if(clientsFournisseur.contains(fournisseurID))
		{
			clientsFournisseur.remove(fournisseurID);
		}else
		{
			System.out.println("Le client n'est pas présent dans la base");
		}
	}
	
	protected void takeDown()
	{
		System.out.println("Le ProducteurAgent " + getAID().getName() + " is terminating.");
		super.takeDown();
	}
	
}
