package render;

import components.SpriteRender;
import jade.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go){
        SpriteRender sprite = go.getComponent(SpriteRender.class);
        if(sprite != null){
            add(sprite);
        }
    }
    public void add(SpriteRender sprite){
        boolean added = false;
        for(RenderBatch batch : batches){
            if(batch.hasRoom() && batch.getLayer() == sprite.gameObject.getLayer()){
                Texture texture = sprite.getTexture();
                if(texture == null || (batch.hasTexture(texture) || batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }
        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.getLayer());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void render(){
        for(RenderBatch batch : batches){
            batch.render();
        }
    }
}
