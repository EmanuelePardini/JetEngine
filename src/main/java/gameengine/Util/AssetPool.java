package gameengine.Util;

import Renderer.Shader;
import Renderer.Texture;
import gameengine.Components.Spritesheet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool
{
    private static Map<String, Shader> Shaders = new HashMap<>(); //hashmap non tiene ordine, ma è più efficiente delle map
    private static Map<String, Texture> Textures = new HashMap<>();
    private static Map<String, Spritesheet> Spritesheets = new HashMap<>();

    public static Shader getShader(String resourceName)
    {
        File file = new File(resourceName);

        //if you find shader path, return it
        if (AssetPool.Shaders.containsKey(file.getAbsolutePath()))
        {
            return Shaders.get(file.getAbsolutePath());
        }
        //else create and compile new shader, then add it to the path
        else
        {
            Shader shader = new Shader(resourceName);
            shader.Compile();
            AssetPool.Shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName)
    {
        File file = new File(resourceName);
        if(AssetPool.Textures.containsKey(file.getAbsolutePath()))
        {
            return AssetPool.Textures.get(file.getAbsolutePath());
        }
        else
        {
            Texture texture = new Texture(resourceName);
            AssetPool.Textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void AddSpritesheet(String resourceName, Spritesheet spritesheet)
    {
        File file = new File(resourceName);
        if(!AssetPool.Spritesheets.containsKey(file.getAbsolutePath()))
        {
            AssetPool.Spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet GetSpritesheet (String resourceName)
    {
        File file = new File(resourceName);
        if(!AssetPool.Spritesheets.containsKey(file.getAbsolutePath()))
        {
            assert false : "Error: Tried to access spritesheet '" + resourceName + "' and is has not been added to the asset pool.";
        }
        return AssetPool.Spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
