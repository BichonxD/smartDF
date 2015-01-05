package reseauSimple;
import jade.core.Agent;

public class ObservateurAgent extends Agent {

  protected void setup() {
  	System.out.println("Hello World! My name is "+getLocalName());
   	// Make this agent terminate
  	doDelete();
  } 
}

// On ne va pas s'intéresser à comprendre chaque ligne du code mais juste comment compiler et lancer l'agent.
