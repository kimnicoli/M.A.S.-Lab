package src.mas_lab;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Vector;

import org.json.simple.JSONObject;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

import src.people.Chose;
import src.people.Evaluate;
import src.people.Person;
import src.people.Search;

public class GlobalReceiver extends CyclicBehaviour {

	Vector<AID> allRestaurants;
	Vector<AID> allPeople;
	Vector<AID> currentRestaurant;
	Vector<AID> currentPeople;
	DFAgentDescription[] allRArray;
	int PeopleReceived;
	int RestaurantReceived;
	JSONObject peopleSetup;
	JSONObject restaurantsSetup;

	static boolean printedPeople;
	static boolean printedRestaurants;

	public GlobalReceiver(Agent a, DFAgentDescription[] allPeople, DFAgentDescription[] allRestaurants) {
		super(a);
		this.myAgent = a;
		this.allRestaurants = new Vector<AID>();
		this.allPeople = new Vector<AID>();

		allRArray = allRestaurants;

		for (DFAgentDescription dfd : allPeople)
			if (dfd != null)
				this.allPeople.add(dfd.getName());
		for (DFAgentDescription dfd : allRestaurants)
			if (dfd != null)
				this.allRestaurants.add(dfd.getName());

		peopleSetup = new JSONObject();
		restaurantsSetup = new JSONObject();
		printedPeople = false;
		printedRestaurants = false;

	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			block();
		} else {
			switch (msg.getPerformative()) {
			case (ACLMessage.INFORM): {
				if (currentPeople.contains(msg.getSender())) {
					currentPeople.remove(msg.getSender());
					PeopleReceived++;

					if (PeopleReceived == allPeople.size()) {
						if (((Global) myAgent).turn < Main.MaxTurns) {
							try {
								myAgent.addBehaviour(new Log(allRArray, msg.getSender(), 
										false, (Object[]) msg.getContentObject()));
							} catch (Exception e) {
								e.printStackTrace();
							}
							reset();
						} else {
							try {
								myAgent.addBehaviour(new Log(allRArray, msg.getSender(), 
										true, (Object[]) msg.getContentObject()));
							} catch (UnreadableException e) {
								e.printStackTrace();
							}
						}
					} else {
						try {
							myAgent.addBehaviour(new Log(allRArray, msg.getSender(), 
									false, (Object[]) msg.getContentObject()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
				break;
			}

			case (ACLMessage.CONFIRM): {
				if (!msg.getSender().equals(myAgent.getAID())) {
					String sender = msg.getSender().getLocalName();
					Object obj = null;
					try {
						obj = msg.getContentObject();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					if (sender.split(" ")[0].equals("Person")) {
						peopleSetup.put(sender, obj);
						System.out.println("People JSON at: " + peopleSetup.size()*100/((Global)myAgent).nPeople
								+ "%");
					} else if (sender.split(" ")[0].equals("Restaurant")) {
						restaurantsSetup.put(sender, obj);
					}

					if (peopleSetup.size() == ((Global) myAgent).nPeople & !printedPeople) {
						printedPeople = true;
						String fname = String.valueOf("people.json");
						FileWriter fstream = null;
						try {
							fstream = new FileWriter(fname);
						} catch (IOException e) {
							e.printStackTrace();
						}
						BufferedWriter out = new BufferedWriter(fstream);
						try {
							out.write(peopleSetup.toJSONString());
							out.flush();
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						reset();
					}
					
					if (restaurantsSetup.size() == ((Global) myAgent).nRestaurants & !printedRestaurants) {
						printedRestaurants = true;
						String fname = String.valueOf("restaurants.json");
						FileWriter fstream = null;
						try {
							fstream = new FileWriter(fname);
						} catch (IOException e) {
							e.printStackTrace();
						}
						BufferedWriter out = new BufferedWriter(fstream);
						try {
							out.write(restaurantsSetup.toJSONString());
							out.flush();
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
			
			case (ACLMessage.PROPAGATE):{
				if (currentRestaurant.contains(msg.getSender())) {
					currentRestaurant.remove(msg.getSender());
					RestaurantReceived++;
					System.out.println("Restaurant reset at: "
										+ RestaurantReceived*100/allRestaurants.size() + "%");

					if (RestaurantReceived == allRestaurants.size())
						myAgent.addBehaviour(new NewDayBCast(myAgent));
				}
				break;
			}

			default:
				block();
			}
		}
	}

	public void reset() {
		currentRestaurant = (Vector<AID>) allRestaurants.clone();
		currentPeople = (Vector<AID>) allPeople.clone();
		System.out.println("Restaurants: " + allRestaurants.size());
		System.out.println("Restaurants: " + currentRestaurant.size());
		PeopleReceived = 0;
		RestaurantReceived = 0;
		myAgent.addBehaviour(new NewDayBCast(myAgent, true));
	}
}
