package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pilot.
 */
@Entity
@Table(name = "pilot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pilot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Pattern(regexp = "[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]")
    @Column(name = "dni", nullable = false)
    private String dni;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "horas_vuelo", nullable = false)
    private Long horasVuelo;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pilot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Pilot name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDni() {
        return this.dni;
    }

    public Pilot dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getAdress() {
        return this.adress;
    }

    public Pilot adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return this.email;
    }

    public Pilot email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getHorasVuelo() {
        return this.horasVuelo;
    }

    public Pilot horasVuelo(Long horasVuelo) {
        this.setHorasVuelo(horasVuelo);
        return this;
    }

    public void setHorasVuelo(Long horasVuelo) {
        this.horasVuelo = horasVuelo;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Pilot picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Pilot pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pilot)) {
            return false;
        }
        return id != null && id.equals(((Pilot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pilot{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dni='" + getDni() + "'" +
            ", adress='" + getAdress() + "'" +
            ", email='" + getEmail() + "'" +
            ", horasVuelo=" + getHorasVuelo() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            "}";
    }
}
