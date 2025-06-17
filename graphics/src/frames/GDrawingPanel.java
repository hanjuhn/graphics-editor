package frames;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import global.GConstants.EAnchor;
import global.GConstants.EShapeTool;
import popup.GPopupMenuBuilder;
import shapes.GShape;
import shapes.GShape.EPoints;
import transformers.GDecorator;
import transformers.GDrawer;
import transformers.GMover;
import transformers.GResizer;
import transformers.GTransformer;

public class GDrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public enum EDrawingState {
		eIdle,
		e2P,
		eNP
	}

	private Vector<GShape> shapes;
	private GTransformer transformer;
	private GShape currentShape;
	private GShape selectedShape;
	private EShapeTool eShapeTool;
	private EDrawingState eDrawingState;
	private boolean bUpdated;
	private boolean isPopupActive = false;

	private GPreviewPanel previewPanel; // 미리보기 패널 참조

	public GDrawingPanel() {
		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);

		this.shapes = new Vector<>();
		this.currentShape = null;
		this.selectedShape = null;
		this.eShapeTool = null;
		this.eDrawingState = EDrawingState.eIdle;
		this.bUpdated = false;
	}

	public void associatePreviewPanel(GPreviewPanel previewPanel) {
		this.previewPanel = previewPanel;
	}

	private void updatePreview() {
		if (this.previewPanel != null) {
			this.previewPanel.repaint();
		}
	}

	public void initialize() {
		this.shapes.clear();
		this.repaint();
		updatePreview();
	}

	public Vector<GShape> getShapes() {
		return this.shapes;
	}

	@SuppressWarnings("unchecked")
	public void setShapes(Object shapes) {
		this.shapes = (Vector<GShape>) shapes;
		this.repaint();
		updatePreview();
	}

	public void setEShapeTool(EShapeTool eShapeTool) {
		this.eShapeTool = eShapeTool;
	}

	public boolean isUpdated() {
		return this.bUpdated;
	}

	public void setBUpdated(boolean bUpdated) {
		this.bUpdated = bUpdated;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g2d = (Graphics2D) graphics;
		for (GShape shape : shapes) {
			shape.draw(g2d);
		}
	}

	private GShape onShape(int x, int y) {
		for (GShape shape : this.shapes) {
			if (shape.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}

	private void startTransform(int x, int y) {
		this.currentShape = eShapeTool.newShape();
		this.shapes.add(this.currentShape);

		if (this.eShapeTool == EShapeTool.eSelect) {
			this.selectedShape = onShape(x, y);

			if (this.selectedShape == null) {
				this.transformer = new GDrawer(this.currentShape);
			} else {
				EAnchor anchor = this.selectedShape.getESelectedAnchor();
				switch (anchor) {
					case eMM:
						this.transformer = new GMover(this.selectedShape);
						break;
					case eRR:
						this.transformer = new transformers.GRotator(this.selectedShape);
						break;
					default:
						this.transformer = new GResizer(this.selectedShape);
				}
			}
		} else {
			this.transformer = new GDrawer(this.currentShape);
		}

		this.transformer.start((Graphics2D) getGraphics(), x, y);
	}

	private void keepTransform(int x, int y) {
		if (transformer != null) {
			this.transformer.drag((Graphics2D) getGraphics(), x, y);
			this.repaint();
			updatePreview();
		}
	}

	private void addPoint(int x, int y) {
		if (transformer != null) {
			this.transformer.addPoint((Graphics2D) getGraphics(), x, y);
			updatePreview();
		}
	}

	private void finishTransform(int x, int y) {
		if (transformer != null) {
			this.transformer.finish((Graphics2D) getGraphics(), x, y);
			this.selectShape(this.currentShape);
			if (this.eShapeTool == EShapeTool.eSelect) {
				this.shapes.remove(this.shapes.size() - 1);
				for (GShape shape : this.shapes) {
					shape.setSelected(this.currentShape.contains(shape));
				}
			}
			this.bUpdated = true;
			this.repaint();
			updatePreview();
		}
	}

	private void selectShape(GShape shape) {
		for (GShape otherShape : this.shapes) {
			otherShape.setSelected(false);
		}
		if (shape != null) {
			shape.setSelected(true);
			this.selectedShape = shape;
		} else {
			this.selectedShape = null;
		}
	}

	private void changeCursor(int x, int y) {
		if (this.eShapeTool == EShapeTool.eSelect) {
			this.selectedShape = onShape(x, y);
			this.setCursor(this.selectedShape == null ? new Cursor(Cursor.DEFAULT_CURSOR) : this.selectedShape.getESelectedAnchor().getCursor());
		}
	}

	public void promptDecorateShape() {
		if (selectedShape == null) {
			JOptionPane.showMessageDialog(this, "먼저 도형을 선택해 주세요.", "알림", JOptionPane.WARNING_MESSAGE);
			return;
		}

		GDecorator decorator = new GDecorator(selectedShape);
		GChatbotDialog dialog = new GChatbotDialog(decorator, this);
		dialog.showDialog();
	}

	private class MouseHandler implements MouseListener, MouseMotionListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) mouse1Clicked(e);
			else if (e.getClickCount() == 2) mouse2Clicked(e);
		}

		private void mouse1Clicked(MouseEvent e) {
			if (isPopupActive || eShapeTool == null) return;

			if (eDrawingState == EDrawingState.eIdle) {
				if (eShapeTool.getEPoints() == EPoints.e2P) {
					startTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.e2P;
				} else if (eShapeTool.getEPoints() == EPoints.eNP) {
					startTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.eNP;
				}
			} else if (eDrawingState == EDrawingState.e2P) {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			} else if (eDrawingState == EDrawingState.eNP) {
				addPoint(e.getX(), e.getY());
			}
		}

		private void mouse2Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eNP) {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) handleRightClick(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) handleRightClick(e);
		}

		private void handleRightClick(MouseEvent e) {
			isPopupActive = true;

			if (eDrawingState != EDrawingState.eIdle) {
				if (currentShape != null) shapes.remove(currentShape);
				eDrawingState = EDrawingState.eIdle;
				transformer = null;
				currentShape = null;
				GDrawingPanel.this.repaint();
				updatePreview();
			}

			selectedShape = null;
			for (GShape shape : shapes) {
				if (shape.contains(e.getPoint())) {
					selectedShape = shape;
					selectShape(shape);
					break;
				}
			}

			JPopupMenu menu = GPopupMenuBuilder.build(
				selectedShape,
				GDrawingPanel.this::repaint,
				shapes,
				() -> {
					setBUpdated(true);
					GDrawingPanel.this.repaint();
					updatePreview();
				}
			);

			menu.addPopupMenuListener(popupListener());
			menu.show(e.getComponent(), e.getX(), e.getY());
		}

		private PopupMenuListener popupListener() {
			return new PopupMenuListener() {
				@Override 
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
					
				}
				@Override 
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					isPopupActive = false;
				}
				@Override public void popupMenuCanceled(PopupMenuEvent e) {
					isPopupActive = false;
				}
			};
		}

		@Override 
		public void mouseDragged(MouseEvent e) {
			
		}
		@Override public void mouseMoved(MouseEvent e) {
			if ((eDrawingState == EDrawingState.e2P || eDrawingState == EDrawingState.eNP) && transformer != null) {
				keepTransform(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eIdle) {
				if (eShapeTool != null) changeCursor(e.getX(), e.getY());
			}
		}
		@Override 
		public void mouseEntered(MouseEvent e) {
			
		}
		@Override public void mouseExited(MouseEvent e) {
			
		}
	}
}