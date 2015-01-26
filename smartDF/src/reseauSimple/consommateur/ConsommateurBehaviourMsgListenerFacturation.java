package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ConsommateurBehaviourMsgListenerFacturation extends CyclicBehaviour
{

	private static final long serialVersionUID = 1L;
	private ACLMessage msgHorlogeToAnswer;
	private boolean isDone = false;
	
	public ConsommateurBehaviourMsgListenerFacturation(Agent a, ACLMessage msgHorlogeToAnswer)
	{
		super(a);
		this.msgHorlogeToAnswer = msgHorlogeToAnswer;
	}
	
	@Override
	public void action()
	{
		// Reçoit les messages des producteurs
		MessageTemplate mt = null;
		AID[] producteurpossible = ((AbstractAgent) myAgent).getAnnuairePerso();
		if(producteurpossible != null && producteurpossible.length != 0)
		{
			for(AID aid : producteurpossible)
			{
				if(mt == null)
					mt = MessageTemplate.MatchSender(aid);
				else
					mt = MessageTemplate.or(mt, MessageTemplate.MatchSender(aid));
			}
		}
		ACLMessage msg = myAgent.receive(mt);
		
		// TODO Traitement du message
		if(msg != null)
		{
			if(msg.getPerformative() == AbstractAgent.FACTURATION_PRODUCTEUR_DEMANDE)
			{	
				int besoin = ((ConsommateurAgent) myAgent).getBesoin();
				
				//calcul du besoin reel si l'agent est aussi producteur
				if(((ConsommateurAgent) myAgent).isConsommateurProducteur())
				{
					//si le consommateur produit trop d'electricité
					if(((ConsommateurAgent) myAgent).getCapaciteProducteur() > besoin)
						besoin = 0;
					
					else
						besoin -= ((ConsommateurAgent) myAgent).getCapaciteProducteur();
				}
				
				
				int aPayer = besoin * ((ConsommateurAgent) myAgent).getPrixfournisseur();
				ACLMessage reply = msg.createReply();
				reply.setPerformative(AbstractAgent.FACTURATION_PRODUCTEUR_REPONSE);
				myAgent.send(reply);
			}
			
			else
			{
				System.out.println("Message non compris.\n" + msg);
				System.out.println(((ConsommateurAgent) myAgent).getFournisseurID());
				System.out.println(msg.getSender());
			}
			
			//prevenir horloge et terminer behaviour
			myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msgHorlogeToAnswer));
			isDone = true;
		}
	else
		block();
		
	}
}
