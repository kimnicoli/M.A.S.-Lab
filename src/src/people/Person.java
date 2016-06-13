package src.people;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.SortedSet;
import src.mas_lab.Main;

public class Person extends Agent {

	TreeMap<AID, Double> restMap;
	double maxValue; //Consideriamo una scala di valutazione inversa. Perché la TreeMap ordina dal più basso al più alto
	double boldness;
	
	Hashtable<AID, Double> worldTrust;
	Vector<AID> friends;
	
	Hashtable<AID, Hashtable<AID, Double>> opinions;
	
	boolean fromFile;
	
	protected void setup() {
		
		restMap = new TreeMap();
		maxValue = Main.EvaluateRange;//Main.MinimumEvaluation + Math.random() * (Main.EvaluateRange - Main.MinimumEvaluation);//
		boldness = Main.MinBoldness + Math.random() * (Main.MaxBoldness - Main.MinBoldness);
		worldTrust = new Hashtable<AID, Double>();
		friends = new Vector<AID>();
		opinions = new Hashtable<AID, Hashtable<AID,Double>>();
		
		Object[] args = getArguments();
		DFAgentDescription[] results = (DFAgentDescription[]) args[1];
		
		System.out.println(getLocalName()+ ": init on " + results.length);
		for (DFAgentDescription result : results){
			restMap.put(result.getName(), 0.5*Main.EvaluateRange);//Math.random() * Main.EvaluateRange);
			opinions.put(result.getName(), new Hashtable<AID, Double>());
		}
		
		DFAgentDescription mydfd = new DFAgentDescription();
		ServiceDescription mysd = new ServiceDescription();
		mysd.setType("Person");
		mysd.setName(this.getName());
		mydfd.addServices(mysd);
		try {
			DFService.register(this, mydfd);	
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		fromFile = false;
		if(args[0] != null){
			Decode((JSONObject)args[0]);
			fromFile = true;
		}
		
		System.out.println("Hi from " + getLocalName() + "! I prefer " + maxValue);
		
		addBehaviour(new PersonReceiver(this));
	}
	
	public JSONObject Encode() {
		JSONObject extObj = new JSONObject();
		
		extObj.put("maxValue", maxValue);
		extObj.put("boldness", boldness);
		
		Vector<String> friends = new Vector<String>();		
		for(AID address : this.friends){
			friends.add(address.getLocalName());
		}
		extObj.put("friends", friends);
		
		Hashtable<String, Double> localMap = new Hashtable<String, Double>();
		for(AID address : this.worldTrust.keySet()){
			localMap.put(address.getLocalName(), this.worldTrust.get(address));
		}
		JSONObject wTobj = new JSONObject();
		wTobj.putAll(localMap);
		extObj.put("worldTrust", wTobj);
		
		localMap.clear();
		for(AID address : this.restMap.keySet()){
			localMap.put(address.getLocalName(), this.restMap.get(address));
		}
		JSONObject rMObj = new JSONObject();
		rMObj.putAll(localMap);
		extObj.put("restMap", rMObj);
		
		return extObj;
	}
	
	void Decode(JSONObject obj) {
		System.out.println("Loading json");
		if(obj != null){
			System.err.println("Loading json");
			maxValue = (Double)obj.get("maxValue");
			boldness = (Double)obj.get("boldness");
			
			String address = null;
			try {
				address = "@" + InetAddress.getLocalHost().getHostAddress() + ":1099/JADE";
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			this.friends.clear();
			JSONArray friends = (JSONArray)obj.get("friends");
			for(int i = 0; i < friends.size(); i++){
				this.friends.add(new AID(((String)friends.get(i)) + address, true));
			}
			
			this.worldTrust.clear();
			JSONObject wTobj = (JSONObject)obj.get("worldTrust");
			for(Object key : wTobj.keySet()){
				this.worldTrust.put(new AID((String)key + address, true), (Double)wTobj.get(key));
			}
			
			this.restMap.clear();
			JSONObject rMObj = (JSONObject)obj.get("restMap");
			for(Object key : rMObj.keySet()){
				this.restMap.put(new AID((String)key + address, true), (Double)rMObj.get(key));
			}
		}
	}
}
