package components;

import jade.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;
import render.Texture;

public class SpriteRender extends Component {
    private Vector4f color;
    private Sprite sprite;

    public SpriteRender(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRender(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1,1,1,1);
    }

    @Override
    public void start(){

    }
    @Override
    public void update(float deltaTime) {

    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }
}
