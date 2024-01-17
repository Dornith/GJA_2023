package extensions.org.springframework.ui.Model;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.springframework.ui.Model;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.models.CsvColumn;
import vut.fit.gja2023.app.enums.View;

import java.util.List;
import java.util.Optional;

@Extension
public class ModelExtension {
    private static final String TARGET_VIEW = "targetView";

    public static void setTargetView(@This Model model, View view) {
        model.addAttribute(TARGET_VIEW, view.getValue());
    }

    public static void setErrorView(@This Model model, String errorCode, String errorMessage) {
        model.addAttribute(TARGET_VIEW, View.ERROR.toString());
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);
    }

    public static void setCsvColumns(@This Model model, List<CsvColumn> columns) {
        model.addAttribute("csvColumns", columns);
    }

    public static void setLoggedInUser(@This Model model, UserBo user) {
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", user);
        model.addAttribute("isAdmin", user.getRole() == UserRole.ADMIN);
        model.addAttribute("isTeacher", user.getRole() == UserRole.TEACHER);
        model.addAttribute("isStudent", user.getRole() == UserRole.STUDENT);
        model.addAttribute("isTeacherPlus", user.getRole() == UserRole.TEACHER || user.getRole() == UserRole.ADMIN);
    }
}
