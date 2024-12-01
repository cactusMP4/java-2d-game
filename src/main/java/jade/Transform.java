package jade;

import org.joml.Vector2f;

public class Transform {
    public Vector2f position;
    public Vector2f scale;

    public Transform(){
        init(new Vector2f(),new Vector2f());
    }
    public Transform(Vector2f position){
        init(position,new Vector2f());
    }
    public Transform(Vector2f position,Vector2f scale){
        init(position,scale);
    }


    public void init(Vector2f position, Vector2f scale){
        this.position = position;
        this.scale = scale;
    }

    public Transform copy(){
        return new Transform(new Vector2f(position),new Vector2f(scale));
    }

    public void copy(Transform to){
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object object){
        if (object == null) return false;
        if (!(object instanceof Transform transform)) return false;

        return transform.position.equals(this.position) && transform.scale.equals(this.scale);
    }
}
