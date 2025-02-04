package components;

import org.joml.Vector2f;
import render.Texture;

public class Sprite {
    private float width, height;

    private Texture texture = null;
    private Vector2f[] texCoords = {
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1),
                new Vector2f(0,0)
        };

    public Texture getTexture() {return this.texture;}
    public Vector2f[] getTexCords() {return this.texCoords;}
    public float getWidth() {return width;}
    public float getHeight() {return height;}
    public int getTexId(){return texture==null ? -1 : texture.getId();}

    public void setTexture(Texture texture) {this.texture = texture;}
    public void setTexCords(Vector2f[] texCords) {this.texCoords = texCords;}
    public void setWidth(float width) {this.width = width;}
    public void setHeight(float height) {this.height = height;}
}
