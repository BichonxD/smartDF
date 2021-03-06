package reseauSimple.transporteur;

import java.util.TreeMap;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TransporteurBehaviourMsgListenerAllPhases extends CyclicBehaviour
{
	private static final long serialVersionUID = 1L;
	private boolean facturationMonFournisseur = false;
	private int capaciteRestante = ((TransporteurAgent) myAgent).getCapaciteTransporteur();
	private int prixTransporteur = ((TransporteurAgent) myAgent).getPrixKWhTransporteur();
	private TreeMap<AID, Integer> demandeEnAttente = new TreeMap<AID, Integer>();
	
	public TransporteurBehaviourMsgListenerAllPhases(Agent a)
	{
		super(a);
	}
	
	public void termineTour()
	{
		capaciteRestante = ((TransporteurAgent) myAgent).getCapaciteTransporteur();
		prixTransporteur = ((TransporteurAgent) myAgent).getPrixKWhTransporteur();
		
		// on envoi notre argent a notre producteur :
		if(!((TransporteurAgent) myAgent).isTransporteurOfficiel())
		{
			ACLMessage msg = new ACLMessage(AbstractAgent.TRANSPORTEUR_ENVOI_ARGENT);
			msg.setSender(((TransporteurAgent) myAgent).getFournisseurID());
			msg.setContent(Integer.toString(((TransporteurAgent) myAgent).getArgentTransporteur()));
			myAgent.send(msg);
			
			// et on le met a zero
			((TransporteurAgent) myAgent).setArgentTransporteur(0);
			facturationMonFournisseur = false;
			demandeEnAttente.clear();
		}
	}
	
	@Override
	public void action()
	{
		// Recoit la quantite que veut transporter chaque producteur sur son reseau
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_DEMANDE), MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_ENCAISSE_PAIEMENT));
		ACLMessage msg = myAgent.receive(mt);
		
		// Traitement du message
		if(msg != null)
		{
			// demande facturation =>
			// regarder si c'est mon agent
			// si ca l'est pas attendre que mon agent demande la facturation et stocker la requete
			// une fois que j'ai la demande de mon agent je choisi une autre demande dans mes capacite
			// ou faire la difference avec la capacite qu'il me reste
			// augmenter ou diminuer le prix
			
			if(msg.getPerformative() == AbstractAgent.TRANSPORTEUR_FACTURATION_DEMANDE)
			{
				// cas simple du transporteur officiel
				if(((TransporteurAgent) myAgent).isTransporteurOfficiel())
				{
					int demande = Integer.parseInt(msg.getContent());
					ACLMessage msgReply = msg.createReply();
					msgReply.setPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE);
					int aPayer = demande * prixTransporteur;
					msgReply.setContent(Integer.toString(aPayer));
					myAgent.send(msgReply);
				}
				
				else
				{
					// dans le cas ou c'est mon agent la capacite diminue, pas de facture
					if(msg.getSender() == ((TransporteurAgent) myAgent).getFournisseurID())
					{
						capaciteRestante -= ((TransporteurAgent) myAgent).getCapaciteTransporteur();
						facturationMonFournisseur = true;
						
						// on diminue la capacite et on facture les autres s'il y en a en attente
						for(AID id : demandeEnAttente.navigableKeySet())
						{
							if(demandeEnAttente.get(id) < capaciteRestante)
							{
								// soit on accepte une demande dans les capacite et on demande a etre paye
								capaciteRestante -= demandeEnAttente.get(id);
								ACLMessage msgReply = new ACLMessage(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE);
								msgReply.setSender(id);
								int aPayer = demandeEnAttente.get(id) * prixTransporteur;
								msgReply.setContent(Integer.toString(aPayer));
								myAgent.send(msgReply);
								// Luc doit traiter ce message et me payer! Sinon p�nalit� :)
							}
							
							else
							{
								// si ca passe pas on la refuse!
								ACLMessage msgReply = new ACLMessage(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_NEGATIVE);
								msgReply.setSender(id);
								myAgent.send(msgReply);
							}
						}
						
						// on verifie la capacite restante pour savoir la politique restante au tour suivant
					}
					
					// dans le cas ou c'est pas une demande de facturation de mon fournisseur, mais que je l'ai deja transporte :
					else if(msg.getSender() != ((TransporteurAgent) myAgent).getFournisseurID() && facturationMonFournisseur)
					{
						int demande = Integer.parseInt(msg.getContent());
						if(demande < capaciteRestante)
						{
							// si la demande passe
							capaciteRestante -= demande;
							ACLMessage msgReply = msg.createReply();
							msgReply.setPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE);
							int aPayer = demande * prixTransporteur;
							msgReply.setContent(Integer.toString(aPayer));
							myAgent.send(msgReply);
							// On traite positivement
						}
						
						else
						{
							// si ca passe pas on la refuse!
							ACLMessage msgReply = msg.createReply();
							msgReply.setPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_NEGATIVE);
							myAgent.send(msgReply);
						}
					}
					
					// et enfin si on a pas traite le fournisseur, on met le message dans la pile des demandes en attendant de recevoir la demande de son producteur
					else
					{
						AID demandeAidTransport = msg.getSender();
						int demandePrixTransport = Integer.parseInt(msg.getContent());
						demandeEnAttente.put(demandeAidTransport, demandePrixTransport);
					}
				}
			}
			else if(msg.getPerformative() == AbstractAgent.TRANSPORTEUR_ENCAISSE_PAIEMENT)
			{
				//System.out.println("TRANSPORTEUR_ENCAISSE_PAIEMENT:" + myAgent.getAID() + " je gagne pour mon dur labeur " + Integer.parseInt(msg.getContent()) + " euros paye par " + msg.getSender());
				int argentDispo = ((TransporteurAgent) myAgent).getArgentTransporteur();
				argentDispo += Integer.parseInt(msg.getContent());
				((TransporteurAgent) myAgent).setArgentTransporteur(argentDispo);
			}
			else
			{
				System.out.println("Performatif non compris par le transporteur");
			}
			
		}
		else
			block();
	}
}
