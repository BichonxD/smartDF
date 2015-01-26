package reseauSimple.producteur;

import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ProducteurBehaviourFactureClient extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ProducteurBehaviourFactureClient(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// TODO
		List<AID> clientsFournisseur = ((ProducteurAgent) myAgent).getClientsFournisseur();
		
		if(clientsFournisseur != null)
		{
			for(AID a : clientsFournisseur)
			{
				ACLMessage facture = new ACLMessage(ACLMessage.INFORM);
				facture.addReceiver(a);
				int prix = ((ProducteurAgent) myAgent).getPrixFournisseur();
				facture.setConversationId(Integer.toString(prix));
				facture.setContent("demande-paiement");
				myAgent.send(facture);
			}
		}
	}
	
}
