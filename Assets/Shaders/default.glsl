// Vertex Shader
#type vertex
#version 330 core

// Input layout specifies that `aPos` is the first vertex attribute (location = 0)
// and represents the position of the vertex as a 3-component vector (vec3).
layout (location=0) in vec3 aPos;

// Input layout specifies that `aColor` is the second vertex attribute (location = 1)
// and represents the color of the vertex as a 4-component vector (vec4).
layout (location=1) in vec4 aColor;

// This is an output variable that will pass the color information from the vertex
// shader to the fragment shader.
out vec4 fColor;

void main()
{
    // The color provided as input (`aColor`) is passed directly to the output variable `fColor`,
    // which will be sent to the fragment shader.
    fColor = aColor;

    // The position of the vertex is transformed into a 4-component vector (vec4) 
    // by adding a 1.0 in the fourth component (w). This is necessary because OpenGL 
    // expects positions to be in homogeneous coordinates.
    gl_Position = vec4(aPos, 1.0);
}

// Fragment Shader
#type fragment
#version 330 core

// The fragment shader receives the interpolated color from the vertex shader through `fColor`.
in vec4 fColor;

// Declare an output variable `color` that will hold the final color value for the fragment.
out vec4 color;

void main(){
    // Assign the input color `fColor` to the output variable `color`.
    // This value will be used to color the corresponding fragment on the screen.
    color = fColor;
}

/*
Vertex Shader:
 This shader processes each vertex, passing its position to the OpenGL pipeline (gl_Position)
 and forwarding its color to the fragment shader via the fColor output variable.

Fragment Shader:
 This shader takes the interpolated color received from the vertex shader (fColor),
 assigns it to the color output variable, which is then used to color the pixel corresponding to the fragment.
*/
