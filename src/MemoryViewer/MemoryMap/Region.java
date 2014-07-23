package MemoryViewer.MemoryMap;

import java.util.ArrayList;

public class Region
{
	// Constants
	public static final String DEFAULT_NAME = "";
	public static final RegionData DEFAULT_DATA = null;
	public static final int DEFAULT_FILL = -1;
	
	// Attributes
	private String name;
	private RegionData data;
	private int fill;
	private ArrayList<SubRegion> subRegions;
	
	// Attributes - protection
	private boolean isSetName = false;
	private boolean isSetData = false;
	private boolean isSetFill = false;
	
	// Constructors
	public Region (String name, RegionData data, int fill, ArrayList<SubRegion> subRegions) {
		setName(name);
		setData(data);
		setFill(fill);
		this.subRegions = subRegions;
	}
	
	public Region () {
		this.name = DEFAULT_NAME;
		this.data = DEFAULT_DATA;
		this.fill = DEFAULT_FILL;
		this.subRegions = new ArrayList<SubRegion>();
	}
	
	// Getters
	public String getName () {
		return this.name;
	}
	
	public RegionData getData () {
		return this.data;
	}
	
	public int getFill () {
		return this.fill;
	}
	
	public int getNumberOfSubRegions () {
		return this.subRegions.size();
	}
	
	public SubRegion getSubRegion (int subRegion) {
		return this.subRegions.get(subRegion);
	}
	
	// Setters
	public void setName (String name) {
		if (!isSetName) {
			this.name = name;
			isSetName = true;
		}
		else
			System.err.println("The name of the region can only be set once!");
	}
	
	public void setData (RegionData data) {
		if (!isSetData) {
			this.data = data;
			isSetData = true;
		}
		else
			System.err.println("The data of the region can only be set once!");
	}
	
	public void setFill (int fill) {
		if (!isSetFill) {
			this.fill = fill;
			isSetFill = true;
		}
		else
			System.err.println("The fill of the region can only be set once!");
	}
	
	public void addSubRegion (SubRegion subRegion) {
		for (int i = 0; i < subRegions.size(); i++) {
			if (subRegions.get(i).getNumberOfRegionData() > 0 && subRegion.getNumberOfRegionData() > 0) {
				if (subRegions.get(i).getRegionData(0).getAddress() > subRegion.getRegionData(0).getAddress()) {
					subRegions.add(i, subRegion);
					return;
				}
			}
		}
		subRegions.add(subRegion);
	}
}
