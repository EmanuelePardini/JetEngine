package gameengine.Util;

import Renderer.Shader;
import Renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool
{
    private static Map<String, Shader> Shaders = new HashMap<>(); //hashmap non tiene ordine, ma è più efficiente delle map
    private static Map<String, Texture> Textures = new HashMap<>();

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
}
