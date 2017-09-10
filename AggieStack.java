import java.util.ArrayList;
import java.util.Scanner;

public class AggieStack{
	private static AggieStack instance;
	public static AggieStack getInstance(){return instance;}
	public static void main(String... args){new AggieStack();}
	private ArrayList<Machine> machines = new ArrayList<Machine>();
	private ArrayList<Image> images = new ArrayList<Image>();
	private ArrayList<Flavor> flavors = new ArrayList<Flavor>();

	public AggieStack(){
		instance = this;
		new CommandAdmin();
		new CommandConfig();
		new CommandShow();

		Scanner scanner = new Scanner(System.in);

		while(scanner.hasNext()){
			String input = scanner.nextLine();
			if(input.toLowerCase().startsWith("aggiestack ")){
				input = input.substring(11);
			}
			if(input.equals("exit")) break;
			
			String cmdName = input;
			String[] args = new String[]{};

			int i = input.indexOf(" ");
			if(i != -1){
				cmdName = input.substring(0, i);
				args = input.substring(i).split(" ");
			}

			Command cmd = Command.getCommand(cmdName);
			if(cmd != null){
				boolean result = cmd.runCommand(args);
				
				String log = cmdName+" >> "+(result ? "SUCCESS" : "FAILURE")+'\n';
				FileIO.appendString("aggiestack-log.txt", log);
			}
			else{
				String log = cmdName+" >> INVALID\n";
				FileIO.appendString("aggiestack-log.txt", log);
			}
		}

		scanner.close();
	}
	
	void addMachine(Machine machine){
		machines.add(machine);
	}
	void addImage(Image image){
		images.add(image);
	}
	void addFlavor(Flavor flavor){
		flavors.add(flavor);
	}
}