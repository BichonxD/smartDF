package reseauSimple;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class GlobalBehaviourAskPrixProducteur extends TickerBehaviour {

	public GlobalBehaviourAskPrixProducteur(Agent a, long period) {
		super(a, period);
		// TODO Stub du constructeur généré automatiquement
	}
	@Override
	protected void onTick() {
		// TODO Stub de la méthode généré automatiquement
		AID[] producteurpossible = ((AbstractAgent) myAgent).getAnnuairePerso();
		
		if(producteurpossible.length != 0) {
		
			for(AID aid : producteurpossible) {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(aid);
				msg.setContent("demande prix");
				myAgent.send(msg); 
			}
		}
		
	}
}
