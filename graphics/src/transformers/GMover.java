package transformers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import shapes.GShape;

public class GMover extends GTransformer {

	private int px, py;
	
	public GMover(GShape shape) {
		super(shape);
	}

	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.px = x;
		this.py = y;
	}

	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		int dx = x - px;
		int dy = y - py;

		AffineTransform transform = new AffineTransform();
		transform.translate(dx, dy);
		shape.transform(transform);
		shape.updateBounds(); // 이동 후 기준 갱신 (회전된 도형 포함)

		this.px = x;
		this.py = y;
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {
		// 이동 후 추가 동작 없음
	}

	@Override
	public void addPoint(Graphics2D graphics, int x, int y) {
		// 이동에는 불필요
	}
}