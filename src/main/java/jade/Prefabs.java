package jade;

import components.Sprite;
import components.SpriteRender;
import org.joml.Vector2f;

public class Prefabs {
    public static GameObject generateSpriteObj(Sprite sprite, float sizeX, float sizeY){
        GameObject block = new GameObject("Sprite_Object_gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)));
        SpriteRender render = new SpriteRender();
        render.setSprite(sprite);
        block.addComponent(render);

        return block;
    }
}
