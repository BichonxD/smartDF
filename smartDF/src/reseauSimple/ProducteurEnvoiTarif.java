package reseauSimple;

import jade.core.behaviours.Behaviour;

public class ProducteurEnvoiTarif extends Behaviour {
	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println("Hello voici mon tarif : ");
		System.out.println(" 42 ");
	}

	@Override
	public boolean done() {
		return true;
	}

}
