package gameengine.Components;

import gameengine.Engine.GameObject;
import gameengine.Engine.Window;
import gameengine.Physics.Components.RigidBody2D;
import org.joml.Vector2f;
import java.util.concurrent.ThreadLocalRandom;

public class Spawner extends Component {
    private float spawnDelay;  // The delay between each spawn in seconds
    private float timeSinceLastSpawn = 0;  // Tracks the time since the last spawn
    private float minDelay;  // Minimum delay between spawns
    private float maxDelay;  // Maximum delay between spawns
    private Vector2f minOffset;  // Minimum offset range for spawning position
    private Vector2f maxOffset;  // Maximum offset range for spawning position
    private boolean addRigidBody = true;
    private transient boolean  isDead = false;

    // Default constructor with predefined delay and offset ranges
    public Spawner() {
        this.minDelay = 2;
        this.maxDelay = 6;
        this.minOffset = new Vector2f(-500, 0);
        this.maxOffset = new Vector2f(500, 0);

        // Initialize the spawn delay using ThreadLocalRandom
        spawnDelay = minDelay + ThreadLocalRandom.current().nextFloat() * (maxDelay - minDelay);
    }

    // Constructor with customizable delay and offset ranges
    public Spawner(float minDelay, float maxDelay, Vector2f minOffset, Vector2f maxOffset) {
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.minOffset = minOffset;
        this.maxOffset = maxOffset;

        // Initialize the spawn delay using ThreadLocalRandom
        spawnDelay = minDelay + ThreadLocalRandom.current().nextFloat() * (maxDelay - minDelay);
    }

    @Override
    public void Update(float deltaTime)
    {
        if(isDead) return;

        // Increment the time since the last spawn by the time elapsed since the last update
        timeSinceLastSpawn += deltaTime;

        // Check if it's time to spawn a new object
        if (timeSinceLastSpawn >= spawnDelay) {System.out.println("Enter");
            // Generate a random offset within the specified range
            float offsetX = minOffset.x + ThreadLocalRandom.current().nextFloat() * (maxOffset.x - minOffset.x);
            float offsetY = minOffset.y + ThreadLocalRandom.current().nextFloat() * (maxOffset.y - minOffset.y);
            Vector2f offset = new Vector2f(offsetX, offsetY);

            // Create a copy of the GameObject and apply the random offset to its position

            GameObject newObj = gameObject.Copy();
            newObj.transform.position.add(offset);
            newObj.GetComponent(Spawner.class).Destroy();
            newObj.RemoveComponent(Spawner.class);



            if(addRigidBody)
            {
                RigidBody2D rb = new RigidBody2D();
                newObj.AddComponent(rb);
            }

            Window.GetScene().AddGameObjectToScene(newObj);

            // Reset the spawn delay with a new random value within the range
            spawnDelay = minDelay + ThreadLocalRandom.current().nextFloat() * (maxDelay - minDelay);
            timeSinceLastSpawn = 0;  // Reset the time since the last spawn
        }
    }

    @Override
    public void Destroy()
    {
        isDead = true;
    }
}
