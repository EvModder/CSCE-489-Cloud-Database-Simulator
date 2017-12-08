import java.util.HashMap;
import java.util.Vector;

public class Rack{
	String name;
	long storage;
	final long TOTAL_CAPACITY;
	Vector<Machine> machines;
	boolean enabled = true;
	
	// System to query for and update available images in storage
	HashMap<String, Image> images;

	Rack(String name, long storage){
		this.name = name;
		TOTAL_CAPACITY = this.storage = storage;
		machines = new Vector<Machine>();
		images = new HashMap<String, Image>();
	}
}