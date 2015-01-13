/**
 * A ajouter :
 * -gui jade.Boot smartDFTest:test.Test
 */

package test;

import java.util.ArrayList;

import reseauSimple.ProducteurAgent;
import reseauSimple.ConsommateurAgent;
import reseauSimple.ObservateurAgent;
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
		
		int nbClient = (int) (Math.random() * 7 + 3);
		
		ArrayList<AgentController> lAC = new ArrayList<AgentController>();
		int besoin = 0;
		int prod = 0;
		for(int i = 0; i < nbClient + 1; i++){
			besoin = (int) (Math.random() * 9 + 1);
			prod = (int) (Math.random() * 9 + 1);
			lAC.add(mc.createNewAgent("Consommateur " + (i + 1), ConsommateurAgent.class.getName(), new Object[]{besoin, (i % 2 == 0), prod}));
		}
		lAC.add(mc.createNewAgent("Producteur 1", ProducteurAgent.class.getName(), new Object[]{10}));
		lAC.add(mc.createNewAgent("Observateur 1", ObservateurAgent.class.getName(), new Object[0]));
		
		for(AgentController aC : lAC)
		{
			aC.start();
		}
	}
	
}
