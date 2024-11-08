package gameengine.Renderer;

import gameengine.Components.SpriteRenderer;
import gameengine.Engine.GameObject;
import gameengine.Engine.Window;
import gameengine.Util.AssetPool;
import gameengine.Util.JMath;
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
    private static int MAX_LINES = 1024;

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
        if (lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

    //==========================
    //Add Box2D Methods
    //==========================
    public static void AddBox2D(Vector2f center, Vector2f dimensions, float rotation)
    {
        AddBox2D(center, dimensions, rotation, new Vector3f(0,1,0), 1);
    }

    public static void AddBox2D(Vector2f center, Vector2f dimensions,float rotation, Vector3f color)
    {
        AddBox2D(center, dimensions, rotation, color, 1);
    }

    public static void AddBox2D(Vector2f center, Vector2f dimensions,float rotation, Vector3f color, int lifetime)
    {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addBox2D(center, dimensions, rotation, new Vector3f(0, 1, 0), 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {
        addBox2D(center, dimensions, rotation, color, 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation,
                                Vector3f color, int lifetime) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };

        if (rotation != 0.0f) {
            for (Vector2f vert : vertices) {
                JMath.rotate(vert, rotation, center);
            }
        }

        AddLine2D(vertices[0], vertices[1], color, lifetime);
        AddLine2D(vertices[0], vertices[3], color, lifetime);
        AddLine2D(vertices[1], vertices[2], color, lifetime);
        AddLine2D(vertices[2], vertices[3], color, lifetime);
    }
    //==========================
    //Add Circle2D Methods
    //==========================
    public static void AddCircle2D(Vector2f center, float radius)
    {
        AddCircle2D(center, radius, new Vector3f(0,1,0), 1);
    }

    public static void AddCircle2D(Vector2f center, float radius, Vector3f color)
    {
        AddCircle2D(center, radius, color, 1);
    }
    
    public static void AddCircle2D(Vector2f center, float radius, Vector3f color, int lifetime)
    {
        Vector2f[] points = new Vector2f[16];
        int increment = 360 / points.length;
        int currentAngle = 0;

        for(int i = 0; i < points.length; i++)
        {
            Vector2f tmp = new Vector2f(radius, 0);
            JMath.rotate(tmp, currentAngle, new Vector2f());
            points[i] = new Vector2f(tmp).add(center);

            if(i > 0)
            {
                AddLine2D(points[i - 1], points[i], color, lifetime);
            }
            currentAngle += increment;
        }

        AddLine2D(points[points.length - 1], points[0], color, lifetime);
    }

}
