package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import reseauSimple.global.GlobalBehaviourSearchAnnuaires;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ConsommateurBehaviourHorlogeListener extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private int nbTourEffectue = 0;
	
	public ConsommateurBehaviourHorlogeListener(Agent a)
	{
		super(a);
	}
	
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
				 * - on met à jour l'annuaire des producteurs tous les ,
				 * - on demande le prix à tous les producteurs,
				 * - on ecoute les messages émis par les producteurs.
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
						myAgent.addBehaviour(new GlobalBehaviourSearchAnnuaires(myAgent, rechercheProducteur));
						
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
					ConsommateurBehaviourMsgListenerFacturation cbmlf = new ConsommateurBehaviourMsgListenerFacturation(myAgent);
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
