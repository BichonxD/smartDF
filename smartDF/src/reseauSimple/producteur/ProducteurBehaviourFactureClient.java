package reseauSimple.producteur;

import java.util.List;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducteurBehaviourFactureClient extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private ACLMessage msgHorlogeToAnswer;
	
	public ProducteurBehaviourFactureClient(Agent a, ACLMessage msgHorlogeToAnswer)
	{
		super(a);
		this.msgHorlogeToAnswer = msgHorlogeToAnswer;
	}
	
	@Override
	public void action()
	{
		List<AID> clientsFournisseur = ((ProducteurAgent) myAgent).getClientsFournisseur();
		
		/*
		 * Envoi des demande de facturation, montant calculé par les consomateurs, ils connaissent le prix de leur fournisseur
		 */
		if(clientsFournisseur != null)
		{
			ACLMessage facture = new ACLMessage(AbstractAgent.PRODUCTEUR_FACTURATION_DEMANDE);
			for(AID a : clientsFournisseur)
				facture.addReceiver(a);
			myAgent.send(facture);
		}
		
		// Encaissement
		int nombreReponse = 0;
		
		while (nombreReponse < clientsFournisseur.size())
		{
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.PRODUCTEUR_FACTURATION_REPONSE));
			
			if(msg != null)
			{
				((ProducteurAgent) myAgent).setArgentFournisseur(((ProducteurAgent) myAgent).getArgentFournisseur() + Integer.parseInt(msg.getContent()));
				nombreReponse++;
				
				//System.out.println("PRODUCTEUR_FACTURATION_REPONSE: " + myAgent.getAID() + " recoit " + msg.getContent() + " de " + msg.getSender());
			}
			else
				block();
		}
		
		// On signale la fin du tour de facturation à l'horloge.
		myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msgHorlogeToAnswer));
	}
	
}
