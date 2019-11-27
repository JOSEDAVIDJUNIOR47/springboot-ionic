package com.david.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.david.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {

	@SuppressWarnings("static-access")
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(cal.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}

}
