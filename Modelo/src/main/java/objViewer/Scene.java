package objViewer;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Model> models;

    public Scene() {
        models = new ArrayList<>();
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public List<Model> getModels() {
        return models;
    }

    public void cleanup() {
        for (Model model : models) {
            model.cleanup();
        }
    }
}
