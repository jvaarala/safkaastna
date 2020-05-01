package controllers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object class for Restaurants Handles CRUD
 * (Create-Read-Update-Delete) operations for Restaurant Objects
 */

public class RestaurantDAO extends OnlineDatabase {

	SessionFactory sessionFactory = null;

	/**
	 * Constructor for class Initializes database connection
	 */
	public RestaurantDAO() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			System.out.println("Sessiotehtaan luonti epäonnistui, suljetaan..");
		}
	}

	/**
	 * Needed only in unit testing where mock objects are used
	 *
	 * @param sessionFactory - takes a sessionFactory as an argument so that
	 *                       database connection is NOT created
	 */
	public RestaurantDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Finalize method takes care of closing the sessionFactory after program is
	 * closed
	 */
	protected void finalize() {
		try {
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new restaurant to database
	 *
	 * @param restaurant - Restaurant to be added to database
	 * @return true if operation was successful
	 */
	public boolean createRestaurant(Restaurant restaurant) {
		boolean success = false;

		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(restaurant);
			transaction.commit();
			success = true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * Updates a certain restaurants location (latitude and longitude) information
	 * on database
	 *
	 * @param restaurant - Restaurant to be modified on database
	 * @return true if operation was successful
	 */
	public boolean updateRestaurant(Restaurant restaurant) {
		boolean success = false;
		Transaction transaction = null;

		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// features to be changed
			int id = restaurant.getId();
			double lng = restaurant.getLng();
			double lat = restaurant.getLat();

			Restaurant r = (Restaurant) session.get(Restaurant.class, id);

			if (r != null) {
				// r.setFeature(feature);
				r.setLng(lng);
				r.setLat(lat);
				success = true;
				transaction.commit();
			}
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * Deletes restaurant from database permanently
	 *
	 * @param id - Restaurant is searched from database with id number
	 * @return true if operation was successful
	 */
	public boolean deleteRestaurant(int id) {
		boolean success = false;
		Transaction transaction = null;

		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			Restaurant r = (Restaurant) session.get(Restaurant.class, id);
			if (r != null) {
				session.delete(r);
				transaction.commit();
				success = true;
			}
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public List<Restaurant> downloadRestaurants() {
		System.out.println("Fetching from database");
		List restaurants = new ArrayList<>();
		Transaction transaction = null;

		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			restaurants = session.createQuery("from Restaurant").getResultList();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		System.out.println(".. done");

		return restaurants;
	}

	@Override
	public boolean uploadRestaurants(List<Restaurant> restaurantsFromDb) throws Exception {

		boolean allGood = true;
		for (Restaurant res : restaurantsFromDb) {
			boolean success = updateRestaurant(res);
			if (!success) {
				allGood = false;
			}

		}
		return allGood;
	}
}
