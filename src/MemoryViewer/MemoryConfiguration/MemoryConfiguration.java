package MemoryViewer.MemoryConfiguration;
import java.util.ArrayList;

public class MemoryConfiguration
{
	// Constants
	public static final long DEFAULT_TOTALSIZE = -1;
	
	// Attributes
	private long totalSize;
	private ArrayList<Space> spaces;
	
	// Attributes - protection
	private boolean isSetTotalSize = false;
	
	// Constructors
	public MemoryConfiguration (long totalSize, ArrayList<Space> spaces) {
		setTotalSize(totalSize);
		this.spaces = spaces;
	}
	
	public MemoryConfiguration () {
		this.totalSize = DEFAULT_TOTALSIZE;
		this.spaces = new ArrayList<Space>();
	}
	
	// Getters
	public long getTotalSize () {
		return this.totalSize;
	}
	
	public int getNumberOfSpaces () {
		return this.spaces.size();
	}
	
	public Space getSpace (int space) {
		return this.spaces.get(space);
	}
	
	// Setters
	public void setTotalSize (long totalSize) {
		if (!isSetTotalSize) {
			this.totalSize = totalSize;
			isSetTotalSize = true;
		}
		else
			System.err.println("The total size of the memory configuration can only be set once!");
	}
	
	public void addSpace (Space space) {
		for (int i = 0; i < spaces.size(); i++) {
			if (spaces.get(i).getOrigin() > space.getOrigin()) {
				spaces.add(i, space);
				return;
			}
		}
		spaces.add(space);
	}
}
