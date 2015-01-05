package reseauSimple;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ConsommateurBehaviourChoisirProducteur extends OneShotBehaviour {

	@Override
	public void action() {
		// TODO Stub de la méthode généré automatiquement
		
		DFAgentDescription dfd = new DFAgentDescription();
		
		ServiceDescription SDProducteur = new ServiceDescription();
		SDProducteur.setName("producteur");
		SDProducteur.setType("producteur");
	  	dfd.addServices(SDProducteur);
		
	  	DFAgentDescription[] results = null;
	  	
	  	try {
			results = DFService.search(myAgent, dfd);
		} catch (FIPAException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	  	
	  	if (results.length > 0) {
	  		AID provider = null;
	  		int tarif = 0;
	  		
  			System.out.println("Agent "+myAgent.getLocalName()+" producteur trouve:");
  			for (int i = 0; i < results.length; ++i) {
  				DFAgentDescription d = results[i];
  				if (i == 0) {
  					provider = d.getName();
  				}
  				else if ( ) {
  					
  				}
  				DFAgentDescription d = results[i];
  				AID provider = dfd.getName();
  				// The same agent may provide several services; we are only interested
  				// in the weather-forcast one
  				Iterator it = dfd.getAllServices();
  				while (it.hasNext()) {
  					ServiceDescription sd = (ServiceDescription) it.next();
  					if (sd.getType().equals("weather-forecast")) {
  						System.out.println("- Service \""+sd.getName()+"\" provided by agent "+provider.getName());
  					}
  				}
  			}
  		}	
	}
}
