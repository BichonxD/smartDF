package reseauSimple;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ConsommateurBehaviourAskPrixProducteur extends TickerBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ConsommateurBehaviourAskPrixProducteur(Agent a, long period)
	{
		super(a, period);
	}
	
	@Override
	protected void onTick()
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
