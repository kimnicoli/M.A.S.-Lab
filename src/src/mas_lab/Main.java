package src.mas_lab;

public class Main {
	
	public static double EvaluateRange = 5.0;
	public static int MaxCapacity = 20;
	public static double MinBoldness = 0.0;
	public static double MaxBoldness = 1.0;
	public static int MaxTurns = 5;
	
	public static double RandomEvaluation = 1.0;
	public static double RandomFuzzyEvaluation = 0.1;
	
	public static double RandomChoose = 	0.1;
	public static double RandomGoToFirst = 0.1;
	
			
	public static int MaxFriends = 2;

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		launcher.InitJade(2, 5, "restaurants001.json", "people001.json");
	}

}
