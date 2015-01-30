/**
 * A ajouter pour lancer le programme :
 * -gui jade.Boot smartDFTest:test.Test
 */

package test;

import java.util.ArrayList;

import reseauSimple.consommateur.ConsommateurAgent;
import reseauSimple.horloge.HorlogeAgent;
import reseauSimple.observateur.ObservateurAgent;
import reseauSimple.producteur.ProducteurAgent;
import reseauSimple.transporteur.TransporteurAgent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Test
{
	public static Runtime rt = Runtime.instance();
	public static Profile pMain = new ProfileImpl("localhost", 8888, null);
	public static AgentContainer mc = rt.createMainContainer(pMain);
	
	public static void main(String[] a) throws StaleProxyException
	{
		rt.setCloseVM(true);

		//int nbConsommateur = (int) (Math.random() * 5 + 1);
		//int nbProducteur = (int) (Math.random() * 5 + 1);
		int nbConsommateur = 80;
		int nbProducteur = 3;
		
		ArrayList<AgentController> lAC = new ArrayList<AgentController>();
		
		// Consommateurs
		for(int i = 0; i < nbConsommateur; i++)
		{
			lAC.add(mc.createNewAgent("Consommateur " + (i + 1), ConsommateurAgent.class.getName(), new Object[0]));
		}
		
		// Producteurs
		for(int i = 0; i < nbProducteur; i++)
		{
			lAC.add(mc.createNewAgent("Producteur " + (i + 1), ProducteurAgent.class.getName(), new Object[0]));
		}
		
		// Transporteur
		lAC.add(mc.createNewAgent("Transporteur Officiel", TransporteurAgent.class.getName(), new Object[0]));

		// Observateur
		lAC.add(mc.createNewAgent("Observateur", ObservateurAgent.class.getName(), new Object[0]));
		
		// Horloge
		lAC.add(mc.createNewAgent("Horloge", HorlogeAgent.class.getName(), new Object[0]));
		
		for(AgentController aC : lAC)
		{
			aC.start();
		}
	}
	
}
