package transformers;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import global.GConstants.EAnchor;
import shapes.GShape;

public class GResizer extends GTransformer {

	private GShape shape;
	
	private int px, py;
	private int cx, cy;
	private EAnchor eReiszeAnchor;
	
	public GResizer(GShape shape) {
		super(shape);
		this.shape = shape;
		this.eReiszeAnchor = null;
	}
	public void start(Graphics2D graphics, int x, int y) {
		this.px = x;
		this.py = y;
		Rectangle r = this.shape.getBounds();
		
		EAnchor eSlectedAnchor = this.shape.getESelectedAnchor();
		switch (eSlectedAnchor) {
		case eNW: eReiszeAnchor = EAnchor.eSE; cx=r.x+r.width; 		cy=r.y+r.height; 	break;
		case eWW: eReiszeAnchor = EAnchor.eEE; cx=r.x+r.width;		cy=r.y+r.height/2; 	break;				
		case eSW: eReiszeAnchor = EAnchor.eNE; cx=r.x+r.width;		cy=r.y; 			break;				
		case eSS: eReiszeAnchor = EAnchor.eNN; cx=r.x+r.width/2;	cy=r.y; 			break;				
		case eSE: eReiszeAnchor = EAnchor.eNW; cx=r.x; 				cy=r.y;			 	break;				
		case eEE: eReiszeAnchor = EAnchor.eWW; cx=r.x; 				cy=r.y+r.height/2; 	break;				
		case eNE: eReiszeAnchor = EAnchor.eSW; cx=r.x; 				cy=r.y+r.height; 	break;				
		case eNN: eReiszeAnchor = EAnchor.eSS; cx=r.x+r.width/2;	cy=r.y+r.height; 	break;				
		default:
			break;
		}
		
	}
	public void drag(Graphics2D graphics, int x, int y) {
		
		double dx =0; double dy=0;
		switch (eReiszeAnchor) {
			case eNW: dx = (x-px); 	dy = (y-py); 	break;
			case eWW: dx = (x-px); 	dy = 0; 		break;				
			case eSW: dx = (x-px); 	dy = -(y-py);  	break;				
			case eSS: dx = 0; 		dy = -(y-py);  	break;				
			case eSE: dx = -(x-px); dy = -(y-py);  	break;				
			case eEE: dx = -(x-px); dy = 0;  		break;				
			case eNE: dx = -(x-px); dy = (y-py);  	break;				
			case eNN: dx = 0; 		dy = (y-py);  	break;				
			default: break;
		}
		Shape transformedShape = this.shape.getTransformedShape();
		double w1 = transformedShape.getBounds().width;
		double w2 = dx + w1;
		double h1 = transformedShape.getBounds().height;
		double h2 = dy + h1;
		
		double xScale = w2/w1;
		double yScale = h2/h1;
		
		this.shape.getAffineTransform().translate(cx, cy);
		this.shape.getAffineTransform().scale(xScale, yScale);
		this.shape.getAffineTransform().translate(-cx, -cy);
		
		px = x;
		py = y;
	}
	public void finish(Graphics2D graphics, int x, int y) {
	}
	@Override
	public void addPoint(Graphics2D graphics, int x, int y) {
	}
}