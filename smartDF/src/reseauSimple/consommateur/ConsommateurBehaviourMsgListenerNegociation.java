package reseauSimple.consommateur;

import java.util.TreeMap;
import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ConsommateurBehaviourMsgListenerNegociation extends Behaviour
{
	private static final long serialVersionUID = 1L;
	
	private ACLMessage msgHorlogeToAnswer;
	private boolean isDone = false;
	private TreeMap<AID, Integer> repProducteur = new TreeMap<AID, Integer>(); 
	int cpt = 0;
	
	public ConsommateurBehaviourMsgListenerNegociation(Agent a, ACLMessage msgHorlogeToAnswer)
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
			if(cpt < producteurpossible.length){
				if(msg.getPerformative() == AbstractAgent.PRIX_PRODUCTEUR_REPONSE)
				{
					int prix = Integer.parseInt(msg.getContent());
					repProducteur.put(msg.getSender(), prix);
					cpt ++;
				}
				else
				{
					System.out.println("Message non compris.\n" + msg);
					System.out.println(((ConsommateurAgent) myAgent).getFournisseurID());
					System.out.println(msg.getSender());
				}
			}		
			
			else {
				//prendre la décision => dire au mec que c'est ok.
				int prixTemp = repProducteur.get(repProducteur.firstKey());
				AID aidTemp = repProducteur.firstKey();
				
				for (AID id : repProducteur.navigableKeySet()) {
						if (repProducteur.get(id) < prixTemp) {
							prixTemp = repProducteur.get(id);
							aidTemp = id;
						}
				}
				
				((ConsommateurAgent) myAgent).setFournisseurID(aidTemp);
				((ConsommateurAgent) myAgent).setPrixfournisseur(prixTemp);
				
				ACLMessage reply = msg.createReply();
				reply.setPerformative(AbstractAgent.PRIX_PRODUCTEUR_ABONNEMENT);
				myAgent.send(reply);
				
				myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msgHorlogeToAnswer));
				isDone = true;
			}
		}
		else
			block();
	}

	@Override
	public boolean done()
	{
		return isDone;
	}
	
}
