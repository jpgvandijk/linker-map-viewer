package MemoryViewer.MemoryMap;

public class RegionData
{
	// Constants
	public static final long DEFAULT_ADDRESS = -1;
	public static final long DEFAULT_SIZE = -1;
	public static final String DEFAULT_INFO = "";
	
	// Attributes
	private long address;
	private long size;
	private String info;
	
	// Attributes - protection
	private boolean isSetAddress = false;
	private boolean isSetSize = false;
	private boolean isSetInfo = false;
	
	// Constructors
	public RegionData (long address, long size, String info) {
		setAddress(address);
		setSize(size);
		setInfo(info);
	}
	
	public RegionData () {
		this.address = DEFAULT_ADDRESS;
		this.size = DEFAULT_SIZE;
		this.info = DEFAULT_INFO;
	}
	
	// Getters
	public long getAddress () {
		return this.address;
	}
	
	public long getSize () {
		return this.size;
	}
	
	public String getInfo () {
		return this.info;
	}
	
	// Setters
	public void setAddress (long address) {
		if (!isSetAddress) {
			this.address = address;
			isSetAddress = true;
		}
		else
			System.err.println("The address of the region data can only be set once!");
	}
	
	public void setSize (long size) {
		if (!isSetSize) {
			this.size = size;
			isSetSize = true;
		}
		else
			System.err.println("The size of the region data can only be set once!");
	}
	
	public void setInfo (String info) {
		if (!isSetInfo) {
			this.info = info;
			isSetInfo = true;
		}
		else
			System.err.println("The info of the region data can only be set once!");
	}	
}
