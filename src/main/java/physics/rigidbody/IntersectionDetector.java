package physics.rigidbody;

import org.joml.Vector2f;
import physics.primitives.*;
import render.Line;
import util.JadeMath;

public class IntersectionDetector {
    /// POINT IN PRIMITIVE
    public static boolean pointOnLine(Vector2f point, Line line){
        float dx = line.getEnd().x - line.getStart().x;
        float dy = line.getEnd().y - line.getStart().y;
        if(dx==0f){
            return JadeMath.compare(point.x, line.getStart().x);
        }
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

    /// LINE IN PRIMITIVE
    public static boolean lineInCircle(Line line, Circle circle){
        if (pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)){
            return true;
        }

        Vector2f ab = new Vector2f(line.getEnd().sub(line.getStart()));
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToLineStart = new Vector2f(circleCenter).sub(line.getStart());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if (t < 0.0f || t > 1.0f){return false;}

        Vector2f closestPoint = new Vector2f(line.getStart()).add(ab.mul(t));

        return pointInCircle(closestPoint, circle);
    }
    public static boolean lineInAABB(Line line, AABB box){
        if (pointInAABB(line.getStart(), box) || pointInAABB(line.getEnd(), box)){
            return true;
        }

        Vector2f unitVector = new Vector2f(line.getEnd()).sub(line.getStart());
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0)? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0)? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(line.getStart()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(line.getStart()).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));

        if (tmax < 0 || tmin > tmax){
            return false;
        }

        float t = (tmin < 0f) ? tmax: tmin;
        return t > 0f && t * t < line.lengthSquared();
    }
    public static boolean lineInBox(Line line, Box box){
        float theta = -box.getRigidBody().getRotation();
        Vector2f center = box.getRigidBody().getPosition();
        Vector2f localStart = new Vector2f(line.getStart());
        Vector2f localEnd = new Vector2f(line.getEnd());

        JadeMath.rotate(localStart, theta, center);
        JadeMath.rotate(localEnd, theta, center);

        Line localLine = new Line(localStart, localEnd);
        AABB localBox = new AABB(box.getMin(), box.getMax());

        return  lineInAABB(localLine, localBox);
    }

    //RayCasts
    public static boolean rayCast(Circle circle, Ray ray, RayCast result){
        RayCast.reset(result);

        Vector2f originToCircle = new Vector2f(circle.getCenter().sub(ray.getOrigin()));
        float radiusSquared = circle.getRadius() * circle.getRadius();
        float originToCircleLengthSquared = originToCircle.lengthSquared();

        //Projection
        float a = originToCircle.dot(ray.getDirection());
        float bSq = originToCircleLengthSquared - (a*a);

        if (radiusSquared - bSq < 0f) return false;

        float f = (float) Math.sqrt(radiusSquared-bSq);
        //a + f if the ray starts inside the circle else a - f
        float t = (originToCircleLengthSquared < radiusSquared)? a + f : a - f;

        if (t < 0) return false;
        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(ray.getDirection().mul(t));
            Vector2f normal = new Vector2f(point).sub(circle.getCenter());
            normal.normalize();

            result.init(point, normal, t, true);
        }
        return true;
    }
}