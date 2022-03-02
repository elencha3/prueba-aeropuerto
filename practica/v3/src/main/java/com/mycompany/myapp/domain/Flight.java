package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Flight.
 */
@Entity
@Table(name = "flight")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 0, max = 10)
    @Column(name = "num_flight", length = 10)
    private String numFlight;

    @ManyToOne
    @JsonIgnoreProperties(value = { "flights" }, allowSetters = true)
    private Pilot pilot;

    @ManyToOne
    private Plane plane;

    @ManyToMany
    @JoinTable(name = "rel_flight__crew", joinColumns = @JoinColumn(name = "flight_id"), inverseJoinColumns = @JoinColumn(name = "crew_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "flights" }, allowSetters = true)
    private Set<Crew> crews = new HashSet<>();

    @ManyToOne
    private Airport origin;

    @ManyToOne
    private Airport destination;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Flight id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumFlight() {
        return this.numFlight;
    }

    public Flight numFlight(String numFlight) {
        this.setNumFlight(numFlight);
        return this;
    }

    public void setNumFlight(String numFlight) {
        this.numFlight = numFlight;
    }

    public Pilot getPilot() {
        return this.pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public Flight pilot(Pilot pilot) {
        this.setPilot(pilot);
        return this;
    }

    public Plane getPlane() {
        return this.plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Flight plane(Plane plane) {
        this.setPlane(plane);
        return this;
    }

    public Set<Crew> getCrews() {
        return this.crews;
    }

    public void setCrews(Set<Crew> crews) {
        this.crews = crews;
    }

    public Flight crews(Set<Crew> crews) {
        this.setCrews(crews);
        return this;
    }

    public Flight addCrew(Crew crew) {
        this.crews.add(crew);
        crew.getFlights().add(this);
        return this;
    }

    public Flight removeCrew(Crew crew) {
        this.crews.remove(crew);
        crew.getFlights().remove(this);
        return this;
    }

    public Airport getOrigin() {
        return this.origin;
    }

    public void setOrigin(Airport airport) {
        this.origin = airport;
    }

    public Flight origin(Airport airport) {
        this.setOrigin(airport);
        return this;
    }

    public Airport getDestination() {
        return this.destination;
    }

    public void setDestination(Airport airport) {
        this.destination = airport;
    }

    public Flight destination(Airport airport) {
        this.setDestination(airport);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flight)) {
            return false;
        }
        return id != null && id.equals(((Flight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Flight{" +
            "id=" + getId() +
            ", numFlight='" + getNumFlight() + "'" +
            "}";
    }
}
