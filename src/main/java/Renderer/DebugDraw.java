package Renderer;

import gameengine.Engine.Window;
import gameengine.Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;

public class DebugDraw
{
    private static int MAX_LINES = 500;

    private static List<Line2D> lines = new ArrayList<>();

    //6 floats per vertex, 2 vertices per lines
    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    //Same logic of the RenderBatch
    public static void Start()
    {
        //Generate the VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create the VBO and buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Enable the vertex array attributes

        //Enable Position
        glVertexAttribPointer(0,3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        //Enable Color
        glVertexAttribPointer(1,3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void BeginFrame()
    {
        if(!started)
        {
            Start();
            started = true;
        }

        //Remove dead lines
        for(int i = 0; i < lines.size(); i++)
        {
            if(lines.get(i).BeginFrame() < 0)
            {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void Draw()
    {
        if(lines.size() <= 0) return;

        int index = 0;
        for(Line2D line : lines)
        {
            for(int i=0; i<2; i++)
            {
                Vector2f position = i == 0 ? line.GetFrom() : line.GetTo();
                Vector3f color = line.GetColor();

                //Load position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.f; //z not enabled

                //Load the color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;

                index += 6; //Add the offset to jump to the nex vertex
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        //Create a copy of the Vertex Array and upload that to the GPU
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        //Use the shader
        shader.Use();
        shader.UploadMat4f("uProjection", Window.GetScene().camera().GetProjectionMatrix());
        shader.UploadMat4f("uView", Window.GetScene().camera().GetViewMatrix());

        //Bind the VAO
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        //Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.Detach();
    }

    //==========================
    //Add Line2D Methods
    //==========================
    public static void AddLine2D(Vector2f from, Vector2f to)
    {
        AddLine2D(from, to, new Vector3f(0,1,0), 1);
    }

    public static void AddLine2D(Vector2f from, Vector2f to, Vector3f color)
    {
        AddLine2D(from, to, color, 1);
    }


    public static void AddLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime)
    {
        if(lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }
}
