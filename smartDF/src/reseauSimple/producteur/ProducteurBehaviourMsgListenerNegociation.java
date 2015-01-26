package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducteurBehaviourMsgListenerNegociation extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;

	public ProducteurBehaviourMsgListenerNegociation (Agent a) {
		super(a);
	}

	@Override
	public void action() {
		// Reçoit les messages suivant et ecarte les autres
		MessageTemplate mt = 
				MessageTemplate.or(MessageTemplate.or(
				MessageTemplate.MatchPerformative(AbstractAgent.PRIX_PRODUCTEUR_ABONNEMENT),
				MessageTemplate.MatchPerformative(AbstractAgent.PRIX_PRODUCTEUR_DEMANDE)),
				MessageTemplate.MatchPerformative(AbstractAgent.PRIX_PRODUCTEUR_DESABONNEMENT)); 
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null)
		{
			
			switch(msg.getPerformative())
			{
				
				case AbstractAgent.PRIX_PRODUCTEUR_ABONNEMENT :
					((ProducteurAgent) myAgent).addClientsFournisseur(msg.getSender());
					break;
				
				case AbstractAgent.PRIX_PRODUCTEUR_DESABONNEMENT :
					((ProducteurAgent) myAgent).removeClientsFournisseur(msg.getSender());
					break;
					
				case AbstractAgent.PRIX_PRODUCTEUR_DEMANDE :
					ACLMessage reply = msg.createReply();
					reply.setPerformative(AbstractAgent.PRIX_PRODUCTEUR_REPONSE);
					reply.setContent(Integer.toString(((ProducteurAgent) myAgent).getPrixFournisseur()));
					myAgent.send(reply);
					break;
					
				default:
					System.out.println("Producteur " + myAgent.getAID() + " Negociation : Erreur n'est pas sense arriver");
					break;
			}
		}
	}
	
}
