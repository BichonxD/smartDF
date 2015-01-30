package reseauSimple.transporteur;

import reseauSimple.global.AbstractAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TransporteurBehaviourMsgListenerDemandeInfos extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public TransporteurBehaviourMsgListenerDemandeInfos(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// Reçoit les messages suivant et ecarte les autres
		MessageTemplate mt = 
				MessageTemplate.or(MessageTemplate.or(
				MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_PRIX_DEMANDE),
				MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_ARGENT_DEMANDE)),
				MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_CAPACITE_DEMANDE));
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null)
		{
			ACLMessage reply = null;
			
			switch(msg.getPerformative())
			{
				// On renvoie notre prix
				case AbstractAgent.TRANSPORTEUR_PRIX_DEMANDE:
					reply = msg.createReply();
					reply.setPerformative(AbstractAgent.TRANSPORTEUR_PRIX_REPONSE);
					reply.setContent(Integer.toString(((TransporteurAgent) myAgent).getPrixKWhTransporteur()));
					myAgent.send(reply);
					break;
				
				// On renvoie notre argent
				case AbstractAgent.TRANSPORTEUR_ARGENT_DEMANDE:
					reply = msg.createReply();
					reply.setPerformative(AbstractAgent.TRANSPORTEUR_ARGENT_REPONSE);
					reply.setContent(Integer.toString(((TransporteurAgent) myAgent).getArgentTransporteur()));
					myAgent.send(reply);
					break;
				
				// On renvoie notre capacité
				case AbstractAgent.TRANSPORTEUR_CAPACITE_DEMANDE:
					reply = msg.createReply();
					reply.setPerformative(AbstractAgent.TRANSPORTEUR_CAPACITE_REPONSE);
					reply.setContent(Integer.toString(((TransporteurAgent) myAgent).getCapaciteTransporteur()));
					myAgent.send(reply);
					break;
				
				// Cas d'erreur ne devant pas arriver
				default:
					System.out.println("Transporteur " + myAgent.getAID() + ", demande d'infos : Erreur ne devant pas arriver.");
					break;
			}
		}
		else
			block();
	}
	
}
