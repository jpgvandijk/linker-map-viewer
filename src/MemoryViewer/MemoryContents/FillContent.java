package MemoryViewer.MemoryContents;

public class FillContent extends MemoryContent
{
	// Constants
	public static int DEFAULT_FILL = -1;
	
	// Attributes
	private int fill;
	
	// Attributes - protection
	private boolean isSetFill = false;
	
	// Constructors
	public FillContent (long address, long size, int fill) {
		super(address, size);
		setFill(fill);
	}
	
	public FillContent () {
		super();
		this.fill = DEFAULT_FILL;
	}
	
	// Getters
	public int getFill () {
		return this.fill;
	}
	
	// Setters
	public void setFill (int fill) {
		if (!isSetFill) {
			this.fill = fill;
			isSetFill = true;
		}
		else
			System.err.println("The fill of the fill content can only be set once!");
	}
}
