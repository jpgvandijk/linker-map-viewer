package MemoryViewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import MemoryViewer.MemoryMap.MemoryMap;
import MemoryViewer.MemoryMap.Region;
import MemoryViewer.MemoryMap.RegionData;
import MemoryViewer.MemoryMap.SubRegion;

public class MemoryMapComponent extends JPanel implements MemoryInfoUpdateListener, TreeSelectionListener, ItemListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	// Constants
	private static final String SELECTION_RAW = "Raw";
	private static final String SELECTION_FILTERED = "Filtered";
	private static final String SELECTION_COMPRESSED = "Compressed";
	
	// Listeners
	private ArrayList<MemoryDetailsListener> memoryDetailsListeners;
	
	// Memory info
	private MemoryMap rawMemoryMap;
	private MemoryMap filteredMemoryMap;
	private MemoryMap compressedMemoryMap;
	
	// Tree view
	private JComboBox<String> memoryMapSelectionBox;
	private MemoryMapTree memoryMapTree;
	private JPanel memoryTreePanel;
	private Border memoryTreeBorder;
	
	// Picture view
	private MemoryMapPicture memoryMapPicture;
	private JPanel zoomPanel;
	private JButton buttonZoomIn;
	private JButton buttonZoomOut;
	private JPanel memoryPicturePanel;
	private Border memoryPictureBorder;

	// Constructor
	public MemoryMapComponent () {
		
		memoryDetailsListeners = new ArrayList<MemoryDetailsListener>();
		rawMemoryMap = null;
		filteredMemoryMap = null;
		compressedMemoryMap = null;
		
		// Create contents
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		memoryMapTree = new MemoryMapTree();
		memoryMapSelectionBox = new JComboBox<String>();
		memoryMapSelectionBox.addItem(SELECTION_RAW);
		memoryMapSelectionBox.addItem(SELECTION_FILTERED);
		memoryMapSelectionBox.addItem(SELECTION_COMPRESSED);
		memoryMapSelectionBox.setSelectedIndex(2);
		memoryTreePanel = new JPanel(new BorderLayout());
		memoryTreeBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Memory Tree");
		memoryMapPicture = new MemoryMapPicture();
		zoomPanel = new JPanel(new GridLayout(1, 2));
		buttonZoomIn = new JButton("+");
		buttonZoomOut = new JButton("-");
		memoryPicturePanel = new JPanel(new BorderLayout());
		memoryPictureBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Memory Picture");
		
		// Add listeners
		memoryMapTree.addTreeSelectionListener(this);
		memoryMapSelectionBox.addItemListener(this);
		buttonZoomIn.addActionListener(this);
		buttonZoomOut.addActionListener(this);
		
		// Bind contents
		setLayout(new GridLayout(1, 1));
		add(splitPane);
		splitPane.setLeftComponent(memoryTreePanel);
		memoryTreePanel.add(memoryMapSelectionBox, BorderLayout.NORTH);
		memoryTreePanel.add(new JScrollPane(memoryMapTree), BorderLayout.CENTER);
		memoryTreePanel.setBorder(memoryTreeBorder);
		splitPane.setRightComponent(memoryPicturePanel);
		memoryPicturePanel.add(new JScrollPane(memoryMapPicture), BorderLayout.CENTER);
		memoryPicturePanel.add(zoomPanel, BorderLayout.NORTH);
		memoryPicturePanel.setBorder(memoryPictureBorder);
		zoomPanel.add(buttonZoomIn);
		zoomPanel.add(buttonZoomOut);
		
		// Set preferred sizes
		memoryTreePanel.setPreferredSize(new Dimension(300, 700));
		memoryPicturePanel.setPreferredSize(new Dimension(300, 700));
	}
	
	public void addMemoryDetailsListener (MemoryDetailsListener memoryDetailsListener) {
		memoryDetailsListeners.add(memoryDetailsListener);
	}
	
	private void notifyMemoryDetailsListeners (Object object) {
		for (int i = 0; i < memoryDetailsListeners.size(); i++)
			memoryDetailsListeners.get(i).showDetails(object);
	}
	
	// ItemListener
	public void itemStateChanged(ItemEvent e) {
		updateTree();
		updatePicture();
	}

	// TreeSelectionListener
	public void valueChanged(TreeSelectionEvent e) {
		
		// Get info of selected node
		Object component = memoryMapTree.getLastSelectedPathComponent();
		if (component instanceof DefaultMutableTreeNode) {

			Object nodes[] = ((DefaultMutableTreeNode)component).getPath();
			
			for (int i = 0; i < nodes.length; i++) {
				Object nodeInfo = ((DefaultMutableTreeNode)nodes[i]).getUserObject();
			
				if (nodeInfo instanceof Region)
					memoryMapPicture.setSelectedRegion((Region)nodeInfo);
				if (nodeInfo instanceof SubRegion)
					memoryMapPicture.setSelectedSubRegion((SubRegion)nodeInfo);
				if (nodeInfo instanceof RegionData)
					memoryMapPicture.setSelectedRegionData((RegionData)nodeInfo);
			
				if (i == nodes.length - 1)
					notifyMemoryDetailsListeners(nodeInfo);
			}
		}
	}

	// ActionListener
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonZoomIn)
			memoryMapPicture.zoomIn();
		else if (e.getSource() == buttonZoomOut)
			memoryMapPicture.zoomOut();
	}
	
	// MemoryInfoUpdateListener
	public void update (MemoryInfoFactory memoryInfoFactory) {
		rawMemoryMap = memoryInfoFactory.getRawMemoryMap();
		filteredMemoryMap = memoryInfoFactory.getFilteredMemoryMap();
		compressedMemoryMap = memoryInfoFactory.getCompressedMemoryMap();
		
		updateTree();
		updatePicture();
	}
	
	// Updaters
	private void updateTree () {
		if (memoryMapSelectionBox.getSelectedItem().equals(SELECTION_RAW)) {
			memoryMapTree.setMemoryMap(rawMemoryMap);
		} else if (memoryMapSelectionBox.getSelectedItem().equals(SELECTION_FILTERED)) {
			memoryMapTree.setMemoryMap(filteredMemoryMap);
		} else if (memoryMapSelectionBox.getSelectedItem().equals(SELECTION_COMPRESSED)) {
			memoryMapTree.setMemoryMap(compressedMemoryMap);
		}
	}
	
	private void updatePicture () {
		// Happens automatically by selecting a region
	}
}

