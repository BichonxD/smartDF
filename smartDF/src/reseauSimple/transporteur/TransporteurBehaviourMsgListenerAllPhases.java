package reseauSimple.transporteur;

import java.util.TreeMap;

import reseauSimple.consommateur.ConsommateurAgent;
import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TransporteurBehaviourMsgListenerAllPhases extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private int capaciteRestante = ((TransporteurAgent) myAgent).getCapaciteTransporteur();

	public TransporteurBehaviourMsgListenerAllPhases(Agent a) {
		super(a);
	}
	
	public void termineTour(){
		capaciteRestante = ((TransporteurAgent) myAgent).getCapaciteTransporteur();
	}

	@Override
	public void action() {
		// Recoit la quantite que veut transporter chaque producteur sur son reseau
		MessageTemplate mt = null;
		AID[] producteurpossible = ((AbstractAgent) myAgent).getAnnuairePerso();
		if (producteurpossible != null && producteurpossible.length != 0) {
			for (AID aid : producteurpossible) {
				if (mt == null)
					mt = MessageTemplate.MatchSender(aid);
				else
					mt = MessageTemplate.or(mt,
							MessageTemplate.MatchSender(aid));
			}
		}
		ACLMessage msg = myAgent.receive(mt);

		// TODO Traitement du message
		if (msg != null) {
			// 2 type de messages, celui pour connaitre le prix, celui pour facturer
			if (msg.getPerformative() == AbstractAgent.PRIX_TRANSPORTEUR_DEMANDE) {
				ACLMessage reply = msg.createReply();
				reply.setPerformative(AbstractAgent.PRIX_TRANSPORTEUR_REPONSE);
				reply.setContent(((TransporteurAgent) myAgent).getPrixKWhTransporteur());
			}
			
			//regarder si c'est mon agent
			//si ca l'est pas attendre que mon agent demande la facturation et stocker la requete
			//une fois que j'ai la demande de mon agent je choisi une demande dans mes capacite 
			//ou faire la difference avec la capacite qu'il me reste
			//augmenter ou diminuer le prix
			else if (msg.getPerformative() == AbstractAgent.DEMANDE_FACTURATION){
				if(msg.getSender() == ((TransporteurAgent) myAgent).getFournisseurID()){
					((TransporteurAgent) myAgent).get())
				}
				int demandeTransport = Integer.parseInt(msg.getContent());
			}
		}
				block();
	}
	
	

}