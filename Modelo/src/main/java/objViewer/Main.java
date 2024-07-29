package objViewer;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    private Window window;
    private RenderCopia renderer;
    private InputHandler inputHandler;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = new Window(1280, 720, "3D Model Viewer");
        window.init();

        renderer = new RenderCopia();
        renderer.init();

        inputHandler = new InputHandler(renderer.getCamera(), window.getWindowHandle());
    }

    private void loop() {
        while (!window.shouldClose()) {
            inputHandler.processInput();
            renderer.render();
            window.update();
        }
    }

    private void cleanup() {
        //renderer.cleanup();
        window.cleanup();
        glfwTerminate();
    }
}
