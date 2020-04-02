package com.david.cursomc.dto;

import java.io.Serializable;

import com.david.cursomc.domain.Cidade;
import com.david.cursomc.domain.Estado;

public class CidadeDTO implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private Integer id;	
	private String nome;
	
	public CidadeDTO(Cidade obj) {
		id = obj.getId();
		nome = obj.getNome();
	}
	
	public CidadeDTO() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
