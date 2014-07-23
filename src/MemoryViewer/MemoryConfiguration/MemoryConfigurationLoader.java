package MemoryViewer.MemoryConfiguration;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemoryConfigurationLoader
{	
	// Basic patterns
	private static final String startConfiguration = "Memory Configuration";
	private static final String endConfiguration = "Linker script and memory map";
	private static final String defaultSpace = "*default*";
	private static final String regexOfName = "(\\S+)";
	private static final String regexOfOrigin = "(0x[a-fA-F0-9]{8})";
	private static final String regexOfLength = "(0x[a-fA-F0-9]+)";
	private static final String regexOfAttributes = "(x?)(r?)(w?)";
	
	// Compiled patterns
	private static final Pattern patternOfSpace = Pattern.compile(regexOfName + "\\s+" + regexOfOrigin + "\\s+" + regexOfLength + "\\s*" + regexOfAttributes);
	
	// Helper function - Data
	private static long getOrigin (String origin) {
		if (origin == null)
			return Space.DEFAULT_ORIGIN;
		else
			return Long.parseLong(origin.substring(2, origin.length()), 16);
	}
	
	private static long getLength (String length) {
		if (length == null)
			return Space.DEFAULT_LENGTH;
		else
			return Long.parseLong(length.substring(2, length.length()), 16);
	}
	
	// Helper functions - Reader
	private static BufferedReader reader;
	private static String readerLine;
	
	private static String getNextLine ()
	{
		// Read next line (skip blank lines) and store it AND return it
		try
		{
			while (readerLine != null)
			{	
				readerLine = reader.readLine();
				if (readerLine.length() != 0)
					return readerLine;
			}
		}
		catch (IOException e)
		{
			System.err.println("Error reading next line: " + e.getMessage());
		}
		
		return readerLine;
	}
	
	// Helper functions - Loader
	private static boolean isDefaultSpace (Space space)
	{
		return ((space.getName().equals(defaultSpace)) &&
				(space.getOrigin() == 0) &&
				(space.getLength() > 0) &&
				(!space.getAttributes().isErasable()) &&
				(!space.getAttributes().isReadable()) &&
				(!space.getAttributes().isWritable()));
	}
	
	private static MemoryConfiguration loadConfiguration ()
	{
		MemoryConfiguration todoConfiguration = new MemoryConfiguration();
		
		// Find the start of the memory configuration
		while (!readerLine.startsWith(startConfiguration))
			getNextLine();
		
		// Parse and store the spaces of the memory configuration
		while (!readerLine.startsWith(endConfiguration))
		{
			Matcher matcher = patternOfSpace.matcher(readerLine);
			if (matcher.matches())
			{
				// Create a new space with the name, origin, length and attributes found
				SpaceAttributes todoAttributes = new SpaceAttributes();
				todoAttributes.setErasable(matcher.group(4).length() != 0);
				todoAttributes.setReadable(matcher.group(5).length() != 0);
				todoAttributes.setWritable(matcher.group(6).length() != 0);
				
				Space todoSpace = new Space();
				todoSpace.setName(matcher.group(1));
				todoSpace.setOrigin(getOrigin(matcher.group(2)));
				todoSpace.setLength(getLength(matcher.group(3)));
				todoSpace.setAttributes(todoAttributes);
				
				// Check for the space defining the addressable area
				if (isDefaultSpace(todoSpace))
					todoConfiguration.setTotalSize(todoSpace.getLength());
				else
					todoConfiguration.addSpace(todoSpace);
			}
			getNextLine();
		}
		
		return todoConfiguration;
	}
	
	// Wrapper - Loader
	public static MemoryConfiguration load (BufferedReader bufferedReader)
	{
		// Wrap to other loaders, using one buffered reader
		reader = bufferedReader;
		readerLine = "";
		return loadConfiguration();
	}
}
