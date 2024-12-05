package jade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Sprite;
import components.SpriteRender;
import components.SpriteSheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import render.Texture;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private SpriteSheet spriteSheet;
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera();
        if(levelLoaded){return;}

        spriteSheet = AssetPool.getSpriteSheet("src/assets/textures/spritesheet.png");

        GameObject block = new GameObject("block", new Transform(new Vector2f(400,150), new Vector2f(100,100)));
        SpriteRender blockSprite = new SpriteRender();
        blockSprite.setColor(new Vector4f(0.3f, 1, 0.3f, 1));
        block.addComponent(blockSprite);
        this.addGameObjectToScene(block);

        GameObject niko = new GameObject("niko", new Transform(new Vector2f(200,150), new Vector2f(100,100)));
        SpriteRender nikoSprite = new SpriteRender();
        nikoSprite.setSprite(spriteSheet.getSprite(7));
        niko.addComponent(nikoSprite);
        this.addGameObjectToScene(niko);

        this.activeObject=block;
    }
    private void loadResources() {
        AssetPool.getShader("src/assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("src/assets/textures/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("src/assets/textures/spritesheet.png"), 24,32,12));
    }

    @Override
    public void render(float deltaTime) {
        for (GameObject go : this.gameObjects){
            go.update(deltaTime);
        }
        this.renderer.render();
    }

    @Override
    public void imgui(){

    }
}