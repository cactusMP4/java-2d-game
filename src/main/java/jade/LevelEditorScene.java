package jade;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import render.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {
    private String vertexShaderSrc = """
            #version 330 core
            layout (location=0) in vec3 aPos;
            layout (location=1) in vec4 aColor;
            
            out vec4 fColor;
            
            void main(){
                fColor = aColor;
                gl_Position = vec4(aPos, 1.0);
            }""";
    private String fragmentShaderSrc = """
            #version 330 core
            in vec4 fColor;
            
            out vec4 color;
            
            void main(){
                color = fColor;
            }""";
    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexData = {
         //POS                      //COLOR
         100.0f,  0.0f,    0.0f,        1.0f, 0.0f, 1.0f, 1.0f, //Bottom Right 0
         0.0f,    100.0f,  0.0f,        1.0f, 0.0f, 0.0f, 1.0f, //Top Left     1
         100.0f,  100.0f,  0.0f,        0.0f, 1.0f, 0.0f, 1.0f, //Top Right    2
         0.0f,    0.0f,    0.0f,        0.0f, 0.0f, 1.0f, 1.0f, //Bottom Left  3
    };
    private int[] elementData = {
        /*
                1----------2
                |          |
                |          |
                |          |
                3----------0
         */
        2,1,0,
        0,1,3
    };
    private int vaoID, vboID, eboID;

    private Shader shader;

    public LevelEditorScene() {

    }

    @Override
    public void init() { //compile & link
        this.camera = new Camera(new Vector2f(0, 0));
        shader = new Shader("src/assets/shaders/default.glsl");
        shader.compile();

        //VAO VBO EBO stuff and sending it to gpu
        //VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexData.length);
        vertexBuffer.put(vertexData).flip();
        //VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        //indices
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementData.length);
        elementBuffer.put(elementData).flip();
        //EBO
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = ((positionSize + colorSize) * floatSizeBytes);
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }
    @Override
    public void render(float deltaTime) {
        camera.position.x -= deltaTime * 69;

        //bind shader
        shader.use();
        shader.uploadMat4f("uProj", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());
        //bind VAO
        glBindVertexArray(vaoID);
        //enable attribute pointers
        glEnableVertexAttribArray(0);//vertex
        glEnableVertexAttribArray(1);//fragment

        glDrawElements(GL_TRIANGLES, elementData.length, GL_UNSIGNED_INT, 0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        shader.detach();
        glBindVertexArray(0);
    }
}