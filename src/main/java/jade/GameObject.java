package jade;

import components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    private String name;
    private List<Component> components;
    public Transform transform;
    private int layer;

    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.layer = 0;
        this.uid = ID_COUNTER++;
    }
    public GameObject(String name, Transform transform){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.layer = 0;
        this.uid = ID_COUNTER++;
    }
    public GameObject(String name, Transform transform, int layer){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.layer = layer;
        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for (Component c : components){
            if (componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace(System.err);
                    assert false: "Error casting component";
                }
            }
        }
        return null;
    }
    public List<Component> getAllComponents() {return this.components;}
    public <T extends Component> void removeComponent(Class<T> componentClass){
        for (int i=0; i< components.size(); i++){
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }
    public void addComponent(Component c){
        c.generateID();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float deltaTime){
        for (Component component : components) {
            component.update(deltaTime);
        }
    }
    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    public void imgui(){
        for (Component c : components){
            c.imgui();
        }
    }
    public int getLayer() {return this.layer;}
    public static void init(int maxId){ID_COUNTER = maxId;}
    public int getUid() {return this.uid;}
}
