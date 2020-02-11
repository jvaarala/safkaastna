package model;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    SessionFactory sessionFactory = null;

    public RestaurantDAO() {
//        Dotenv dotenv = Dotenv.load();
//        final String SECRET = dotenv.get("SECRET");
//
//        Configuration cfg = new Configuration()
//                .addResource("Item.hbm.xml")
//                .addResource("Bid.hbm.xml")
//                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect")
//                .setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.driver")
////                .setProperty("hibernate.connection.datasource", "java:comp/env/jdbc/test")
////                .setProperty("hibernate.order_updates", "true")
//                .setProperty("hibernate.connection.url", "jdbc.mysql://localhost:2206/restaurants")
//                .setProperty("hibernate.connection.username", "user")
//                .setProperty("hibernate.connection.password", SECRET)
//                .setProperty("hibernate.hbm2ddl.auto", "update")
//                .setProperty("show_sql", "false")
//                .addAnnotatedClass(model.Restaurant.class);
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

    public RestaurantDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
            transaction.rollback();
            e.printStackTrace();
        }
        return success;
    }

    public boolean deleteRestaurant(int id) {
        boolean success = false;

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Restaurant r = (Restaurant)session.get(Restaurant.class, id);
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
}
