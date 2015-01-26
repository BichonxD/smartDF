package reseauSimple.global;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class GlobalBehaviourHorlogeTalker extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private ACLMessage msgHorlogeToAnswer;
	
	public GlobalBehaviourHorlogeTalker(Agent a, ACLMessage msgHorlogeToAnswer)
	{
		super(a);
		this.msgHorlogeToAnswer = msgHorlogeToAnswer;
	}
	
	@Override
	public void action()
	{
		ACLMessage reply = msgHorlogeToAnswer.createReply();
		
		switch(msgHorlogeToAnswer.getContent())
		{
			case "horloge-phase-negociation" :
				reply.setContent("horloge-phase-negociation-termine");
				myAgent.send(reply);
				break;
			
			case "horloge-phase-facturaction" :
				reply.setContent("horloge-phase-facturaction-termine");
				myAgent.send(reply);
				break;
				
			case "horloge-phase-departage" :
				reply.setContent("horloge-phase-departage-termine");
				myAgent.send(reply);
				break;
				
			default :
				System.err.println("Message reçu inconnu.");
				break;
		}
	}
	
}
