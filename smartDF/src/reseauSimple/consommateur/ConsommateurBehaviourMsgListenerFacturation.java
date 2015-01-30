package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ConsommateurBehaviourMsgListenerFacturation extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ConsommateurBehaviourMsgListenerFacturation(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// Reçoit les messages des producteurs
		MessageTemplate mt = MessageTemplate.or(
				MessageTemplate.MatchPerformative(AbstractAgent.PRODUCTEUR_FACTURATION_DEMANDE),
				MessageTemplate.MatchPerformative(AbstractAgent.CONSOMMATEUR_BESOIN_DEMANDE));
		ACLMessage msg = myAgent.receive(mt);
		
		// Traitement du message
		if(msg != null)
		{
			if(msg.getPerformative() == AbstractAgent.PRODUCTEUR_FACTURATION_DEMANDE)
			{
				int besoin = ((ConsommateurAgent) myAgent).getBesoin();
				
				// calcul du besoin reel si l'agent est aussi producteur
				if(((ConsommateurAgent) myAgent).isConsommateurProducteur())
				{
					// si le consommateur produit trop d'electricité
					if(((ConsommateurAgent) myAgent).getCapaciteProducteur() > besoin)
						besoin = 0;
					else
						besoin -= ((ConsommateurAgent) myAgent).getCapaciteProducteur();
				}
				
				int aPayer = besoin * ((ConsommateurAgent) myAgent).getPrixfournisseur();
				ACLMessage reply = msg.createReply();
				reply.setPerformative(AbstractAgent.PRODUCTEUR_FACTURATION_REPONSE);
				reply.setContent(Integer.toString(aPayer));
				myAgent.send(reply);
				
				((ConsommateurAgent) myAgent).setaEteFacture(true);
			}
			else if(msg.getPerformative() == AbstractAgent.CONSOMMATEUR_BESOIN_DEMANDE)
			{
				ACLMessage reply = msg.createReply();
				reply.setPerformative(AbstractAgent.CONSOMMATEUR_BESOIN_REPONSE);
				
				int besoin = ((ConsommateurAgent) myAgent).getBesoin();
				
				// calcul du besoin reel si l'agent est aussi producteur
				if(((ConsommateurAgent) myAgent).isConsommateurProducteur())
				{
					// si le consommateur produit trop d'electricité
					if(((ConsommateurAgent) myAgent).getCapaciteProducteur() > besoin)
						besoin = 0;
					else
						besoin -= ((ConsommateurAgent) myAgent).getCapaciteProducteur();
				}
				
				reply.setContent(Integer.toString(besoin));
				myAgent.send(reply);
			}
			else
			{
				System.out.println("Message non compris dans le msgListenerFacturation du consommateur.\n" + msg);
				System.out.println(msg.getPerformative());
			}
		}
		else
			block();
		
	}
}
