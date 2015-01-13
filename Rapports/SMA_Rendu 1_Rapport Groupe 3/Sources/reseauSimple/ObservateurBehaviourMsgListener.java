package reseauSimple;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ObservateurBehaviourMsgListener extends CyclicBehaviour
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
				((ObservateurAgent) myAgent).getMyGUI().ajouterPrixProducteur(sender, "Le producteur " + sender.getName() + " a pour prix : " + prix + ".");
			}
			else if(title.startsWith("argent-producteur "))
			{
				AID sender = msg.getSender();
				int argent = Integer.parseInt(title.split(" ")[1]);
				((ObservateurAgent) myAgent).getMyGUI().ajouterArgentProducteur(sender, "Le producteur " + sender.getName() + " a fait " + argent + "€ de bénéfices.");
			}
			else if(title.startsWith("nb-clients "))
			{
				AID sender = msg.getSender();
				int nbClient = Integer.parseInt(title.split(" ")[1]);
				((ObservateurAgent) myAgent).getMyGUI().ajouterNbClientsProducteur(sender, "Le producteur " + sender.getName() + " a " + nbClient + " clients.");
			}
			else
				System.out.println("Message non compris.");
		}
		else
			block();
	}
	
}
