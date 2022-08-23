package com.itbulls.learnit.hibernate.cache;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class QueryLevelCacheDemo {
	public static void main(String[] args) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		createEntityInDb(sessionFactory);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("FROM User");
		query.setCacheable(true);
		query.setCacheRegion("user");
		List<User> users = query.list();
		System.out.println(users);
		
		// Release resources
		transaction.commit();
		session.close();
		sessionFactory.close();
	}

	private static void createEntityInDb(SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		User user = new User("John", "Doe", "john.doe@email.com", new Date());
		session.save(user);
		transaction.commit();
		session.close();
	}
}
