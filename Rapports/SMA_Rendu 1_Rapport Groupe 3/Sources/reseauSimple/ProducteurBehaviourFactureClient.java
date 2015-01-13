package reseauSimple;

import java.util.List;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ProducteurBehaviourFactureClient extends TickerBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ProducteurBehaviourFactureClient(ProducteurAgent a, int period)
	{
		super(a, period);
	}
	
	@Override
	protected void onTick()
	{
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
