package MemoryViewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;

import MemoryViewer.MemoryContents.DataContent;
import MemoryViewer.MemoryContents.FillContent;
import MemoryViewer.MemoryContents.IdentifierContent;
import MemoryViewer.MemoryContents.MemoryContents;
import MemoryViewer.MemoryConfiguration.MemoryConfiguration;
import MemoryViewer.MemoryConfiguration.Space;

public class MemoryConfigurationAndContentsComponent extends JPanel implements MemoryInfoUpdateListener, ItemListener, MemoryConfigurationSelectionListener
{
	private static final long serialVersionUID = 1L;
	
	// Constants
	private static final String SELECTION_IDENTIFIER = "Identifier";
	private static final String SELECTION_DATA = "Data";
	private static final String SELECTION_FILL = "Fill";
	
	// Listeners
	private ArrayList<MemoryDetailsListener> memoryDetailsListeners;
	
	// Memory info
	private MemoryConfiguration memoryConfiguration;
	private boolean[] memoryConfigurationEnables;
	private MemoryContents memoryContents;
	
	// Configuration view
	private MemoryConfigurationTableModel memoryConfigurationTableModel;
	private JTable memoryConfigurationTable;
	private JPanel memoryConfigurationPanel;
	private Border memoryConfigurationBorder;
	
	// Contents view
	private JComboBox<String> memoryContentsSelectionBox;
	private MemoryContentsList memoryContentsList;
	private JPanel memoryContentsPanel;
	private Border memoryContentsBorder;
	
	// Constructor
	public MemoryConfigurationAndContentsComponent () {
		
		memoryDetailsListeners = new ArrayList<MemoryDetailsListener>();
		memoryConfiguration = null;
		memoryConfigurationEnables = new boolean[0];
		memoryContents = null;
		
		// Create contents
		memoryConfigurationTableModel = new MemoryConfigurationTableModel();
		memoryConfigurationTable = new JTable(memoryConfigurationTableModel);
		memoryConfigurationPanel = new JPanel(new BorderLayout());
		memoryConfigurationBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Memory Configuration");
		memoryContentsSelectionBox = new JComboBox<String>();
		memoryContentsSelectionBox.addItem(SELECTION_IDENTIFIER);
		memoryContentsSelectionBox.addItem(SELECTION_DATA);
		memoryContentsSelectionBox.addItem(SELECTION_FILL);
		memoryContentsList = new MemoryContentsList();
		memoryContentsPanel = new JPanel(new BorderLayout());
		memoryContentsBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Memory Contents");
		
		// Add listeners
		memoryConfigurationTable.addMouseListener(new MemoryConfigurationMouseListener());
		memoryConfigurationTableModel.addMemoryConfigurationSelectionListener(this);
		memoryContentsSelectionBox.addItemListener(this);
		memoryContentsList.addMouseListener(new MemoryContentsMouseListener());
		
		// Bind contents
		setLayout(new BorderLayout());
		add(memoryConfigurationPanel, BorderLayout.NORTH);
		memoryConfigurationPanel.add(memoryConfigurationTable.getTableHeader(), BorderLayout.NORTH);
		memoryConfigurationPanel.add(memoryConfigurationTable, BorderLayout.CENTER);
		memoryConfigurationPanel.setBorder(memoryConfigurationBorder);
		add(memoryContentsPanel, BorderLayout.CENTER);
		memoryContentsPanel.add(memoryContentsSelectionBox, BorderLayout.NORTH);
		memoryContentsPanel.add(new JScrollPane(memoryContentsList), BorderLayout.CENTER);
		memoryContentsPanel.setBorder(memoryContentsBorder);
		
		// Set preferred sizes
		memoryContentsPanel.setPreferredSize(new Dimension(300, 100));
	}
	
	public void addMemoryDetailsListener (MemoryDetailsListener memoryDetailsListener) {
		memoryDetailsListeners.add(memoryDetailsListener);
	}
	
	private void notifyMemoryDetailsListeners (Object object) {
		for (int i = 0; i < memoryDetailsListeners.size(); i++)
			memoryDetailsListeners.get(i).showDetails(object);
	}
	
