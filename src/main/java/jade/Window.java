package jade;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import render.DebugDraw;
import scenes.LevelEditorScene;
import scenes.LevelScene;
import scenes.Scene;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    //window stuff
    private int width, height;
    private final String title;
    private long glfwWindow;
    private static Window window = null;
    //scene stuff
    private static Scene currentScene;
    //colors stuff
    private ImGuiLayer imGuiLayer;

    public float r,g,b;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "2Dgame";
        r = 0.11f;
        g = 0.11f;
        b = 0.18f;
    }
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene :" + newScene;
                break;
        }
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }
    public static Scene getScene() {return currentScene;}

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
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        //create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL) {
            throw new IllegalStateException("Unable to create GLFW window");
        }
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        //OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //V-Sync
        glfwSwapInterval(1);

        //make window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        Window.changeScene(0);
    }
    private void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float deltaTime = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)) {
            //Poll events
            glfwPollEvents();

            DebugDraw.beginFrame();

            glClearColor(r,g,b,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (deltaTime >= 0.0f) {
                DebugDraw.draw();
                currentScene.render(deltaTime);
            }

            this.imGuiLayer.update(deltaTime, currentScene);

            glfwSwapBuffers(glfwWindow);

            //count time
            endTime = (float)glfwGetTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.save();
    }

    public static int getWidth() {return getWindow().width;}
    public static int getHeight() {return getWindow().height;}

    public static void setWidth(int newWidth) {
        getWindow().width = newWidth;
    }
    public static void setHeight(int newHeight) {
        getWindow().height = newHeight;
    }
}
