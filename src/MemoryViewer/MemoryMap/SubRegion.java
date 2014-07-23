package MemoryViewer.MemoryMap;

import java.util.ArrayList;

public class SubRegion
{
	// Constants
	public static final String DEFAULT_NAME = "";
	
	// Attributes
	private String name;
	private ArrayList<RegionData> data;
	
	// Attributes - protection
	private boolean isSetName = false;
	
	// Constructors
	public SubRegion (String name, ArrayList<RegionData> data) {
		setName(name);
		this.data = data;
	}
	
	public SubRegion () {
		this.name = DEFAULT_NAME;
		this.data = new ArrayList<RegionData>();
	}
	
	// Getters
	public String getName () {
		return this.name;
	}
	
	public int getNumberOfRegionData () {
		return this.data.size();
	}
	
	public RegionData getRegionData (int regionData) {
		return this.data.get(regionData);
	}
	
	// Setters
	public void setName (String name) {
		if (!isSetName) {
			this.name = name;
			isSetName = true;
		}
		else
			System.err.println("The name of the subregion can only be set once!");
	}
	
	public void addData (RegionData data) {
		for (int i = 0; i < this.data.size(); i++) {
			if (this.data.get(i).getAddress() > data.getAddress()) {
				this.data.add(i, data);
				return;
			}
		}
		this.data.add(data);
	}
}
