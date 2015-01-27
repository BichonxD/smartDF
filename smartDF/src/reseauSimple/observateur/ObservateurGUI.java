package reseauSimple.observateur;

import java.awt.GridLayout;
import java.util.HashMap;

import jade.core.AID;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import reseauSimple.global.AbstractAgent;

public class ObservateurGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	private JLabel labelPhase = new JLabel("Tour 1 - Phase de Négociation :");
	private int nbTours = 0;
	private HashMap<AID, JLabel> labelsPrixProd = new HashMap<AID, JLabel>();
	private HashMap<AID, JLabel> labelsArgentProd = new HashMap<AID, JLabel>();
	private HashMap<AID, JLabel> labelsNbClientsProd = new HashMap<AID, JLabel>();
	private HashMap<AID, JLabel> labelsPrixTrans = new HashMap<AID, JLabel>();
	private HashMap<AID, JLabel> labelsArgentTrans = new HashMap<AID, JLabel>();
	private HashMap<AID, JLabel> labelsCapaciteTrans = new HashMap<AID, JLabel>();
	private GridLayout grille = new GridLayout(10, 1);
	
	public ObservateurGUI(final ObservateurAgent myAgent)
	{
		this.setTitle("smartDF");
		this.setSize(600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		panel.setLayout(grille);
		panel.add(labelPhase);
	}
	
	public void changementDePhase(int nouvellePhase)
	{
		if(nouvellePhase == AbstractAgent.HORLOGE_PHASE_NEGOCIATION)
			nbTours++;
		
		switch(nouvellePhase)
		{
			case AbstractAgent.HORLOGE_PHASE_NEGOCIATION :
				labelPhase.setText("Tour " + nbTours + " - Phase de Négociation :");
				break;
			
			case AbstractAgent.HORLOGE_PHASE_FACTURATION :
				labelPhase.setText("Tour " + nbTours + " - Phase de Facturation :");
				break;
			
			case AbstractAgent.HORLOGE_PHASE_DEPARTAGE :
				labelPhase.setText("Tour " + nbTours + " - Phase de Départage :");
				break;
			
			default :
				labelPhase.setText("Tour " + nbTours + " - Phase Inconnue :");
				break;
		}
		
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterPrixProducteur(AID a, String s)
	{
		if(labelsPrixProd.containsKey(a))
			labelsPrixProd.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsPrixProd.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterArgentProducteur(AID a, String s)
	{
		if(labelsArgentProd.containsKey(a))
			labelsArgentProd.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsArgentProd.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterNbClientsProducteur(AID a, String s)
	{
		if(labelsNbClientsProd.containsKey(a))
			labelsNbClientsProd.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsNbClientsProd.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterPrixTransporteur(AID a, String s)
	{
		if(labelsPrixTrans.containsKey(a))
			labelsPrixTrans.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsPrixTrans.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterArgentTransporteur(AID a, String s)
	{
		if(labelsArgentTrans.containsKey(a))
			labelsArgentTrans.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsArgentTrans.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterCapaciteTransporteur(AID a, String s)
	{
		if(labelsCapaciteTrans.containsKey(a))
			labelsCapaciteTrans.get(a).setText(s);
		else
		{
			JLabel l = new JLabel(s);
			labelsCapaciteTrans.put(a, l);
			getContentPane().add(l);
		}
		
		pack();
		revalidate();
		repaint();
	}
	
}
