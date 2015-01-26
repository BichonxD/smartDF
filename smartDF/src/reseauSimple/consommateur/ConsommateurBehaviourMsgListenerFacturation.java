package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ConsommateurBehaviourMsgListenerFacturation extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ConsommateurBehaviourMsgListenerFacturation(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// Reçoit les messages des producteurs
		MessageTemplate mt = null;
		AID[] producteurpossible = ((AbstractAgent) myAgent).getAnnuairePerso();
		if(producteurpossible != null && producteurpossible.length != 0)
		{
			for(AID aid : producteurpossible)
			{
				if(mt == null)
					mt = MessageTemplate.MatchSender(aid);
				else
					mt = MessageTemplate.or(mt, MessageTemplate.MatchSender(aid));
			}
		}
		ACLMessage msg = myAgent.receive(mt);
		
		// TODO Traitement du message
		if(msg != null)
		{
			
		}
	}
	
}
