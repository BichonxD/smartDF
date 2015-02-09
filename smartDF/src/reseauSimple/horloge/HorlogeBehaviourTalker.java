package reseauSimple.horloge;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class HorlogeBehaviourTalker extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private int msgPerformative;
	private boolean finPhase = true;

	public HorlogeBehaviourTalker(Agent a, long period, int msgPerformative) {
		super(a, period);
		this.msgPerformative = msgPerformative;
	}

	@Override
	protected void onTick() {
		if (finPhase) {
			AID[] allAgents = ((AbstractAgent) myAgent).getAnnuairePerso();

			if (allAgents != null && allAgents.length != 0) {
				for (AID aid : allAgents) {
					ACLMessage msg = new ACLMessage(msgPerformative);
					msg.addReceiver(aid);
					myAgent.send(msg);
				}
			}
			
			finPhase = false;
		}
	}

	public void notifyNouvellePhase(int nouvellePhase) {
		msgPerformative = nouvellePhase;
		finPhase = true;
	}

}
