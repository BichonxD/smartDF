package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducteurBehaviourMsgListenerNegociation extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ProducteurBehaviourMsgListenerNegociation(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// Reçoit les messages suivant et ecarte les autres
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(AbstractAgent.PRODUCTEUR_ABONNEMENT), MessageTemplate.MatchPerformative(AbstractAgent.PRODUCTEUR_DESABONNEMENT));
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null)
		{
			switch(msg.getPerformative())
			{
				/*
				 * On ajoute le consomateur a la liste de nos client
				 */
				case AbstractAgent.PRODUCTEUR_ABONNEMENT:
					((ProducteurAgent) myAgent).addClientsFournisseur(msg.getSender());
					break;
				
				/*
				 * On enleve le consomateur de nos client
				 */
				case AbstractAgent.PRODUCTEUR_DESABONNEMENT:
					((ProducteurAgent) myAgent).removeClientsFournisseur(msg.getSender());
					break;
				
				/*
				 * Cas d'erreur ne doit pas arriver
				 */
				default:
					System.out.println("Producteur " + myAgent.getAID() + " Negociation : Erreur n'est pas censé arriver");
					break;
			}
		}
		else
			block();
	}
	
}
