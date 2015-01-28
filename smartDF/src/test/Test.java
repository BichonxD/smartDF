/**
 * A ajouter :
 * -gui jade.Boot smartDFTest:test.Test
 */

package test;

import java.util.ArrayList;

import reseauSimple.consommateur.ConsommateurAgent;
import reseauSimple.observateur.ObservateurAgent;
import reseauSimple.producteur.ProducteurAgent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Test
{
	public static void main(String[] a) throws StaleProxyException
	{
		Runtime rt = Runtime.instance();
		rt.setCloseVM(true);
		
		Profile pMain = new ProfileImpl("localhost", 8888, null);
		AgentContainer mc = rt.createMainContainer(pMain);

		int nbConsommateur = (int) (Math.random() * 7 + 3);
		int nbProducteur = (int) (Math.random() * 7 + 3);
		int nbTransporteur = (int) (Math.random() * 7 + 3);
		
		ArrayList<AgentController> lAC = new ArrayList<AgentController>();
		for(int i = 0; i < nbClient + 1; i++)
		{
			lAC.add(mc.createNewAgent("Consommateur " + (i + 1), ConsommateurAgent.class.getName(), new Object[0]));
		}
		lAC.add(mc.createNewAgent("Producteur 1", ProducteurAgent.class.getName(), new Object[0]));
		lAC.add(mc.createNewAgent("Observateur 1", ObservateurAgent.class.getName(), new Object[0]));
		
		for(AgentController aC : lAC)
		{
			aC.start();
		}
	}
	
}
