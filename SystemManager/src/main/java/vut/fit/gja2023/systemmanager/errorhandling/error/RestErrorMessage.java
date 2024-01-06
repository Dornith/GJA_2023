package vut.fit.gja2023.systemmanager.errorhandling.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Part of the {@link RestError} that contains details about error thrown
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestErrorMessage {

    private String code;
    private String message;
}
