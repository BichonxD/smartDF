package reseauSimple.transporteur;

import reseauSimple.consommateur.ConsommateurBehaviourAskPrixProducteur;
import reseauSimple.global.AbstractAgent;
import reseauSimple.global.GlobalSearchBehaviour;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class TransporteurAgent extends AbstractAgent
{
	private static final long serialVersionUID = 1L;

	private AbstractAgent proprietaire;
	private int capaciteTransporteur;
	private int prixKWhTransporteur;
	
	protected void setup()
	{
		if(getArguments().length != 3)
		{
			System.err.println("Nombre d'argument invalides. Il faut 3 arguments : proprietaire, capaciteTransporteur, prixKWhTransporteur.");
		}
		else
		{
			proprietaire = (AbstractAgent) getArguments()[0];
			capaciteTransporteur = (int) getArguments()[1];
			prixKWhTransporteur = (int) getArguments()[2];
			setServiceName("transporteur");
			super.setup();
			
			System.out.println("Creation d'un nouveau transporteur :\n" + toString());
		}
	}

	public AID getFournisseurID() {
		return proprietaire.getAID();
	}
	
	public int getCapaciteTransporteur(){
		return capaciteTransporteur;
	}
}
