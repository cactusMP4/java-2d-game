package jade;

import components.Sprite;
import components.SpriteRender;
import components.SpriteSheet;
import org.joml.Vector2f;
import render.Texture;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera();

        SpriteSheet sprites = AssetPool.getSpriteSheet("src/assets/textures/spritesheet.png");

        for (int i = 0; i < 12; i++){
            GameObject obj = new GameObject(Integer.toString(i), new Transform(new Vector2f(i*48,100), new Vector2f(48,64)));
            obj.addComponent(new SpriteRender(sprites.getSprite(i)));
            this.addGameObjectToScene(obj);
        }

        GameObject ss = new GameObject("SpriteSheet", new Transform(new Vector2f(0,200), new Vector2f(144,256)));
        ss.addComponent(new SpriteRender(new Sprite(new Texture("src/assets/textures/spritesheet.png"))));
        this.addGameObjectToScene(ss);
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
}