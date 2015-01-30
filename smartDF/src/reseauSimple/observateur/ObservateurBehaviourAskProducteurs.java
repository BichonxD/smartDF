package reseauSimple.observateur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ObservateurBehaviourAskProducteurs extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ObservateurBehaviourAskProducteurs(Agent a)
	{
		super(a);
	}

	@Override
	public void action()
	{
		// Récupération de l'annuaire des producteurs
		AID[] producteursDisponibles = ((AbstractAgent) myAgent).getAnnuairePerso();
		
		// Envoie de tous les messages
		if(producteursDisponibles != null && producteursDisponibles.length != 0)
		{
			for(AID aid : producteursDisponibles)
			{
				// Prix
				ACLMessage msgPrix = new ACLMessage(AbstractAgent.PRODUCTEUR_PRIX_DEMANDE);
				msgPrix.addReceiver(aid);
				myAgent.send(msgPrix);
				
				// Argent
				ACLMessage msgArgent = new ACLMessage(AbstractAgent.PRODUCTEUR_ARGENT_DEMANDE);
				msgArgent.addReceiver(aid);
				myAgent.send(msgArgent);
				
				// Nombre de clients
				ACLMessage msgNbClient = new ACLMessage(AbstractAgent.PRODUCTEUR_NBCLIENT_DEMANDE);
				msgNbClient.addReceiver(aid);
				myAgent.send(msgNbClient);
				
				// Nombre de transporteurs perso
				ACLMessage msgNbTransporteurPerso = new ACLMessage(AbstractAgent.PRODUCTEUR_NBTRANSPORTEUR_DEMANDE);
				msgNbTransporteurPerso.addReceiver(aid);
				myAgent.send(msgNbTransporteurPerso);
			}
		}
	}
	
}
