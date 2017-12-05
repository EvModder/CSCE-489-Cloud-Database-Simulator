import java.util.Vector;

public class Machine{
	String name, ip;
	Rack rack;
	long memory, disks, vcpus;// available
	final long TOTAL_MEMORY, TOTAL_DISKS, TOTAL_VCPUS;// maximum supported
	Vector<Instance> instances;

	Machine(String name, Rack rack, String ip, long mem, long disks, long vcpus){
		this.name = name;
		this.rack = rack;
		this.ip = ip;
		TOTAL_MEMORY = memory = mem; // memory = amount of RAM in GB
		TOTAL_DISKS = this.disks = disks;
		TOTAL_VCPUS = this.vcpus = vcpus;
		instances = new Vector<Instance>();
	}

	public boolean canHost(Flavor flavor){
		return rack.enabled && memory >= flavor.ram && disks >= flavor.disks && vcpus >= flavor.vcpus;
	}
}