package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ConsommateurBehaviourGestionnaireDepartage extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private ACLMessage msgHorlogeToAnswer;
	
	public ConsommateurBehaviourGestionnaireDepartage(Agent a, ACLMessage msgHorlogeToAnswer)
	{
		super(a);
		this.msgHorlogeToAnswer = msgHorlogeToAnswer;
	}
	
	@Override
	public void action()
	{
		// TODO Auto-generated method stub
		if (((ConsommateurAgent) myAgent).isaEteFacture())
			((ConsommateurAgent) myAgent).setaEteFacture(false);
		else {
			ACLMessage amende = new ACLMessage(AbstractAgent.PRODUCTEUR_AMENDE);
			int prix = ((ConsommateurAgent) myAgent).getBesoin() * ((ConsommateurAgent) myAgent).getPrixfournisseur() * 3;
			amende.addReceiver(((ConsommateurAgent) myAgent).getFournisseurID());
			amende.setContent(Integer.toString(prix));
			myAgent.send(amende);
		}
		
		myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msgHorlogeToAnswer));
	}
	
}
