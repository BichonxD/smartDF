/**
 * A ajouter :
 * -gui jade.Boot smartDFTest:test.Test
 */

package test;

import reseauSimple.ObservateurAgent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class TestObservator
{
	public static void main(String[] a) throws StaleProxyException
	{
		
		Runtime rt = Runtime.instance();
		rt.setCloseVM(true);
		
		Profile pMain = new ProfileImpl("localhost", 8888, null);
		AgentContainer mc = rt.createMainContainer(pMain);
		
		AgentController test = mc.createNewAgent("bob", ObservateurAgent.class.getName(), new Object[0]);
		test.start();
	}
}
