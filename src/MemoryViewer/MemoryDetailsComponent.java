package MemoryViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;

import MemoryViewer.MemoryConfiguration.MemoryConfiguration;
import MemoryViewer.MemoryConfiguration.Space;
import MemoryViewer.MemoryConfiguration.SpaceAttributes;
import MemoryViewer.MemoryContents.DataContent;
import MemoryViewer.MemoryContents.FillContent;
import MemoryViewer.MemoryContents.IdentifierContent;
import MemoryViewer.MemoryContents.MemoryContents;
import MemoryViewer.MemoryMap.MemoryMap;
import MemoryViewer.MemoryMap.Region;
import MemoryViewer.MemoryMap.RegionData;
import MemoryViewer.MemoryMap.SubRegion;

public class MemoryDetailsComponent extends JPanel implements MemoryDetailsListener
{
	private static final long serialVersionUID = 1L;
	
	// Details table
	private DetailsTableModel detailsTableModel;
	private JTable detailsTable;
	private JPanel memoryDetailsPanel;
	private Border memoryDetailsBorder;

	// Constructor
	public MemoryDetailsComponent () {
		
		// Create contents
		detailsTableModel = new DetailsTableModel();
		detailsTable = new JTable(detailsTableModel);
		memoryDetailsPanel = new JPanel(new BorderLayout());
		memoryDetailsBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Memory Details");
		
		// Bind contents
		setLayout(new BorderLayout());
		add(memoryDetailsPanel, BorderLayout.CENTER);
		memoryDetailsPanel.add(detailsTable.getTableHeader(), BorderLayout.NORTH);
		memoryDetailsPanel.add(detailsTable, BorderLayout.CENTER);
		memoryDetailsPanel.setBorder(memoryDetailsBorder);
		
		// Set preferred sizes
		memoryDetailsPanel.setPreferredSize(new Dimension(300, 700));
	}
	
	public void showDetails (Object object) {
		detailsTableModel.setDetails(object);
	}
}


class DetailsTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	// Attributes
	private String[] properties;
	private Object[] values;
	
	// Constructor
	public DetailsTableModel () {
		properties = new String[1];
		values = new Object[1];
		properties[0] = "";
		values[0] = "";
	}
	
	// Helpers
	private String toLargeHexString (long number)
	{
		number &= 0xFFFFFFFF;
		return "0x" + Long.toHexString(0x100000000L | number).substring(1).toUpperCase();
	}

	// Make it possible to set a new details object
	public void setDetails (Object detailsObject) {
		if (detailsObject != null) {
		
			if (detailsObject instanceof MemoryMap) {
				load((MemoryMap) detailsObject);
			} else if (detailsObject instanceof Region) {
				load((Region) detailsObject);
			} else if (detailsObject instanceof SubRegion) {
				load((SubRegion) detailsObject);
			} else if (detailsObject instanceof RegionData) {
				load((RegionData) detailsObject);
			} else if (detailsObject instanceof MemoryConfiguration) {
				load((MemoryConfiguration) detailsObject);
			} else if (detailsObject instanceof Space) {
				load((Space) detailsObject);
			} else if (detailsObject instanceof SpaceAttributes) {
				load((SpaceAttributes) detailsObject);
			} else if (detailsObject instanceof MemoryContents) {
				load((MemoryContents) detailsObject);
			} else if (detailsObject instanceof IdentifierContent) {
				load((IdentifierContent) detailsObject);
			} else if (detailsObject instanceof DataContent) {
				load((DataContent) detailsObject);
			} else if (detailsObject instanceof FillContent) {
				load((FillContent) detailsObject);
			} else {
				load(detailsObject.toString());
			}
			
			fireTableStructureChanged();
		}
	}

	// Implement/override AbstractTableModel methods
	public int getColumnCount() {
		return 2;
	}
	
	public Class<?> getColumnClass (int columnIndex) {
		return String.class;
	}
	
	public String getColumnName (int columnIndex) {
		switch (columnIndex)
		{
		case 0: return "Property";
		case 1: return "Value";
		default: return "";
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex)
		{
		case 0: return properties[rowIndex];
		case 1: return values[rowIndex];
		default: return "";
		}
	}
	
	public int getRowCount() {
		return properties.length;
	}
	
	public boolean isCellEditable (int rowIndex, int columnIndex) {
		return false;
	}
	
	// Different details loaders
	private void load (MemoryMap memoryMap) {
		properties = new String[2];
		values = new Object[2];
		
		properties[0] = "Type";						values[0] = "MemoryMap";
		properties[1] = "Number of regions";		values[1] = memoryMap.getNumberOfRegions();
	}
	
	private void load (Region region) {
		properties = new String[7];
		values = new Object[7];
		
		properties[0] = "Type";						values[0] = "Region";
		properties[1] = "Number of subregions";		values[1] = region.getNumberOfSubRegions();
		properties[2] = "Name";						values[2] = region.getName();
		properties[3] = "Fill";						values[3] = region.getFill();
		properties[4] = "Address";					values[4] = toLargeHexString(region.getData().getAddress());
		properties[5] = "Size";						values[5] = region.getData().getSize();
		properties[6] = "Info";						values[6] = region.getData().getInfo();
	}
	
	private void load (SubRegion subRegion) {
		properties = new String[3];
		values = new Object[3];
		
		properties[0] = "Type";						values[0] = "SubRegion";
		properties[1] = "Number of regiondata";		values[1] = subRegion.getNumberOfRegionData();
		properties[2] = "Name";						values[2] = subRegion.getName();
	}
	
	private void load (RegionData regionData) {
		properties = new String[4];
		values = new Object[4];
		
		properties[0] = "Type";						values[0] = "RegionData";
		properties[1] = "Address";					values[1] = toLargeHexString(regionData.getAddress());
		properties[2] = "Size";						values[2] = regionData.getSize();
		properties[3] = "Info";						values[3] = regionData.getInfo();
	}
	
	private void load (MemoryConfiguration memoryConfiguration) {
		properties = new String[3];
		values = new Object[3];
		
		properties[0] = "Type";						values[0] = "MemoryConfiguration";
		properties[1] = "Number of spaces";			values[1] = memoryConfiguration.getNumberOfSpaces();
		properties[2] = "Total size";				values[2] = memoryConfiguration.getTotalSize();
	}
	
	private void load (Space space) {
		properties = new String[7];
		values = new Object[7];
		
		properties[0] = "Type";						values[0] = "Space";
		properties[1] = "Name";						values[1] = space.getName();
		properties[2] = "Origin";					values[2] = toLargeHexString(space.getOrigin());
		properties[3] = "Length";					values[3] = toLargeHexString(space.getLength());
		properties[4] = "Erasable";					values[4] = space.getAttributes().isErasable();
		properties[5] = "Readable";					values[5] = space.getAttributes().isReadable();
		properties[6] = "Writable";					values[6] = space.getAttributes().isWritable();
	}
	
	private void load (SpaceAttributes spaceAttributes) {
		properties = new String[4];
		values = new Object[4];
		
		properties[0] = "Type";						values[0] = "SpaceAttributes";
		properties[1] = "Erasable";					values[1] = spaceAttributes.isErasable();
		properties[2] = "Readable";					values[2] = spaceAttributes.isReadable();
		properties[3] = "Writable";					values[3] = spaceAttributes.isWritable();
	}
	
	private void load (MemoryContents memoryContents) {
		properties = new String[4];
		values = new Object[4];
		
		properties[0] = "Type";						values[0] = "MemoryContents";
		properties[1] = "Number of identifiers";	values[1] = memoryContents.getNumberOfIdentifierContents();
		properties[2] = "Number of datas";			values[2] = memoryContents.getNumberOfDataContents();
		properties[3] = "Number of fills";			values[3] = memoryContents.getNumberOfFillContents();
	}
	
	private void load (IdentifierContent identifierContent) {
		properties = new String[5];
		values = new Object[5];
		
		properties[0] = "Type";						values[0] = "IdentifierContent";
		properties[1] = "Identifier";				values[1] = identifierContent.getIdentifier();
		properties[2] = "File";						values[2] = identifierContent.getFile();
		properties[3] = "Address";					values[3] = toLargeHexString(identifierContent.getAddress());
		properties[4] = "Size";						values[4] = identifierContent.getSize();
	}
	
	private void load (DataContent dataContent) {
		properties = new String[4];
		values = new Object[4];
		
		properties[0] = "Type";						values[0] = "DataContent";
		properties[1] = "Data";						values[1] = dataContent.getData();
		properties[2] = "Address";					values[2] = toLargeHexString(dataContent.getAddress());
		properties[3] = "Size";						values[3] = dataContent.getSize();
	}
	
	private void load (FillContent fillContent) {
		properties = new String[4];
		values = new Object[4];
		
		properties[0] = "Type";						values[0] = "FillContent";
		properties[1] = "Fill";						values[1] = fillContent.getFill();
		properties[2] = "Address";					values[2] = toLargeHexString(fillContent.getAddress());
		properties[3] = "Size";						values[3] = fillContent.getSize();
	}
	
	private void load (String string) {
		properties = new String[1];
		values = new Object[1];
		
		properties[0] = "";							values[0] = string;
	}
}
