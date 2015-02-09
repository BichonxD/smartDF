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
		
		/*
		 * Generation de l'electricite pour chaque client, autrement dit coup de facturation
		 */
		
		//Determination du prix de fabrication pour ce tour ci
		((ProducteurAgent) myAgent).setPrixFabrication((int) (Math.random() * (ProducteurAgent.getPrixFabricationMax() - ProducteurAgent.getPrixFabricationMin()) + ProducteurAgent.getPrixFabricationMin()));
		
		//Facturation
		
		//System.out.println(myAgent.getAID() + " paye " + ((ProducteurAgent) myAgent).getArgentFournisseur() + " - " + ((ProducteurAgent) myAgent).getPrixFabrication() + " * " + electriciteATransporter + " = " + (((ProducteurAgent) myAgent).getArgentFournisseur() - ((ProducteurAgent) myAgent).getPrixFabrication() * electriciteATransporter));
		
		((ProducteurAgent) myAgent).setArgentFournisseur(((ProducteurAgent) myAgent).getArgentFournisseur() - ((ProducteurAgent) myAgent).getPrixFabrication() * electriciteATransporter);
		
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
		
		/*
		 * On essaye d'utiliser les transporteurs personnels des concurents si ils sont moins cher que le transporteur officiel
		 */
		//Recuperation du prix du transporteur officiel pour comparer
		ACLMessage demandePrixTransporteurOfficiel = new ACLMessage(AbstractAgent.TRANSPORTEUR_PRIX_DEMANDE);
		demandePrixTransporteurOfficiel.addReceiver(((ProducteurAgent) myAgent).getAnnuairePersoTransporteurOfficiel()[0]);
		myAgent.send(demandePrixTransporteurOfficiel);
		
		boolean reponse = false;
		int prixTransporteurOfficiel = 0;
		
		while(!reponse)
		{
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_PRIX_REPONSE));
			
			if(msg != null)
			{
				prixTransporteurOfficiel = Integer.parseInt(msg.getContent());
				reponse = true;
			}
		}
		
		//Recuperation du prix de chaque transporteur personnel
		int nombreAgentTransporteur = 0;
		
		if (((ProducteurAgent) myAgent).getAnnuairePersoOptionnel() != null)
			nombreAgentTransporteur = ((ProducteurAgent) myAgent).getAnnuairePersoOptionnel().length;
		
		while (nombreAgentTransporteur > 0 && electriciteATransporter > 0) {
			
			AID tempAID = ((ProducteurAgent) myAgent).getAnnuairePersoOptionnel()[nombreAgentTransporteur-1];
				
			//On enleve nos transporteurs personnels
			if (((ProducteurAgent) myAgent).getTransportsFournisseur().contains(tempAID))
				nombreAgentTransporteur--;
			
			//On compare le prix des autres a celui du transporteur officiel
			ACLMessage prixTransporteurNonPersonnel = new ACLMessage(AbstractAgent.TRANSPORTEUR_PRIX_DEMANDE);
			prixTransporteurNonPersonnel.addReceiver(tempAID);
			myAgent.send(prixTransporteurNonPersonnel);
			
			reponse = false;
			int prixTransporteurNonPersonnelValue = 0;
			
			while(!reponse)
			{
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_PRIX_REPONSE));
				
				if(msg != null)
				{
					prixTransporteurNonPersonnelValue = Integer.parseInt(msg.getContent());
					reponse = true;
				}
			}				
			
			//Si il est plus petit on l'utilise
			if(prixTransporteurNonPersonnelValue != 0 && prixTransporteurNonPersonnelValue < prixTransporteurOfficiel) {
				ACLMessage facturationTransporteurNonPersonnel = new ACLMessage(AbstractAgent.TRANSPORTEUR_FACTURATION_DEMANDE);
				facturationTransporteurNonPersonnel.addReceiver(tempAID);
				facturationTransporteurNonPersonnel.setContent(Integer.toString(electriciteATransporter));
				myAgent.send(facturationTransporteurNonPersonnel);
				
				reponse = false;
				
				while(!reponse)
				{
					ACLMessage msg = myAgent.receive(MessageTemplate.or(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE), MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_NEGATIVE)));
					
					if(msg != null)
					{
						reponse = true;
						nombreAgentTransporteur--;
						
						switch(msg.getPerformative())
						{
							//Si la reponse est positive
							case AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE:
								((ProducteurAgent) myAgent).setArgentFournisseur(((ProducteurAgent) myAgent).getArgentFournisseur() - Integer.parseInt(msg.getContent()));
								electriciteATransporter -= Integer.parseInt(msg.getContent());
								
								//System.out.println("TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE: " + myAgent.getAID() + " paye " + msg.getContent() + " a " + msg.getSender());
								
								ACLMessage reply = msg.createReply();
								reply.setPerformative(AbstractAgent.TRANSPORTEUR_ENCAISSE_PAIEMENT);
								reply.setContent(msg.getContent());
								myAgent.send(reply);
								
								break;
							
							//Sinon
							case AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_NEGATIVE:									
								break;
							
							
							 // Cas d'erreur ne doit pas arriver							 
							default:
								System.out.println("Producteur " + myAgent.getAID() + " Facturation transport erreur : Erreur n'est pas censé arriver");
								break;
						}
					}
					else
						block();
				}
			}	
		}
			
		
		/*
		 * Si il reste de l'electricite a transporter on utilise le transporteur universel et on le paye
		 */
		if(electriciteATransporter > 0)
		{
			ACLMessage facturationTransporteurUniversel = new ACLMessage(AbstractAgent.TRANSPORTEUR_FACTURATION_DEMANDE);
			facturationTransporteurUniversel.addReceiver(((ProducteurAgent) myAgent).getAnnuairePersoTransporteurOfficiel()[0]);
			facturationTransporteurUniversel.setContent(Integer.toString(electriciteATransporter));
			myAgent.send(facturationTransporteurUniversel);
			
			reponse = false;
			
			while(!reponse)
			{
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(AbstractAgent.TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE));
				
				if(msg != null)
				{
					((ProducteurAgent) myAgent).setArgentFournisseur(((ProducteurAgent) myAgent).getArgentFournisseur() - Integer.parseInt(msg.getContent()));
					reponse = true;
					
					//System.out.println("TRANSPORTEUR_FACTURATION_REPONSE_POSITIVE: " + myAgent.getAID() + " paye " + msg.getContent() + " a " + msg.getSender());
					
					ACLMessage reply = msg.createReply();
					reply.setPerformative(AbstractAgent.TRANSPORTEUR_ENCAISSE_PAIEMENT);
					reply.setContent(msg.getContent());
					myAgent.send(reply);
				}
				else
					block();
			}
		}
	}
	
}
