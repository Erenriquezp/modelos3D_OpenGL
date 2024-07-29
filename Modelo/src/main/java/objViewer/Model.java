package objViewer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Model {
    private List<Vector3f> vertices;
    private List<Vector3f> normals;
    private List<int[]> faces;
    private Vector3f position;
    private Vector3f rotation;
    float rotationAngle = 2f; // Ángulo de rotación
    float[] color; // Color del modelo

    private int vaoId;
    
    public float getRotationAngle() {
		return rotationAngle;
	}

	public void setRotationAngle(float rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

	public float[] getColor() {
		return color;
	}

	public void setColor(float[] color) {
		this.color = color;
	}

	private int vboId;
    private int eboId;
    private int vertexCount;

    // Constructor para inicializar el modelo con vertices, normales y caras
    public Model(List<Vector3f> vertices, List<Vector3f> normals, List<int[]> faces) {
        this.vertices = vertices;
        this.normals = normals;
        this.faces = faces;
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        new Vector3f(1, 1, 1);

        setupMesh();
    }

    private void setupMesh() {
        // Crear el VAO y vincularlo
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Crear el VBO para los datos de los vértices
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.size() * 3);
        for (Vector3f vertex : vertices) {
            vertexBuffer.put(vertex.x).put(vertex.y).put(vertex.z);
        }
        vertexBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Configurar la posición de los atributos de vértices en el VAO
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Crear el VBO para los datos de normales si es necesario
        if (normals != null && !normals.isEmpty()) {
            int normalVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, normalVboId);

            FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normals.size() * 3);
            for (Vector3f normal : normals) {
                normalBuffer.put(normal.x).put(normal.y).put(normal.z);
            }
            normalBuffer.flip();
            glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);

            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(0);
        }

        // Crear el EBO para los datos de los índices de las caras
        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);

        IntBuffer indexBuffer = BufferUtils.createIntBuffer(faces.size() * 3);
        for (int[] face : faces) {
            if (face.length != 3) {
                System.err.println("Invalid face with " + face.length + " indices: " + java.util.Arrays.toString(face));
                throw new IllegalArgumentException("Face does not have 3 indices.");
            }
            indexBuffer.put(face);
        }
        indexBuffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        vertexCount = faces.size() * 3;

        // Desvincular el VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0); // Desvincular buffer de atributos
    }


    public void render() {
    	 glBindVertexArray(vaoId);
    	    glEnableVertexAttribArray(0); // Habilitar atributo de vértices
    	    if (normals != null && !normals.isEmpty()) {
    	        glEnableVertexAttribArray(1); // Habilitar atributo de normales si existen
    	    }
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId); // Vincular buffer de índices
    	    glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
    	    glDisableVertexAttribArray(0);
    	    if (normals != null && !normals.isEmpty()) {
    	        glDisableVertexAttribArray(1);
    	    }
    	    glBindVertexArray(0);
    }

    public void cleanup() {
    	// Liberar recursos gráficos
        glBindBuffer(GL_ARRAY_BUFFER, 0); // Desvincular buffer de atributos
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); // Desvincular buffer de índices
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
        glDeleteVertexArrays(vaoId);
    }

    // Getters y setters
    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public List<int[]> getFaces() {
        return faces;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.set(x, y, z);
    }


	public void setVertices(List<Vector3f> vertices) {
		this.vertices = vertices;
	}

	public void setFaces(List<int[]> faces) {
		this.faces = faces;
	}

}
