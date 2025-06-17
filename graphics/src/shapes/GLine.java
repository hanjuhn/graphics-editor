package shapes;

import java.awt.geom.Line2D;

public class GLine extends GShape {
    private static final long serialVersionUID = 1L;

    public GLine() {
        super(new Line2D.Double());
    }

    @Override
    public void setPoint(int x, int y) {
        Line2D line = (Line2D) this.getShape();
        line.setLine(x, y, x, y);
    }

    @Override
    public void addPoint(int x, int y) {
        Line2D line = (Line2D) this.getShape();
        line.setLine(line.getX1(), line.getY1(), x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        this.addPoint(x, y);
    }
}