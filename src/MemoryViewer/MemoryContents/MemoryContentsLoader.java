package MemoryViewer.MemoryContents;

public class MemoryContentsLoader
{
	// Basic patterns
	private static final String fillContent = "*fill*";
	
	// Attributes
	private static MemoryContents memoryContents;
	
	// Helper functions - Data
	private static int getFill (String fill) {
		// e.g.: "ff" returns 255
		if (fill == null)
			return FillContent.DEFAULT_FILL;
		else if (fill.isEmpty())
			return FillContent.DEFAULT_FILL;
		else
			return Integer.parseInt(fill, 16);
	}
	
	// Helper functions - Loader
	public static void clear () {
		memoryContents = new MemoryContents();
	}
	
	public static void addIdentifierContent (long address, long size, String identifier, String file) {
		memoryContents.addIdentifierContent(new IdentifierContent(address, size, identifier, file));
	}
	
	public static void addDataContent (long address, long size, String data) {
		memoryContents.addDataContent(new DataContent(address, size, data));
	}
	
	public static void addFillContent (long address, long size, int fill) {
		memoryContents.addFillContent(new FillContent(address, size, fill));
	}
	
	public static void addOtherContent (long address, long size, String info, String subRegionName) {
		
		// Determine type of content (identifier, data or fill)
		if ((info.contains("/") || info.contains("\\"))) {
			addIdentifierContent(address, size, IdentifierContent.UNKNOWN_IDENTIFIER + subRegionName, info);
		} else if (subRegionName.equals(fillContent)) {
			addFillContent(address, size, getFill(info));
		} else {
			addDataContent(address, size, info);
		}
	}
	
	// Wrapper - Loader
	public static MemoryContents load () {
		return memoryContents;
	}
}
