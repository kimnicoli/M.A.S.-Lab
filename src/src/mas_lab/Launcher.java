package src.mas_lab;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Launcher {

	AgentController ac;
	jade.core.Runtime runtime;
	ContainerController cc;

	public void InitJade(int nRestaurants, int nPeople) {
		runtime = jade.core.Runtime.instance();
		
		Profile p = new ProfileImpl();
		p.setParameter(Profile.NO_MTP, "true");
		
		ContainerController cc = runtime.createMainContainer(p);
		
		Object[] args = new Object[2];
		args[0] = nRestaurants;
		args[1] = nPeople;
		
		try {
			ac = cc.createNewAgent("Global Agent", "src.mas_lab.Global", args);
			ac.start();
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
		
	}
}
