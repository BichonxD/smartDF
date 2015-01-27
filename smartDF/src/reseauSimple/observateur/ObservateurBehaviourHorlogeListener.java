package reseauSimple.observateur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ObservateurBehaviourHorlogeListener extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private int nbTourEffectue = 0;
	
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
				 * - on MAJ les annuaires tous les 3 coups.
				 */
				case AbstractAgent.HORLOGE_PHASE_NEGOCIATION :
					
					// On MAJ les annuaires tous les 3 coups.
					if(nbTourEffectue % 3 == 0)
					{
						// Paramètres pour la récupération des annuaires nécessaires
						DFAgentDescription dfdRechercheProducteur = new DFAgentDescription();
						ServiceDescription sdProd = new ServiceDescription();
						sdProd.setName("producteur");
						sdProd.setType("producteur");
						dfdRechercheProducteur.addServices(sdProd);
						
						DFAgentDescription dfdRechercheTransporteur = new DFAgentDescription();
						ServiceDescription sdTrans = new ServiceDescription();
						sdTrans.setName("transporteur");
						sdTrans.setType("transporteur");
						dfdRechercheTransporteur.addServices(sdTrans);
						
						DFAgentDescription dfdrechercheTransporteurOfficiel = new DFAgentDescription();
						ServiceDescription SDTransporteurOfficiel = new ServiceDescription();
						SDTransporteurOfficiel.setName("transporteur-officiel");
						SDTransporteurOfficiel.setType("transporteur-officiel");
						dfdrechercheTransporteurOfficiel.addServices(SDTransporteurOfficiel);
						
						// Récupération des annuaires
						myAgent.addBehaviour(new GlobalSearchBehaviour(myAgent, dfdRechercheProducteur, dfdRechercheTransporteur, dfdrechercheTransporteurOfficiel));
					}
					break;
				
				case AbstractAgent.HORLOGE_PHASE_DEPARTAGE :
					nbTourEffectue++;
					break;
					
				default :
					break;
			}
			
			/* 
			 * Dans tous les cas :
			 * - on signale à la GUI qu'on change de phase,
			 * - on demande les prix et l'argent de tous les producteurs,
			 * - on demande les prix et l'argent de tous les transporteurs.
			 */
			((ObservateurAgent) myAgent).getMyGUI().changementDePhase(msg.getPerformative());
			myAgent.addBehaviour(new ObservateurBehaviourAskProducteurs(myAgent));
			myAgent.addBehaviour(new ObservateurBehaviourAskTransporteurs(myAgent));
		}
		else
			block();
	}
	
}
