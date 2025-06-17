package popup;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import clipboard.GClipboardManager;
import shapes.GShape;

public class GPopupMenuBuilder {
    public static JPopupMenu build(GShape selectedShape, Runnable repaintCallback,
                                   Vector<GShape> shapes, Runnable updateCallback) {
        JPopupMenu menu = new JPopupMenu();

        // 도형 정보 보기
        JMenuItem infoItem = new JMenuItem("도형 정보 보기");
        infoItem.addActionListener(e -> {
            if (selectedShape != null) {
                JOptionPane.showMessageDialog((Component) null,
                        "도형 정보:\n"
                        + "타입: " + selectedShape.getClass().getSimpleName() + "\n"
                        + "색상: " + selectedShape.getLineColor() + "\n"
                        + "선 두께: " + selectedShape.getStrokeWidth());
            } else {
                JOptionPane.showMessageDialog(null, "선택된 도형이 없습니다");
            }
        });

        // 복사
        JMenuItem copyItem = new JMenuItem("복사");
        copyItem.addActionListener(e -> {
            if (selectedShape != null) {
                GClipboardManager.copy(selectedShape);
            } else {
                JOptionPane.showMessageDialog(null, "선택된 도형이 없습니다");
            }
        });

        // 잘라내기
        JMenuItem cutItem = new JMenuItem("잘라내기");
        cutItem.addActionListener(e -> {
            if (selectedShape != null) {
                GClipboardManager.copy(selectedShape);
                shapes.remove(selectedShape);
                updateCallback.run();
            } else {
                JOptionPane.showMessageDialog(null, "선택된 도형이 없습니다");
            }
        });

        // 붙여넣기
        JMenuItem pasteItem = new JMenuItem("붙여넣기");
        pasteItem.addActionListener(e -> {
            if (GClipboardManager.hasContent()) {
                GShape pasted = GClipboardManager.paste();
                if (pasted != null) {
                    shapes.add(pasted);
                    updateCallback.run();
                }
            } else {
                JOptionPane.showMessageDialog(null, "붙여넣을 내용이 없습니다");
            }
        });
        
        // 전체 선택
        JMenuItem selectAllItem = new JMenuItem("전체 선택");
        selectAllItem.addActionListener(e -> {
            for (GShape shape : shapes) shape.setSelected(true);
            repaintCallback.run();
        });

        // 전체 선택 해제
        JMenuItem deselectAllItem = new JMenuItem("전체 선택 해제");
        deselectAllItem.addActionListener(e -> {
            for (GShape shape : shapes) shape.setSelected(false);
            repaintCallback.run();
        });

        // 전체 삭제
        JMenuItem deleteAllItem = new JMenuItem("전체 도형 삭제");
        deleteAllItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "정말 모든 도형을 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                shapes.clear();
                updateCallback.run();
            }
        });
        
        
    

        // 메뉴 구성
        menu.add(infoItem);
        menu.addSeparator();
        menu.add(copyItem);
        menu.add(cutItem);
        menu.add(pasteItem);
        menu.addSeparator();
        menu.add(selectAllItem);
        menu.add(deselectAllItem);
        menu.addSeparator();
        menu.add(deleteAllItem);

        return menu;
    }
}