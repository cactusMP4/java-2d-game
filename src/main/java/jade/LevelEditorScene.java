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

        GameObject blend1 = new GameObject("blend1", new Transform(new Vector2f(200,100), new Vector2f(100,100)));
        blend1.addComponent(new SpriteRender(new Sprite(AssetPool.getTexture("src/assets/textures/blendImage1.png"))));
        this.addGameObjectToScene(blend1);
        GameObject blend2 = new GameObject("blend2", new Transform(new Vector2f(230,150), new Vector2f(100,100)));
        blend2.addComponent(new SpriteRender(new Sprite(AssetPool.getTexture("src/assets/textures/blendImage2.png"))));
        this.addGameObjectToScene(blend2);

        niko = new GameObject("niko", new Transform(new Vector2f(0,100), new Vector2f(96,128)));
        niko.addComponent(new SpriteRender(spriteSheet.getSprite(1)));
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