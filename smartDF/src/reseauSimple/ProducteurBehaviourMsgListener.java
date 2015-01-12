package reseauSimple;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ProducteurBehaviourMsgListener extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void action()
	{
		ACLMessage msg = myAgent.receive();
		
		if(msg != null)
		{
			String contenu = msg.getContent();
			AID sender = msg.getSender();
			
			if(contenu.equals("demande-prix"))
			{
				ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
				reply.addReceiver(sender);
				int prix = ((ProducteurAgent) myAgent).getPrixFournisseur();
				reply.setContent("prix-producteur " + prix);
				myAgent.send(reply);
			}
			else if(contenu.equals("demande-argent"))
			{
				ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
				reply.addReceiver(sender);
				int argent = ((ProducteurAgent) myAgent).getArgentFournisseur();
				reply.setContent("argent-producteur " + argent);
				myAgent.send(reply);
			}
			else if(contenu.equals("abonnement"))
			{
				((ProducteurAgent) myAgent).addClientsFournisseur(sender);
			}
			
			else if(contenu.equals("desabonnement"))
			{
				((ProducteurAgent) myAgent).removeClientsFournisseur(sender);
			}
			else if(contenu.startsWith("paiement "))
			{
				int argentDispo = ((ProducteurAgent) myAgent).getArgentFournisseur();
				int paiement = Integer.parseInt(contenu.split(" ")[1]);
				argentDispo += paiement;
				((ProducteurAgent) myAgent).setArgentFournisseur(argentDispo);
			}
			else
				System.out.println("Message non géré.");
		}
		else
			block();
	}
	
}
