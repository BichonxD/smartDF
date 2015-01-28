package reseauSimple.transporteur;

import reseauSimple.global.AbstractAgent;
import jade.core.AID;

public class TransporteurAgent extends AbstractAgent {
	private static final long serialVersionUID = 1L;

	private AbstractAgent proprietaire;
	private int capaciteTransporteur;
	private int prixKWhTransporteur;
	private int argentTransporteur;
	private TransporteurBehaviourMsgListenerAllPhases tansporteurBehaviourMsg;

	protected void setup() {
		if (getArguments().length != 3) {
			System.err
					.println("Nombre d'argument invalides. Il faut 3 arguments : proprietaire, capaciteTransporteur, prixKWhTransporteur.");
		} else {
			proprietaire = (AbstractAgent) getArguments()[0];
			capaciteTransporteur = (int) getArguments()[1];
			prixKWhTransporteur = (int) getArguments()[2];
			setServiceName("transporteur");
			super.setup();

			System.out.println("Creation d'un nouveau transporteur :\n" + toString());

			addBehaviour(new TransporteurBehaviourHorlogeListener());
			tansporteurBehaviourMsg = new TransporteurBehaviourMsgListenerAllPhases();
			addBehaviour(tansporteurBehaviourMsg);
		}
	}

	public AID getFournisseurID() {
		return proprietaire.getAID();
	}

	public int getCapaciteTransporteur() {
		return capaciteTransporteur;
	}

	public int getPrixKWhTransporteur() {
		return prixKWhTransporteur;
	}

	public void setPrixKWhTransporteur(int prixKWhTransporteur) {
		this.prixKWhTransporteur = prixKWhTransporteur;
	}
	
	public int getArgentTransporteur() {
		return argentTransporteur;
	}
	
	public void setArgentTransporteur(int argentTransporteur) {
		this.argentTransporteur = argentTransporteur;
	}

	public TransporteurBehaviourMsgListenerAllPhases getTansporteurBehaviourMsg() {
		return tansporteurBehaviourMsg;
	}

}
