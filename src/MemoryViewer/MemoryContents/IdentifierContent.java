package MemoryViewer.MemoryContents;

public class IdentifierContent extends MemoryContent
{
	// Constants
	public static final String UNKNOWN_IDENTIFIER = ">? ";
	public static final String DEFAULT_IDENTIFIER = "?";
	public static final String DEFAULT_FILE = "?";
	
	// Attributes
	private String identifier;
	private String file;
	
	// Attributes - protection
	private boolean isSetIdentifier = false;
	private boolean isSetFile = false;
	
	// Constructors
	public IdentifierContent (long address, long size, String identifier, String file) {
		super(address, size);
		setIdentifier(identifier);
		setFile(file);
	}
	
	public IdentifierContent () {
		super();
		this.identifier = DEFAULT_IDENTIFIER;
		this.file = DEFAULT_FILE;
	}
	
	// Getters
	public String getIdentifier () {
		return this.identifier;
	}
	
	public String getFile () {
		return this.file;
	}
	
	// Setters
	public void setIdentifier (String identifier) {
		if (!isSetIdentifier) {
			this.identifier = identifier;
			isSetIdentifier = true;
		}
		else
			System.err.println("The identifier of the identifier content can only be set once!");
	}
	
	public void setFile (String file) {
		if (!isSetFile) {
			this.file = file;
			isSetFile = true;
		}
		else
			System.err.println("The file of the identifier content can only be set once!");
	}
}
