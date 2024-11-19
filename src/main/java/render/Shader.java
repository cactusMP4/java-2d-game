package render;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.*;

import static org.lwjgl.opengl.GL20.*;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSrc, fragmentSrc, filepath;
    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String src = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitstring = src.split("(#type)( )+([a-zA-Z]+)");

            //first "#type"
            int index  = src.indexOf("#type") + 6;
            int endOfLine = src.indexOf("\r\n", index);
            String firstPattern = src.substring(index, endOfLine).trim();
            //second "#type"
            index = src.indexOf("#type", endOfLine) + 6;
            endOfLine = src.indexOf("\r\n", index);
            String secondPattern = src.substring(index, endOfLine).trim();

            if(firstPattern.equals("vertex")) {
                vertexSrc = splitstring[1];
            } else if(firstPattern.equals("fragment")) {
                fragmentSrc = splitstring[1];
            } else {
                throw new IOException("WTF is this token?: "+firstPattern);
            }
            if(secondPattern.equals("vertex")) {
                vertexSrc = splitstring[2];
            } else if(secondPattern.equals("fragment")) {
                fragmentSrc = splitstring[2];
            } else {
                throw new IOException("WTF is this token?: "+secondPattern);
            }

        } catch (IOException e){
            e.printStackTrace(System.err);
            assert false : "no shader file on "+filepath;
        }
    }

    public void compile() {
        int vertexID, fragmentID;

        //load & compile VERTEX shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass shader src to gpu
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);
        //check for errs
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("failed compiling vertex shader XDDD: \n" + glGetShaderInfoLog(vertexID, len) +"\nfile: " + filepath);
            assert false : "";
        }


        //load & compile FRAGMENT shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass shader src to gpu
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);
        //check for errs
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("failed compiling fragment shader XDDD: \n" + glGetShaderInfoLog(fragmentID, len) +"\nfile: " + filepath);
            assert false : "";
        }

        //LINK
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        //check for errs
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("failed linking shaders XDDD: \n" + glGetShaderInfoLog(shaderProgramID, len) +"\nfile: " + filepath);
            assert false : "";
        }
    }
    public void use() {
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }
    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }


    public void uploadVex4f(String varName, Vector4f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }
    public void uploadVex3f(String varName, Vector3f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }
    public void uploadVex2f(String varName, Vector2f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }


    public void uploadFloat(String varName, float val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }
    public void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTex(String varName, int slot){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }
}
