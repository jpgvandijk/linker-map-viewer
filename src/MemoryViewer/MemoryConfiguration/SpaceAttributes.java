package MemoryViewer.MemoryConfiguration;

public class SpaceAttributes
{
	// Constants
	public static final boolean DEFAULT_ERASABLE = false;
	public static final boolean DEFAULT_READABLE = false;
	public static final boolean DEFAULT_WRITABLE = false;
	
	// Attributes
	private boolean erasable;
	private boolean readable;
	private boolean writable;
	
	// Attributes - protection
	private boolean isSetErasable = false;
	private boolean isSetReadable = false;
	private boolean isSetWritable = false;
	
	// Constructors
	public SpaceAttributes (boolean erasable, boolean readable, boolean writable) {
		setErasable(erasable);
		setReadable(readable);
		setWritable(writable);
	}
	
	public SpaceAttributes () {
		this.erasable = DEFAULT_ERASABLE;
		this.readable = DEFAULT_READABLE;
		this.writable = DEFAULT_WRITABLE;
	}
	
	// Getters
	public boolean isErasable () {
		return this.erasable;
	}
	
	public boolean isReadable () {
		return this.readable;
	}
	
	public boolean isWritable () {
		return this.writable;
	}
	
	// Setters
	public void setErasable (boolean erasable) {
		if (!isSetErasable) {
			this.erasable = erasable;
			isSetErasable = true;
		}
		else
			System.err.println("The attribute erasable of the space attributes can only be set once!");
	}
	
	public void setReadable (boolean readable) {
		if (!isSetReadable) {
			this.readable = readable;
			isSetReadable = true;
		}
		else
			System.err.println("The attribute readable of the space attributes can only be set once!");
	}
	
	public void setWritable (boolean writable) {
		if (!isSetWritable) {
			this.writable = writable;
			isSetWritable = true;
		}
		else
			System.err.println("The attribute writable of the space attributes can only be set once!");
	}
}
