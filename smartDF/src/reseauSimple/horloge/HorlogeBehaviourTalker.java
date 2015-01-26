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
		this.msgPerformative = msgPerformative;
	}
	
	@Override
	public void action()
	{
		/* TODO
		 * Envoie un message de début de tour à tous les agents.
		 * Une fois que les agents ont finis leur tour elle envoie un message pour signaler que c'est le moment de facturer.
		 * Les Producteurs signalent qu'ils ont bien facturer à l'horloge.
		 * L'horloge annonce le début du tour suivant.
		 * Si un utilisateur n'a pas été facturé il le signale à l'EtatAgent qui sermonne le Producteur en question.
		 */
		

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
