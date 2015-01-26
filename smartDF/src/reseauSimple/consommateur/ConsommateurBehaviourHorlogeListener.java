package reseauSimple.consommateur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ConsommateurBehaviourHorlogeListener extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void action()
	{
		// Reçoit les messages envoyés par l'horloge
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.or(MessageTemplate.MatchConversationId("horloge-phase-negociation"), MessageTemplate.MatchConversationId("horloge-phase-facturaction")), MessageTemplate.MatchConversationId("horloge-phase-departage")); 
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null)
		{
			/*
			 * A chaque fois que nous rentrons dans une nouvelle phase nous supprimons tous les comportements
			 * existants de la phase précédente.
			 */
			switch(msg.getConversationId())
			{
				/* 
				 * Si nous sommes en phase de négociation :
				 * - on met à jour l'annuaire des producteurs,
				 * - on demande le prix à tous les producteurs.
				 */
				case "horloge-phase-negociation" :
					
					// On supprime tous les comportements existants.
					for(Behaviour b : ((AbstractAgent) myAgent).getListCyclicBehaviour())
					{
						myAgent.removeBehaviour(b);
					}
					((AbstractAgent) myAgent).getListCyclicBehaviour().clear();
					
					// On met à jour l'annuaire des producteurs.
					DFAgentDescription rechercheProducteur = new DFAgentDescription();
					ServiceDescription SDProducteur = new ServiceDescription();
					SDProducteur.setName("producteur");
					SDProducteur.setType("producteur");
					rechercheProducteur.addServices(SDProducteur);
					myAgent.addBehaviour(new GlobalSearchBehaviour(myAgent, rechercheProducteur));
					
					// On demande le prix à tous les producteurs.
					myAgent.addBehaviour(new ConsommateurBehaviourAskPrixProducteur(myAgent, msg));
					break;
				
				/*
				 * Si nous sommes en phase de facturation :
				 * - on créé un listener pour cette phase,
				 * - on signale qu'on a terminé notre phase de facturation.
				 */
				case "horloge-phase-facturaction" :
					
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
					myAgent.addBehaviour(new ConsommateurBehaviourHorlogeTalker(myAgent, msg));
					break;
					
				/*
				 * Si nous sommes en phase de departage :
				 * - on créé un comportement qui va gérer la phase.
				 */
				case "horloge-phase-departage" :
					
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
