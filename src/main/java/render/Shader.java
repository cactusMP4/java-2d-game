package render;

import java.io.IOException;
import java.nio.file.*;

import static org.lwjgl.opengl.GL20.*;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
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
            e.printStackTrace();
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
        int succes = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (succes == GL_FALSE) {
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
        succes = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (succes == GL_FALSE) {
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
        succes = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("failed linking shaders XDDD: \n" + glGetShaderInfoLog(shaderProgramID, len) +"\nfile: " + filepath);
            assert false : "";
        }
    }
    public void use() {
        glUseProgram(shaderProgramID);
    }
    public void detach() {
        glUseProgram(0);
    }
}
