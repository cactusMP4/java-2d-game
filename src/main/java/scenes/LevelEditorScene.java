package scenes;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import util.AssetPool;
import util.Settings;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {
    private SpriteSheet  tilemap;

    GameObject levelEditorObj = new GameObject("LevelEditor");
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        levelEditorObj.addComponent(new MouseControls());
        levelEditorObj.addComponent(new GirdLines());

        loadResources();
        this.camera = new Camera();
        tilemap = AssetPool.getSpriteSheet("spritesheets/tilemap.png");

        if(levelLoaded){
            this.activeObject = gameObjects.getFirst();
            return;
        }
    }
    private void loadResources() {
        AssetPool.getShader("default.glsl");
        AssetPool.addSpriteSheet("spritesheets/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("spritesheets/spritesheet.png"), 24,32,12));
        AssetPool.addSpriteSheet("spritesheets/tilemap.png",
                new SpriteSheet(AssetPool.getTexture("spritesheets/tilemap.png"), 64,64,20));
    }

    @Override
    public void render(float deltaTime) {
        levelEditorObj.update(deltaTime);

        for (GameObject go : this.gameObjects){
            go.update(deltaTime);
        }

        //camera movement
        int defaultSpeed = 50;
        int sprintSpeed = 150;

        Map<Integer, Vector2f> keyBinds = new HashMap<>();
        keyBinds.put(GLFW_KEY_W, new Vector2f(0, 1));
        keyBinds.put(GLFW_KEY_A, new Vector2f(-1, 0));
        keyBinds.put(GLFW_KEY_S, new Vector2f(0, -1));
        keyBinds.put(GLFW_KEY_D, new Vector2f(1, 0));

        for (var entry : keyBinds.entrySet()){
            if (KeyListener.isKeyPressed(entry.getKey())){
                float speed = KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)? sprintSpeed : defaultSpeed;
                camera.position.add(entry.getValue().mul(speed*deltaTime));
            }
        }

        this.renderer.render();
    }

    @Override
    public void imgui(float dt){
        ImGui.begin("some blocks");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < tilemap.getSize(); i++){
            Sprite sprite = tilemap.getSprite(i);
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            int id = sprite.getTexId();
            Vector2f[] texCords = sprite.getTexCords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCords[3].x, texCords[3].y, texCords[1].x, texCords[1].y)){
                GameObject object = Prefabs.generateSpriteObj(sprite, spriteWidth, spriteHeight);
                levelEditorObj.getComponent(MouseControls.class).pickupObj(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < tilemap.getSize() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();

        ImGui.begin("Coordinates");

        GameObject object = levelEditorObj.getComponent(MouseControls.class).getHoldingObj();
        if (object != null) {
            ImGui.labelText("(" + object.transform.position.x/Settings.GRID_WIDTH + "; " + object.transform.position.y/Settings.GRID_HEIGHT + ")",
                    "(" + object.transform.position.x + "; " + object.transform.position.y + ")");
        } else {
            ImGui.labelText("-","-");
        }

        ImGui.end();

        ImGui.begin("FPS & DeltaTime");

        ImGui.labelText("dt: "+dt,"FPS: "+(int)(1/dt));

        ImGui.end();
    }
}