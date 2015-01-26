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
		reply.setPerformative(msgHorlogeToAnswer.getPerformative());
		myAgent.send(reply);
	}
	
}
