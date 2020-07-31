package com.david.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.david.cursomc.domain.Categoria;
import com.david.cursomc.dto.CategoriaDTO;
import com.david.cursomc.services.CategoriaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value = "categorias")
public class CategoriaResources {
	
	@Autowired
	private CategoriaService service;
	
	@ApiOperation(value="Busca por id")
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Insere categoria")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
		Categoria obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Atualiza categoria")
	@RequestMapping(value="/{id}",method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto,@PathVariable Integer id){
		Categoria obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
		
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Remove categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"),
			@ApiResponse(code = 404, message = "Código inexistente") })
	@RequestMapping(value="/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);		
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value="Retorna todas categorias")
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<Categoria> list = service.findAll();
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	@ApiOperation(value="Retorna todas categorias com paginacao ascendente")
	@RequestMapping(value="pageASC",method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPageAsc(
		@RequestParam(value="page",defaultValue = "0") Integer page,
		@RequestParam(value="linesPerPage",defaultValue = "24") Integer linesPerPage,
		@RequestParam(value="orderBy",defaultValue = "nome") String orderBy){
		Page<Categoria> list = service.findPage(page, linesPerPage, Sort.by(orderBy).ascending());
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}
	
	@ApiOperation(value="Retorna todas categorias com paginacao descendente")
	@RequestMapping(value="pageDesc",method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPageDesc(
		@RequestParam(value="page",defaultValue = "0") Integer page,
		@RequestParam(value="linesPerPage",defaultValue = "24") Integer linesPerPage,
		@RequestParam(value="orderBy",defaultValue = "nome") String orderBy){
		Page<Categoria> list = service.findPage(page, linesPerPage, Sort.by(orderBy).descending());
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}
	
	

}
