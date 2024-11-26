package jade;

import components.SpriteRender;
import org.joml.Vector2f;
import org.joml.Vector4f;
import render.Texture;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera();

        GameObject obj1 = new GameObject("Obj1", new Transform(new Vector2f(100,100), new Vector2f(100,100)));
        obj1.addComponent(new SpriteRender(AssetPool.getTexture("src/assets/textures/test.png")));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Obj2", new Transform(new Vector2f(300,100), new Vector2f(100,100)));
        obj2.addComponent(new SpriteRender(AssetPool.getTexture("src/assets/textures/test.png")));
        this.addGameObjectToScene(obj2);

        loadResources();
    }
    private void loadResources() {
        AssetPool.getShader("src/assets/shaders/default.glsl");
    }

    @Override
    public void render(float deltaTime) {
        for (GameObject go : this.gameObjects){
            go.update(deltaTime);
        }
        this.renderer.render();
    }
}