package transformers;

import javax.swing.*;

import shapes.GShape;

import java.awt.event.*;

public class GSelecter extends MouseAdapter {
    private java.util.List<GShape> shapeList;
    private JPopupMenu popupMenu;
    private GShape selectedShape;

    public GSelecter(java.util.List<GShape> shapeList) {
        this.shapeList = shapeList;
        this.popupMenu = createPopupMenu();
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem infoItem = new JMenuItem("도형 정보 보기");
        infoItem.addActionListener(e -> showShapeInfo());

        JMenuItem deleteItem = new JMenuItem("도형 삭제");
        deleteItem.addActionListener(e -> deleteShape());

        menu.add(infoItem);
        menu.add(deleteItem);
        return menu;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (!e.isPopupTrigger()) return;

        // 도형이 있는지 확인하되 없으면 null
        selectedShape = null;
        for (GShape shape : shapeList) {
            if (shape.contains(e.getPoint())) {
                selectedShape = shape;
                break;
            }
        }

        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void showShapeInfo() {
        if (selectedShape != null) {
            JOptionPane.showMessageDialog(null, "도형 정보: " + selectedShape.toString());
        }
    }

    private void deleteShape() {
        if (selectedShape != null) {
            System.out.println("도형 삭제 요청됨: " + selectedShape);
            // 삭제 처리 필요
        }
    }
}