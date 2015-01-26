package reseauSimple.transporteur;

import reseauSimple.consommateur.ConsommateurBehaviourAskPrixProducteur;
import reseauSimple.consommateur.ConsommateurBehaviourMsgListenerNegociation;
import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class TransporteurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;

	private AbstractAgent proprietaire;
	private int capaciteTransporteur;
	private int prixKWhTransporteur;
	
	protected void setup()
	{
		if(getArguments().length != 3)
		{
			System.err.println("Nombre d'argument invalides. Il faut 3 arguments : proprietaire, capaciteTransporteur, prixKWhTransporteur.");
		}
		else
		{
			proprietaire = (AbstractAgent) getArguments()[0];
			capaciteTransporteur = (int) getArguments()[1];
			prixKWhTransporteur = (int) getArguments()[2];
			super.setup();
			
			System.out.println("Céation d'un nouveau transporteur :\n" + toString());
			
			DFAgentDescription rechercheProducteur = new DFAgentDescription();
			
			ServiceDescription SDProducteur = new ServiceDescription();
			SDProducteur.setName("producteur");
			SDProducteur.setType("producteur");
			rechercheProducteur.addServices(SDProducteur);
			
			addBehaviour(new GlobalSearchBehaviour(this, 6000, rechercheProducteur));
			addBehaviour(new ConsommateurBehaviourAskPrixProducteur(this, 1000));
			addBehaviour(new ConsommateurBehaviourMsgListenerNegociation());
		}
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
