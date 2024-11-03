package gameengine.Physics.Components;

import gameengine.Components.Component;
import gameengine.Physics.Enums.BodyType;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Velocity;
import org.joml.Vector2f;

public class RigidBody2D extends Component
{
    // Object's velocity vector.
    private Vector2f velocity;
    //  Angular Damping controls how much they resist rotating
    private float angularDamping;
    //  Linear Damping controls how much the Physics Body or Constraint resists translation
    private float linearDamping;
    // Mass of the object; 0 typically means a static object.
    private float mass;
    // Type of body (e.g., dynamic, static, or kinematic).
    private BodyType bodyType;
    // Disables rotation if true.
    private boolean fixedRotation;
    // To decide if on a collision it has to go trought the collider or hit it
    private boolean continuousCollision;
    //JBox2D Body
    private transient Body rawBody;

    public RigidBody2D()
    {
        this.velocity = new Vector2f();
        this.angularDamping = 0.8f;
        this.linearDamping = 0.9f;
        this.mass = 0;
        this.bodyType = BodyType.Dynamic;
        this.fixedRotation = false;
        this.continuousCollision = true;
        this.rawBody = null;
    }

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
