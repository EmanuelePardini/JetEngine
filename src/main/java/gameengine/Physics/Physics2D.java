package gameengine.Physics;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.physics.box2d.dynamics.World;

//This will be a wrapper class of the JBox library
public class Physics2D
{
    // Defines a gravity vector with a y-component of -10.0 (indicating downward acceleration).
    private Vec2 gravity = new Vec2(0, -10.f);

    // Creates a new physics world with the specified gravity vector.
// This `world` object will handle all physics simulations.
    private World world = new World(gravity);

    // Initializes an accumulator to track time for physics updates.
// This helps synchronize physics calculations with elapsed game time.
    private float physicsTime = 0.0f;

    // Defines the physics timestep at 1/60th of a second (60 Hz),
// representing the desired interval between each physics update.
    private float physicsTimeStep = 1.0f / 60.0f;

    // Specifies the number of velocity iterations for collision resolution.
// Higher values yield more precise velocity calculations but increase computational cost.
    private int velocityIterations = 8;

    // Specifies the number of position iterations for contact resolution.
// Higher values improve position accuracy for contacts but increase the computational load.
    private int positionIterations = 3;

    // This method updates the physics world, designed to be called every frame.
    public void Update(float DeltaTime)
    {
        // Accumulates the time elapsed since the last update.
        physicsTime += DeltaTime;

        // Checks if enough time has accumulated to proceed with a physics step.
        // If physicsTime exceeds or equals zero, a step can be executed.
        if(physicsTime >= 0)
        {
            // Subtracts the fixed timestep to keep the simulation synchronized.
            physicsTime -= physicsTimeStep;

            // Advances the physics simulation by one step with the specified timestep and iterations.
            // `world.step()` updates all entities based on physics laws, with
            // parameters for the timestep, velocity iterations, and position iterations.
            world.step(physicsTime, velocityIterations, positionIterations);
        }


    }

}
