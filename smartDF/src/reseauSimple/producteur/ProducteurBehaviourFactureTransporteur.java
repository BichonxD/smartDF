package reseauSimple.producteur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducteurBehaviourFactureTransporteur extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	public ProducteurBehaviourFactureTransporteur(Agent a)
	{
		super(a);
	}
	
	@Override
	public void action()
	{
		System.out.println("here1 ");
		/*
		 * Calcul de l'electricite a fournir ce tour-ci si on a des clients
		 */
		int electriciteATransporter = 0;
		
		if(((ProducteurAgent) myAgent).getClientsFournisseur() != null && ((ProducteurAgent) myAgent).getClientsFournisseur().size() != 0)
		{
			ACLMessage electriciteprevisionnel = new ACLMessage(AbstractAgent.CONSOMMATEUR_BESOIN_DEMANDE);
			for(AID id : ((ProducteurAgent) myAgent).getClientsFournisseur())
				electriciteprevisionnel.addReceiver(id);
			myAgent.send(electriciteprevisionnel);
			
			for(int nombreReponse = 0; nombreReponse < ((ProducteurAgent) myAgent).getClientsFournisseur().size();)
			{
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.CONSOMMATEUR_BESOIN_REPONSE));
				
				if(msg != null)
				{
					electriciteATransporter += Integer.parseInt(msg.getContent());
					nombreReponse++;
				}
				else
					block();
			}
		}
		System.out.println("here2 " + electriciteATransporter);
		
		/*
		 * Calcul et utilisation de la capacite de transport personnel, si on a des transporteur, au maximum de ce qu'on peut utiliser, ils sont gratuit donc pas de facturation
		 */
		if(((ProducteurAgent) myAgent).getTransportsFournisseur() != null && ((ProducteurAgent) myAgent).getTransportsFournisseur().size() != 0)
		{
			for(AID a : ((ProducteurAgent) myAgent).getTransportsFournisseur())
			{
				if(electriciteATransporter <= 0)
					break;
				else
				{
					ACLMessage demandeCapacite = new ACLMessage(AbstractAgent.TRANSPORTEUR_CAPACITE_DEMANDE);
					demandeCapacite.addReceiver(a);
					myAgent.send(demandeCapacite);
					
					boolean reponse = false;
					int capacite = 0;
					
					while(!reponse)
					{
						ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_CAPACITE_REPONSE));
						
						if(msg != null)
						{
							int facture = 0;
							
							capacite = Integer.parseInt(msg.getContent());
							if(capacite < electriciteATransporter)
							{
								electriciteATransporter -= capacite;
								facture = capacite;
							}
							else
							{
								facture = electriciteATransporter;
								electriciteATransporter = 0;
							}
							ACLMessage reply = msg.createReply();
							reply.setPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_DEMANDE);
							reply.setContent(Integer.toString(facture));
							reponse = true;
							/*
							 * On attends pas de reponse de leur part car ils sont a nous, ils n'ont pas le choix
							 */
						}
						else
							block();
					}
				}
			}
		}
		System.out.println("here3 " + electriciteATransporter);
		
		/*
		 * Si il reste de l'electricite a transporter on utilise le transporteur universel et on le paye
		 */
		if(electriciteATransporter > 0)
		{
			ACLMessage facturationTransporteurUniversel = new ACLMessage(AbstractAgent.TRANSPORTEUR_FACTURATION_DEMANDE);
			facturationTransporteurUniversel.addReceiver(((ProducteurAgent) myAgent).getAnnuairePersoTransporteurOfficiel()[0]);
			facturationTransporteurUniversel.setContent(Integer.toString(electriciteATransporter));
			
			boolean reponse = false;
			
			while(!reponse)
			{
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE));
				
				if(msg != null)
				{
					((ProducteurAgent) myAgent).setArgentFournisseur(((ProducteurAgent) myAgent).getArgentFournisseur() - Integer.parseInt(msg.getContent()));
					reponse = true;
				}
				else
					block();
			}
		}
		System.out.println("here4 " + electriciteATransporter);
	}
	
}
