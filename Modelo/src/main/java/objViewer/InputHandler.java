package objViewer;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {

    private Camera camera;
    private long window;

    private boolean[] keys = new boolean[1024];
    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;

    // Ajustar sensibilidad para rotación del mouse
    private static final float MOUSE_SENSITIVITY = 0.005f;
    // Ajustar velocidad de movimiento de la cámara
    private static final float MOVE_SPEED = 2.9f;

    public InputHandler(Camera camera, long window) {
        this.camera = camera;
        this.window = window;
        initCallbacks();
    }

    private void initCallbacks() {
        // Configurar los callbacks de GLFW para las entradas
        glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key >= 0 && key < keys.length) {
                    if (action == GLFW_PRESS) {
                        keys[key] = true;
                    } else if (action == GLFW_RELEASE) {
                        keys[key] = false;
                    }
                }
            }
        });

        glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                if (firstMouse) {
                    lastMouseX = xpos;
                    lastMouseY = ypos;
                    firstMouse = false;
                }

                float xoffset = (float) (xpos - lastMouseX) * MOUSE_SENSITIVITY;
                float yoffset = (float) (lastMouseY - ypos) * MOUSE_SENSITIVITY;
                lastMouseX = xpos;
                lastMouseY = ypos;

                // Rotar la cámara
                camera.rotate(xoffset, new Vector3f(0, 1, 0)); // Rotar alrededor del eje Y
                camera.rotate(yoffset, new Vector3f(1, 0, 0)); // Rotar alrededor del eje X
            }
        });
    }

    public void processInput() {
        Vector3f cameraMove = new Vector3f();

        if (keys[GLFW_KEY_W]) {
            cameraMove.add(camera.getFront());
        }
        if (keys[GLFW_KEY_S]) {
            cameraMove.sub(camera.getFront());
        }
        if (keys[GLFW_KEY_A]) {
            cameraMove.sub(camera.getRight());
        }
        if (keys[GLFW_KEY_D]) {
            cameraMove.add(camera.getRight());
        }

        if (cameraMove.lengthSquared() > 0) {
            cameraMove.normalize().mul(MOVE_SPEED);
            camera.move(cameraMove);
        }
    }
}
