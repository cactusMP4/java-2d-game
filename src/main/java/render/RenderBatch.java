package render;

import components.SpriteRender;
import jade.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>{
    //pos               //color                         //tex cords     //tex id
    //float, float,     float, float, float, float,     float, float,   float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRender[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0,1,2,3,4,5,6,7};

    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;
    private int layer;

    public RenderBatch(int maxBatchSize, int layer) {
        this.layer = layer;
        shader = AssetPool.getShader("src/assets/shaders/default.glsl");
        this.sprites = new SpriteRender[maxBatchSize];
        this.maxBatchSize = maxBatchSize;
        //                                 4 vertices quads
        vertices = new float[maxBatchSize *4* VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }
    public void start(){
        //generate and bind
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRender sprite){
        //index & renderObject
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        if(sprite.getTexture() != null){
            if (!textures.contains(sprite.getTexture())){
                textures.add(sprite.getTexture());
            }
        }

        //add properties
        loadVertexProperties(index);
        if (numSprites >= maxBatchSize){
            this.hasRoom = false;
        }
    }

    public void render(){
        boolean rebufferData = false;
        for(int i = 0; i < numSprites; i++){
            SpriteRender sprite = sprites[i];
            if(sprite.isDirty()){
                loadVertexProperties(i);
                sprite.clean();
                rebufferData = true;
            }
        }
        if(rebufferData) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }
        //use shader
        shader.use();
        shader.uploadMat4f("uProj", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());
        for (int i = 0; i < textures.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);//vertex
        glEnableVertexAttribArray(1);//fragment

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        for (int i = 0; i < textures.size(); i++){
            textures.get(i).unbind();
        }

        shader.detach();
    }
    private void loadVertexProperties(int index){
        SpriteRender sprite = sprites[index];
        //find offset
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texId = 0;
        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == sprite.getTexture()) {
                    texId = i + 1;
                    break;
                }
            }
        }


        //add vertices
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for(int i = 0; i < 4; i++){
            if (i == 1){
                yAdd = 0.0f;
            } else if (i == 2){
                xAdd = 0.0f;
            } else if (i == 3){
                yAdd = 1.0f;
            }

            //load pos
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);
            //load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;
            //load texture coords
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;
            //load texture id
            vertices[offset + 8] = texId;

            offset += VERTEX_SIZE;
        }
    }

    public int[] generateIndices() {
        //6/quad (3/triangle)
        int[] elements = new int[6*maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }
        return elements;
    }
    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //3,2,0,  0,2,1
        //triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;// + 0
        //triangle 2
        elements[offsetArrayIndex + 3] = offset;// + 0
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }
    public boolean hasTextureRoom(){return this.textures.size() < 8;}
    public boolean hasTexture(Texture texture){return this.textures.contains(texture);}
    public int getLayer() {return this.layer;}

    @Override
    public int compareTo(@NotNull RenderBatch o) {
        return Integer.compare(this.layer, o.getLayer());
    }
}
