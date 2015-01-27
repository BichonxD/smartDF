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
	private int prixTransporteur = ((TransporteurAgent) myAgent).getPrixKWhTransporteur();
	private TreeMap<AID, Integer> demandeEnAttente = new TreeMap<AID, Integer>();
	
	public void termineTour(){
		capaciteRestante = ((TransporteurAgent) myAgent).getCapaciteTransporteur();
		prixTransporteur = ((TransporteurAgent) myAgent).getPrixKWhTransporteur();
		demandeEnAttente.clear();
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
			if (msg.getPerformative() == AbstractAgent.TRANSPORTEUR_PRIX_DEMANDE) {
				ACLMessage reply = msg.createReply();
				reply.setPerformative(AbstractAgent.TRANSPORTEUR_PRIX_REPONSE);
				reply.setContent(Integer.toString(prixTransporteur));
				myAgent.send(reply);
			}
			else if (msg.getPerformative() == AbstractAgent.TRANSPORTEUR_CAPACITE_DEMANDE){
				ACLMessage reply = msg.createReply();
				reply.setPerformative(AbstractAgent.TRANSPORTEUR_CAPACITE_REPONSE);
				reply.setContent(Integer.toString(capaciteRestante));
				myAgent.send(reply);
			}
			
			//regarder si c'est mon agent
			//si ca l'est pas attendre que mon agent demande la facturation et stocker la requete
			//une fois que j'ai la demande de mon agent je choisi une demande dans mes capacite 
			//ou faire la difference avec la capacite qu'il me reste
			//augmenter ou diminuer le prix
			
			else if (msg.getPerformative() == AbstractAgent.DEMANDE_FACTURATION_TRANSPORTEUR){
				//dans le cas ou c'est mon agent la capacit� diminue, pas de facture
				if(msg.getSender() == ((TransporteurAgent) myAgent).getFournisseurID()){
					capaciteRestante -= ((TransporteurAgent) myAgent).getCapaciteTransporteur();
					//on diminue la capacit� et on facture les autres gus
					for (AID id : demandeEnAttente.navigableKeySet()) {
						if (demandeEnAttente.get(id) < capaciteRestante) {
							//soit on accepte une demande dans les capacit� et on demande a etre pay�
							capaciteRestante -= demandeEnAttente.get(id);
							ACLMessage msgReply = new ACLMessage(AbstractAgent.REPONSE_FACTURATION_TRANSPORTEUR_POSITIVE);
							msgReply.setSender(id);
							msgReply.setContent(Integer.toString(demandeEnAttente.get(id)));
							myAgent.send(msgReply);
						}
						
						else{
							//on refuse
							ACLMessage msgReply = new ACLMessage(AbstractAgent.REPONSE_FACTURATION_TRANSPORTEUR_NEGATIVE);
							msgReply.setSender(id);
							myAgent.send(msgReply);
						}
						
					}
					
					//on verifie la capacit� restante pour savoir la politique restante au tour suivant
				}
				//ajout du message � la pile des demandes en attendant de recevoir la demande de son producteur
				else{
					AID demandeAidTransport = msg.getSender();
					int demandePrixTransport = Integer.parseInt(msg.getContent());
					demandeEnAttente.put(demandeAidTransport, demandePrixTransport);
				}
			}
		}
				block();
	}
}