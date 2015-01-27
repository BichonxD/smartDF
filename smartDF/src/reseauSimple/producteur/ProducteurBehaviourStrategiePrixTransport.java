package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducteurBehaviourStrategiePrixTransport extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private ACLMessage msgHorlogeToAnswer;
	
	public ProducteurBehaviourStrategiePrixTransport(Agent a, ACLMessage msgHorlogeToAnswer)
	{
		super(a);
		this.msgHorlogeToAnswer= msgHorlogeToAnswer;
	}
	
	@Override
	public void action()
	{
		/* TODO
		 * Définit un prix de vente.
		 * Envoie à tous les clients le changement des tarifs s'il y a.
		 */
		int electriciteAFournirPrevisionnel = 0;
		
		if (((ProducteurAgent) myAgent).getClientsFournisseur() != null && ((ProducteurAgent) myAgent).getClientsFournisseur().size() != 0) {
			ACLMessage electriciteprevisionnel = new ACLMessage(AbstractAgent.CONSOMMATEUR_BESOIN_DEMANDE);
			for (AID id : ((ProducteurAgent) myAgent).getClientsFournisseur())
				electriciteprevisionnel.addReceiver(id);
			myAgent.send(electriciteprevisionnel);
			
			int nombreReponse = 0;
			
			while (nombreReponse < ((ProducteurAgent) myAgent).getClientsFournisseur().size()) {
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.CONSOMMATEUR_BESOIN_REPONSE));
				
				if(msg != null)
				{
					electriciteAFournirPrevisionnel += Integer.parseInt(msg.getContent());
					nombreReponse++;
				}
			}
		}		
		
		int capaciteTransportPersonnel = 0;
		
		if (((ProducteurAgent) myAgent).getTransportsFournisseur() != null && ((ProducteurAgent) myAgent).getTransportsFournisseur().size() != 0) {
			ACLMessage capaciteprevisionnel = new ACLMessage(AbstractAgent.TRANSPORTEUR_CAPACITE_DEMANDE);
			for (AID id : ((ProducteurAgent) myAgent).getTransportsFournisseur())
				capaciteprevisionnel.addReceiver(id);
			myAgent.send(capaciteprevisionnel);
			
			int nombreReponse = 0;
			
			while (nombreReponse < ((ProducteurAgent) myAgent).getTransportsFournisseur().size()) {
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_CAPACITE_REPONSE));
				
				if(msg != null)
				{
					capaciteTransportPersonnel += Integer.parseInt(msg.getContent());
					nombreReponse++;
				}
			}
		}
			
		int electriciteTransporteurUniverselPrevisionnel =  electriciteAFournirPrevisionnel - capaciteTransportPersonnel;
		
		int nouveauPrix = ((ProducteurAgent) myAgent).getPrixFournisseur();
		
		//Si on est pas dans le cas initial, a savoir un fournisseur sans client
		if(electriciteAFournirPrevisionnel != 0) {
			if (electriciteTransporteurUniverselPrevisionnel > 0)
				nouveauPrix *= 1.1;
			else if (electriciteTransporteurUniverselPrevisionnel < 0)
				nouveauPrix *= 0.9;
		}
		
		if (((ProducteurAgent) myAgent).getPrixFournisseur() != nouveauPrix) {
			ACLMessage changementPrix = new ACLMessage(AbstractAgent.PRODUCTEUR_PRIX_CHANGEMENT);
			for (AID id : ((ProducteurAgent) myAgent).getClientsFournisseur())
				changementPrix.addReceiver(id);
			changementPrix.setContent(Integer.toString(nouveauPrix));
			myAgent.send(changementPrix);
		}
		
		
		/*
		 * Signale la fin des négociations à l'horloge.
		 */
		myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msgHorlogeToAnswer));
		
		/*
		 * Lance le MsgListenerNegociation du Producteur et l'ajoute à la liste des cyclicBehaviour qui tournent
		 */
		ProducteurBehaviourMsgListenerNegociation pbmln = new ProducteurBehaviourMsgListenerNegociation(myAgent);
		myAgent.addBehaviour(pbmln);
		((AbstractAgent) myAgent).getListCyclicBehaviour().add(pbmln);
	}

}
