package shapes;

import java.awt.Polygon;

public class GTriangle extends GShape {
    private static final long serialVersionUID = 1L;

    private int startX, startY, endX, endY;

    public GTriangle() {
        super(new Polygon());
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.endX = x;
        this.endY = y;
        updateShape();
    }

    @Override
    public void addPoint(int x, int y) {
        this.endX = x;
        this.endY = y;
        updateShape();
    }

    @Override
    public void dragPoint(int x, int y) {
        addPoint(x, y);
    }

    private void updateShape() {
        int[] xPoints = {
            startX, (startX + endX) / 2, endX
        };
        int[] yPoints = {
            endY, startY, endY
        };
        Polygon triangle = new Polygon(xPoints, yPoints, 3);
        // set new shape
        try {
            java.lang.reflect.Field shapeField = GShape.class.getDeclaredField("shape");
            shapeField.setAccessible(true);
            shapeField.set(this, triangle);
        } catch (Exception e) {
            throw new RuntimeException("Shape 필드 접근 실패", e);
        }
    }
}