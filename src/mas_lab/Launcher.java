package mas_lab;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Launcher {

	AgentController ac;
	jade.core.Runtime runtime;
	ContainerController cc;
	
	int nRestaurants;
	int nPeople;
	
	private static Launcher _instance_;
	
	protected Launcher() {}
	
	public static Launcher instance() {
		if(_instance_ == null)
			_instance_ = new Launcher();
		return _instance_;
	}

	public void InitJade(int nRestaurants, int nPeople) {
		this.nPeople = nPeople;
		this.nRestaurants = nRestaurants;
		runtime = jade.core.Runtime.instance();
		
		Profile p = new ProfileImpl();
		p.setParameter(Profile.NO_MTP, "true");
		
		ContainerController cc = runtime.createMainContainer(p);

		for(int i = 0; i < nRestaurants; i++) {
			try {
				ac = cc.createNewAgent("Restaurant " + i, "restaurants.Restaurant", null);
				ac.start();
			} catch(StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
		try {
			ac = cc.createNewAgent("Global Agent", "mas_lab.Global", null);
			ac.start();
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getnRestaurants() {
		return nRestaurants;
	}

	public int getnPeople() {
		return nPeople;
	}
}
