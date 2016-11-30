package com.journaldev.jsf.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.journaldev.jsf.dao.LoginDAO;

public class MyAppServletContextListener
               implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
//		LoginDAO.execute("CREATE TABLE Users(uid int(20) NOT NULL AUTO_INCREMENT, uname VARCHAR(60) NOT NULL, password VARCHAR(60) NOT NULL, roles VARCHAR(60) NOT NULL, PRIMARY KEY(uid))");
//		LoginDAO.execute("INSERT INTO Users VALUES(null, 'admin','123', 'ADMIN')");
//		LoginDAO.execute("INSERT INTO Users VALUES(null, 'client','123', 'CLIENT')");
//		LoginDAO.execute("INSERT INTO Users VALUES(null, 'user','123', 'USER')");
//		LoginDAO.execute("INSERT INTO Users VALUES(null, 'all','123', 'ADMIN,CLIENT,USER')");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

}