package clipboard;

import shapes.GShape;

public class GClipboardManager {
    private static GShape clipboardShape = null;

    public static void copy(GShape shape) {
        clipboardShape = shape; // 얕은 복사, 필요 시 deep copy 구현
    }

    public static GShape paste() {
        return clipboardShape == null ? null : cloneShape(clipboardShape);
    }

    public static boolean hasContent() {
        return clipboardShape != null;
    }

    private static GShape cloneShape(GShape shape) {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(bos);
            out.writeObject(shape);
            out.flush();
            out.close();

            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(bis);
            return (GShape) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
