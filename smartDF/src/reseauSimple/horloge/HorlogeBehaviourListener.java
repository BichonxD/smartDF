package reseauSimple.horloge;

import reseauSimple.global.AbstractAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class HorlogeBehaviourListener extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private int phaseActuelle = AbstractAgent.HORLOGE_PHASE_NEGOCIATION;
	private int nbReponseRecu = 0;
	
	public HorlogeBehaviourListener(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// Reçoit les messages envoyés à l'horloge
		ACLMessage msg = myAgent.receive();
		
		if(msg != null)
		{
			if(msg.getPerformative() == phaseActuelle)
			{
				if(nbReponseRecu < ((AbstractAgent) myAgent).getAnnuairePerso().length)
				{
					nbReponseRecu++;
				}
				// Si j'ai reçu la réponse de tous les agents
				else
				{
					switch(phaseActuelle)
					{
						case AbstractAgent.HORLOGE_PHASE_NEGOCIATION :
							phaseActuelle = AbstractAgent.HORLOGE_PHASE_FACTURATION;
							break;
						
						case AbstractAgent.HORLOGE_PHASE_FACTURATION :
							phaseActuelle = AbstractAgent.HORLOGE_PHASE_DEPARTAGE;
							break;
						
						case AbstractAgent.HORLOGE_PHASE_DEPARTAGE :
							phaseActuelle = AbstractAgent.HORLOGE_PHASE_NEGOCIATION;
							break;
							
						default :
							System.err.println("Phase actuelle inconnue.");
							break;
					}
					
					myAgent.addBehaviour(new HorlogeBehaviourTalker(myAgent, phaseActuelle));
					nbReponseRecu = 0;
				}
			}
			else
			{
				System.err.println("Un agent pense être dans la mauvaise phase.");
			}
		}
		else
			block();
	}
	
}
