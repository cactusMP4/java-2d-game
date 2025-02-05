package jade;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int layer = context.deserialize(jsonObject.get("layer"), int.class);

        GameObject go = new GameObject(name, transform, layer);
        for (JsonElement component : components) {
            Component c = context.deserialize(component, Component.class);
            go.addComponent(c);
        }
        return go;
    }
}
