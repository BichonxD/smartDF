package reseauSimple.horloge;

import reseauSimple.consommateur.ConsommateurBehaviourAskPrixProducteur;
import reseauSimple.consommateur.ConsommateurBehaviourMsgListenerNegociation;
import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class HorlogeAgent
{
	/* 
	 * Envoie un message de début de tour à tous les agents.
	 * Une fois que les agents ont finis leur tour elle envoie un message pour signaler que c'est le moment de facturer.
	 * Les Producteurs signalent qu'ils ont bien facturer à l'horloge.
	 * L'horloge annonc le début du tour suivant.
	 * Si un utilisateur n'a pas été facturé il le signale à l'EtatAgent qui sermonne le Producteur en question.
	 */
	
	private static final long serialVersionUID = 1L;
	
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
