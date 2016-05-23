package restaurants;

import com.sun.glass.ui.View.Capability;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas_lab.Main;

public class Restaurant extends Agent {

	double quality;
	int capacity;
	
	protected void setup() {
		quality = Math.random()* Main.EvaluateRange;
		capacity = (int)(Math.random() * Main.MaxCapacity);
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Restaurant");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		System.out.println("Hello from " + getLocalName() + "! My quality is " + quality + "and my capacity is " + capacity);
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
}
