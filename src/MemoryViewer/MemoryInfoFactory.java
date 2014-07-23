package MemoryViewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import MemoryViewer.MemoryConfiguration.MemoryConfiguration;
import MemoryViewer.MemoryConfiguration.MemoryConfigurationLoader;
import MemoryViewer.MemoryContents.MemoryContents;
import MemoryViewer.MemoryContents.MemoryContentsLoader;
import MemoryViewer.MemoryMap.MemoryMap;
import MemoryViewer.MemoryMap.MemoryMapCompressor;
import MemoryViewer.MemoryMap.MemoryMapFilter;
import MemoryViewer.MemoryMap.MemoryMapLoader;

public class MemoryInfoFactory
{
	// Attributes
	private MemoryConfiguration memoryConfiguration;
	private MemoryContents memoryContents;
	private MemoryMap rawMemoryMap;
	private MemoryMap filteredMemoryMap;
	private MemoryMap compressedMemoryMap;
	
	// Constructor
	public MemoryInfoFactory () {
		memoryConfiguration = null;
		memoryContents = null;
		rawMemoryMap = null;
		filteredMemoryMap = null;
		compressedMemoryMap = null;
	}
	
	// Memory info creator
	public void load (File file)
	{
		try
		{
			// Load the configuration
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			memoryConfiguration = MemoryConfigurationLoader.load(bufferedReader);
			bufferedReader.close();
			fileReader.close();
			
			// Load, filter and compress the map
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			rawMemoryMap = MemoryMapLoader.load(bufferedReader);
			filteredMemoryMap = MemoryMapFilter.filter(rawMemoryMap);
			compressedMemoryMap = MemoryMapCompressor.compress(filteredMemoryMap);
			bufferedReader.close();
			fileReader.close();
			
			// Load the contents (actually created by the compressor!)
			memoryContents = MemoryContentsLoader.load();
		}
		catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	// Getters
	public MemoryConfiguration getMemoryConfiguration () {
		return memoryConfiguration;
	}
	
	public MemoryContents getMemoryContents () {
		return memoryContents;
	}
	
	public MemoryMap getRawMemoryMap () {
		return rawMemoryMap;
	}
	
	public MemoryMap getFilteredMemoryMap () {
		return filteredMemoryMap;
	}
	
	public MemoryMap getCompressedMemoryMap () {
		return compressedMemoryMap;
	}
}
