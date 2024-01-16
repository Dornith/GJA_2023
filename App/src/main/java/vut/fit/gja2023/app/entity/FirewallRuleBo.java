package vut.fit.gja2023.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * A business object representing a firewall rule.
 */
@Entity
@Data
@Table(name = "firewall_rule")
public class FirewallRuleBo {
    public static final int ADDRESS_MAX_LENGTH = 30;

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = ADDRESS_MAX_LENGTH)
    @Column(name = "address", nullable = false, length = ADDRESS_MAX_LENGTH)
    private String address;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @ManyToOne
    private UserBo author;
}
