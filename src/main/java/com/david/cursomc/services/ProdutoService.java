package com.david.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.david.cursomc.domain.Categoria;
import com.david.cursomc.domain.Produto;
import com.david.cursomc.repositories.CategoriaRepository;
import com.david.cursomc.repositories.ProdutoRepository;
import com.david.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(    "Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
		
	public Page<Produto> search(String nome, List<Integer> ids,Integer page, Integer linesPerPage,Sort sort) {
		List<Categoria> categorias = categoriaRepository.findAllById(ids);		
		
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, PageRequest.of(page,linesPerPage, sort));
		
	}
	
    
}
