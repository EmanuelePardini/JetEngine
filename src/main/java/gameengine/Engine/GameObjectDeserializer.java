package gameengine.Engine;

import com.google.gson.*;
import gameengine.Components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject>
{

    @Override
    public GameObject deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();

        //We need to deserialize all the properties a gameobject has
        JsonArray components = jsonObject.getAsJsonArray("components");

        GameObject go = new GameObject(name);

        for (JsonElement e : components)
        {
            Component c = context.deserialize(e, Component.class);
            go.AddComponent(c);
        }

        go.transform = go.GetComponent(Transform.class);

        return go;
    }
}
