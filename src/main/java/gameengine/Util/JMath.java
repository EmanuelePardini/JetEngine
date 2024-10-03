package gameengine.Util;

import org.joml.Vector2f;

public class JMath
{
    // This method rotates a 2D vector (vec) around a given origin by a specified angle (angleDeg).
    // The angle is in degrees and the origin is also a Vector2f representing the pivot point of rotation.
    public static void rotate(Vector2f vec, float angleDeg, Vector2f origin)
    {
        // Translate the vector so that the origin becomes the new origin (move origin to (0, 0)).
        float x = vec.x - origin.x;
        float y = vec.y - origin.y;

        // Calculate the sine and cosine of the angle (converted from degrees to radians).
        float cos = (float) Math.cos(Math.toRadians(angleDeg));
        float sin = (float) Math.sin(Math.toRadians(angleDeg));

        // Perform the 2D rotation transformation using the rotation matrix:
        // x' = x * cos(angle) - y * sin(angle)
        // y' = x * sin(angle) + y * cos(angle)
        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        // Translate back the result to the original origin.
        xPrime += origin.x;
        yPrime += origin.y;

        // Update the original vector with the rotated coordinates.
        vec.x = xPrime;
        vec.y = yPrime;
    }

    // This method compares two floating-point values (x and y) with a tolerance value (epsilon).
    // The comparison ensures that the absolute difference between x and y is less than or equal to
    // epsilon times the largest value between |x| and |y|, or 1.0f, whichever is larger.
    // This method is useful for comparing floating-point numbers with some margin of error.
    public static boolean compare(float x, float y, float epsilon)
    {
        // Use relative comparison to handle floating-point imprecision.
        return Math.abs(x - y) <= epsilon * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
    }

    // This method compares two 2D vectors (vec1 and vec2) using the same epsilon tolerance for
    // each component (x and y). It returns true if both components are within the epsilon range.
    public static boolean compare(Vector2f vec1, Vector2f vec2, float epsilon)
    {
        // Compare the x and y components individually with the provided epsilon tolerance.
        return compare(vec1.x, vec2.x, epsilon) && compare(vec1.y, vec2.y, epsilon);
    }

    // This overloaded method compares two floating-point values (x and y) without an explicit epsilon value.
    // Instead, it uses the smallest positive value (Float.MIN_VALUE) multiplied by the largest magnitude
    // between 1.0, |x|, or |y|. This ensures a very strict comparison.
    public static boolean compare(float x, float y)
    {
        // Perform comparison with the minimal float value for high precision comparison.
        return Math.abs(x - y) <= Float.MIN_VALUE * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
    }

    // This overloaded method compares two 2D vectors (vec1 and vec2) without an explicit epsilon.
    // It returns true if both the x and y components are within the Float.MIN_VALUE precision range.
    public static boolean compare(Vector2f vec1, Vector2f vec2)
    {
        // Compare the x and y components of both vectors using the strict float comparison method.
        return compare(vec1.x, vec2.x) && compare(vec1.y, vec2.y);
    }
}
