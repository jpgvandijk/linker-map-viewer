package MemoryViewer.MemoryMap;

public class MemoryMapFilter
{
	public static MemoryMap filter (MemoryMap map)
	{
		MemoryMap filteredMap = new MemoryMap();
		for (int i = 0; i < map.getNumberOfRegions(); i++)
		{
			Region filteredRegion = filter(map.getRegion(i));
			if (filteredRegion.getNumberOfSubRegions() > 0)
				filteredMap.addRegion(filteredRegion);
		}
		return filteredMap;
	}
	
	private static Region filter (Region region)
	{
		Region filteredRegion = new Region();
		filteredRegion.setName(region.getName());
		filteredRegion.setData(region.getData());
		filteredRegion.setFill(region.getFill());
		for (int i = 0; i < region.getNumberOfSubRegions(); i++)
		{
			SubRegion filteredSubRegion = filter(region.getSubRegion(i));
			if (filteredSubRegion.getNumberOfRegionData() > 0)
				filteredRegion.addSubRegion(filteredSubRegion);
		}
		return filteredRegion;
	}
	
	private static SubRegion filter (SubRegion subRegion)
	{
		SubRegion filteredSubRegion = new SubRegion();
		filteredSubRegion.setName(subRegion.getName());
		for (int i = 0; i < subRegion.getNumberOfRegionData(); i++)
		{
			RegionData filteredRegionData = subRegion.getRegionData(i);
			if (passesFilter(filteredRegionData))
				filteredSubRegion.addData(filteredRegionData);
		}
		return filteredSubRegion;
	}
	
	private static boolean passesFilter (RegionData regionData)
	{
		String info = regionData.getInfo();
		boolean validInfo = !(info.contains(" = ") || info.contains("PROVIDE") || info.contains("ASSERT") || info.contains("linker stubs"));
		boolean validAddress = (regionData.getAddress() != RegionData.DEFAULT_ADDRESS);
		
		return validInfo && validAddress;
	}
}
