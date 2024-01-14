package extensions.org.springframework.ui.Model;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.springframework.ui.Model;
import vut.fit.gja2023.app.View;

@Extension
public class ModelExtension {
    private static final String TARGET_VIEW = "targetView";

    public static void setTargetView(@This Model model, View view) {
        model.addAttribute(TARGET_VIEW, view.toString());
    }
}