class MemoryMapTree extends JTree
{
	private static final long serialVersionUID = 1L;
	
	// Attributes
	private DefaultTreeModel memoryMapTreeModel;
	
	// Constructor
	public MemoryMapTree() {	
		memoryMapTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("No map loaded..."));
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		super.setModel(memoryMapTreeModel);
		setRootVisible(true);
	}
	
	// Overriden method that controls the text in the tree
	public String convertValueToText (Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if (value instanceof DefaultMutableTreeNode)
		{
			// Get user object
			Object info = ((DefaultMutableTreeNode)value).getUserObject();
			
			// Change behavior depending on class
			if (info instanceof MemoryMap)
				return "Memory Map";
			else if (info instanceof Region)
				return "Region: " + ((Region)info).getName();
			else if (info instanceof SubRegion)
				return "Subregion: " + ((SubRegion)info).getName();
			else if (info instanceof RegionData)
				return "Regiondata @ " + "0x" + Long.toHexString(0x100000000L | (((RegionData)info).getAddress() & 0xFFFFFFFF)).substring(1).toUpperCase();
			else
				return info.toString();
		}
		return value.toString();
	}
	
	public void setMemoryMap (MemoryMap memoryMap) {
		
		// Update tree
		DefaultMutableTreeNode mapTreeRoot = new DefaultMutableTreeNode(memoryMap);
		for (int i = 0; i < memoryMap.getNumberOfRegions(); i++)
		{
			Region region = memoryMap.getRegion(i);
			DefaultMutableTreeNode nodeRegion = new DefaultMutableTreeNode(region);
			mapTreeRoot.add(nodeRegion);
			
			for (int j = 0; j < region.getNumberOfSubRegions(); j++)
			{
				SubRegion subRegion = region.getSubRegion(j);
				DefaultMutableTreeNode nodeSubRegion = new DefaultMutableTreeNode(subRegion);
				nodeRegion.add(nodeSubRegion);
				
				for (int k = 0; k < subRegion.getNumberOfRegionData(); k++)
				{
					RegionData regionData = subRegion.getRegionData(k);
					DefaultMutableTreeNode nodeRegionData = new DefaultMutableTreeNode(regionData);
					nodeSubRegion.add(nodeRegionData);
				}
			}
		}
		
		memoryMapTreeModel.setRoot(mapTreeRoot);
		setRootVisible(true);
	}
}

