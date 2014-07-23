package MemoryViewer.MemoryConfiguration;

public class Space
{
	// Constants
	public static final String DEFAULT_NAME = "";
	public static final long DEFAULT_ORIGIN = -1;
	public static final long DEFAULT_LENGTH = -1;
	public static final SpaceAttributes DEFAULT_ATTRIBUTES = null; 
	
	// Attributes
	private String name;
	private long origin;
	private long length;
	private SpaceAttributes attributes;
	
	// Attributes - protection
	private boolean isSetName = false;
	private boolean isSetOrigin = false;
	private boolean isSetLength = false;
	private boolean isSetAttributes = false;
	
	// Constructors
	public Space (String name, long origin, long length, SpaceAttributes attributes) {
		setName(name);
		setOrigin(origin);
		setLength(length);
		setAttributes(attributes);
	}
	
	public Space () {
		this.name = DEFAULT_NAME;
		this.origin = DEFAULT_ORIGIN;
		this.length = DEFAULT_LENGTH;
		this.attributes = DEFAULT_ATTRIBUTES;
	}
	
	// Getters
	public String getName () {
		return this.name;
	}
	
	public long getOrigin () {
		return this.origin;
	}
	
	public long getLength() {
		return this.length;
	}
	
	public SpaceAttributes getAttributes () {
		return this.attributes;
	}
	
	// Setters
	public void setName (String name) {
		if (!isSetName) {
			this.name = name;
			isSetName = true;
		}
		else
			System.err.println("The name of the space can only be set once!");
	}
	
	public void setOrigin (long origin) {
		if (!isSetOrigin) {
			this.origin = origin;
			isSetOrigin = true;
		}
		else
			System.err.println("The origin of the space can only be set once!");
	}

	public void setLength (long length) {
		if (!isSetLength) {
			this.length = length;
			isSetLength = true;
		}
		else
			System.err.println("The length of the space can only be set once!");
	}
	
	public void setAttributes (SpaceAttributes attributes) {
		if (!isSetAttributes) {
			this.attributes = attributes;
			isSetAttributes = true;
		}
		else
			System.err.println("The attributes of the space can only be set once!");
	}
}
