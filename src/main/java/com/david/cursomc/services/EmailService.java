package com.david.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.david.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);

}
