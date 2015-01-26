package reseauSimple.consommateur;

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
		
	}
	
}
