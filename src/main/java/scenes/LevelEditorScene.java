package scenes;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import render.DebugDraw;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private SpriteSheet spriteSheet;

    MouseControls mouseControls = new MouseControls();
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera();
        spriteSheet = AssetPool.getSpriteSheet("spritesheets/spritesheet.png");

        DebugDraw.addLine2D(new Vector2f(200,200), new Vector2f(800,700), new Vector3f(0,1,0), 100);

        if(levelLoaded){
            this.activeObject = gameObjects.getFirst();
            this.activeObject.addComponent(new RigidBody());
            return;
        }

        GameObject block = new GameObject("block", new Transform(new Vector2f(400,150), new Vector2f(100,100)));
        SpriteRender blockSprite = new SpriteRender();
        blockSprite.setColor(new Vector4f(0.3f, 1, 0.3f, 1));
        block.addComponent(blockSprite);
        block.addComponent(new RigidBody());
        this.addGameObjectToScene(block);

        GameObject niko = new GameObject("niko", new Transform(new Vector2f(200,150), new Vector2f(100,100)));
        SpriteRender nikoSprite = new SpriteRender();
        nikoSprite.setSprite(spriteSheet.getSprite(7));
        niko.addComponent(nikoSprite);
        niko.addComponent(new RigidBody());
        this.addGameObjectToScene(niko);

        this.activeObject=block;
    }
    private void loadResources() {
        AssetPool.getShader("default.glsl");
        AssetPool.addSpriteSheet("spritesheets/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("spritesheets/spritesheet.png"), 24,32,12));
    }

    @Override
    public void render(float deltaTime) {
        mouseControls.update(deltaTime);

        for (GameObject go : this.gameObjects){
            go.update(deltaTime);
        }
        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("some blocks");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < spriteSheet.getSize(); i++){
            Sprite sprite = spriteSheet.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCords = sprite.getTexCords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCords[0].x, texCords[0].y, texCords[2].x, texCords[2].y)){
                GameObject object = Prefabs.generateSpriteObj(sprite, spriteWidth, spriteHeight);
                mouseControls.pickupObj(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < spriteSheet.getSize() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}