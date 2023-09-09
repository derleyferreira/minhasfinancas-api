package com.wanderley.minhasfinancas.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name="usuario", schema="financas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
 
  @Id 	
  @Column(name="id")
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private long id;
  
  @Column(name = "nome")
  @Setter
  private String nome;
  
  @Column(name = "email")
  private String email;
  
  @Column(name="senha")
  private String senha;
      
}
