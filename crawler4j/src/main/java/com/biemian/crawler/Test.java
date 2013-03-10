package com.biemian.crawler;

import java.sql.Connection;

import com.biemian.db.common.ConnectionFactory;
import com.biemian.db.common.JDBCUtils;


public class Test {
public static void main(String[] args) throws Exception {
	Connection c = ConnectionFactory.getInstance().getConnection();
	if (c == null) {
		System.out.println("null");
	} else {
		System.out.println("ok");
	}
	
	
}
}
