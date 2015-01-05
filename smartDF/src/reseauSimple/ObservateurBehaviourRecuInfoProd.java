
package reseauSimple;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ObservateurBehaviourRecuInfoProd extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void action()
	{
		// On cherche tous les producteurs
		DFAgentDescription dfdRecherche = new DFAgentDescription();
		ServiceDescription sdRecherche = new ServiceDescription();
		sdRecherche.setName("producteur");
		sdRecherche.setType("producteur");
		dfdRecherche.addServices(sdRecherche);
		DFAgentDescription[] resultRecherche = null;
		
		try
		{
			resultRecherche = DFService.search(myAgent, dfdRecherche);
		} catch (FIPAException e)
		{
			e.printStackTrace();
			System.err.println("Erreur aucun producteur trouvé.");
		}
		
		if(resultRecherche != null)
		{
			// Affichage des résultats trouvés
			for(DFAgentDescription dfd : resultRecherche)
			{
				dfd.getName();
			}
		}
		
	}
	
}
