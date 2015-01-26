package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducteurBehaviourHorlogeListener extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void action()
	{
		// Reçoit les messages envoyés par l'horloge
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.or(MessageTemplate.MatchPerformative(AbstractAgent.HORLOGE_PHASE_NEGOCIATION), MessageTemplate.MatchPerformative(AbstractAgent.HORLOGE_PHASE_FACTURATION)), MessageTemplate.MatchPerformative(AbstractAgent.HORLOGE_PHASE_DEPARTAGE)); 
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null)
		{
			/*
			 * A chaque fois que nous rentrons dans une nouvelle phase nous supprimons tous les comportements
			 * existants de la phase précédente.
			 */
			switch(msg.getPerformative())
			{
				/* 
				 * Si nous sommes en phase de négociation :
				 * - on met à jour l'annuaire des transporteurs,
				 * - on repense la politique de prix et de transport.
				 */
				case AbstractAgent.HORLOGE_PHASE_NEGOCIATION :
					
					// On supprime tous les comportements existants.
					for(Behaviour b : ((AbstractAgent) myAgent).getListCyclicBehaviour())
					{
						myAgent.removeBehaviour(b);
					}
					((AbstractAgent) myAgent).getListCyclicBehaviour().clear();
					
					// On met à jour l'annuaire des transporteurs.
					DFAgentDescription rechercheTransporteur = new DFAgentDescription();
					ServiceDescription SDTransporteur = new ServiceDescription();
					SDTransporteur.setName("transporteur");
					SDTransporteur.setType("transporteur");
					rechercheTransporteur.addServices(SDTransporteur);
					myAgent.addBehaviour(new GlobalSearchBehaviour(myAgent, rechercheTransporteur));
					
					// On repense la politique de prix et de transport.
					myAgent.addBehaviour(new ProducteurBehaviourStrategiePrixTransport(myAgent, msg));
					break;
				
				/*
				 * Si nous sommes en phase de facturation :
				 * - on demande au transporteur de nous facturer,
				 * - on facture le consommateur,
				 * - on signale la fin du tour de facturation à l'horloge.
				 */
				case AbstractAgent.HORLOGE_PHASE_FACTURATION :
					
					// On supprime tous les comportements existants.
					for(Behaviour b : ((AbstractAgent) myAgent).getListCyclicBehaviour())
					{
						myAgent.removeBehaviour(b);
					}
					((AbstractAgent) myAgent).getListCyclicBehaviour().clear();
					
					// On demande au transporteur de nous facturer.
					myAgent.addBehaviour(new ProducteurBehaviourFactureTransporteur(myAgent));
					
					// On signale qu'on a terminé notre phase de facturation.
					myAgent.addBehaviour(new ProducteurBehaviourFactureClient(myAgent));
					
					// On signale la fin du tour de facturation à l'horloge.
					myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msg));
					break;
					
				/*
				 * Si nous sommes en phase de departage :
				 * - on créé un comportement qui va gérer la phase,
				 * - on signale la fin du tour de departage à l'horloge.
				 */
				case AbstractAgent.HORLOGE_PHASE_DEPARTAGE :
					
					// On supprime tous les comportements existants.
					for(Behaviour b : ((AbstractAgent) myAgent).getListCyclicBehaviour())
					{
						myAgent.removeBehaviour(b);
					}
					((AbstractAgent) myAgent).getListCyclicBehaviour().clear();
					
					// On signale la fin du tour de facturation à l'horloge.
					myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msg));
					
					// On créé un comportement qui va gérer la phase
					ProducteurBehaviourMsgListenerDepartage pbmld = new ProducteurBehaviourMsgListenerDepartage(myAgent);
					myAgent.addBehaviour(pbmld);
					((AbstractAgent) myAgent).getListCyclicBehaviour().add(pbmld);
					break;
					
				default :
					System.err.println("Message reçu inconnu.");
					break;
			}
		}
		else
			block();
	}
	
}
