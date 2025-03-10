package render;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line {
    private Vector2f start, end;
    private Vector3f color;
    private float lifetime;

    public Line(Vector2f from, Vector2f to){
        this.start = from;
        this.end = to;
    }

    public Line(Vector2f from, Vector2f to, Vector3f color, float lifetime) {
        this.start = from;
        this.end = to;
        this.color = color;
        this.lifetime = lifetime;
    }

    public float beginFrame(){
        this.lifetime--;
        return this.lifetime;
    }

    public Vector2f getStart() {return start;}
    public Vector2f getEnd() {return end;}
    public Vector3f getColor() {return color;}
}
