// GShape.java
package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import global.GConstants.EAnchor;

public abstract class GShape implements Serializable {
    private static final long serialVersionUID = 1L;

    private final static int ANCHOR_W = 10;
    private final static int ANCHOR_H = 10;

    public enum EPoints {
        e2P,
        eNP
    }

    protected Shape shape;
    protected AffineTransform affineTransform;

    private Ellipse2D anchors[];
    private boolean bSelected;
    private EAnchor eSelectedAnchor;

    private Color lineColor = Color.BLACK;
    private float strokeWidth = 1.0f;

    public AffineTransform getAffineTransform() {
        return this.affineTransform;
    }

    public GShape(Shape shape) {
        this.shape = shape;
        this.affineTransform = new AffineTransform();

        this.anchors = new Ellipse2D[EAnchor.values().length - 1];
        for (int i = 0; i < this.anchors.length; i++) {
            this.anchors[i] = new Ellipse2D.Double();
        }
        this.bSelected = false;
        this.eSelectedAnchor = null;
    }

    public Shape getShape() {
        return this.shape;
    }

    public Shape getTransformedShape() {
        return this.affineTransform.createTransformedShape(this.shape);
    }

    public void transform(AffineTransform at) {
        this.affineTransform.concatenate(at);
    }

    public void updateBounds() {
        this.shape = this.affineTransform.createTransformedShape(this.shape);
        this.affineTransform = new AffineTransform();
        this.setAnchors();
    }

    public boolean isSelected() {
        return this.bSelected;
    }

    public void setSelected(boolean bSelected) {
        this.bSelected = bSelected;
    }

    public EAnchor getESelectedAnchor() {
        return this.eSelectedAnchor;
    }

    public Rectangle getBounds() {
        return this.shape.getBounds();
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    private void setAnchors() {
        Rectangle bounds = this.shape.getBounds();
        int bx = bounds.x;
        int by = bounds.y;
        int bw = bounds.width;
        int bh = bounds.height;

        int cx = 0;
        int cy = 0;
        for (int i = 0; i < this.anchors.length; i++) {
            switch (EAnchor.values()[i]) {
                case eSS: cx = bx + bw / 2; cy = by + bh; break;
                case eSE: cx = bx + bw; cy = by + bh; break;
                case eSW: cx = bx; cy = by + bh; break;
                case eNN: cx = bx + bw / 2; cy = by; break;
                case eNE: cx = bx + bw; cy = by; break;
                case eNW: cx = bx; cy = by; break;
                case eEE: cx = bx + bw; cy = by + bh / 2; break;
                case eWW: cx = bx; cy = by + bh / 2; break;
                case eRR: cx = bx + bw / 2; cy = by - 30; break;
                default: break;
            }
            anchors[i].setFrame(cx - ANCHOR_W / 2, cy - ANCHOR_H / 2, ANCHOR_W, ANCHOR_H);
        }
    }

    public void draw(Graphics2D graphics2D) {
        Color prevColor = graphics2D.getColor();
        Stroke prevStroke = graphics2D.getStroke();

        graphics2D.setColor(this.lineColor);
        graphics2D.setStroke(new java.awt.BasicStroke(this.strokeWidth));

        Shape transformedShape = this.affineTransform.createTransformedShape(shape);
        graphics2D.draw(transformedShape);

        graphics2D.setColor(prevColor);
        graphics2D.setStroke(prevStroke);

        if (bSelected) {
            this.setAnchors();
            for (int i = 0; i < this.anchors.length; i++) {
                Shape transformedAnchor = this.affineTransform.createTransformedShape(anchors[i]);
                Color penColor = graphics2D.getColor();
                graphics2D.setColor(graphics2D.getBackground());
                graphics2D.fill(transformedAnchor);
                graphics2D.setColor(penColor);
                graphics2D.draw(transformedAnchor);
            }
        }
    }

    public boolean contains(int x, int y) {
        if (bSelected) {
            for (int i = 0; i < this.anchors.length; i++) {
                Shape transformedAnchor = this.affineTransform.createTransformedShape(anchors[i]);
                if (transformedAnchor.contains(x, y)) {
                    this.eSelectedAnchor = EAnchor.values()[i];
                    return true;
                }
            }
        }
        Shape transformedShape = this.affineTransform.createTransformedShape(shape);
        if (transformedShape.contains(x, y)) {
            this.eSelectedAnchor = EAnchor.eMM;
            return true;
        }
        this.eSelectedAnchor = null;
        return false;
    }

    public boolean contains(GShape shape) {
        return this.shape.contains(shape.getShape().getBounds());
    }

    public boolean contains(Point p) {
        return this.shape.contains(p);
    }

    public abstract void setPoint(int x, int y);
    public abstract void addPoint(int x, int y);
    public abstract void dragPoint(int x, int y);
}