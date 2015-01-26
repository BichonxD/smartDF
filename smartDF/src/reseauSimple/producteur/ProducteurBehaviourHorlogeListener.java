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
					myAgent.addBehaviour(new ProducteurBehaviourStrategiePrixTransport(myAgent));
					break;
				
				/*
				 * Si nous sommes en phase de facturation :
				 * - on créé un listener pour cette phase,
				 * - on signale qu'on a terminé notre phase de facturation.
				 */
				case AbstractAgent.HORLOGE_PHASE_FACTURATION :
					
					// On supprime tous les comportements existants.
					for(Behaviour b : ((AbstractAgent) myAgent).getListCyclicBehaviour())
					{
						myAgent.removeBehaviour(b);
					}
					((AbstractAgent) myAgent).getListCyclicBehaviour().clear();
					
					// On créé un listener pour cette phase.
					ConsommateurBehaviourMsgListenerFacturation cbmlf = new ConsommateurBehaviourMsgListenerFacturation(myAgent, msg);
					myAgent.addBehaviour(cbmlf);
					((AbstractAgent) myAgent).getListCyclicBehaviour().add(cbmlf);
					
					// On signale qu'on a terminé notre phase de facturation.
					myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msg));
					break;
					
				/*
				 * Si nous sommes en phase de departage :
				 * - on créé un comportement qui va gérer la phase.
				 */
				case AbstractAgent.HORLOGE_PHASE_DEPARTAGE :
					
					// On supprime tous les comportements existants.
					for(Behaviour b : ((AbstractAgent) myAgent).getListCyclicBehaviour())
					{
						myAgent.removeBehaviour(b);
					}
					((AbstractAgent) myAgent).getListCyclicBehaviour().clear();
					
					// On créé un comportement qui va gérer la phase
					myAgent.addBehaviour(new ConsommateurBehaviourGestionnaireDepartage(myAgent, msg));
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
