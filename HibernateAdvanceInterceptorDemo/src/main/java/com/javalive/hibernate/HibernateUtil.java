package com.javalive.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import com.javalive.entity.Book;

/**
 * @author javalive.com 
 * 		   In Hibernate, interceptors are used to inspect the
 *         changes in entity’s property values before they are written and after
 *         they are read from a database.
 * 
 *         You can use the Hibernate interceptor to perform the various
 *         operations such as logging, auditing, profiling etc.
 * 
 *         In Hibernate, an interceptor can be either Session-scoped or
 *         SessionFactory-scoped.
 * 
 *         Session-scoped interceptors are used when a Session is opened. The
 *         following code snippet shows the how to add an interceptor to a
 *         Session.
 * 
 *         Session session = HibernateUtil.getSessionFactory() .withOptions()
 *         .interceptor(new LoggingInterceptor()) .openSession();
 *         
 *         SessionFactory-scoped or global interceptors are used when
 *         SessionFactory is configured and these interceptors will be applied
 *         to applied to all Session opened from that SessionFactory. The
 *         following code snippet shows the how to add an interceptor to a
 *         SessionFactory.
 * 
 *         SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
 *         .applyInterceptor(new LoggingInterceptor()) .build(); 
 *         
 *         The following
 *         example demonstrates how to implement a custom interceptor to log the
 *         SQL queries and entity’s property values when an entity is saved.
 *         
 */
public class HibernateUtil {

	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

				Map<String, Object> settings = new HashMap<>();
				settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
				settings.put(Environment.URL, "jdbc:mysql://localhost:3306/test1?useSSL=false");
				settings.put(Environment.USER, "root");
				settings.put(Environment.PASS, "root");
				// settings.put(Environment.HBM2DDL_AUTO, "update");

				registryBuilder.applySettings(settings);
				registry = registryBuilder.build();

				MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(Book.class);

				Metadata metadata = sources.getMetadataBuilder().build();

				// To apply logging Interceptor using session factory
				sessionFactory = metadata.getSessionFactoryBuilder()
						// .applyInterceptor(new LoggingInterceptor())
						.build();
			} catch (Exception e) {
				if (registry != null) {
					StandardServiceRegistryBuilder.destroy(registry);
				}
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}

	public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}
