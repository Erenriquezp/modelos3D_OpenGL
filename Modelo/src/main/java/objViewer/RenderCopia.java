package objViewer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.nio.FloatBuffer;

public class RenderCopia {

    private Camera camera;
    private Scene scene;
    private Plane groundPlane;

    private float modelRotationSpeed = 0.7f;  // Velocidad de rotación de las figuras

    // Parámetros de la luz
    float[] lightPos = {0.0f, 10.0f, 0.0f, 1.0f}; // Posición de la luz en coordenadas del mundo
    float[] lightColor = {1.0f, 1.0f, 1.0f, 1.0f}; // Color blanco

    public void init() {
        camera = new Camera((float) Math.toRadians(70.0f), 1280.0f / 720.0f, 0.1f, 1000.0f);
        camera.setPosition(new Vector3f(0, 4, 20));
        scene = new Scene();
        groundPlane = new Plane(new Vector3f(0, 0, 0), 150.0f, 110.0f, new float[]{0.5f, 0.5f, 0.5f});
        
        loadModels();
    }

    private void loadModels() {
        float spacing = 5.0f;
        float initialX = -spacing * 9;
        try {
            for (int i = 1; i <= 21; i++) {
                Model model = OBJLoader.loadModel("src/main/resources/models/estatua" + i + "Z.obj");
                if (i==1) {
                	model.setPosition(new Vector3f(initialX - 10f, 0, 0));
                } else {                	
                	model.setPosition(new Vector3f(initialX + (i - 1) * spacing, 0, 0));
                }
                float[] color = new float[]{(float) Math.random(), (float) Math.random(), (float) Math.random()};
                model.setColor(color);
                scene.addModel(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render() {
        glClearColor(0.6f, 0.71f, 0.58f, 1.0f);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        float aspectRatio = (float) 1280 / 720;
        float fov = 40.0f;
        float nearPlane = 0.1f;
        float farPlane = 1000.0f;
        float y_scale = (float) (1 / Math.tan(Math.toRadians(fov / 2)));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;

        FloatBuffer projectionMatrix = BufferUtils.createFloatBuffer(16);
        projectionMatrix.put(0, x_scale);
        projectionMatrix.put(5, y_scale);
        projectionMatrix.put(10, -((farPlane + nearPlane) / frustum_length));
        projectionMatrix.put(11, -1);
        projectionMatrix.put(14, -((2 * nearPlane * farPlane) / frustum_length));
        projectionMatrix.put(15, 0);
        glLoadMatrixf(projectionMatrix);

        setupSunLight();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        camera.applyView();
        
        // Update rotation for the ground plane
        groundPlane.update();

        // Renderizar los modelos
        for (Model model : scene.getModels()) {
        	//model.setRotationAngle(model.getRotationAngle() +3);
        	model.setRotationAngle(model.getRotationAngle() + modelRotationSpeed * 2f);
        	renderShadow(model);
            renderModel(model);
        }

        // Renderizar el plano de suelo
        renderGroundPlane();
        
        glfwPollEvents();
    }

    private void renderGroundPlane() {
        groundPlane.render();
    }

    private void renderModel(Model model) {
        glPushMatrix();
        glTranslatef(model.getPosition().x, model.getPosition().y, model.getPosition().z);
        glRotatef(model.getRotationAngle(), 0.0f, 1.0f, 0.0f); // Rotación alrededor del eje Y

        glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
        glEnable(GL_COLOR_MATERIAL);
        glColor3f(0.7f, 0.7f, 0.7f); // Establecer el color del modelo

        glBegin(GL_TRIANGLES);
        for (int[] face : model.getFaces()) {
            for (int index : face) {
                Vector3f vertex = model.getVertices().get(index);
                glVertex3f(vertex.x, vertex.y, vertex.z);
            }
        }
        glEnd();

        glPopMatrix();
    }

    private void setupSunLight() {
        glEnable(GL_LIGHT0);
        FloatBuffer lightPositionBuffer = BufferUtils.createFloatBuffer(4).put(lightPos);
        lightPositionBuffer.flip(); // Reset position for reading
        glLightfv(GL_LIGHT0, GL_POSITION, lightPositionBuffer);

        FloatBuffer lightColorBuffer = BufferUtils.createFloatBuffer(4).put(lightColor);
        lightColorBuffer.flip(); // Reset position for reading
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightColorBuffer);
        glLightfv(GL_LIGHT0, GL_SPECULAR, lightColorBuffer);
    }

    private void renderShadow(Model model) {
        glPushMatrix();
        glTranslatef(model.getPosition().x, 0.01f, model.getPosition().z);  // Proyectar sombra en el suelo con pequeña altura
        glRotatef(model.getRotationAngle(), 0.0f, 1.0f, 0.0f); // Aplica la rotación del modelo a la sombra

        // Ajusta el color y la transparencia de la sombra
        glColor4f(0.8f, 0.8f, 0.8f, 0.9f); // Color negro con transparencia al 50%
        
        // Aplastar la sombra en el eje Y y hacerla un poco más grande
        glScalef(1.25f, 0.0f, 2.05f); // Ajusta el tamaño y forma de la sombra

        glBegin(GL_TRIANGLES);
        for (int[] face : model.getFaces()) {
            for (int index : face) {
                Vector3f vertex = model.getVertices().get(index);
                glVertex3f(vertex.x, vertex.y, vertex.z);
            }
        }
        glEnd();
        glPopMatrix();
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public void cleanup() {
        scene.cleanup();
    }
}
