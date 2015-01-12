package reseauSimple;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import jade.core.AID;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ObservateurGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	private HashMap<AID, JLabel> labels = new HashMap<AID, JLabel>();
	
	public ObservateurGUI(final ObservateurAgent myAgent)
	{
		this.setTitle("smartDF");
		this.setSize(600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		
		this.addWindowListener(new WindowAdapter()
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	                System.out.println("Ordre de terminer l'application.");
	                e.getWindow().dispose();
	                myAgent.doDelete();
	            }
	        });
	}
	
	public void ajouterLabel(AID a, String s)
	{
		if(labels.containsKey(a))
			labels.get(a).setText(s);
		else
		{
			JLabel newLabel = new JLabel(s);
			labels.put(a, newLabel);
			this.getContentPane().add(newLabel);
		}
		
		this.pack();
		this.revalidate();
		this.repaint();
	}
}
