public class CommandAdmin extends Command{
	private AggieStack stack;
	
	CommandAdmin(){
		stack = AggieStack.getInstance();
	}

	@Override public boolean runCommand(String... args){
		if(args.length < 2){
			System.err.println("Invalid number of arguments!");
			return false;
		}
		args[0] = args[0].toLowerCase();
		if(args[0].equals("can_host")){
			if(args.length != 3){
				System.err.println("Invalid number of arguments!");
				return false;
			}
			String machineName = args[1], flavorName = args[2];
			
		}
		else if(args[0].equals("show")){
			args[1] = args[1].toLowerCase();
			if(args[1].equals("hardware")){
				showAvailableHardware();
			}
			else{
				System.err.println("Invalid 'show' arguments");
				return false;
			}
		}
		else{
			System.err.println("Invalid arguments");
			return false;
		}
		return true;
	}
	
	void showAvailableHardware(){
		
	}
}