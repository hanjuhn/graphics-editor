package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import menus.GDecorateMenu;
import menus.GFileMenu;

public class GMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private GFileMenu fileMenu;
	private GDecorateMenu decorateMenu;
	private GShapeToolPanel toolPanel;
	private GDrawingPanel drawingPanel;
	private GPreviewPanel previewPanel;

	public GMainFrame() {
		this.setTitle("Graphics Editor");
		this.setLocation(100, 100);
		this.setSize(1200, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 메인 도화지 생성
		this.drawingPanel = new GDrawingPanel();
		this.drawingPanel.setBackground(Color.WHITE);

		// 미리보기 패널 생성 (더 작게)
		this.previewPanel = new GPreviewPanel();
		this.previewPanel.setPreferredSize(new Dimension(200, 200));
		this.previewPanel.setMaximumSize(new Dimension(200, 200));
		this.previewPanel.setBackground(Color.LIGHT_GRAY);
		this.previewPanel.associate(this.drawingPanel);
		this.drawingPanel.associatePreviewPanel(this.previewPanel);

		// 도구 패널
		this.toolPanel = new GShapeToolPanel();
		this.toolPanel.associate(this.drawingPanel);

		// 메뉴
		this.fileMenu = new GFileMenu();
		this.decorateMenu = new GDecorateMenu();
		this.fileMenu.associate(this.drawingPanel);
		this.decorateMenu.associate(this.drawingPanel);

		// 메뉴바 설정
		JMenuBar swingMenuBar = new JMenuBar();
		swingMenuBar.add(this.fileMenu);
		swingMenuBar.add(this.decorateMenu);
		this.setJMenuBar(swingMenuBar);

		// 좌측 사이드 패널 (작은 미리보기 포함)
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		sidePanel.setBackground(Color.lightGray);
		sidePanel.setPreferredSize(new Dimension(200, 100));

		// 미리보기 위치 정렬
		this.previewPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidePanel.add(Box.createVerticalStrut(10));
		sidePanel.add(this.previewPanel);
		sidePanel.add(Box.createVerticalGlue());

		// 전체 프레임 구성
		this.setLayout(new BorderLayout());
		this.add(sidePanel, BorderLayout.WEST);      // 왼쪽: 보조 패널
		this.add(toolPanel, BorderLayout.NORTH);     // 상단: 도구 툴
		this.add(drawingPanel, BorderLayout.CENTER); // 중앙: 도화지
	}

	public void initialize() {
		this.setVisible(true);
		this.fileMenu.initialize();
		this.toolPanel.initialize();
		this.drawingPanel.initialize();
		this.previewPanel.initialize();
	}
}