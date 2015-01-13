package reseauSimple;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public abstract class AbstractAgent extends Agent
{
	private static final long serialVersionUID = 1L;
	private String serviceName = "defaultService";
	private AID[] annuairePerso;
	
	protected void setup()
	{
		// Inscription de l'agent dans l'annuaire
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(serviceName);
		sd.setType(serviceName);
		dfd.addServices(sd);
		
		try
		{
			DFService.register(this, dfd);
		}catch(FIPAException e)
		{
			e.printStackTrace();
			System.err.println("Erreur d'inscription de l'observateur " + getAID() + " dans l'annuaire.");
		}
	}
	
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	
	public void setAnnuairePerso(AID[] annuairePerso)
	{
		this.annuairePerso = annuairePerso;
	}
	
	public AID[] getAnnuairePerso()
	{
		return annuairePerso;
	}
	
	// Put agent clean-up operations here
	protected void takeDown()
	{
		// Deregister from the yellow pages
		try
		{
			DFService.deregister(this);
		}catch(FIPAException fe)
		{
			fe.printStackTrace();
		}
	}
	
}
