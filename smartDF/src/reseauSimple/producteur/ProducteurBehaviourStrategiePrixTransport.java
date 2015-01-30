package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalBehaviourHorlogeTalker;
import reseauSimple.transporteur.TransporteurAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class ProducteurBehaviourStrategiePrixTransport extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private ACLMessage msgHorlogeToAnswer;
	
	public ProducteurBehaviourStrategiePrixTransport(Agent a, ACLMessage msgHorlogeToAnswer)
	{
		super(a);
		this.msgHorlogeToAnswer= msgHorlogeToAnswer;
	}
	
	@Override
	public void action()
	{
		/*
		 * Calcul de l'electricite previsionnel, par rapport a ce qu'on a fourni au tour d'avant, pour calculer notre prix a ce tout ci, si on avait des clients
		 */
		int electriciteAFournirPrevisionnel = 0;
		
		if (((ProducteurAgent) myAgent).getClientsFournisseur() != null && ((ProducteurAgent) myAgent).getClientsFournisseur().size() != 0) {
			ACLMessage electriciteprevisionnel = new ACLMessage(AbstractAgent.CONSOMMATEUR_BESOIN_DEMANDE);
			for (AID id : ((ProducteurAgent) myAgent).getClientsFournisseur())
				electriciteprevisionnel.addReceiver(id);
			myAgent.send(electriciteprevisionnel);
			
			int nombreReponse = 0;
			
			while (nombreReponse < ((ProducteurAgent) myAgent).getClientsFournisseur().size()) {
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.CONSOMMATEUR_BESOIN_REPONSE));
				
				if(msg != null)
				{
					electriciteAFournirPrevisionnel += Integer.parseInt(msg.getContent());
					nombreReponse++;
				}
			}
		}		
		
		/*
		 * Calcul de la capacite personnel de transport pour calculer notre prix a ce tout ci, si on a des transporteurs
		 */
		int capaciteTransportPersonnel = 0;
		
		if (((ProducteurAgent) myAgent).getTransportsFournisseur() != null && ((ProducteurAgent) myAgent).getTransportsFournisseur().size() != 0) {
			ACLMessage capaciteprevisionnel = new ACLMessage(AbstractAgent.TRANSPORTEUR_CAPACITE_DEMANDE);
			for (AID id : ((ProducteurAgent) myAgent).getTransportsFournisseur())
				capaciteprevisionnel.addReceiver(id);
			myAgent.send(capaciteprevisionnel);
			
			int nombreReponse = 0;
			
			while (nombreReponse < ((ProducteurAgent) myAgent).getTransportsFournisseur().size()) {
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_CAPACITE_REPONSE));
				
				if(msg != null)
				{
					capaciteTransportPersonnel += Integer.parseInt(msg.getContent());
					nombreReponse++;
				}
			}
		}
			
		/*
		 * Calcul du prix a ce tour ci
		 */
		int electriciteTransporteurUniverselPrevisionnel =  electriciteAFournirPrevisionnel - capaciteTransportPersonnel;
		
		int nouveauPrix = ((ProducteurAgent) myAgent).getPrixFournisseur();
		
		//Si on est pas dans le cas initial, a savoir un fournisseur sans client
		if(electriciteAFournirPrevisionnel != 0) {
			
			//Si il arrive a remplier ses transporteur personnel il augmente son prix
			if (electriciteTransporteurUniverselPrevisionnel > 0) {
				nouveauPrix *= 1.1;
				//Si il a assez d'argent pour construire plus de deux transporteur, il en construit un et garde le reste afin de toujours avoir une marge pour payer les amendes
				if (((ProducteurAgent) myAgent).getArgentFournisseur() > ((ProducteurAgent) myAgent).getPrixTransporteur() *3) {
					Runtime rt = Runtime.instance();
					rt.setCloseVM(true);
					Profile pMain = new ProfileImpl("localhost", 8888, null);
					AgentContainer mc = rt.createMainContainer(pMain);
					
					Object[] argument = new Object[1];
					argument[0] = myAgent.getAID();
					
					try {
						AgentController ac = mc.createNewAgent("Transporteur " + myAgent.getAID(), TransporteurAgent.class.getName(), argument);
						ac.start();
						//Ajout a mes transporteurs via l'annuaire au debut de la phase 1 avec l'horloge
					} catch (StaleProxyException e) {
						e.printStackTrace();
					}
				}
			}
			//Sinon il le diminue (marche pour le cas ou on a pas eu de client)
			else
				nouveauPrix *= 0.9;
		}
		
		/*
		 * Si le prix a varier par rapport au tour d'avant on notifie tout nos consomateurs
		 */
		if (((ProducteurAgent) myAgent).getPrixFournisseur() != nouveauPrix) {
			ACLMessage changementPrix = new ACLMessage(AbstractAgent.PRODUCTEUR_PRIX_CHANGEMENT);
			for (AID id : ((ProducteurAgent) myAgent).getClientsFournisseur())
				changementPrix.addReceiver(id);
			changementPrix.setContent(Integer.toString(nouveauPrix));
			myAgent.send(changementPrix);
		}
		
		
		/*
		 * Signale la fin des négociations à l'horloge.
		 */
		myAgent.addBehaviour(new GlobalBehaviourHorlogeTalker(myAgent, msgHorlogeToAnswer));
		
		/*
		 * Lance le MsgListenerNegociation du Producteur et l'ajoute à la liste des cyclicBehaviour qui tournent
		 */
		ProducteurBehaviourMsgListenerNegociation pbmln = new ProducteurBehaviourMsgListenerNegociation(myAgent);
		myAgent.addBehaviour(pbmln);
		((AbstractAgent) myAgent).getListCyclicBehaviour().add(pbmln);
	}

}
