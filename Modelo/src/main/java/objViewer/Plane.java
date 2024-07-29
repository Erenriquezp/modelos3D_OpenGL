package objViewer;

import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Plane {
    private float width;
    private float length;
    private int vaoId;
    private int vboId;

    private float rotationAngle = 0.0f;  // Current rotation angle
    private static final float ROTATION_SPEED = 0.5f;  // Speed of rotation (degrees per frame)

    public Plane(Vector3f position, float width, float length, float[] color) {
        this.width = width;
        this.length = length;
        setupMesh();
    }

    public float getRotationAngle() {
        return rotationAngle;
    }
    
    private void setupMesh() {
        float[] vertices = {
            -width / 2, 0, -length / 2,
            -width / 2, 0, length / 2,
            width / 2, 0, length / 2,
            width / 2, 0, -length / 2,
        };

        vaoId = glGenVertexArrays();
        vboId = glGenBuffers();
        glBindVertexArray(vaoId);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
    }

    public void update() {
        // Increment the rotation angle
        rotationAngle += ROTATION_SPEED;
        if (rotationAngle >= 360.0f) {
            rotationAngle -= 360.0f; // Keep the angle in [0, 360)
        }
    }

    public void render() {
        glPushMatrix();
        glColor3f(0.43f, 0.69f, 0.2f); // Set the color for the ground plane
        glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f); // Apply rotation
        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);
        glPopMatrix();
    }

    public void cleanup() {
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
    }
}
