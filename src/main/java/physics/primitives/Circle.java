package physics.primitives;

import org.joml.Vector2f;
import physics.rigidbody.RigidBody;

public class Circle {
    private float radius = 1.0f;
    private RigidBody body = null;

    public float getRadius(){return radius;}
    public Vector2f getCenter(){return body.getPosition();}
}
