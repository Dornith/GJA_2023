package vut.fit.gja2023.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vut.fit.gja2023.app.entity.UserBo;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.util.CsvReaderConfig;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentCsvParser {
    private final CsvReaderService reader;

    public List<UserBo> parse(MultipartFile file, CsvReaderConfig config) {
        try {
            var rows = reader.readCsv(file.getInputStream(), config);
            return rows.stream().map(row -> {
                var student = new UserBo();
                student.setLogin(row[0]);
                student.setName(row[1]);
                student.setRole(UserRole.STUDENT);
                student.setStudiedCourses(new ArrayList<>());
                return student;
            }).toList();
        } catch (Exception ex) {
            log.error("Error occurred when parsing CSV.", ex);
            return List.of();
        }
    }
}
