package MemoryViewer.MemoryMap;
import MemoryViewer.MemoryContents.MemoryContentsLoader;

public class MemoryMapCompressor
{	
	// Attributes - temporary storage of regiondata
	private static long address;
	private static long size;
	private static String info;
	private static boolean used;
	
	// Helper functions - temporary storage of regiondata
	private static boolean isStored () {
		return (size > 0);
	}
	
	private static void clearStorage () {
		size = RegionData.DEFAULT_SIZE;
	}
	
	private static void setUsed () {
		used = true;
	}
	
	private static long getEndAddress () {
		if (!isStored())
			return RegionData.DEFAULT_ADDRESS;
		else
			return address + size;
	}
	
	private static boolean isToBeStored (RegionData regionData) {
		return ((regionData.getAddress() != RegionData.DEFAULT_ADDRESS) && (regionData.getSize() > 0));
	}
	
	private static void store (RegionData regionData) {
		address = regionData.getAddress();
		size = regionData.getSize();
		info = regionData.getInfo();
		used = false;
	}
	
	private static boolean isLeftToBeAdded () {
		return (isStored() && !used);
	}
	
	private static RegionData add () {
		return new RegionData(address, size, info);
	}
	
	private static boolean isToBeMerged (RegionData regionData) {
		return (isStored() && (regionData.getAddress() < getEndAddress()) && (regionData.getSize() == RegionData.DEFAULT_SIZE)); 
	}
	
	private static RegionData merge (RegionData regionData, long mergeSize) {
		return new RegionData(regionData.getAddress(), mergeSize, regionData.getInfo() + " (" + info + ")");
	}
	
	// Method to compress maps
	public static MemoryMap compress (MemoryMap map)
	{
		// Start gathering new memory contents
		MemoryContentsLoader.clear();
		
		MemoryMap compressedMap = new MemoryMap();
		for (int i = 0; i < map.getNumberOfRegions(); i++)
		{
			Region region = map.getRegion(i);
			Region compressedRegion = new Region();
			compressedRegion.setName(region.getName());
			compressedRegion.setData(region.getData());
			compressedRegion.setFill(region.getFill());
			for (int j = 0; j < region.getNumberOfSubRegions(); j++)
			{
				SubRegion subRegion = region.getSubRegion(j);
				SubRegion compressedSubRegion = new SubRegion();
				compressedSubRegion.setName(subRegion.getName());
				
				// Start of compression
				clearStorage();
				for (int k = 0; k < subRegion.getNumberOfRegionData(); k++)
				{
					RegionData regionData = subRegion.getRegionData(k);
					
					if (isToBeStored(regionData))
					{
						// Store it, but first save anything left in storage not already used
						if (isLeftToBeAdded())
						{
							compressedSubRegion.addData(add());
							MemoryContentsLoader.addOtherContent(address, size, info, subRegion.getName());
						}
						store(regionData);
					}
					else if (isToBeMerged(regionData))
					{
						// Add the merged data, keep in storage, but note that it has been used
						long mergeSize = RegionData.DEFAULT_SIZE;
						if ((k + 1) < subRegion.getNumberOfRegionData()) {
							RegionData nextRegionData = subRegion.getRegionData(k + 1);
							if (nextRegionData.getAddress() <= getEndAddress())
								mergeSize = nextRegionData.getAddress() - regionData.getAddress();
							else
								mergeSize = getEndAddress() - regionData.getAddress();
						} else {
							mergeSize = getEndAddress() - regionData.getAddress();
						}
						
						setUsed();
						compressedSubRegion.addData(merge(regionData, mergeSize));
						MemoryContentsLoader.addIdentifierContent(regionData.getAddress(), mergeSize, regionData.getInfo(), info);
					}
					//else
					//{
					//	System.out.println("Unexpected data format while compressing...");
					//}
				}
				if (isLeftToBeAdded())
				{
					compressedSubRegion.addData(add());
					MemoryContentsLoader.addOtherContent(address, size, info, subRegion.getName());
				}
				// End of compression
				
				if (compressedSubRegion.getNumberOfRegionData() > 0)
					compressedRegion.addSubRegion(compressedSubRegion);
			}
			
			if (compressedRegion.getNumberOfSubRegions() > 0)
				compressedMap.addRegion(compressedRegion);
		}
		
		// Memory contents have been gathered
		return compressedMap;
	}
}
