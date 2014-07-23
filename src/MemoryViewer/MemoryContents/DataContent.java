package MemoryViewer.MemoryContents;

public class DataContent extends MemoryContent
{
	// Constants
	public static final String DEFAULT_DATA = "?";
	
	// Attributes
	private String data;
	
	// Attributes - protection
	private boolean isSetData = false;
	
	// Constructors
	public DataContent (long address, long size, String data) {
		super(address, size);
		setData(data);
	}
	
	public DataContent () {
		super();
		this.data = DEFAULT_DATA;
	}
	
	// Getters
	public String getData () {
		return this.data;
	}
	
	// Setters
	public void setData (String data) {
		if (!isSetData) {
			this.data = data;
			isSetData = true;
		}
		else
			System.err.println("The data of the data content can only be set once!");
	}
}
