package Renderer;

import gameengine.Util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class DebugDraw
{
    private static int MAX_LINES = 500;

    private static List<Line2D> lines = new ArrayList<>();

    //6 floats per vertex, 2 vertices per lines
    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private boolean started = false;

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

        //TODO:Continue this and Set line width
    }
}
