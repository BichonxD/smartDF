package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

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
