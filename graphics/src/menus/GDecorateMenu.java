package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.GDrawingPanel;

public class GDecorateMenu extends JMenu {
    private static final long serialVersionUID = 1L;

    private GDrawingPanel drawingPanel;

    public GDecorateMenu() {
        super("Style");

        JMenuItem decorateShapeItem = new JMenuItem("AI 도형 꾸미기 챗봇");
        decorateShapeItem.addActionListener(e -> {
            if (drawingPanel != null) {
                drawingPanel.promptDecorateShape();  // GDrawingPanel이 GDecorator와 연결되어 있어야 함
            }
        });

        this.add(decorateShapeItem);
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}