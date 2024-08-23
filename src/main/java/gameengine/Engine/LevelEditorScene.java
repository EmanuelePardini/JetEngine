package gameengine.Engine;


import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class LevelEditorScene extends Scene
{
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene()
    {
        //check editor level correctly initialized.
        System.out.println("Inside level editor scene");
    }

    @Override
    public void Update(float dt)
    {
        //check fps (dt is length of a frame, so 1/dt is frames ina a second)
        //Since v-sync is on, it's expected to be locked to monitor refresh rate
        System.out.println("" + 1.0f / dt + " FPS");

        //Change scene start after command
        if (!changingScene && KeyListener.isKeyPressed(GLFW_KEY_R))
        {
            changingScene = true;
        }

        //wait time, before changing scene
        if (changingScene && timeToChangeScene > 0)
        {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene)
        {
            Window.changeScene(1);
        }
    }

}
