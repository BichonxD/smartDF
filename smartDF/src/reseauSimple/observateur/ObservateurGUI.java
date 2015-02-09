package reseauSimple.observateur;

import java.awt.GridLayout;
import java.util.ArrayList;
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
	private HashMap<AID, ArrayList<JLabel>> labelsProd = new HashMap<AID, ArrayList<JLabel>>();
	private HashMap<AID, ArrayList<JLabel>> labelsTrans = new HashMap<AID, ArrayList<JLabel>>();
	private GridLayout grille = new GridLayout(60, 0);
	
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
	
	/**
	 * Créé une nouvelle liste de JLabel pour afficher un producteur.
	 * @param a
	 */
	private void newEltLabelsProd(AID a)
	{
		if(!labelsProd.containsKey(a))
		{
			ArrayList<JLabel> lLabels = new ArrayList<JLabel>();
			
			// On ajoute 4 labels dans l'arraylist
			for(int i = 0; i < 5; i++)
			{
				JLabel l = new JLabel();
				lLabels.add(l);
				getContentPane().add(l);
			}
			labelsProd.put(a, lLabels);
		}
	}
	
	/**
	 * Créé une nouvelle liste de JLabel pour afficher un transporteur.
	 * @param a
	 */
	private void newEltLabelsTrans(AID a)
	{
		if(!labelsTrans.containsKey(a))
		{
			ArrayList<JLabel> lLabels = new ArrayList<JLabel>();
			
			// On ajoute 5 labels dans l'arraylist
			for(int i = 0; i < 5; i++)
			{
				JLabel l = new JLabel();
				lLabels.add(l);
				getContentPane().add(l);
			}
			labelsTrans.put(a, lLabels);
		}
	}
	
	public void ajouterPrixProducteur(AID a, String s)
	{
		newEltLabelsProd(a);
		labelsProd.get(a).get(0).setText(s);
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterArgentProducteur(AID a, String s)
	{
		newEltLabelsProd(a);
		labelsProd.get(a).get(1).setText(s);
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterNbClientsProducteur(AID a, String s)
	{
		newEltLabelsProd(a);
		labelsProd.get(a).get(2).setText(s);
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterNbTransporteursProducteur(AID a, String s)
	{
		newEltLabelsProd(a);
		labelsProd.get(a).get(3).setText(s);
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterProprioTransporteur(AID a, String s)
	{
		newEltLabelsTrans(a);
		labelsTrans.get(a).get(0).setText(s);
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterPrixTransporteur(AID a, String s)
	{
		newEltLabelsTrans(a);
		labelsTrans.get(a).get(1).setText(s);
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterArgentTransporteur(AID a, String s)
	{
		newEltLabelsTrans(a);
		labelsTrans.get(a).get(2).setText(s);
		pack();
		revalidate();
		repaint();
	}
	
	public void ajouterCapaciteTransporteur(AID a, String s)
	{
		newEltLabelsTrans(a);
		labelsTrans.get(a).get(3).setText(s);
		pack();
		revalidate();
		repaint();
	}
	
}
