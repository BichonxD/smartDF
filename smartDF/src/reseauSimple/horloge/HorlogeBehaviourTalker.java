package reseauSimple.horloge;

import jade.core.behaviours.OneShotBehaviour;

public class HorlogeBehaviourTalker extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void action()
	{
		/* TODO
		 * Envoie un message de début de tour à tous les agents.
		 * Une fois que les agents ont finis leur tour elle envoie un message pour signaler que c'est le moment de facturer.
		 * Les Producteurs signalent qu'ils ont bien facturer à l'horloge.
		 * L'horloge annonce le début du tour suivant.
		 * Si un utilisateur n'a pas été facturé il le signale à l'EtatAgent qui sermonne le Producteur en question.
		 */
		
	}
}
