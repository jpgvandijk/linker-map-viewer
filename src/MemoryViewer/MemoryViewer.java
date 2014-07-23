package MemoryViewer;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class MemoryViewer extends JFrame
{
	private static final long serialVersionUID = 1L;

	// Attributes
	private MemoryViewerMenu memoryViewerMenu;
	private MemoryConfigurationAndContentsComponent memoryConfigurationAndContentsComponent;
	private MemoryMapComponent memoryMapComponent;
	private MemoryDetailsComponent memoryDetailsComponent;
	
	// Constructor
	public MemoryViewer (String fileNameAndPath) {
		
		// Create contents
		memoryViewerMenu = new MemoryViewerMenu();
		memoryConfigurationAndContentsComponent = new MemoryConfigurationAndContentsComponent();
		memoryMapComponent = new MemoryMapComponent();
		memoryDetailsComponent = new MemoryDetailsComponent();
		
		// Add listeners
		memoryViewerMenu.addMemoryInfoUpdateListener(memoryMapComponent);
		memoryViewerMenu.addMemoryInfoUpdateListener(memoryConfigurationAndContentsComponent);
		memoryConfigurationAndContentsComponent.addMemoryDetailsListener(memoryDetailsComponent);
		memoryMapComponent.addMemoryDetailsListener(memoryDetailsComponent);
		
		// Bind contents
		setJMenuBar(memoryViewerMenu);
		JSplitPane memoryInfoPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane memoryDetailsPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		memoryInfoPane.setLeftComponent(memoryConfigurationAndContentsComponent);
		memoryInfoPane.setRightComponent(memoryMapComponent);
		memoryDetailsPane.setLeftComponent(memoryInfoPane);
		memoryDetailsPane.setRightComponent(memoryDetailsComponent);
		getContentPane().add(memoryDetailsPane);
		
		// Setup frame
		setTitle("Memory Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
		setVisible(true);
		
		// Load file if specified
		if (fileNameAndPath != null) {
			File file = new File(fileNameAndPath);
			memoryViewerMenu.load(file);
		}
	}
	
	// Program entry point
	public static void main (String[] args) {
		
		if (args.length > 0)
			new MemoryViewer(args[0]);
		else
			new MemoryViewer(null);
	}
}