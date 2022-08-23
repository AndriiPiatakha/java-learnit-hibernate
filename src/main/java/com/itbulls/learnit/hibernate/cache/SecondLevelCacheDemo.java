package com.itbulls.learnit.hibernate.cache;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

public class SecondLevelCacheDemo {

	public static void main(String[] args) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Statistics stats = sessionFactory.getStatistics();
		stats.setStatisticsEnabled(true); //Enable statistics logs 

		createEntityInDb(sessionFactory);
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();		
		printStats(stats);
		
		System.out.println("***** Example #1 *****");
		User user = (User) session.load(User.class, 1);
		printStats(stats);
		
		System.out.println("***** Example #2 *****");
		user = (User) session.load(User.class, 1);
		printStats(stats);

		// clear first level cache, so that second level cache is used
		System.out.println("***** Example #3 *****");
		session.evict(user);
		user = (User) session.load(User.class, 1);
		printStats(stats);
		
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

	private static void printStats(Statistics stats) {
		System.out.println("Second Level Hit Count = " + stats.getSecondLevelCacheHitCount());
		System.out.println("Second Level Miss Count = " + stats.getSecondLevelCacheMissCount());
		System.out.println("Second Level Put Count = " + stats.getSecondLevelCachePutCount());
	}
}
