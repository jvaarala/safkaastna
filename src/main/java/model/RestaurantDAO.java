package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    SessionFactory sessionFactory = null;

    public RestaurantDAO() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Sessiotehtaan luonti epäonnistui, suljetaan..");
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
            System.exit(-1);
        }
    }


    protected void finalize() {
        try {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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


    public List<Restaurant> readRestaurants() {
        List restaurants = new ArrayList<>();
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            restaurants = session.createQuery("from Restaurant").getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return restaurants;
    }

    //TODO if necessary
    public boolean updateRestaurant(Restaurant restaurant) {
        boolean success = false;
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // muuutettavat ominaisuudet
            int id = restaurant.getId();
            double lng = restaurant.getLng();

            Restaurant r = (Restaurant)session.get(Restaurant.class, id);

            if (r != null) {
                // r.setFeature(feature);
                r.setLng(lng);
                success = true;
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return success;
    }

    public boolean deleteRestaurant(String id) {
        boolean success = false;

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Restaurant r = (Restaurant)session.get(Restaurant.class, id);
            if (r != null) {
                session.delete(r);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return success;
    }
}
