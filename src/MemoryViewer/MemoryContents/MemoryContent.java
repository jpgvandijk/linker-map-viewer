package MemoryViewer.MemoryContents;

public class MemoryContent
{
	// Constants
	public static final long DEFAULT_ADDRESS = -1;
	public static final long DEFAULT_SIZE = -1;
	
	// Attributes
	private long address;
	private long size;
	
	// Attributes - protection
	private boolean isSetAddress = false;
	private boolean isSetSize = false;
	
	// Constructors
	public MemoryContent (long address, long size) {
		setAddress(address);
		setSize(size);
	}
	
	public MemoryContent () {
		this.address = DEFAULT_ADDRESS;
		this.size = DEFAULT_SIZE;
	}
	
	// Getters
	public long getAddress () {
		return this.address;
	}
	
	public long getSize () {
		return this.size;
	}
	
	// Setters
	public void setAddress (long address) {
		if (!isSetAddress) {
			this.address = address;
			isSetAddress = true;
		}
		else
			System.err.println("The address of the memory content can only be set once!");
	}
	
	public void setSize (long size) {
		if (!isSetSize) {
			this.size = size;
			isSetSize = true;
		}
		else
			System.err.println("The size of the memory content can only be set once!");
	}
}
