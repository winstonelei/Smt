package com.github.netty.learning.productionIdel.example;


import com.github.netty.learning.productionIdel.acceptor.DefaultCommonSrvAcceptor;

public class SrvAcceptorStartup {
	
	public static void main(String[] args) throws InterruptedException {
		
		DefaultCommonSrvAcceptor defaultCommonSrvAcceptor = new DefaultCommonSrvAcceptor(20011,null);
		defaultCommonSrvAcceptor.start();
		
	}

}
