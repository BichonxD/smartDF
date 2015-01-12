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
		
		Object[] parametresProducteur = new Object[1];
		parametresProducteur[0] = 5;
		
		Object[] parametresConsommateur = new Object[3];
		Object[] parametresObservateur = new Object[0];
		
		ArrayList<AgentController> lAC = new ArrayList<AgentController>();
		
		for(int i = 0; i < 5; i++)
		{
			parametresConsommateur[0] = 2 * i;
			parametresConsommateur[1] = (i % 2 == 0);
			parametresConsommateur[2] = (i % 2 == 0) ? i : 0;
			lAC.add(mc.createNewAgent("Consommateur " + i, ConsommateurAgent.class.getName(), parametresConsommateur));
		}
		
		lAC.add(mc.createNewAgent("Producteur 1", ProducteurAgent.class.getName(), parametresProducteur));
		lAC.add(mc.createNewAgent("Observateur 1", ObservateurAgent.class.getName(), parametresObservateur));
		
		for(AgentController aC : lAC)
		{
			aC.start();
		}
	}
	
}
