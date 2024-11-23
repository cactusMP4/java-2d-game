package jade;

import render.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    
    private boolean isRunning = false;

    public Scene() {}
    public void init(){}

    public void start(){
        for (GameObject go : gameObjects){
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }
    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void render(float deltaTime);

    public Camera getCamera() {
        return this.camera;
    }
}
