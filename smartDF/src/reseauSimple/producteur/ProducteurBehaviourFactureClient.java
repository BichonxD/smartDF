package reseauSimple.producteur;

import java.util.List;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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
		List<AID> clientsFournisseur = ((ProducteurAgent) myAgent).getClientsFournisseur();
		
		/*
		 * Envoi des demande de facturation, montant calculer par les consomateurs, ils ont le prix de leur fournisseur
		 */
		if(clientsFournisseur != null)
		{
			ACLMessage facture = new ACLMessage(AbstractAgent.PRODUCTEUR_FACTURATION_DEMANDE);
			for(AID a : clientsFournisseur)
				facture.addReceiver(a);
			myAgent.send(facture);
		}
		
		int nombreReponse = 0;
		
		/*
		 * Encaissement
		 */
		while (nombreReponse < clientsFournisseur.size()) {
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.PRODUCTEUR_FACTURATION_REPONSE));
			
			if(msg != null)
			{
				((ProducteurAgent) myAgent).setArgentFournisseur(((ProducteurAgent) myAgent).getArgentFournisseur() + Integer.parseInt(msg.getContent()));
			}
		}
	}
	
}
