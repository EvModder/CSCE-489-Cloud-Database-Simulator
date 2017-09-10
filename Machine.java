
public class Machine{
	String name, ip;
	long memory, disks, vcpus;
	
	Machine(String name, String ip, long mem, long disks, long vcpus){
		this.name = name;
		this.ip = ip;
		memory = mem;//memory = amount of RAM in GB
		this.disks = disks;
		this.vcpus = vcpus;
	}
}
