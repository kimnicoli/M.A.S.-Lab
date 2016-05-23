import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Launcher {

	public Launcher() {
		
	}

	public void InitJade(int nRestaurants, int nPeople) {
		AgentController ac;
		jade.core.Runtime runtime = jade.core.Runtime.instance();
		
		Profile p = new ProfileImpl();
		p.setParameter(Profile.NO_MTP, "true");
		
		ContainerController cc = runtime.createMainContainer(p);

		for(int i = 0; i < nRestaurants; i++) {
			try {
				ac = cc.createNewAgent("Restaurant " + i, "restaurants.Restaurant", null);
				ac.start();
			} catch(StaleProxyException e) {
				System.out.println(e.getMessage());
			}
		}
		for(int i = 0; i < nPeople; i++) {
			try {
				ac = cc.createNewAgent("Person " + i, "people.Person", null);
				ac.start();
			} catch(StaleProxyException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	public void QuitJade() {
		
	}
}
