package components;

import jade.GameObject;
import jade.MouseListener;
import jade.Window;
import util.Settings;

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

            if (holdingObj.transform.position.x < 0){
                holdingObj.transform.position.x -= Settings.GRID_WIDTH;
            }
            if (holdingObj.transform.position.y < 0){
                holdingObj.transform.position.y -= Settings.GRID_HEIGHT;
            }

            holdingObj.transform.position.x = (int)(holdingObj.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingObj.transform.position.y = (int)(holdingObj.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
    }

    public GameObject getHoldingObj() {
        return this.holdingObj;
    }
}
