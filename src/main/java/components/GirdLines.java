package components;

import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.DebugDraw;
import util.Settings;

public class GirdLines extends Component{
    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getScene().getCamera().position;
        Vector2f projectionSize = Window.getScene().getCamera().getProjectionSize();

        int firstX = ((int) (cameraPos.x/ Settings.GRID_WIDTH) * Settings.GRID_WIDTH);
        int firstY = ((int) (cameraPos.y/ Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT);

        int numVertLines = (int)(projectionSize.x/Settings.GRID_WIDTH);
        int numHoriLines = (int)(projectionSize.y/Settings.GRID_HEIGHT);

        int width = (int) projectionSize.x;
        int height = (int)projectionSize.y;

        int maxLines = Math.max(numVertLines, numHoriLines);
        Vector3f color = new Vector3f(0,0,0);
        for (int i=0; i<maxLines; i++){
            int x = firstX + (Settings.GRID_WIDTH*i);
            int y = firstY + (Settings.GRID_HEIGHT*i);

            if (i < numVertLines){
                DebugDraw.addLine2D(new Vector2f(x,firstY),new Vector2f(x,firstY+height), color);
            }
            if (i < numHoriLines){
                DebugDraw.addLine2D(new Vector2f(firstX,y),new Vector2f(firstX+width, y), color);
            }
        }
    }
}
