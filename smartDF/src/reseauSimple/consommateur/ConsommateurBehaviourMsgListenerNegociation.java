package reseauSimple.consommateur;

import java.util.TreeMap;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ConsommateurBehaviourMsgListenerNegociation extends Behaviour {
	private static final long serialVersionUID = 1L;

	private ACLMessage msgHorlogeToAnswer;
	private boolean isDone = false;
	private TreeMap<AID, Integer> repProducteur = new TreeMap<AID, Integer>();
	private int cpt = 0;

	public ConsommateurBehaviourMsgListenerNegociation(Agent a,
			ACLMessage msgHorlogeToAnswer) {
		super(a);
		this.msgHorlogeToAnswer = msgHorlogeToAnswer;
	}

	@Override
	public void action() {
		// Reçoit les messages des producteurs
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
			if (msg.getPerformative() == AbstractAgent.PRODUCTEUR_PRIX_REPONSE) {
				if (cpt < producteurpossible.length) {
					{
						int prix = Integer.parseInt(msg.getContent());
						repProducteur.put(msg.getSender(), prix);
						cpt++;
					}
					if (cpt == producteurpossible.length) {

					}
					// prendre la décision => dire au mec que c'est ok.
					int prixTemp = repProducteur.get(repProducteur.firstKey());
					AID aidTemp = repProducteur.firstKey();

					for (AID id : repProducteur.navigableKeySet()) {
						if (repProducteur.get(id) < prixTemp) {
							prixTemp = repProducteur.get(id);
							aidTemp = id;
						}
					}

					AID ancienFournisseur = ((ConsommateurAgent) myAgent)
							.getFournisseurID();

					if (ancienFournisseur != null) {
						if (ancienFournisseur == aidTemp) {
							((ConsommateurAgent) myAgent)
									.setPrixfournisseur(prixTemp);
						} else {
							ACLMessage desabonnement = new ACLMessage(
									AbstractAgent.PRODUCTEUR_DESABONNEMENT);
							desabonnement.addReceiver(ancienFournisseur);
							myAgent.send(desabonnement);

							((ConsommateurAgent) myAgent)
									.setFournisseurID(aidTemp);
							((ConsommateurAgent) myAgent)
									.setPrixfournisseur(prixTemp);
							ACLMessage abonnement = new ACLMessage(
									AbstractAgent.PRODUCTEUR_ABONNEMENT);
							abonnement.addReceiver(aidTemp);
							myAgent.send(abonnement);
						}
					} else {
						((ConsommateurAgent) myAgent).setFournisseurID(aidTemp);
						((ConsommateurAgent) myAgent)
								.setPrixfournisseur(prixTemp);
						ACLMessage abonnement = new ACLMessage(
								AbstractAgent.PRODUCTEUR_ABONNEMENT);
						abonnement.addReceiver(aidTemp);
						myAgent.send(abonnement);
					}

					myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(
							myAgent, msgHorlogeToAnswer));
					isDone = true;
				}
			}

			else if (msg.getPerformative() == AbstractAgent.PRODUCTEUR_PRIX_CHANGEMENT) {
				((ConsommateurAgent) myAgent).setPrixfournisseur(Integer
						.parseInt(msg.getContent()));
			}

			else if (msg.getPerformative() == AbstractAgent.CONSOMMATEUR_BESOIN_DEMANDE) {
				ACLMessage reply = msg.createReply();
				reply.setPerformative(AbstractAgent.CONSOMMATEUR_BESOIN_REPONSE);
				
				int besoin = ((ConsommateurAgent) myAgent).getBesoin();

				// calcul du besoin reel si l'agent est aussi producteur
				if (((ConsommateurAgent) myAgent).isConsommateurProducteur()) {
					// si le consommateur produit trop d'electricité
					if (((ConsommateurAgent) myAgent).getCapaciteProducteur() > besoin)
						besoin = 0;

					else
						besoin -= ((ConsommateurAgent) myAgent)
								.getCapaciteProducteur();
				}
				
				reply.setContent(Integer.toString(besoin));
				myAgent.send(reply);
			}

			else {
				System.out.println("Message non compris.\n" + msg);
				System.out.println(((ConsommateurAgent) myAgent)
						.getFournisseurID());
				System.out.println(msg.getSender());
			}
		} else
			block();
	}

	@Override
	public boolean done() {
		return isDone;
	}

}
