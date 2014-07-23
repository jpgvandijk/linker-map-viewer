package MemoryViewer.MemoryContents;
import java.util.ArrayList;

public class MemoryContents
{
	// Attributes
	private ArrayList<IdentifierContent> identifierContents;
	private ArrayList<DataContent> dataContents;
	private ArrayList<FillContent> fillContents;
	
	// Constructors
	public MemoryContents (ArrayList<IdentifierContent> identifierContents, ArrayList<DataContent> dataContents, ArrayList<FillContent> fillContents) {
		this.identifierContents = identifierContents;
		this.dataContents = dataContents;
		this.fillContents = fillContents;
	}
	
	public MemoryContents () {
		this.identifierContents = new ArrayList<IdentifierContent>();
		this.dataContents = new ArrayList<DataContent>();
		this.fillContents = new ArrayList<FillContent>();
	}
	
	// Getters
	public int getNumberOfIdentifierContents () {
		return this.identifierContents.size();
	}
	
	public IdentifierContent getIdentifierContent (int identifierContent) {
		return this.identifierContents.get(identifierContent);
	}
	
	public int getNumberOfDataContents () {
		return this.dataContents.size();
	}
	
	public DataContent getDataContent (int dataContent) {
		return this.dataContents.get(dataContent);
	}
	
	public int getNumberOfFillContents () {
		return this.fillContents.size();
	}
	
	public FillContent getFillContent (int fillContent) {
		return this.fillContents.get(fillContent);
	}
	
	// Setters
	public void addIdentifierContent (IdentifierContent identifierContent) {
		this.identifierContents.add(identifierContent);
	}
	
	public void addDataContent (DataContent dataContent) {
		this.dataContents.add(dataContent);
	}
	
	public void addFillContent (FillContent fillContent) {
		this.fillContents.add(fillContent);
	}
}
