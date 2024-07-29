package objViewer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Camera {

    private Vector3f position;
    private Vector3f target;
    private Vector3f up;

    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    private float yaw = -90.0f;  // Horizontal angle, looking along the negative Z-axis
    private float pitch = 0.0f;  // Vertical angle

    private static final float PITCH_LIMIT = 89.0f;  // Limit pitch to prevent flipping

    public Camera(float fov, float aspectRatio, float nearPlane, float farPlane) {
        this.position = new Vector3f(0, 0, 20);
        this.target = new Vector3f(0, 0, 0);
        this.up = new Vector3f(0, 1, 0);
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
        this.projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, nearPlane, farPlane);
        this.viewMatrix = new Matrix4f().lookAt(position, target, up);
    }

    public void updateViewMatrix() {
        viewMatrix.identity().lookAt(position, target, up);
    }

    public void applyView() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(16);
        projectionMatrix.get(projectionBuffer);
        glLoadMatrixf(projectionBuffer);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        FloatBuffer viewBuffer = BufferUtils.createFloatBuffer(16);
        viewMatrix.get(viewBuffer);
        glLoadMatrixf(viewBuffer);
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
        updateViewMatrix();
    }

    public Vector3f getPosition() {
        return position;
    }
    
    public void setTarget(Vector3f target) {
        this.target.set(target);
        updateViewMatrix();
    }

    public Vector3f getTarget() {
        return target;
    }

    public void setUp(Vector3f up) {
        this.up.set(up);
        updateViewMatrix();
    }

    public Vector3f getUp() {
        return up;
    }
    
    public Matrix4f getProjectionMatrix() {
        projectionMatrix.setPerspective(fov, aspectRatio, nearPlane, farPlane);
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        viewMatrix.identity().lookAt(position, target, up);
        return viewMatrix;
    }

    public Vector3f getFront() {
        Vector3f front = new Vector3f();
        target.sub(position, front);
        return front.normalize();
    }

    public Vector3f getRight() {
        Vector3f right = new Vector3f();
        getFront().cross(up, right);
        return right.normalize();
    }
    
    public void move(Vector3f delta) {
        position.add(delta);
        updateViewMatrix();
    }

    public void rotate(float angle, Vector3f axis) {
        if (Float.isNaN(angle) || Float.isNaN(axis.x) || Float.isNaN(axis.y) || Float.isNaN(axis.z)) {
            System.out.println("Warning: rotate parameters contain NaN values");
        }
        Vector3f direction = new Vector3f();
        target.sub(position, direction); // Calculate direction vector
        direction.rotateAxis(angle, axis.x, axis.y, axis.z);
        target.set(position).add(direction);
        updateViewMatrix();
    }

    public void zoom(float zoomFactor) {
        fov -= zoomFactor;
        if (fov < 10.0f) fov = 10.0f; // Limit zoom in
        if (fov > 120.0f) fov = 20.0f; // Limit zoom out
        projectionMatrix.perspective(fov, aspectRatio, nearPlane, farPlane);
        updateViewMatrix();
    }

    // Getters and setters for yaw and pitch
    public Vector3f getRotation() {
        return new Vector3f(pitch, yaw, 0);
    }

    public void setRotation(Vector3f rotation) {
        this.pitch = rotation.x;
        this.yaw = rotation.y;

        // Clamp the pitch to prevent flipping
        if (this.pitch > PITCH_LIMIT) {
            this.pitch = PITCH_LIMIT;
        }
        if (this.pitch < -PITCH_LIMIT) {
            this.pitch = -PITCH_LIMIT;
        }

        updateViewMatrix();
    }
}
