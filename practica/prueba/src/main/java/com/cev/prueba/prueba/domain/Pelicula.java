package com.cev.prueba.prueba.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class Pelicula {

	@Id
	@GeneratedValue
	Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	String titulo;
	int puntuacion;
	String sinopsis;
	String director;
	Date fechaEstreno;
	
	@OneToOne(mappedBy = "pelicula") //Para que no genere otra FK ponemos esta anotación y hacemos referencia a la foreign key definida en estreno que es pelicula
	@JsonManagedReference //Para que no se nos cree una referencia circular y empiece a serializar de una a otra y de otra a una añadimos esto y JsonBackReference en estreno
	Estreno estreno;
	
	
	@OneToMany(mappedBy="pelicula")
	@JsonManagedReference
	List<Review> reviews;
	
	@ManyToMany(mappedBy="peliculas")
	@JsonIgnoreProperties({"peliculas"})
	List<Actor> actor;

	public List<Actor> getActores() {
		return actor;
	}
	public void setActores(List<Actor> actor) {
		this.actor= actor;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	public String getSinopsis() {
		return sinopsis;
	}
	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public Date getFechaEstreno() {
		return fechaEstreno;
	}
	public void setFechaEstreno(Date fechaEstreno) {
		this.fechaEstreno = fechaEstreno;
	}
	public Estreno getEstreno() {
		return estreno;
	}
	public void setEstreno(Estreno estreno) {
		this.estreno = estreno;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	
}
