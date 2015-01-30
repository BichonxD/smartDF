package reseauSimple.horloge;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class HorlogeBehaviourTalker extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	private int msgPerformative;
	
	public HorlogeBehaviourTalker(Agent a, int msgPerformative)
	{
		super(a);
		this.msgPerformative = msgPerformative;
	}
	
	@Override
	public void action()
	{
		AID[] allAgents = ((AbstractAgent) myAgent).getAnnuairePerso();
		
		if(allAgents != null && allAgents.length != 0)
		{
			for(AID aid : allAgents)
			{
				ACLMessage msg = new ACLMessage(msgPerformative);
				msg.addReceiver(aid);
				myAgent.send(msg);
			}
		}
	}
	
}
