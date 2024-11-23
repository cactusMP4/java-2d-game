package components;

import jade.Component;

public class FontRender extends Component {
    public FontRender() {
        gameObject = null;
    }

    @Override
    public void start(){
        if(gameObject.getComponent(SpriteRender.class) != null){
            System.out.println("FontRender start");
        }
    }

    @Override
    public void update(float deltaTime) {

    }
}
