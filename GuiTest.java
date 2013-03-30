import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class GuiTest {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Window window = new Window();
				window.setVisible(true);
			}
		});
	}
}

class Window extends JFrame {
	private static final long serialVersionUID = -6749154936109796560L;

	public Window() {
		this.initUI();
	}

	public void initUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {}
		
		this.setTitle("List adder");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);
		
		String[] entries = { "Alice", "Bob", "Carol", "Dennis", "Eve", "Fred" };
		final DefaultListModel<String> sampleModel = new DefaultListModel<String>();
		
		for (int i = 0; i < entries.length; i++) {
			sampleModel.addElement(entries[i]);
		}
		
		final JList<String> sampleJList = new JList<String>(sampleModel);
		sampleJList.setVisibleRowCount(4);
		sampleJList.setFixedCellWidth(150);
		
		JScrollPane listPane = new JScrollPane(sampleJList);
		
		JPanel listPanel = new JPanel();
		Border listPanelBorder = BorderFactory.createTitledBorder("Sample JList");
		listPanel.setBorder(listPanelBorder);
		listPanel.add(listPane);
		
		this.getContentPane().add(listPanel, BorderLayout.NORTH);
		
		final JTextField textField = new JTextField(20);
		
		JButton addButton = new JButton("Add entry");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int index = sampleModel.getSize();
				String text = textField.getText();
				sampleModel.addElement(text);
				textField.selectAll();
				sampleJList.setSelectedIndex(index);
				sampleJList.ensureIndexIsVisible(index);
			}
		});
		
		JPanel lowerPanel = new JPanel();
		Border lowerPanelBorder = BorderFactory.createTitledBorder("Adding entries");
		lowerPanel.setBorder(lowerPanelBorder);
		lowerPanel.add(textField, BorderLayout.WEST);
		lowerPanel.add(addButton, BorderLayout.EAST);
		this.getContentPane().add(lowerPanel, BorderLayout.SOUTH);
		this.pack();
	}
}