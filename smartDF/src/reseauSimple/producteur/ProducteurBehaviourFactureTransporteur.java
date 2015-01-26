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
		
		int capaciteTransportPersonnel = 0;
		
		if (((ProducteurAgent) myAgent).getTransportsFournisseur() != null && ((ProducteurAgent) myAgent).getTransportsFournisseur().size() != 0) {
			ACLMessage capaciteprevisionnel = new ACLMessage(AbstractAgent.CAPACITE_TRANSPORTEUR_DEMANDE);
			for (AID id : ((ProducteurAgent) myAgent).getTransportsFournisseur())
				capaciteprevisionnel.addReceiver(id);
			myAgent.send(capaciteprevisionnel);
			
			int nombreReponse = 0;
			
			while (nombreReponse < ((ProducteurAgent) myAgent).getTransportsFournisseur().size()) {
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.CAPACITE_TRANSPORTEUR_REPONSE));
				
				if(msg != null)
				{
					capaciteTransportPersonnel += Integer.parseInt(msg.getContent());
					nombreReponse++;
				}
			}
		}
		
		//Utiliser capacite transporteur prive avec le code juste en dessous
		
		/*
		 * ACLMessage utilisation = new ACLMessage(AbstractAgent.DEMANDE_FACURATION_TRANSPORTEUR);
			for (AID id : ((ProducteurAgent) myAgent).getTransportsFournisseur())
				utilisation.addReceiver(id);
			myAgent.send(utilisation);
		 */
		
		
			
		int electriciteTransporteurUniversel =  electriciteATransporter - capaciteTransportPersonnel;
		
		//Recuperer AID transporteur universelle
		AID TransporteurUniverselle;
		
		
		
		
	}

}
