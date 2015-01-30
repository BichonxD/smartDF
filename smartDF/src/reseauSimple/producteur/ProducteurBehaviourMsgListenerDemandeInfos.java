package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducteurBehaviourMsgListenerDemandeInfos extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ProducteurBehaviourMsgListenerDemandeInfos(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// Reçoit les messages suivant et ecarte les autres
		MessageTemplate mt = 
				MessageTemplate.or(MessageTemplate.or(
				MessageTemplate.MatchPerformative(AbstractAgent.PRODUCTEUR_PRIX_DEMANDE),
				MessageTemplate.MatchPerformative(AbstractAgent.PRODUCTEUR_ARGENT_DEMANDE)),
				MessageTemplate.MatchPerformative(AbstractAgent.PRODUCTEUR_NBCLIENT_DEMANDE));
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null)
		{
			ACLMessage reply = null;
			
			switch(msg.getPerformative())
			{
				// On renvoie notre prix
				case AbstractAgent.PRODUCTEUR_PRIX_DEMANDE:
					reply = msg.createReply();
					reply.setPerformative(AbstractAgent.PRODUCTEUR_PRIX_REPONSE);
					reply.setContent(Integer.toString(((ProducteurAgent) myAgent).getPrixFournisseur()));
					myAgent.send(reply);
					break;
				
				// On renvoie notre argent
				case AbstractAgent.PRODUCTEUR_ARGENT_DEMANDE:
					reply = msg.createReply();
					reply.setPerformative(AbstractAgent.PRODUCTEUR_ARGENT_REPONSE);
					reply.setContent(Integer.toString(((ProducteurAgent) myAgent).getArgentFournisseur()));
					myAgent.send(reply);
					break;
				
				// On renvoie notre capacité
				case AbstractAgent.PRODUCTEUR_NBCLIENT_DEMANDE:
					reply = msg.createReply();
					reply.setPerformative(AbstractAgent.PRODUCTEUR_NBCLIENT_REPONSE);
					reply.setContent(Integer.toString(((ProducteurAgent) myAgent).getClientsFournisseur().size()));
					myAgent.send(reply);
					break;
				
				// Cas d'erreur ne devant pas arriver
				default:
					System.out.println("Producteur " + myAgent.getAID() + ", demande d'infos : Erreur ne devant pas arriver.");
					break;
			}
		}
		else
			block();
	}
	
}
