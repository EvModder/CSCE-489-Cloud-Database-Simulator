import java.util.Vector;

public class Rack{
	String name;
	long storage;
	final long TOTAL_CAPACITY;
	Vector<Machine> machines;
	boolean enabled = true;

	Rack(String name, long storage){
		this.name = name;
		TOTAL_CAPACITY = this.storage = storage;
		machines = new Vector<Machine>();
	}
}