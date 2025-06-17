package frames;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import transformers.GDecorator;

public class GChatbotDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final JTextArea chatArea;
    private final JTextField inputField;
    private final JButton sendButton;
    @SuppressWarnings("unused")
	private final GDecorator decorator;
    @SuppressWarnings("unused")
	private final GDrawingPanel drawingPanel;

    public GChatbotDialog(GDecorator decorator, GDrawingPanel drawingPanel) {
        super((Frame) null, "AI 도형 꾸미기 챗봇", true);
        this.decorator = decorator;
        this.drawingPanel = drawingPanel;

        this.setSize(550, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("전송");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        this.add(inputPanel, BorderLayout.SOUTH);

        chatArea.append("챗봇: 안녕하세요. 도형을 어떻게 꾸며드릴까요?\n");

        Runnable sendAction = () -> {
            String userInput = inputField.getText().trim();
            if (userInput.isEmpty()) return;
            if (userInput.equalsIgnoreCase("종료")) {
                dispose();
                return;
            }

            chatArea.append("사용자: " + userInput + "\n");
            inputField.setText("");

            try {
                String response = decorator.callGptApi(userInput);
                chatArea.append("챗봇: " + response + "\n\n");
                decorator.applyStyleFromResponse(response);
                drawingPanel.repaint(); // 즉시 반영
            } catch (Exception e) {
                chatArea.append("오류: 챗봇 호출 실패 - " + e.getMessage() + "\n");
            }
        };

        sendButton.addActionListener(e -> sendAction.run());
        inputField.addActionListener(e -> sendAction.run());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void showDialog() {
        this.setVisible(true);
    }
}