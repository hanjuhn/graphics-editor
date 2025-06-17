package transformers;

import org.json.JSONArray;
import org.json.JSONObject;
import shapes.GShape;
import frames.GChatbotDialog;
import frames.GDrawingPanel;
import global.GConstants;

import javax.net.ssl.HttpsURLConnection;

import java.awt.Color;
import java.io.*;
import java.net.URL;

public class GDecorator {

    private GShape shape;

    public GDecorator(GShape shape) {
        this.shape = shape;
    }

    public GShape getShape() {
        return this.shape;
    }

    public String callGptApi(String prompt) throws IOException {
    	String apiKey = GConstants.GApi.OPENAI_API_KEY;
        @SuppressWarnings("deprecation")
		URL url = new URL("https://api.openai.com/v1/chat/completions");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);

        JSONObject requestJson = new JSONObject()
            .put("model", "gpt-3.5-turbo")
            .put("messages", new JSONArray()
                .put(new JSONObject().put("role", "system").put("content",
                    "당신은 사용자의 도형 꾸미기 요청을 받아, 어떤 스타일이 적용되었는지 한국어로 간결하게 요약해주는 도우미입니다. 예를 들어 사용자가 '도형을 빨간색으로 굵게 그려줘'라고 입력하면, '네 도형을 빨간색으로 그리고 선을 굵게 설정했습니다.'처럼 응답해야 합니다."))
                .put(new JSONObject().put("role", "user").put("content", prompt))
            );

        OutputStream os = conn.getOutputStream();
        os.write(requestJson.toString().getBytes());
        os.flush();
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        JSONObject responseJson = new JSONObject(response.toString());
        return responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }

    public void applyStyleFromResponse(String response) {
        String lower = response.toLowerCase();

        // 색상 키워드 매핑
        if (lower.contains("빨간") || lower.contains("red")) {
            shape.setLineColor(Color.RED);
        } else if (lower.contains("파란") || lower.contains("blue")) {
            shape.setLineColor(Color.BLUE);
        } else if (lower.contains("초록") || lower.contains("green")) {
            shape.setLineColor(Color.GREEN);
        } else if (lower.contains("노란") || lower.contains("yellow")) {
            shape.setLineColor(Color.YELLOW);
        } else if (lower.contains("검정") || lower.contains("black")) {
            shape.setLineColor(Color.BLACK);
        } else if (lower.contains("하늘") || lower.contains("cyan") || lower.contains("라이트블루")) {
            shape.setLineColor(Color.CYAN);
        } else if (lower.contains("보라") || lower.contains("purple") || lower.contains("magenta")) {
            shape.setLineColor(Color.MAGENTA);
        } else if (lower.contains("회색") || lower.contains("gray") || lower.contains("grey")) {
            shape.setLineColor(Color.GRAY);
        } else if (lower.contains("하얀") || lower.contains("흰") || lower.contains("white")) {
            shape.setLineColor(Color.WHITE);
        } else if (lower.contains("주황") || lower.contains("orange")) {
            shape.setLineColor(Color.ORANGE);
        } else if (lower.contains("분홍") || lower.contains("pink")) {
            shape.setLineColor(Color.PINK);
        } else if (lower.contains("갈색") || lower.contains("brown")) {
            shape.setLineColor(new Color(139, 69, 19)); 
        } else if (lower.contains("연두") || lower.contains("lime")) {
            shape.setLineColor(new Color(50, 205, 50)); 
        } else if (lower.contains("남색") || lower.contains("navy")) {
            shape.setLineColor(new Color(0, 0, 128)); 
        } else if (lower.contains("올리브") || lower.contains("olive")) {
            shape.setLineColor(new Color(128, 128, 0)); 
        }

        // 선 굵기 키워드 매핑
        if (lower.contains("굵게") || lower.contains("두껍게") || lower.contains("thick")) {
            shape.setStrokeWidth(5.0f);
        } else if (lower.contains("얇게") || lower.contains("thin")) {
            shape.setStrokeWidth(1.0f);
        } else {
            // 숫자 기반 파싱
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(\\.\\d+)?)\\s*(픽셀|px|pt|두께)?").matcher(lower);
            if (m.find()) {
                try {
                    float thickness = Float.parseFloat(m.group(1));
                    shape.setStrokeWidth(thickness);
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    public void runChatbotWithDialog(GDrawingPanel panel) {
        new GChatbotDialog(this, panel).showDialog();
    }
}