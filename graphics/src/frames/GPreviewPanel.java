package frames;

import javax.swing.JPanel;
import java.awt.*;
import java.util.Vector;
import shapes.GShape;

public class GPreviewPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private GDrawingPanel drawingPanel;

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	public void initialize() {
		this.setBackground(new Color(240, 240, 240)); // 밝은 회색
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (drawingPanel == null) return;

		Graphics2D g2D = (Graphics2D) g.create();

		Vector<GShape> shapes = drawingPanel.getShapes();
		Dimension drawingSize = drawingPanel.getSize();
		Dimension previewSize = this.getSize();

		if (drawingSize.width == 0 || drawingSize.height == 0) return;

		double xScale = previewSize.getWidth() / drawingSize.getWidth();
		double yScale = previewSize.getHeight() / drawingSize.getHeight();
		double scale = Math.min(xScale, yScale);

		int offsetX = (int) ((previewSize.getWidth() - drawingSize.getWidth() * scale) / 2);
		int offsetY = (int) ((previewSize.getHeight() - drawingSize.getHeight() * scale) / 2);

		g2D.translate(offsetX, offsetY);
		g2D.scale(scale, scale);


		for (GShape shape : shapes) {
			shape.draw(g2D);
		}

		g2D.dispose();
	}
}