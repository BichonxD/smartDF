package reseauSimple.global;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class GlobalBehaviourSearchAnnuaires extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	private DFAgentDescription dfdRecherche = null;
	private DFAgentDescription dfdRechercheTransporteurOfficiel = null;
	private DFAgentDescription dfdRechercheOptionnelle = null;
	
	public GlobalBehaviourSearchAnnuaires(Agent a, DFAgentDescription dfdRecherche)
	{
		super(a);
		this.dfdRecherche = dfdRecherche;
	}
	
	public GlobalBehaviourSearchAnnuaires(Agent a, DFAgentDescription dfdRecherche, DFAgentDescription dfdRechercheTransporteurOfficiel)
	{
		super(a);
		this.dfdRecherche = dfdRecherche;
		this.dfdRechercheTransporteurOfficiel = dfdRechercheTransporteurOfficiel;
	}
	
	public GlobalBehaviourSearchAnnuaires(Agent a, DFAgentDescription dfdRecherche, DFAgentDescription dfdRechercheTransporteurOfficiel, DFAgentDescription dfdRechercheOptionnelle)
	{
		super(a);
		this.dfdRecherche = dfdRecherche;
		this.dfdRechercheTransporteurOfficiel = dfdRechercheTransporteurOfficiel;
		this.dfdRechercheOptionnelle = dfdRechercheOptionnelle;
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
		
		if(dfdRechercheTransporteurOfficiel != null)
		{
			DFAgentDescription[] resultRechercheTransporteurOfficiel = null;
			
			try
			{
				resultRechercheTransporteurOfficiel = DFService.search(myAgent, dfdRechercheTransporteurOfficiel);
			}catch(FIPAException e)
			{
				e.printStackTrace();
				System.err.println("Erreur aucun résultat trouvé pour la recherche optionnnelle.");
			}
			
			if(resultRechercheTransporteurOfficiel != null)
			{
				((AbstractAgent) myAgent).setAnnuairePersoTransporteurOfficiel(new AID[resultRechercheTransporteurOfficiel.length]);
				for(int i = 0; i < resultRechercheTransporteurOfficiel.length; i++)
				{
					((AbstractAgent) myAgent).getAnnuairePersoTransporteurOfficiel()[i] = resultRechercheTransporteurOfficiel[i].getName();
				}
			}
		}
		
		if(dfdRechercheOptionnelle != null)
		{
			DFAgentDescription[] resultRechercheOptionnelle = null;
			
			try
			{
				resultRechercheOptionnelle = DFService.search(myAgent, dfdRechercheOptionnelle);
			}catch(FIPAException e)
			{
				e.printStackTrace();
				System.err.println("Erreur aucun résultat trouvé pour la recherche optionnnelle.");
			}
			
			if(resultRechercheOptionnelle != null)
			{
				((AbstractAgent) myAgent).setAnnuairePersoOptionnel(new AID[resultRechercheOptionnelle.length]);
				for(int i = 0; i < resultRechercheOptionnelle.length; i++)
				{
					((AbstractAgent) myAgent).getAnnuairePersoOptionnel()[i] = resultRechercheOptionnelle[i].getName();
				}
			}
		}
	}
	
}
