package extensions.org.springframework.ui.Model;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.springframework.ui.Model;
import vut.fit.gja2023.app.models.CsvColumn;
import vut.fit.gja2023.app.View;

import java.util.List;

@Extension
public class ModelExtension {
    private static final String TARGET_VIEW = "targetView";

    public static void setTargetView(@This Model model, View view) {
        model.addAttribute(TARGET_VIEW, view.toString());
    }

    public static void setErrorView(@This Model model, String errorCode, String errorMessage) {
        model.addAttribute(TARGET_VIEW, View.ERROR.toString());
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);
    }

    public static void setCsvColumns(@This Model model, List<CsvColumn> columns) {
        model.addAttribute("csvColumns", columns);
    }
}
