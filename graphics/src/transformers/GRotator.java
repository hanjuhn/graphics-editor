package transformers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import shapes.GShape;

public class GRotator extends GTransformer {

    private double centerX;
    private double centerY;
    private double initialAngle;

    public GRotator(GShape shape) {
        super(shape);
    }

    @Override
    public void start(Graphics2D graphics, int x, int y) {
        // 회전 중심 좌표 계산
        centerX = shape.getBounds().getCenterX();
        centerY = shape.getBounds().getCenterY();

        // 초기 마우스 위치에 따른 각도 저장
        initialAngle = Math.atan2(y - centerY, x - centerX);
    }

    @Override
    public void drag(Graphics2D graphics, int x, int y) {
        // 현재 마우스 위치에 따른 각도 계산
        double currentAngle = Math.atan2(y - centerY, x - centerX);
        double rotationAngle = currentAngle - initialAngle;

        // 도형에 회전 변환 적용
        AffineTransform transform = new AffineTransform();
        transform.rotate(rotationAngle, centerX, centerY);
        shape.transform(transform);

        // 회전 후 기준점 정보 갱신 (이동이 잘 되도록)
        shape.updateBounds(); // GShape의 서브클래스에 정의되어야 함

        // 다음 드래그를 위한 초기 각도 갱신
        initialAngle = currentAngle;
    }

    @Override
    public void finish(Graphics2D graphics, int x, int y) {
        // 회전 완료 후 별도 처리 없음
    }

    @Override
    public void addPoint(Graphics2D graphics, int x, int y) {
        // 회전에서는 점 추가 필요 없음
    }
}