package objViewer;

import org.joml.Vector3f;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static Model loadModel(String path) throws IOException {
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\s+");
                switch (tokens[0]) {
                    case "v":
                        Vector3f vertex = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                        );
                        vertices.add(vertex);
                        break;
                    case "vn":
                        Vector3f normal = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                        );
                        normals.add(normal.normalize());
                        break;
                    case "f":
                        List<int[]> triangles = convertFaceToTriangles(tokens);
                        faces.addAll(triangles);
                        break;

                    default:
                        // Ignorar otras líneas
                        break;
                }
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IOException("Error parsing OBJ file: " + e.getMessage(), e);
        }

        return new Model(vertices, normals, faces);
    }
    private static List<int[]> convertFaceToTriangles(String[] faceTokens) {
        List<int[]> triangles = new ArrayList<>();
        int[] face = new int[faceTokens.length - 1];
        for (int i = 1; i < faceTokens.length; i++) {
            String[] faceElements = faceTokens[i].split("/");
            face[i - 1] = Integer.parseInt(faceElements[0]) - 1;
        }

        // Triangulación simple para cuadriláteros y más
        if (face.length == 4) {
            // Cuadrilátero, divide en 2 triángulos
            triangles.add(new int[]{face[0], face[1], face[2]});
            triangles.add(new int[]{face[2], face[3], face[0]});
        } else {
            // Usar triangulación de diagonales para polígonos más complejos
            for (int i = 1; i < face.length - 1; i++) {
                triangles.add(new int[]{face[0], face[i], face[i + 1]});
            }
        }
        return triangles;
    }
}
