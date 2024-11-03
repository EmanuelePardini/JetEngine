package gameengine.Physics;

import gameengine.Engine.GameObject;
import gameengine.Engine.Transform;
import gameengine.Physics.Components.Box2DCollider;
import gameengine.Physics.Components.CircleCollider;
import gameengine.Physics.Components.Collider;
import gameengine.Physics.Components.RigidBody2D;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

import java.awt.*;

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

    //Add to the physics system
    public void Add(GameObject go)
    {
        RigidBody2D rb = go.GetComponent(RigidBody2D.class);

        //If has a rigid body and not a raw body that means we haven't added it to the system yet
        if(rb != null && rb.GetRawBody() == null)
        {
            Transform transform = go.transform;
            BodyDef bodyDef = new BodyDef();
            // Set the body's rotation angle by converting the GameObject's rotation from degrees to radians
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            // Set the body's position based on the GameObject's current position
            bodyDef.position = new Vec2(transform.position.x, transform.position.y);
            // Set the body's angular damping to control rotation slowing, based on Rigidbody2D's angular damping value
            bodyDef.angularDamping = rb.GetAngularDamping();
            // Set the body's linear damping to control movement slowing, based on Rigidbody2D's linear damping value
            bodyDef.linearDamping = rb.GetLinearDamping();
            // Enable continuous collision detection if the Rigidbody2D is set to use it
            bodyDef.bullet = rb.IsContinuousCollision();

            switch (rb.GetBodyType())
            {
                case Kinematic: bodyDef.type = BodyType.KINEMATIC; break;
                case Static: bodyDef.type = BodyType.STATIC; break;
                case Dynamic: bodyDef.type = BodyType.DYNAMIC; break;
            }

            // Create a PolygonShape object, which will be used for either a circle or box collider.
            PolygonShape shape = CheckColliderShape(go, bodyDef);

            Body body = this.world.createBody(bodyDef);
            rb.SetRawBody(body);
            //Create the polygon shape and attach it to the rigid body
            body.createFixture(shape, rb.GetMass());
        }
    }

    public void DestroyGameObject(GameObject go)
    {
        RigidBody2D rb = go.GetComponent(RigidBody2D.class);
        //Access only if there is a RigidBody to destroy
        if(rb != null && rb.GetRawBody() != null)
        {
            world.destroyBody(rb.GetRawBody());
            rb.SetRawBody(null);
        }
    }

    private PolygonShape CheckColliderShape(GameObject go, BodyDef bodyDef)
    {
        PolygonShape shape = new PolygonShape();

        // Attempt to retrieve the CircleCollider and Box2DCollider components from the GameObject.
        CircleCollider circleCollider = go.GetComponent(CircleCollider.class);
        Box2DCollider boxCollider = go.GetComponent(Box2DCollider.class);

        // Check if the GameObject has a CircleCollider component.
        if (circleCollider != null)
        {
            // If it has a CircleCollider, set the shape's radius to match the CircleCollider's radius.
            shape.setRadius(circleCollider.GetRadius());
        }
        else if (boxCollider != null)
        {
            // Get half of the box's size to use with the Box2DCollider (Box2D typically requires half-sizes).
            Vector2f halfSize = new Vector2f(boxCollider.getHalfSize().mul(0.5f));
            // Retrieve the offset and origin of the box collider, which determine its position and center within the GameObject.
            Vector2f offset = boxCollider.GetOffset();
            Vector2f origin = new Vector2f(boxCollider.getOrigin());
            // Set the shape as a box using the half-size, origin position, and no rotation (angle set to 0).
            shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);
            // Adjust the body's position by applying the offset, shifting it to align with the box collider.
            Vec2 pos = bodyDef.position;
            float xPos = pos.x + offset.x;
            float yPos = pos.y + offset.y;
            // Update the body's position to reflect the calculated x and y positions with the offset.
            bodyDef.position.set(xPos, yPos);
        }

        return shape;
    }
}
