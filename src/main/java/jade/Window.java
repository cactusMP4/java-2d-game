package jade;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    //window stuff
    private final int width, height;
    private final String title;
    private long glfwWindow;
    private static Window window = null;
    //scene stuff
    private static Scene currentScene;
    //colors stuff
    public float r,g,b;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "2Dgame";
        r = 1;
        g = 1;
        b = 1;
    }
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene :" + newScene;
                break;
        }
    }
    public static Scene getScene() {
        return getWindow().currentScene;
    }

    public static Window getWindow() {
        if (window == null) {
            window = new Window();
        }
        return window;
    }
    public void run() {
        System.out.println("LWJGL version: " + Version.getVersion());
        init();
        loop();

        //free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //terminate GLFW and free err callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        //error callback
        GLFWErrorCallback.createPrint(System.err).set();
        //initialize GLFW
        if(!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        //config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL) {
            throw new IllegalStateException("Unable to create GLFW window");
        }
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //V-Sync
        glfwSwapInterval(1);

        //make window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0);
    }
    private void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float deltaTime = -1.0f;
        while(!glfwWindowShouldClose(glfwWindow)) {
            //Poll events
            glfwPollEvents();
            glClearColor(r,g,b,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (deltaTime >= 0.0f) {
                currentScene.render(deltaTime);
            }

            glfwSwapBuffers(glfwWindow);

            //count time
            endTime = (float)glfwGetTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;

            if (1/deltaTime < 30) {
                System.out.println("[WARNING]: low FPS: "+1/deltaTime);
            }
        }
    }
}
