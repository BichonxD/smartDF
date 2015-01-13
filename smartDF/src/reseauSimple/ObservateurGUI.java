package reseauSimple;

import java.awt.GridLayout;
import java.util.HashMap;

import jade.core.AID;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ObservateurGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	private HashMap<AID, JLabel> labelsPrix = new HashMap<AID, JLabel>();
	private HashMap<AID, JLabel> labelsArgent = new HashMap<AID, JLabel>();
	private HashMap<AID, JLabel> labelsNbClients = new HashMap<AID, JLabel>();
	private GridLayout grille = new GridLayout(10, 1);
	
	public ObservateurGUI(final ObservateurAgent myAgent)
	{
		this.setTitle("smartDF");
		this.setSize(600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		panel.setLayout(grille);
	}
	
	public void ajouterPrixProducteur(AID a, String s)
	{
		if(labelsPrix.containsKey(a))
			labelsPrix.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsPrix.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterArgentProducteur(AID a, String s)
	{
		if(labelsArgent.containsKey(a))
			labelsArgent.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsArgent.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterNbClientsProducteur(AID a, String s)
	{
		if(labelsNbClients.containsKey(a))
			labelsNbClients.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsNbClients.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
}
