package jade;

import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private final boolean[] mouseButtonPressed = new boolean[9];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener getInstance() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }
    public static void mousePosCallback(long window, double xPos, double yPos) {
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().xPos = xPos;
        getInstance().yPos = yPos;
        getInstance().isDragging = getInstance().mouseButtonPressed[0]
                                || getInstance().mouseButtonPressed[1]
                                || getInstance().mouseButtonPressed[2];
    }
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button < getInstance().mouseButtonPressed.length) {
            if (action == GLFW_PRESS) {
                getInstance().mouseButtonPressed[button] = true;
            } else if (action == GLFW_RELEASE) {
                getInstance().mouseButtonPressed[button] = false;
                getInstance().isDragging = false;
            }
        }
    }
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }
    public static void endFrame() {
        getInstance().scrollX = 0.0;
        getInstance().scrollY = 0.0;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
    }
    public static float getMouseX() {return (float) getInstance().xPos;}
    public static float getMouseY() {return (float) getInstance().yPos;}
    public static float getOrthoX() {
        float currentX = getMouseX();
        currentX = (currentX / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        tmp.mul(Window.getScene().getCamera().getInverseProjection()).mul(Window.getScene().getCamera().getInverseView());
        currentX = tmp.x;

        return currentX;
    }
    public static float getOrthoY() {
        float currentY = Window.getHeight() - getMouseY();
        currentY = (currentY / (float) Window.getHeight()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);
        tmp.mul(Window.getScene().getCamera().getInverseProjection()).mul(Window.getScene().getCamera().getInverseView());
        currentY = tmp.y;

        return currentY;
    }
    public static float getMouseDx() {return (float) (getInstance().lastX - getInstance().xPos);}
    public static float getMouseDy() {return (float) (getInstance().lastY - getInstance().yPos);}
    public static float getMouseScrollX() {return (float) (getInstance().scrollX);}
    public static float getMouseScrollY() {return (float) (getInstance().scrollY);}
    public static boolean isDragging() {return getInstance().isDragging;}
    public static boolean mouseButtonDown(int button) {
        if (button < getInstance().mouseButtonPressed.length) {
            return getInstance().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
