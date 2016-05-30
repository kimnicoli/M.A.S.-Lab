package restaurants;

import com.sun.glass.ui.View.Capability;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas_lab.Main;

public class Restaurant extends Agent {

	double quality;
	int capacity;
	int fullness;
	
	protected void setup() {
		quality = Math.random()* Main.EvaluateRange;
		capacity = (int)(Math.random() * Main.MaxCapacity);
		fullness = 0;
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Restaurant");
		sd.setName(getLocalName());
		sd.addProperties(new Property("quality", quality));
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		System.out.println("Hello from " + getLocalName() + "! My quality is " + quality + "and my capacity is " + capacity);
	
		addBehaviour(new RestaurantReceiver());
	}

	/**
	 * @return the quality
	 */
	public double getQuality() {
		return quality;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	
	boolean isFree () {
		if(fullness < capacity)
			return true;
		return false;
	}
}
