package com.david.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.print.attribute.standard.PageRanges;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.david.cursomc.domain.Categoria;
import com.david.cursomc.domain.Cliente;
import com.david.cursomc.domain.ItemPedido;
import com.david.cursomc.domain.PagamentoComBoleto;
import com.david.cursomc.domain.Pedido;
import com.david.cursomc.domain.enums.EstadoPagamento;
import com.david.cursomc.repositories.ItemPedidoRepository;
import com.david.cursomc.repositories.PagamentoRepository;
import com.david.cursomc.repositories.PedidoRepository;
import com.david.cursomc.security.UserSS;
import com.david.cursomc.security.UserService;
import com.david.cursomc.services.exceptions.AuthorizationException;
import com.david.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService; 
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private EmailService emailService;
	
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(    "Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}	
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);		
		return obj;
		
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage,String ordenaly) {	
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.by(ordenaly).descending());
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
	

}
