package src.restaurants;

import java.io.IOException;

import org.json.simple.JSONObject;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import src.mas_lab.Main;

public class Restaurant extends Agent {

	double quality;
	int capacity;
	int fullness;
	
	boolean fromFile;
	
	AID global;
	
	protected void setup() {
		quality = Math.random()* Main.EvaluateRange;
		capacity = 10;//(int)(10 + Math.random() * (Main.MaxCapacity - 10));
		fullness = 0;
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Restaurant");
		sd.setName(getLocalName());
		sd.addProperties(new Property("quality", quality));
                sd.addProperties(new Property("capacity", capacity));
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		System.out.println("Hello from " + getLocalName() + "! My quality is " + quality + "and my capacity is " + capacity);
	
		fromFile = false;
		Object[] args = getArguments();
		if(args[0] != null){
			Decode((JSONObject)args[0]);
			fromFile = true;
		}
				
		global = getGlobal();
		
		ACLMessage JSONmsg = new ACLMessage(ACLMessage.CONFIRM);
		JSONmsg.addReceiver(global);
		try {
			JSONmsg.setContentObject(Encode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		send(JSONmsg);
		
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
	
	public JSONObject Encode() {
		JSONObject extObj = new JSONObject();
		
		extObj.put("quality", quality);
		extObj.put("capacity", capacity);
		
		return extObj;
	}
	
	void Decode(JSONObject obj) {
		if(obj != null){
			quality = (Double)obj.get("quality");
			capacity = ((Long)obj.get("capacity")).intValue();
		}
	}
	
	AID getGlobal(){
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Global");
		dfd.addServices(sd);
		try {
			AID gAID = DFService.search(this, dfd)[0].getName();
			return gAID;
		} catch (FIPAException e){
			e.printStackTrace();
			return null;
		}
	}
}
