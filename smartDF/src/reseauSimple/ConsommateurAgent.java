package reseauSimple;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.introspection.AddedBehaviour;

public class ConsommateurAgent extends Agent
{
	boolean consommateurProducteur;
	int capaciteProducteur;
	
	int fournisseurID;
	int besoin;
	
	protected void setup() {
	  	System.out.println("Hello World! My name is "+getLocalName());
	  	
	  	consommateurProducteur = false;
	  	capaciteProducteur = 0;
	  	
	  	int fournisseurID = 0;
	  	int besoin = 100;
	  	
	  	DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
	  	
	  	ServiceDescription SDConsommateur = new ServiceDescription();
	  	SDConsommateur.setName("consommateur");
	  	SDConsommateur.seType("consommateur");
	  	dfd.addServices(SDConsommateur);
	  	
	  	if (consommateurProducteur) {
	  		ServiceDescription SDProducteur = new ServiceDescription();
	  		SDProducteur.setName("consommateurProducteur");
	  		SDProducteur.setType("" + capaciteProducteur);
	  		dfd.addServices(SDProducteur);
	  	}
	  	
	  	try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	  	
	  	addBehaviour(new ConsommateurBehaviourChoisirProducteur());
	}	
	
}
