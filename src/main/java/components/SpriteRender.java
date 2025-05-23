package components;

import imgui.ImGui;
import jade.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import render.Texture;

public class SpriteRender extends Component {
    private Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;


//    public SpriteRender(Vector4f color) {
//        this.color = color;
//        this.sprite = new Sprite(null);
//    }
//
//    public SpriteRender(Sprite sprite) {
//        this.sprite = sprite;
//        this.color = new Vector4f(1,1,1,1);
//    }

    @Override
    public void start(){
        this.lastTransform = gameObject.transform.copy();
    }
    @Override
    public void update(float deltaTime) {
        if(!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }
    @Override
    public void imgui(){
        float [] imColor = {color.x, color.y, color.z, color.w};
        if(ImGui.colorPicker4("color: ", imColor)){
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.isDirty = true;
        }
    }

    public Vector4f getColor() {return color;}
    public Texture getTexture() {return sprite.getTexture();}
    public Vector2f[] getTexCoords() {return sprite.getTexCords();}

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }
    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.isDirty = true;
            this.color.set(color);
        }
    }
    public boolean isDirty(){return this.isDirty;}
    public void clean() {this.isDirty = false;}

}
