package reseauSimple.observateur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ObservateurBehaviourMsgListenerAllPhases extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	private int nbReponses = 0;
	private ACLMessage msgHorlogeToAnswer;
	
	public ObservateurBehaviourMsgListenerAllPhases(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		// Récupération de tous les annuaires
		AID[] producteursDisponibles = ((AbstractAgent) myAgent).getAnnuairePerso();
		AID[] transporteursDisponibles = ((AbstractAgent) myAgent).getAnnuairePersoOptionnel();
		AID[] transporteurOfficiel = ((AbstractAgent) myAgent).getAnnuairePersoTransporteurOfficiel();
		int nbReponsesAttendues = 0;
		ACLMessage msg = null;
		
		// Reçoit les messages de n'importe qui dans notre annuaire
		MessageTemplate mt = null;
		if(producteursDisponibles != null)
		{
			nbReponsesAttendues += producteursDisponibles.length * 3;
			
			for(AID aid : producteursDisponibles)
			{
				if(mt == null)
					mt = MessageTemplate.MatchSender(aid);
				else
					mt = MessageTemplate.or(mt, MessageTemplate.MatchSender(aid));
			}
		}
		
		if(transporteursDisponibles != null)
		{
			nbReponsesAttendues += transporteursDisponibles.length * 3;
			
			for(AID aid : transporteursDisponibles)
			{
				if(mt == null)
					mt = MessageTemplate.MatchSender(aid);
				else
					mt = MessageTemplate.or(mt, MessageTemplate.MatchSender(aid));
			}
		}
		
		if(transporteurOfficiel != null)
		{
			nbReponsesAttendues += transporteurOfficiel.length * 3;
			
			for(AID aid : transporteurOfficiel)
			{
				if(mt == null)
					mt = MessageTemplate.MatchSender(aid);
				else
					mt = MessageTemplate.or(mt, MessageTemplate.MatchSender(aid));
			}
		}
		
		if(mt != null)
			msg = myAgent.receive(mt);
		else
			block();
		
		if(msg != null)
		{
			AID sender = msg.getSender();
			
			switch(msg.getPerformative())
			{
				case AbstractAgent.PRODUCTEUR_PRIX_REPONSE:
					int prixProd = Integer.parseInt(msg.getContent());
					((ObservateurAgent) myAgent).getMyGUI().ajouterPrixProducteur(sender, "Le producteur " + sender.getName() + " a pour prix : " + prixProd + ".");
					nbReponses++;
					break;
				
				case AbstractAgent.PRODUCTEUR_ARGENT_REPONSE:
					int argentProd = Integer.parseInt(msg.getContent());
					((ObservateurAgent) myAgent).getMyGUI().ajouterArgentProducteur(sender, "Le producteur " + sender.getName() + " a fait " + argentProd + "€ de bénéfices.");
					nbReponses++;
					break;
				
				case AbstractAgent.PRODUCTEUR_NBCLIENT_REPONSE:
					int nbClientProd = Integer.parseInt(msg.getContent());
					((ObservateurAgent) myAgent).getMyGUI().ajouterNbClientsProducteur(sender, "Le producteur " + sender.getName() + " a " + nbClientProd + " clients.");
					nbReponses++;
					break;
				
				case AbstractAgent.TRANSPORTEUR_PRIX_REPONSE:
					int prixTrans = Integer.parseInt(msg.getContent());
					((ObservateurAgent) myAgent).getMyGUI().ajouterPrixTransporteur(sender, "Le transporteur " + sender.getName() + " a pour prix : " + prixTrans + " €/kWh");
					nbReponses++;
					break;
				
				case AbstractAgent.TRANSPORTEUR_ARGENT_REPONSE:
					int argentTrans = Integer.parseInt(msg.getContent());
					((ObservateurAgent) myAgent).getMyGUI().ajouterArgentTransporteur(sender, "Le transporteur " + sender.getName() + " a fait " + argentTrans + "€ de bénéfices.");
					nbReponses++;
					break;
				
				case AbstractAgent.TRANSPORTEUR_CAPACITE_REPONSE:
					if(msg.getContent().equals("infini"))
						((ObservateurAgent) myAgent).getMyGUI().ajouterCapaciteTransporteur(sender, "Le transporteur " + sender.getName() + " a une capacite de transport infinie.");
					else
					{
						int capaciteTrans = Integer.parseInt(msg.getContent());
						((ObservateurAgent) myAgent).getMyGUI().ajouterPrixProducteur(sender, "Le transporteur " + sender.getName() + " a pour capacite : " + capaciteTrans + " kWh.");
					}
					nbReponses++;
					break;
				
				default:
					System.out.println("Message non compris dans l'observateur.\n" + msg + "\n" + msg.getPerformative());
					System.out.println(mt);
					break;
			}
			
			//System.out.println("Nombre de réponses reçues " + nbReponses + " sur " + nbReponsesAttendues);
		}
		else
			block();
		
		// Si on a reçu des réponses de tous les agents on signale qu'on a finit cette phase à l'horloge
		if(nbReponsesAttendues != 0 && nbReponses == nbReponsesAttendues)
		{
			myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msgHorlogeToAnswer));
			nbReponses = 0;
		}
		
	}
	
	public void setMsgHorlogeToAnswer(ACLMessage msgHorlogeToAnswer)
	{
		this.msgHorlogeToAnswer = msgHorlogeToAnswer;
	}
	
}
