class CommandHelp extends Command{
	@Override public boolean runCommand(String... args){
		if(args.length == 1){
			Command cmd = Command.getCommand(args[0]);
			if(cmd != null) {
				System.out.println("Usage for "+cmd.name+" : "+cmd.usage);
			}
		}
		else{
			for(Command cmd : Command.cmds.values()){
//				String.format( "%5s of %-8s", rank, suit );
				System.out.format("Usage for %-7s: %s\n", cmd.name, cmd.usage);
			}
		}
		return true;
	}
}