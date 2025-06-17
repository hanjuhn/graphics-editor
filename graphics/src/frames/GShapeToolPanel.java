package frames;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import global.GConstants.EShapeTool;

public class GShapeToolPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private GDrawingPanel drawingPanel;
	private JComboBox<String> comboBox;

	public GShapeToolPanel() {
		this.setLayout(new BorderLayout()); // 패널이 NORTH에 붙으면 가로폭 자동 확장

		String[] shapeNames = new String[EShapeTool.values().length];
		for (int i = 0; i < EShapeTool.values().length; i++) {
			shapeNames[i] = EShapeTool.values()[i].getName();
		}
		comboBox = new JComboBox<>(shapeNames);

		// BorderLayout.CENTER로 추가하면 자동으로 패널 가로 길이를 따라감
		this.add(comboBox, BorderLayout.CENTER);

		comboBox.addActionListener(e -> {
			int index = comboBox.getSelectedIndex();
			EShapeTool selectedTool = EShapeTool.values()[index];
			if (drawingPanel != null) {
				drawingPanel.setEShapeTool(selectedTool);
			}
		});
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	public void initialize() {
		comboBox.setSelectedIndex(0);
	}
}