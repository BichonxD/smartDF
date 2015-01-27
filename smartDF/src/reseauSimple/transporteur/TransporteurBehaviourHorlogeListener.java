package reseauSimple.transporteur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TransporteurBehaviourHorlogeListener extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void action()
	{
		// Reçoit les messages envoyés par l'horloge
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.or(MessageTemplate.MatchPerformative(AbstractAgent.HORLOGE_PHASE_NEGOCIATION), MessageTemplate.MatchPerformative(AbstractAgent.HORLOGE_PHASE_FACTURATION)), MessageTemplate.MatchPerformative(AbstractAgent.HORLOGE_PHASE_DEPARTAGE)); 
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null)
		{
			switch(msg.getPerformative())
			{
				case AbstractAgent.HORLOGE_PHASE_NEGOCIATION :
					((TransporteurAgent) myAgent).getTansporteurBehaviourMsg().termineTour();
					break;
					
				default :
					break;
			}
			
			myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msg));
		}
		else
			block();
	}
	
}
