package MemoryViewer.MemoryMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MemoryMapLoader
{	
	// Basic patterns
	private static final String startMap = "Linker script and memory map";
	private static final String endMap = "OUTPUT(";
	private static final String regexOfRegion = "(\\.[a-zA-Z_][\\.a-zA-Z0-9_]*)";
	private static final String regexOfSubRegion = " ([C\\.\\*]{1}\\S+( \\.\\S+)?)";
	private static final String regexOfAddress = "(0x[a-fA-F0-9]{8})";
	private static final String regexOfSize = "(0x[a-fA-F0-9]+)";
	private static final String regexOfFill = " FILL mask (0x[a-fA-F0-9]+)";
	
	// Compiled patters
	private static final Pattern patternOfFill = Pattern.compile(regexOfFill);
	private static final Pattern patternFindRegion = Pattern.compile(regexOfRegion + "(.*)");
	private static final Pattern patternEndOfRegion = patternFindRegion;
	private static final Pattern patternFindSubRegion = Pattern.compile(regexOfSubRegion + "(.*)");
	private static final Pattern patternEndOfSubRegion = Pattern.compile(" ?[C\\.\\*]{1}\\S+.*");
	private static final Pattern patternRegionData = Pattern.compile("\\s+" + regexOfAddress + "\\s+" + regexOfSize + "\\s*(.*)?$");
	private static final Pattern patternSubRegionData = Pattern.compile("\\s+" + regexOfAddress + "\\s+" + regexOfSize + "?\\s*(.*)$");
	
	// Helper functions - Data
	private static long getAddress (String address) {
		// e.g.: "0x00000314" returns 788
		if (address == null)
			return RegionData.DEFAULT_ADDRESS;
		else
			return Long.parseLong(address.substring(2, address.length()), 16);
	}
	
	private static long getSize (String size) {
		// e.g.: "0x4" returns 4
		if (size == null)
			return RegionData.DEFAULT_SIZE;
		else
			return Long.parseLong(size.substring(2, size.length()), 16);
	}
	
	private static int getFill (String fill) {
		// e.g.: "0xff" returns 255
		if (fill == null)
			return Region.DEFAULT_FILL;
		else
			return Integer.parseInt(fill.substring(2, fill.length()), 16);
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
	private static MemoryMap loadMap ()
	{
		MemoryMap todoMap = new MemoryMap();
		
		// Find the start of the memory map
		while (!readerLine.startsWith(startMap))
			getNextLine();
		
		Matcher matcher = patternFindRegion.matcher(readerLine);
		while (!matcher.matches() && !readerLine.startsWith(endMap))
		{
			getNextLine();
			matcher = patternFindRegion.matcher(readerLine);
		}
		
		// Parse and store the regions of the memory map
		while (matcher.matches() && !readerLine.startsWith(endMap))
		{
			todoMap.addRegion(loadRegion());
			matcher = patternFindRegion.matcher(readerLine);
		}
		
		return todoMap;
	}
	
	private static Region loadRegion ()
	{
		Region todoRegion = new Region();
		
		// Find name, data, fill and subregions
		Matcher matcher = patternFindRegion.matcher(readerLine);
		if (matcher.matches())
		{
			// Get name and data
			todoRegion.setName(matcher.group(1));
			if (matcher.group(2).length() != 0)
			{
				// Data on the same line
				readerLine = matcher.group(2);
				todoRegion.setData(loadRegionData());
			}
			else
			{
				// Data on the next line
				getNextLine();
				todoRegion.setData(loadRegionData());
			}
			
			// Check next line for fill
			getNextLine();
			matcher = patternOfFill.matcher(readerLine);
			if (matcher.matches())
			{
				todoRegion.setFill(getFill(matcher.group(1)));
				getNextLine();
			}
			
			// Add subregions
			matcher = patternEndOfRegion.matcher(readerLine);
			while (!matcher.matches() && !readerLine.startsWith(endMap))
			{
				todoRegion.addSubRegion(loadSubRegion());
				matcher = patternEndOfRegion.matcher(readerLine);
			}
		}
		
		return todoRegion;
	}
	
	private static SubRegion loadSubRegion ()
	{
		SubRegion todoSubRegion = new SubRegion();
		
		// Find name and data
		Matcher matcher = patternFindSubRegion.matcher(readerLine);
		if (matcher.matches())
		{
			todoSubRegion.setName(matcher.group(1));
			if (matcher.group(3).length() != 0)
			{
				// First data on the same line
				readerLine = matcher.group(3);
				todoSubRegion.addData(loadSubRegionData());
			}
		}
		else
		{
			// Nameless subregion, just only add data
			todoSubRegion.addData(loadSubRegionData());
		}
		
		// More data on next line or no more data for this subregion
		getNextLine();
		matcher = patternEndOfSubRegion.matcher(readerLine);
		while (!matcher.matches() && !readerLine.startsWith(endMap))
		{
			todoSubRegion.addData(loadSubRegionData());
			getNextLine();
			matcher = patternEndOfSubRegion.matcher(readerLine);
		}
		
		return todoSubRegion;
	}
	
	private static RegionData loadRegionData ()
	{
		RegionData todoRegionData = new RegionData();
		
		// Find address, size and info (optional!)
		Matcher matcher = patternRegionData.matcher(readerLine);
		if (matcher.matches())
		{
			todoRegionData.setAddress(getAddress(matcher.group(1)));
			todoRegionData.setSize(getSize(matcher.group(2)));
			if (matcher.groupCount() == 3)
				todoRegionData.setInfo(matcher.group(3));
		}
		
		return todoRegionData;
	}
	
	private static RegionData loadSubRegionData ()
	{
		RegionData todoRegionData = new RegionData();
		
		// Find address, size (optional!) and info
		Matcher matcher = patternSubRegionData.matcher(readerLine);
		if (matcher.matches())
		{
			todoRegionData.setAddress(getAddress(matcher.group(1)));
			todoRegionData.setInfo(matcher.group(matcher.groupCount()));
			if (matcher.groupCount() == 3)
				todoRegionData.setSize(getSize(matcher.group(2)));
		}
				
		return todoRegionData;
	}
	
	// Wrapper - Loader
	public static MemoryMap load (BufferedReader bufferedReader)
	{
		// Wrap to other loaders, using one buffered reader
		reader = bufferedReader;
		readerLine = "";
		return loadMap();
	}
}
