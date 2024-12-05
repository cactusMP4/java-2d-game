package jade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import render.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeObject = null;
    protected boolean levelLoaded = false;
    
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
        gameObjects.add(go);
        if (isRunning) {
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void render(float deltaTime);

    public Camera getCamera() {return this.camera;}

    public void sceneImgui() {
        if (activeObject != null) {
            ImGui.begin("Inspector");
            activeObject.imgui();
            ImGui.end();
        }
        imgui();
    }
    public void imgui(){

    }

    public void save(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        try {
            FileWriter writer = new FileWriter("level.json");
            writer.write(gson.toJson(gameObjects));
            writer.close();
        } catch (IOException e){
            e.printStackTrace(System.err);
        }
    }

    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.json")));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        if (!inFile.equals("")) {
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);
            }
            this.levelLoaded = true;
        }
    }
}
