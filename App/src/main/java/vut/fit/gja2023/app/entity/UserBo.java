package vut.fit.gja2023.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class UserBo {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
