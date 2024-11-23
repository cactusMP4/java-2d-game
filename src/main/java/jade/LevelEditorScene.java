package jade;

import components.SpriteRender;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-250,0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x=0; x < 100; x++){
            for (int y=0; y < 100; y++){
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject go = new GameObject("Obj" + x + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRender(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }
    }
    @Override
    public void render(float deltaTime) {
        for (GameObject go : this.gameObjects){
            go.update(deltaTime);
        }
        this.renderer.render();
    }
}