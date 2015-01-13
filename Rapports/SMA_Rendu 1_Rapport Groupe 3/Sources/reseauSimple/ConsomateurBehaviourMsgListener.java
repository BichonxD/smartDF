package reseauSimple;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ConsomateurBehaviourMsgListener extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void action()
	{
		ACLMessage msg = myAgent.receive();
		
		if(msg != null)
		{
			String title = msg.getContent();
			
			if(title.startsWith("prix-producteur "))
			{
				AID sender = msg.getSender();
				int prix = Integer.parseInt(title.split(" ")[1]);
				
				if(((ConsommateurAgent) myAgent).getFournisseurID() == null)
				{
					((ConsommateurAgent) myAgent).setFournisseurID(sender);
					((ConsommateurAgent) myAgent).setPrixfournisseur(prix);
					ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
					reply.addReceiver(sender);
					reply.setContent("abonnement");
					myAgent.send(reply);
				}
				else if(prix < ((ConsommateurAgent) myAgent).getPrixfournisseur())
				{
					ACLMessage reply1 = new ACLMessage(ACLMessage.INFORM);
					reply1.addReceiver(((ConsommateurAgent) myAgent).getFournisseurID());
					reply1.setContent("desabonnement");
					myAgent.send(reply1);
					((ConsommateurAgent) myAgent).setFournisseurID(sender);
					((ConsommateurAgent) myAgent).setPrixfournisseur(prix);
					ACLMessage reply2 = new ACLMessage(ACLMessage.INFORM);
					reply2.addReceiver(sender);
					reply2.setContent("abonnement");
					myAgent.send(reply2);
				}
			}
			else if(title.equals("demande-paiement") /*&& msg.getSender() == ((ConsommateurAgent) myAgent).getFournisseurID()*/)
			{
				
				int paiement = ((ConsommateurAgent) myAgent).getBesoin();
				if(((ConsommateurAgent) myAgent).isConsommateurProducteur())
				{
					if(((ConsommateurAgent) myAgent).getCapaciteProducteur() > paiement)
						paiement = 0;
					else
						paiement -= ((ConsommateurAgent) myAgent).getCapaciteProducteur();
				}
				paiement *= ((ConsommateurAgent) myAgent).getPrixfournisseur();
				ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
				reply.addReceiver(msg.getSender());
				reply.setContent("paiement " + paiement);
				myAgent.send(reply);
			}
			else if(title.startsWith("changement-tarif "))
			{
				AID sender = msg.getSender();
				int prix = Integer.getInteger(title.split(" ")[2]);
				
				if(sender == ((ConsommateurAgent) myAgent).getFournisseurID())
				{
					((ConsommateurAgent) myAgent).setPrixfournisseur(prix);
				}
				else if(prix < ((ConsommateurAgent) myAgent).getPrixfournisseur())
				{
					((ConsommateurAgent) myAgent).setFournisseurID(sender);
					((ConsommateurAgent) myAgent).setPrixfournisseur(prix);
				}
			}
			else
			{
				System.out.println("Message non compris.\n" + msg);
				System.out.println(((ConsommateurAgent) myAgent).getFournisseurID());
				System.out.println(msg.getSender());
			}
			
		}
		else
			block();
	}
	
}
