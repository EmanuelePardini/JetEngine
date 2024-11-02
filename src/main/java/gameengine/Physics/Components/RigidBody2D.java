package gameengine.Physics.Components;

import com.almasb.fxgl.physics.box2d.dynamics.Body;
import gameengine.Components.Component;
import gameengine.Physics.Enums.BodyType;
import org.joml.Vector2f;

public class RigidBody2D extends Component
{
    // Object's velocity vector.
    private Vector2f velocity = new Vector2f();

    // Rate at which angular velocity decreases over time.
    private float angularDamping = 0.8f;

    // Rate at which linear velocity decreases over time.
    private float linearDamping = 0.9f;

    // Mass of the object; 0 typically means a static object.
    private float mass = 0;

    // Type of body (e.g., dynamic, static, or kinematic).
    private BodyType bodyType = BodyType.Dynamic;

    // Disables rotation if true.
    private boolean fixedRotation = false;

    // Enables continuous collision detection.
    private boolean continuousCollision = true;

    //JBox2D Body
    private Body rawBody = null;

    // Updates the game object’s transform to match the physics body position and rotation.
    @Override
    public void Update(float DeltaTime)
    {
        // Ensure the physics body (`rawBody`) exists.
        if (rawBody != null)
        {
            // Set game object's position to match the physics body's position.
            this.gameObject.transform.position.set(rawBody.getPosition().x, rawBody.getPosition().y);

            // Set game object's rotation to match the physics body's angle (in degrees).
            this.gameObject.transform.rotation = (float) Math.toDegrees(rawBody.getAngle());
        }
    }

    public Body GetRawBody()
    {
        return rawBody;
    }

    public void SetRawBody(Body rawBody)
    {
        this.rawBody = rawBody;
    }

    public boolean IsContinuousCollision()
    {
        return continuousCollision;
    }

    public void SetContinuousCollision(boolean continuousCollision)
    {
        this.continuousCollision = continuousCollision;
    }

    public boolean IsFixedRotation()
    {
        return fixedRotation;
    }

    public void SetFixedRotation(boolean fixedRotation)
    {
        this.fixedRotation = fixedRotation;
    }

    public BodyType GetBodyType()
    {
        return bodyType;
    }

    public void SetBodyType(BodyType bodyType)
    {
        this.bodyType = bodyType;
    }

    public float GetMass()
    {
        return mass;
    }

    public void SetMass(float mass)
    {
        this.mass = mass;
    }

    public float GetLinearDamping()
    {
        return linearDamping;
    }

    public void SetLinearDamping(float linearDamping)
    {
        this.linearDamping = linearDamping;
    }

    public float GetAngularDamping()
    {
        return angularDamping;
    }

    public void SetAngularDamping(float angularDamping)
    {
        this.angularDamping = angularDamping;
    }

    public Vector2f GetVelocity()
    {
        return velocity;
    }

    public void SetVelocity(Vector2f velocity)
    {
        this.velocity = velocity;
    }


}
