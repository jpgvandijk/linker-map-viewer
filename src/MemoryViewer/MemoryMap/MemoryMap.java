package MemoryViewer.MemoryMap;
import java.util.ArrayList;

public class MemoryMap
{
	// Attributes
	private ArrayList<Region> regions;
	
	// Constructors
	public MemoryMap (ArrayList<Region> regions) {
		this.regions = regions;
	}
	
	public MemoryMap () {
		this.regions = new ArrayList<Region>();
	}
	
	// Getters
	public int getNumberOfRegions () {
		return this.regions.size();
	}
	
	public Region getRegion (int region) {
		return this.regions.get(region);
	}
	
	// Setters
	public void addRegion (Region region) {
		for (int i = 0; i < regions.size(); i++) {
			if (regions.get(i).getData().getAddress() > region.getData().getAddress()) {
				regions.add(i, region);
				return;
			}
		}
		regions.add(region);
	}
}
