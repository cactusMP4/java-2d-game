package render;

import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {
    private static final int MAX_LINES = 500;

    private static final List<Line2D> lines = new ArrayList<>();
    private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];//6 floats 2 vertices
    private static final Shader shader = AssetPool.getShader("debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    public static void start(){
        //VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        //VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);
        //enable attrib
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void beginFrame(){
        if (!started){
            start();
            started = true;
        }

        for (int i=0; i < lines.size(); i++){
            if (lines.get(i).beginFrame() < 0){
                lines.remove(i);
                i--;
            }
        }
    }
    public static void draw(){
        if (lines.isEmpty()) return;

        int index = 0;
        for (Line2D line : lines){
            for (int i=0; i<2; i++){
                Vector2f position = i == 0 ? line.getStart() : line.getEnd();
                Vector3f color = line.getColor();

                vertexArray[index] = position.x;
                vertexArray[index+1] = position.y;
                vertexArray[index+2] = -10.0f;

                vertexArray[index+3] = color.x;
                vertexArray[index+4] = color.y;
                vertexArray[index+5] = color.z;
                index += 6;
            }
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER,0, Arrays.copyOfRange(vertexArray,0,lines.size()*6*2));

        assert shader != null;
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_LINES,0,lines.size()*6*2);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    public static void addLine2D(Vector2f start, Vector2f end){
        addLine2D(start,end, new Vector3f(0,1,0), 10);
    }
    public static void addLine2D(Vector2f start, Vector2f end, Vector3f color){
        addLine2D(start,end, color, 10);
    }
    public static void addLine2D(Vector2f start, Vector2f end, Vector3f color, float lifetime){
        if (lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(start,end,color,lifetime));
    }

//    public static void addBox2D(Vector2f center, Vector2f size, float rotation, Vector3f color, float lifetime){
//        Vector2f min = new Vector2f(center).sub(new Vector2f(size).div(2.0f));
//        Vector2f max = new Vector2f(center).add(new Vector2f(size).div(2.0f));
//
//        Vector2f[] vertices = {
//            new Vector2f(min.x, min.y), new Vector2f(min.x, min.y),
//            new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
//        };
//
//        if (rotation != 0.0f){
//            for (Vector2f vert : vertices){
//                JMath.rotate(vert, rotation, center);
//            }
//        }
//    }
}
