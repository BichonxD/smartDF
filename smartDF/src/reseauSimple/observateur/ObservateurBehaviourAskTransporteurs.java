package reseauSimple.observateur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ObservateurBehaviourAskTransporteurs extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ObservateurBehaviourAskTransporteurs(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// Récupération de l'annuaire des producteurs
		AID[] transporteursDisponibles = ((AbstractAgent) myAgent).getAnnuairePersoOptionnel();
		AID[] transporteurOfficiel = ((AbstractAgent) myAgent).getAnnuairePersoTransporteurOfficiel();
		
		// Envoie de tous les messages aux transporteurs
		if(transporteursDisponibles != null && transporteursDisponibles.length != 0)
		{
			for(AID aid : transporteursDisponibles)
			{
				// Prix
				ACLMessage msgPrix = new ACLMessage(AbstractAgent.TRANSPORTEUR_PRIX_DEMANDE);
				msgPrix.addReceiver(aid);
				myAgent.send(msgPrix);
				
				// Argent
				ACLMessage msgArgent = new ACLMessage(AbstractAgent.TRANSPORTEUR_ARGENT_DEMANDE);
				msgArgent.addReceiver(aid);
				myAgent.send(msgArgent);
				
				// Nombre de clients
				ACLMessage msgCapacite = new ACLMessage(AbstractAgent.TRANSPORTEUR_CAPACITE_DEMANDE);
				msgCapacite.addReceiver(aid);
				myAgent.send(msgCapacite);
				
				// Proprietaire
				ACLMessage msgProprio = new ACLMessage(AbstractAgent.TRANSPORTEUR_PROPRIO_DEMANDE);
				msgProprio.addReceiver(aid);
				myAgent.send(msgProprio);
			}
		}
		
		// Envoie de tous les messages au transporteur officiel
		if(transporteurOfficiel != null && transporteurOfficiel.length != 0)
		{
			for(AID aid : transporteurOfficiel)
			{
				((ObservateurAgent) myAgent).getMyGUI().ajouterProprioTransporteur(aid, "Le transporteur " + aid.getLocalName() + " est le transporteur officiel.");
				
				// Prix
				ACLMessage msgPrix = new ACLMessage(AbstractAgent.TRANSPORTEUR_PRIX_DEMANDE);
				msgPrix.addReceiver(aid);
				myAgent.send(msgPrix);
				
				// Argent
				ACLMessage msgArgent = new ACLMessage(AbstractAgent.TRANSPORTEUR_ARGENT_DEMANDE);
				msgArgent.addReceiver(aid);
				myAgent.send(msgArgent);
				
				// Nombre de clients
				ACLMessage msgCapacite = new ACLMessage(AbstractAgent.TRANSPORTEUR_CAPACITE_DEMANDE);
				msgCapacite.addReceiver(aid);
				myAgent.send(msgCapacite);
			}
		}
	}
	
}
