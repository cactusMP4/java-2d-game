package jade;

import components.Sprite;
import components.SpriteRender;
import components.SpriteSheet;
import org.joml.Vector2f;
import render.Texture;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private SpriteSheet spriteSheet;
    private GameObject niko;
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera();
        spriteSheet = AssetPool.getSpriteSheet("src/assets/textures/spritesheet.png");

        niko = new GameObject("niko", new Transform(new Vector2f(0,100), new Vector2f(96,128)));
        niko.addComponent(new SpriteRender(spriteSheet.getSprite(3)));
        this.addGameObjectToScene(niko);
    }
    private void loadResources() {
        AssetPool.getShader("src/assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("src/assets/textures/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("src/assets/textures/spritesheet.png"), 24,32,12));
    }

    private int spriteIndex = 3;
    private float spriteTime = 0.2f;
    private float spriteTimeLeft = 0.0f;
    @Override
    public void render(float deltaTime) {
        niko.transform.position.x += 69 * deltaTime;
        spriteTimeLeft -= deltaTime;
        if(spriteTimeLeft <= 0.0f){
            spriteTimeLeft = spriteTime;
            spriteIndex++;
            if (spriteIndex > 5){
                spriteIndex = 3;
            }
            niko.getComponent(SpriteRender.class).setSprite(spriteSheet.getSprite(spriteIndex));
        }

        for (GameObject go : this.gameObjects){
            go.update(deltaTime);
        }
        this.renderer.render();
    }
}