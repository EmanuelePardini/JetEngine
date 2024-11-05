package gameengine.Components;

import com.almasb.fxgl.core.collection.Array;
import gameengine.Util.AssetPool;

import java.util.ArrayList;
import java.util.List;

/**
 * The AnimationState class manages a sequence of frames for an animation,
 * enabling control over the timing, looping, and retrieval of the current
 * frame to display. It keeps track of the current frame, updates frame
 * timing, and allows for optional looping behavior.
 */
public class AnimationState {
    //TODO: Refactor private and public variables

    // Title of the animation, used for identification or categorization.
    public String title;

    // List of frames in the animation sequence, each with a sprite and its duration.
    public List<Frame> animationFrames = new ArrayList<>();

    // Default sprite returned if no frames are available.
    private static Sprite defaultSprite = new Sprite();

    // Tracks the remaining time for the current frame.
    private float timeTracker = 0.0f;

    // Index of the current frame in the animation sequence.
    private transient int currentSprite = 0;

    // Indicates whether the animation should loop back to the beginning after the last frame.
    public boolean doesLoop = false;


    public void RefreshTexture()
    {
        for(Frame frame : animationFrames)
        {
            frame.sprite.SetTexture(AssetPool.GetTexture(frame.sprite.GetTexture().GetFilepath()));
        }
    }

    /**
     * Adds a frame to the animation sequence with a specified sprite and frame time.
     *
     * @param sprite the image or visual element for this frame
     * @param frameTime the duration for which this frame should be displayed
     */
    public void AddFrame(Sprite sprite, float frameTime) {
        animationFrames.add(new Frame(sprite, frameTime));
    }

    /**
     * Sets whether the animation should loop back to the start after reaching the last frame.
     *
     * @param doesLoop true if the animation should loop, false otherwise
     */
    public void SetLoop(boolean doesLoop) {
        this.doesLoop = doesLoop;
    }

    /**
     * Updates the current frame based on the elapsed time (DeltaTime).
     * If the frame's time expires, it advances to the next frame.
     *
     * @param DeltaTime the time elapsed since the last update, in seconds
     */
    public void Update(float DeltaTime) {

        if (currentSprite < animationFrames.size()) {
            timeTracker -= DeltaTime;

            // If the time for the current frame has expired
            if (timeTracker <= 0) {
                // Advance frame index or wrap if looping is enabled
                if (!(currentSprite == animationFrames.size() - 1 && !doesLoop))
                    currentSprite = (currentSprite + 1) % animationFrames.size();


                // Reset time for the new frame
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
        }
    }

    /**
     * Returns the current sprite of the animation.
     *
     * @return the current frame's sprite or a default sprite if no frames are set
     */
    public Sprite GetCurrentSprite()
    {
        if (currentSprite < animationFrames.size())
            return animationFrames.get(currentSprite).sprite;
        return defaultSprite;
    }
}
