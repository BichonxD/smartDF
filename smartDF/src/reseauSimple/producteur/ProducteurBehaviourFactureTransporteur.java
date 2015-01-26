package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducteurBehaviourFactureTransporteur extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ProducteurBehaviourFactureTransporteur(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		int electriciteATransporter = 0;
		
		if (((ProducteurAgent) myAgent).getClientsFournisseur() != null && ((ProducteurAgent) myAgent).getClientsFournisseur().size() != 0) {
			ACLMessage electriciteprevisionnel = new ACLMessage(AbstractAgent.BESOIN_CONSOMMATEUR_DEMANDE);
			for (AID id : ((ProducteurAgent) myAgent).getClientsFournisseur())
				electriciteprevisionnel.addReceiver(id);
			myAgent.send(electriciteprevisionnel);
			
			int nombreReponse = 0;
			
			while (nombreReponse < ((ProducteurAgent) myAgent).getClientsFournisseur().size()) {
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.BESOIN_CONSOMMATEUR_REPONSE));
				
				if(msg != null)
				{
					electriciteATransporter += Integer.parseInt(msg.getContent());
					nombreReponse++;
				}
			}
		}
		
		
	}

}
