package physics.rigidbody;

import org.joml.Vector2f;
import physics.primitives.AABB;
import physics.primitives.Box;
import physics.primitives.Circle;
import render.Line;
import util.JadeMath;

public class IntersectionDetector {
    /// POINT IN PRIMITIVE
    public static boolean pointOnLine(Vector2f point, Line line){
        float dx = line.getEnd().x - line.getStart().x;
        float dy = line.getEnd().y - line.getStart().y;
        float m = dy/dx;

        float b = line.getEnd().y - (m*line.getEnd().x);

        return point.y == m*point.x + b;
    }

    public static boolean pointInCircle(Vector2f point, Circle circle){
        Vector2f center = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(center);

        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean pointInAABB(Vector2f point, AABB box){
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return point.x <= max.x && min.x <= point.x &&
                point.y <= max.y && min.y <= point.y;
    }

    public static boolean pointInBox(Vector2f point, Box box){
        Vector2f pointLocalBoxSpace = new Vector2f(point);
        JadeMath.rotate(pointLocalBoxSpace, box.getRigidBody().getRotation(), box.getRigidBody().getPosition());

        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return pointLocalBoxSpace.x <= max.x && min.x <= pointLocalBoxSpace.x &&
                pointLocalBoxSpace.y <= max.y && min.y <= pointLocalBoxSpace.y;
    }
}