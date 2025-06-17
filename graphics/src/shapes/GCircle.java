package shapes;

import java.awt.geom.Ellipse2D;

public class GCircle extends GShape {
    private static final long serialVersionUID = 1L;

    public GCircle() {
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
        double diameter = Math.max(Math.abs(x - startX), Math.abs(y - startY));
        double newX = (x < startX) ? x : startX;
        double newY = (y < startY) ? y : startY;
        ellipse.setFrame(newX, newY, diameter, diameter);
    }

    @Override
    public void dragPoint(int x, int y) {
        this.addPoint(x, y);
    }
}