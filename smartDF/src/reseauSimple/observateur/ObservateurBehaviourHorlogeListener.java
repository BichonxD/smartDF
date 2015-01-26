package reseauSimple.observateur;

import reseauSimple.consommateur.ConsommateurAgent;
import reseauSimple.consommateur.ConsommateurBehaviourAskPrixProducteur;
import reseauSimple.consommateur.ConsommateurBehaviourGestionnaireDepartage;
import reseauSimple.consommateur.ConsommateurBehaviourMsgListenerFacturation;
import reseauSimple.consommateur.ConsommateurBehaviourMsgListenerNegociation;
import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ObservateurBehaviourHorlogeListener extends CyclicBehaviour
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
			switch(msg.getPerformative())
			{
				/* 
				 * Si nous sommes en phase de négociation :
				 * - on met à jour les annuaires,
				 * - on met à jour les prix producteurs et transporteurs.
				 */
				case AbstractAgent.HORLOGE_PHASE_NEGOCIATION :
					
					// On supprime tous les comportements existants.
					for(Behaviour b : ((AbstractAgent) myAgent).getListCyclicBehaviour())
					{
						myAgent.removeBehaviour(b);
					}
					((AbstractAgent) myAgent).getListCyclicBehaviour().clear();
					
					if(nbTourEffectue % ((ConsommateurAgent) myAgent).getDureeRenouvellement() == 0)
					{
						// On met à jour l'annuaire des producteurs.
						DFAgentDescription rechercheProducteur = new DFAgentDescription();
						ServiceDescription SDProducteur = new ServiceDescription();
						SDProducteur.setName("producteur");
						SDProducteur.setType("producteur");
						rechercheProducteur.addServices(SDProducteur);
						myAgent.addBehaviour(new GlobalSearchBehaviour(myAgent, rechercheProducteur));
						
						// On demande le prix à tous les producteurs.
						myAgent.addBehaviour(new ConsommateurBehaviourAskPrixProducteur(myAgent));
					}
					// On ecoute les messages émis par les producteurs.
					myAgent.addBehaviour(new ConsommateurBehaviourMsgListenerNegociation(myAgent, msg));
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
