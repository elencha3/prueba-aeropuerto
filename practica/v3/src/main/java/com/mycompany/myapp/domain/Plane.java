package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Plane.
 */
@Entity
@Table(name = "plane")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Plane implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^.\\\\-.$")
    @Column(name = "plate", nullable = false)
    private String plate;

    @Size(min = 10, max = 255)
    @Column(name = "type", length = 255)
    private String type;

    @Min(value = 0)
    @Column(name = "age")
    private Integer age;

    @Size(min = 10, max = 255)
    @Column(name = "serial_number", length = 255)
    private String serialNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plane id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return this.plate;
    }

    public Plane plate(String plate) {
        this.setPlate(plate);
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getType() {
        return this.type;
    }

    public Plane type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAge() {
        return this.age;
    }

    public Plane age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Plane serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plane)) {
            return false;
        }
        return id != null && id.equals(((Plane) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plane{" +
            "id=" + getId() +
            ", plate='" + getPlate() + "'" +
            ", type='" + getType() + "'" +
            ", age=" + getAge() +
            ", serialNumber='" + getSerialNumber() + "'" +
            "}";
    }
}
