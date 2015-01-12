package reseauSimple;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ConsommateurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;
	
	private boolean consommateurProducteur;
	private int capaciteProducteur;
	private AID fournisseurID;
	private int prixfournisseur;
	private int besoin;
	
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
		
		DFAgentDescription rechercheProducteur = new DFAgentDescription();
		
		ServiceDescription SDProducteur = new ServiceDescription();
		SDProducteur.setName("producteur");
		SDProducteur.setType("producteur");
		rechercheProducteur.addServices(SDProducteur);
		
		addBehaviour(new GlobalSearchBehaviour(this, 6000, rechercheProducteur));
		addBehaviour(new ConsommateurBehaviourAskPrixProducteur(this, 6000));
		addBehaviour(new ConsomateurBehaviourMsgListener());
	}
	
	public boolean isConsommateurProducteur()
	{
		return consommateurProducteur;
	}
	
	public void setConsommateurProducteur(boolean consommateurProducteur)
	{
		this.consommateurProducteur = consommateurProducteur;
	}
	
	public int getCapaciteProducteur()
	{
		return capaciteProducteur;
	}
	
	public void setCapaciteProducteur(int capaciteProducteur)
	{
		this.capaciteProducteur = capaciteProducteur;
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
	
	public int getBesoin()
	{
		return besoin;
	}
	
	public void setBesoin(int besoin)
	{
		this.besoin = besoin;
	}
	
}
