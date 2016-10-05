package com.portalbook.forums;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Copyright &copy; David Minter 2004
 * 
 * @author Dave
 */
public class Environment {

	/**
	 * 
	 */
	private Environment() {
	}

    public static Connection getConnection(String path)
			throws ForumException
	{
		// Acquire a connection from the DataSource
		try
		{
			Context ctx = new InitialContext();
			if (ctx == null)
				throw new ForumException(
						"There was a problem connecting to the database",
						"Null Context retrieved", new NullPointerException());

			DataSource ds = (DataSource) ctx
					.lookup("java:comp/env/jdbc/forum");
			if (ds == null)
				throw new ForumException(
						"There was a problem connecting to the database",
						"Null data source retrieved",
						new NullPointerException());

			return ds.getConnection();
		}
		catch (NamingException e)
		{
			throw new ForumException(
					"There was a problem connecting to the database",
					"Naming lookup failed", e);
		}
		catch (SQLException e)
		{
			throw new ForumException(
					"There was a problem connecting to the database",
					"SQL failed", e);
		}
	}
    
    /**
     * 
     */
    public static final String DATABASE_CONTEXT = "java:comp/env/jdbc/forum";
}
