package src.mas_lab;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Launcher {

	AgentController ac;
	jade.core.Runtime runtime;
	ContainerController cc;

	public void InitJade(int nRestaurants, int nPeople, String restaurantSetup, String peopleSetup) {
		runtime = jade.core.Runtime.instance();
		
		Profile p = new ProfileImpl();
		p.setParameter(Profile.NO_MTP, "true");
		
		ContainerController cc = runtime.createMainContainer(p);
		
		Object[] args = new Object[4];
		args[0] = nRestaurants;
		args[1] = nPeople;
		args[2] = null;
		
		if(peopleSetup != null){
			JSONParser parser = new JSONParser();
			BufferedReader br = null;
			JSONObject peopleJSON = null;
			try {
				br = new BufferedReader(new FileReader(peopleSetup));
				
				peopleJSON = (JSONObject)parser.parse(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			args[3] = peopleJSON;
		} else
			args[3] = null;
		
		if(restaurantSetup != null){
			JSONParser parser = new JSONParser();
			BufferedReader br = null;
			JSONObject restJSON = null;
			try {
				br = new BufferedReader(new FileReader(restaurantSetup));
				
				restJSON = (JSONObject)parser.parse(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			args[2] = restJSON;
		} else
			args[2] = null;
		
		try {
			ac = cc.createNewAgent("Global Agent", "src.mas_lab.Global", args);
			ac.start();
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
		
	}
}
