package physics;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;
import physics.primitives.AABB;
import physics.primitives.Circle;
import physics.rigidbody.IntersectionDetector;
import render.Line;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColliderTests {
    private final float EPSILON = 0.000001f;

    @Test
    public void pointOnLine(){
        Line line = new Line(new Vector2f(0,0), new Vector2f(2,2));
        Vector2f point = new Vector2f(1,1);

        assertTrue(IntersectionDetector.pointOnLine(point, line));
    }
    @Test
    public void pointOnVerticalLine(){
        Line line = new Line(new Vector2f(0,0), new Vector2f(0,2));
        Vector2f point = new Vector2f(0,1);

        assertTrue(IntersectionDetector.pointOnLine(point, line));
    }

    @Test
    public void lineInAABB(){
        Line line = new Line(new Vector2f(0,0), new Vector2f(3,3));
        AABB aabb = new AABB(new Vector2f(1,1), new Vector2f(2,2));

        assertTrue(IntersectionDetector.lineInAABB(line, aabb));
    }
}
