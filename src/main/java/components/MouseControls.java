package components;

import jade.GameObject;
import jade.MouseListener;
import jade.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObj = null;

    public void pickupObj(GameObject obj){
        this.holdingObj = obj;
        Window.getScene().addGameObjectToScene(obj);
    }

    public void place(){
        this.holdingObj = null;
    }

    @Override
    public void update(float dt){
        if (holdingObj != null){
            holdingObj.transform.position.x = MouseListener.getOrthoX();
            holdingObj.transform.position.y = MouseListener.getOrthoY();

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
    }
}
