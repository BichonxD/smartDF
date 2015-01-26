package reseauSimple.observateur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ObservateurBehaviourAskArgentProducteur extends TickerBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ObservateurBehaviourAskArgentProducteur(Agent a, long period)
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
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(aid);
				msg.setContent("demande-argent");
				myAgent.send(msg);
			}
		}
	}
	
}
