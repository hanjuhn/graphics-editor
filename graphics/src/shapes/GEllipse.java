package shapes;

import java.awt.geom.Ellipse2D;

public class GEllipse extends GShape {
    private static final long serialVersionUID = 1L;

    public GEllipse() {
        super(new Ellipse2D.Double());
    }

    @Override
    public void setPoint(int x, int y) {
        Ellipse2D ellipse = (Ellipse2D) this.getShape();
        ellipse.setFrame(x, y, 0, 0);
    }

    @Override
    public void addPoint(int x, int y) {
        Ellipse2D ellipse = (Ellipse2D) this.getShape();
        double startX = ellipse.getX();
        double startY = ellipse.getY();
        double width = x - startX;
        double height = y - startY;
        ellipse.setFrame(
            (width < 0) ? x : startX,
            (height < 0) ? y : startY,
            Math.abs(width),
            Math.abs(height)
        );
    }

    @Override
    public void dragPoint(int x, int y) {
        this.addPoint(x, y);
    }
}