class MemoryMapPicture extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	// Constants
	private static final double zoomFactor = 2;
	private static final double maxZoomIn = 8;
	private static final double minZoomIn = 0.001;
	private static final int offsetX = 20;
	private static final int offsetY = 20;
	private static final int width = 200;
	private static final int selectedRegionWidth = 50;
	
	// Attributes
	private Region selectedRegion;
	private SubRegion selectedSubRegion;
	private RegionData selectedRegionData;
	private double scale;
	private boolean focus;

	// Constructor
	public MemoryMapPicture () {
		selectedRegion = null;
		selectedSubRegion = null;
		selectedRegionData = null;
		scale = 0.25;
		focus = false;
		
		setBackground(Color.WHITE);
	}
	
	public void setSelectedRegion (Region region) {
		this.selectedRegion = region;
		setSelectedSubRegion(region.getSubRegion(0));
		//repaint();
	}

	public void setSelectedSubRegion (SubRegion subRegion) {
		this.selectedSubRegion = subRegion;
		setSelectedRegionData(subRegion.getRegionData(0));
		//repaint();
	}

	public void setSelectedRegionData (RegionData regionData) {
		this.selectedRegionData = regionData;
		repaint();
	}
	
	public void zoomIn () {
		scale *= zoomFactor;
		if (scale > maxZoomIn)
			scale = maxZoomIn;
		focus = true;
		repaint();
	}
	
	public void zoomOut () {
		scale /= zoomFactor;
		if (scale < minZoomIn)
			scale = minZoomIn;
		focus = true;
		repaint();
	}
	
	public void paint (Graphics g) {
		super.paint(g);
		
		// Check whether there is something to draw
		if (selectedRegion == null)
			return;
		
		// Draw all subregions
		int focusY = 0;
		int maxY = 0;
		long baseAddress = selectedRegion.getData().getAddress();
		for (int i = 0; i < selectedRegion.getNumberOfSubRegions(); i++) {
			SubRegion subRegion = selectedRegion.getSubRegion(i);
			for (int j = 0; j < subRegion.getNumberOfRegionData(); j++) {
				RegionData regionData = subRegion.getRegionData(j);
				
				// Draw regiondata
				int y = offsetY + (int)((regionData.getAddress() - baseAddress) * scale);
				int height = (int)(regionData.getSize() * scale);
				
				// Draw selected different
				if (subRegion == selectedSubRegion) {
					g.setColor(Color.RED);
					g.fillRect(offsetX, y, width, height);
					g.setColor(Color.BLACK);
				}
				
				g.drawRect(offsetX, y, width, height);
				
				if (regionData == selectedRegionData) {
					focusY = y;
					g.setColor(Color.BLUE);
					g.drawLine(offsetX, y, offsetX + width + selectedRegionWidth, y);
					g.setColor(Color.BLACK);
				}
				
				// Determine maxY
				if ((height + y) > maxY)
					maxY = height + y;
			}
		}
		
		// Resize panel
		int preferredWidth = 2 * offsetX + width + selectedRegionWidth;
		int preferredHeight = offsetY + maxY;
		super.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
		if (focus) {
			focus = false;
			super.scrollRectToVisible(new Rectangle(0, focusY, 1, (int) (8*scale)));
		}
		super.revalidate();
	}
}
