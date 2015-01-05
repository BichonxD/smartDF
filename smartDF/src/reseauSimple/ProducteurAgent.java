package reseauSimple;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
// permet de donner un identifiant unique au producteur
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
// pour ajouter l'agent à la table 

public class ProducteurAgent extends Agent {
	private static int tarif = 100;
	
	
	public static int getTarif() {
		return tarif;
	}
	
	protected void setup() {
		try {
			System.out.println("Création de l'agent " + getLocalName());
			System.out.println(getAID());
			DFAgentDescription descripteurProd = new DFAgentDescription();
			descripteurProd.setName(getAID());
			
			ServiceDescription sdProd = new ServiceDescription();
			sdProd.setName("producteur");
			sdProd.setType("null");
			descripteurProd.addServices(sdProd);
			
			DFService.register(this, descripteurProd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//behaviour de test
		/*
		addBehaviour(new CyclicBehaviour(this) {
		      public void action() {
		        System.out.println("Cycling");
		      } 
		    });
		*/
		addBehaviour(new ProducteurEnvoiTarif());
		
	}
	
	
}
