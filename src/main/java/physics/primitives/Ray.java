package physics.primitives;

import org.joml.Vector2f;

public class Ray {
    private Vector2f origin;
    private Vector2f direction;

    public Ray(Vector2f origin, Vector2f direction) {
        this.origin = origin;
        this.direction = direction;
        this.direction.normalize();
    }

    public Vector2f getOrigin() {return this.origin;}
    public Vector2f getDirection() {return this.direction;}
}