	// MemoryConfigurationMouseListener
	class MemoryConfigurationMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int selectedSpace = memoryConfigurationTable.getSelectedRow();
			if (memoryConfiguration != null && selectedSpace < memoryConfiguration.getNumberOfSpaces())
				notifyMemoryDetailsListeners(memoryConfiguration.getSpace(selectedSpace));
		}
	}
	
	// MemoryContentsMouseListener
	class MemoryContentsMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			Object selectedObject = memoryContentsList.getSelectedValue();
			if (selectedObject != null)
				notifyMemoryDetailsListeners(selectedObject);
		}
	}
	
	// MemoryConfigurationSelectionListener
	public void selectionChanged (boolean[] memoryConfigurationEnables) {
		this.memoryConfigurationEnables = memoryConfigurationEnables;
		updateContents();
	}
	
	// ItemListener
	public void itemStateChanged(ItemEvent e) {
		updateContents();
	}

	// MemoryInfoUpdateListener
	public void update (MemoryInfoFactory memoryInfoFactory) {
		this.memoryConfiguration = memoryInfoFactory.getMemoryConfiguration();
		this.memoryContents = memoryInfoFactory.getMemoryContents();
		
		updateConfiguration();
		updateContents();
	}
	
	// Updaters
	private void updateConfiguration () {
		memoryConfigurationTableModel.setMemoryConfiguration(memoryConfiguration);
	}
	
	private void updateContents () {
		memoryContentsList.setMemoryContents(filterMemoryContents());
	}
	
	// Filter
	private MemoryContents filterMemoryContents () {
		
		if (memoryContents == null)
			return null;
		
		MemoryContents filteredMemoryContents = new MemoryContents();
		
		if (memoryContentsSelectionBox.getSelectedItem().equals(SELECTION_IDENTIFIER)) {
			// Add all identifiers in selected range
			for (int i = 0; i < memoryContents.getNumberOfIdentifierContents(); i++) {
				if (isInSelectedRange(memoryContents.getIdentifierContent(i).getAddress())) {
					filteredMemoryContents.addIdentifierContent(memoryContents.getIdentifierContent(i));
				}
			}	
		} else if (memoryContentsSelectionBox.getSelectedItem().equals(SELECTION_DATA)) {
			// Add all datas in selected range
			for (int i = 0; i < memoryContents.getNumberOfDataContents(); i++) {
				if (isInSelectedRange(memoryContents.getDataContent(i).getAddress())) {
					filteredMemoryContents.addDataContent(memoryContents.getDataContent(i));
				}
			}	
		} else if (memoryContentsSelectionBox.getSelectedItem().equals(SELECTION_FILL)) {
			// Add all fills in selected range
			for (int i = 0; i < memoryContents.getNumberOfFillContents(); i++) {
				if (isInSelectedRange(memoryContents.getFillContent(i).getAddress())) {
					filteredMemoryContents.addFillContent(memoryContents.getFillContent(i));
				}
			}	
		}
		
		return filteredMemoryContents;
	}

	private boolean isInSelectedRange (long address) {
		for (int i = 0; i < memoryConfigurationEnables.length; i++) {
			if (memoryConfigurationEnables[i]) {
				Space space = memoryConfiguration.getSpace(i);
				if ((space.getOrigin() <= address) && (space.getOrigin() + space.getLength()) > address)
					return true;
			}
		}
		return false;
	}
}

class MemoryConfigurationTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	// Attributes
	private ArrayList<MemoryConfigurationSelectionListener> memoryConfigurationSelectionListeners;
	private MemoryConfiguration memoryConfiguration;
	private boolean[] memoryConfigurationEnables;
	
	// Constructor
	public MemoryConfigurationTableModel () {
		memoryConfigurationSelectionListeners = new ArrayList<MemoryConfigurationSelectionListener>();
		memoryConfiguration = null;
		memoryConfigurationEnables = null;
	}
	
	// Make it possible to set a new configuration
	public void setMemoryConfiguration (MemoryConfiguration memoryConfiguration) {
		if (memoryConfiguration != null)
		{
			this.memoryConfiguration = memoryConfiguration;
			memoryConfigurationEnables = new boolean[memoryConfiguration.getNumberOfSpaces()];
			
			for (int i = 0; i < memoryConfigurationEnables.length; i++)
				memoryConfigurationEnables[i] = true;
			
			// Notify listeners
			fireTableStructureChanged();
			for (int i = 0; i < memoryConfigurationSelectionListeners.size(); i++)
				memoryConfigurationSelectionListeners.get(i).selectionChanged(memoryConfigurationEnables);
		}
	}
	
	// Implement/override AbstractTableModel methods
	public int getColumnCount() {
		return 2;
	}
	
	public Class<?> getColumnClass (int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}
	
	public String getColumnName (int columnIndex) {
		switch (columnIndex)
		{
		case 0: return "";
		case 1: return "Name";
		default: return "";
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (memoryConfiguration == null)
		{
			if (columnIndex == 0)
				return false;
			else
				return "-";
		}
		else
		{
			switch (columnIndex)
			{
			case 0: return memoryConfigurationEnables[rowIndex];
			case 1: return memoryConfiguration.getSpace(rowIndex).getName();
			default: return "";
			}
		}
	}
	
	public int getRowCount() {
		if (memoryConfiguration == null)
			return 1;
		else
			return memoryConfiguration.getNumberOfSpaces();
	}
	
	public boolean isCellEditable (int rowIndex, int columnIndex) {
		if (memoryConfiguration != null && columnIndex == 0)
			return true;
		else
			return false;
	}
		
	public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 0)
		{
			memoryConfigurationEnables[rowIndex] = (Boolean) aValue;
			
			// Notify listeners
			fireTableCellUpdated(rowIndex, columnIndex);
			for (int i = 0; i < memoryConfigurationSelectionListeners.size(); i++)
				memoryConfigurationSelectionListeners.get(i).selectionChanged(memoryConfigurationEnables);
		}
	}

	public void addMemoryConfigurationSelectionListener (MemoryConfigurationSelectionListener memoryConfigurationSelectionListener) {
		memoryConfigurationSelectionListeners.add(memoryConfigurationSelectionListener);
	}
}


class MemoryContentsList extends JList<Object>
{
	private static final long serialVersionUID = 1L;
	
	// Attributes
	private DefaultListModel<Object> memoryContentsListModel;
	private MemoryContentsCellRenderer memoryContentsCellRenderer;
	
	// Constructor
	public MemoryContentsList () {
		
		// Set specific model and renderer
		memoryContentsListModel = new DefaultListModel<Object>();
		memoryContentsCellRenderer = new MemoryContentsCellRenderer();
		super.setModel(memoryContentsListModel);
		super.setCellRenderer(memoryContentsCellRenderer);
		super.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	// Update
	public void setMemoryContents (MemoryContents memoryContents) {
		
		// Clear list
		memoryContentsListModel.clear();
		
		// Add all content, if any
		if (memoryContents == null)
			return;
		for (int i = 0; i < memoryContents.getNumberOfIdentifierContents(); i++)
			memoryContentsListModel.addElement(memoryContents.getIdentifierContent(i));
		for (int i = 0; i < memoryContents.getNumberOfDataContents(); i++)
			memoryContentsListModel.addElement(memoryContents.getDataContent(i));
		for (int i = 0; i < memoryContents.getNumberOfFillContents(); i++)
			memoryContentsListModel.addElement(memoryContents.getFillContent(i));
	}
	
	// Appearance
	private String getMemoryContentsListText (Object object) {
		if (object instanceof IdentifierContent) {
			IdentifierContent identifierContent = (IdentifierContent) object;
			return identifierContent.getIdentifier();
		} else if (object instanceof DataContent) {
			DataContent dataContent = (DataContent) object;
			return dataContent.getData();
		} else if (object instanceof FillContent) {
			FillContent fillContent = (FillContent) object;
			return fillContent.getSize() + " byte(s) of " + fillContent.getFill();
		} else {
			return object.toString();
		}
	}
	
	class MemoryContentsCellRenderer extends DefaultListCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		public Component getListCellRendererComponent (JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
			label.setText(getMemoryContentsListText(value));
			return label;
		}
	}
}