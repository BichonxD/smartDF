package reseauSimple;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class GlobalSearchBehaviour extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	private DFAgentDescription dfdRecherche = null;
	
	public GlobalSearchBehaviour(DFAgentDescription dfdRecherche)
	{
		this.dfdRecherche = dfdRecherche;
	}
	
	@Override
	public void action()
	{
		DFAgentDescription[] resultRecherche = null;
		
		try
		{
			resultRecherche = DFService.search(myAgent, dfdRecherche);
		}catch(FIPAException e)
		{
			e.printStackTrace();
			System.err.println("Erreur aucun résultat trouvé.");
		}
		
		if(resultRecherche != null)
		{
			((AbstractAgent) myAgent).setAnnuairePerso(new AID[resultRecherche.length]);
			for(int i = 0; i < resultRecherche.length; i++)
			{
				((AbstractAgent) myAgent).getAnnuairePerso()[i] = resultRecherche[i].getName();
			}
		}
		
	}
	
}