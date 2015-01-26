package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ConsommateurBehaviourAskPrixProducteur extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private ACLMessage msgHorlogeToAnswer;
	
	public ConsommateurBehaviourAskPrixProducteur(Agent a, ACLMessage msgHorlogeToAnswer)
	{
		super(a);
		this.msgHorlogeToAnswer = msgHorlogeToAnswer;
	}
	
	@Override
	/**
	 * Ce behaviour envoie un message à tous les producteurs en demandant les prix et créé un listener pour ecouter les réponses
	 */
	public void action()
	{
		AID[] producteurpossible = ((AbstractAgent) myAgent).getAnnuairePerso();
		
		if(producteurpossible != null && producteurpossible.length != 0)
		{
			for(AID aid : producteurpossible)
			{
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(aid);
				msg.setContent("demande-prix");
				myAgent.send(msg);
			}
		}
		
		// Créé un listener pour écouter les réponses aux messages
		myAgent.addBehaviour(new ConsommateurBehaviourMsgListenerNegociation(myAgent, msgHorlogeToAnswer));
	}
	
}
