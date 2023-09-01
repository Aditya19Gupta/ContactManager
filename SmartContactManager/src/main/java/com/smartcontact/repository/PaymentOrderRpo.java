package com.smartcontact.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcontact.entities.PaymentOrder;

public interface PaymentOrderRpo extends JpaRepository<PaymentOrder, Long>{
	
	public PaymentOrder findByOrderId(String orderId);
}
