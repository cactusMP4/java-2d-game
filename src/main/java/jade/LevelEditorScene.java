package jade;

import org.lwjgl.BufferUtils;

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
         0.5f, -0.5f,  0.0f,        1.0f, 0.0f, 1.0f, 1.0f, //Bottom Right 0
        -0.5f,  0.5f,  0.0f,        1.0f, 0.0f, 1.0f, 1.0f, //Top Left     1
         0.5f,  0.5f,  0.0f,        1.0f, 0.0f, 1.0f, 1.0f, //Top Right    2
        -0.5f, -0.5f,  0.0f,        1.0f, 0.0f, 1.0f, 1.0f, //Bottom Left  3
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

    public LevelEditorScene() {

    }

    @Override
    public void init() { //compile & link
        //load & compile VERTEX shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass shader src to gpu
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);
        //check for errs
        int succes = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("failed compiling vertex shader XDDD: \n" + glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }


        //load & compile FRAGMENT shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass shader src to gpu
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);
        //check for errs
        succes = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("failed compiling fragment shader XDDD: \n" + glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }


        //LINK
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);
        //check for errs
        succes = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("failed linking shaders XDDD: \n" + glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

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
        //bind shader
        glUseProgram(shaderProgram);
        //bind VAO
        glBindVertexArray(vaoID);
        //enable attribute pointers
        glEnableVertexAttribArray(0);//vertex
        glEnableVertexAttribArray(1);//fragment

        glDrawElements(GL_TRIANGLES, elementData.length, GL_UNSIGNED_INT, 0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glUseProgram(0);
        glBindVertexArray(0);
    }
}