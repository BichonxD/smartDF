package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ConsommateurBehaviourAskPrixProducteur extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ConsommateurBehaviourAskPrixProducteur(Agent a)
	{
		super(a);
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
	}
	
}
