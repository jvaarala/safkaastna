package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import controllers.RestaurantDAO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantDAOTest {
    static SessionFactory sessionFactory;
    static RestaurantDAO dao;
    static Restaurant rMock;
    static Restaurant r1;
    static Restaurant r2;
    Session session;
    Transaction transaction;
    static List<Restaurant> restaurants;

    @BeforeEach
    void beforeEach() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);
        dao = new RestaurantDAO(sessionFactory);

    }

    @BeforeAll
    public static void setup() {
        rMock = mock(Restaurant.class);
        r1 = new Restaurant(1, 33.33, 62.3);
        r2 = new Restaurant(2, 66.66, 63.2);
        restaurants = Arrays.asList(r1, r2);
    }

    @Test
    @DisplayName("create success")
    void create() {
        when(sessionFactory.openSession())
                .thenReturn(session);
        when(session.beginTransaction())
                .thenReturn(transaction);

        boolean success = dao.createRestaurant(rMock);

        verify(session, times(1))
                .saveOrUpdate(rMock);
        verify(transaction, times(1))
                .commit();
        verify(session, times(1))
                .close();

        assertTrue(success, "createRestaurant does not return true");
    }

    @Test
    @DisplayName("create with exception")
    void createException() {
        when(sessionFactory.openSession())
                .thenReturn(session);
        when(session.beginTransaction())
        	.thenThrow(NullPointerException.class);

        boolean success = dao.createRestaurant(rMock);

        assertFalse(success, "createRestaurant returns TRUE when there is an exception");
    }


    @Test
    @DisplayName("read success")
    void read() {
        when(sessionFactory.openSession())
                .thenReturn(session);

        when(session.beginTransaction())
                .thenReturn(transaction);
        Query queryMock = mock(Query.class);
        when(queryMock.getResultList())
                .thenReturn(restaurants);

        when(session.createQuery("from Restaurant"))
                .thenReturn(queryMock);
        List<Restaurant> response = dao.downloadRestaurants();

        verify(session, times(1))
                .createQuery("from Restaurant");

        verify(transaction, times(1))
                .commit();

        assertEquals(2, response.size(), "Size not correct");
        assertEquals(restaurants, response);
    }

    @Test
    @DisplayName("read with exception")
    void readException() {
        when(sessionFactory.openSession())
                .thenReturn(session);

        when(session.beginTransaction())
                .thenReturn(transaction);
        Query queryMock = mock(Query.class);
        when(queryMock.getResultList())
        	.thenThrow(NullPointerException.class);
        when(session.createQuery("from Restaurant"))
                .thenReturn(queryMock);

        List<Restaurant> response = dao.downloadRestaurants();

        assertEquals(0, response.size(), "Size not correct");
    }

    @Test
    @DisplayName("update success")
    void update() {
        when(sessionFactory.openSession())
                .thenReturn(session);
        when(session.beginTransaction())
                .thenReturn(transaction);
        when(session.get(Restaurant.class, 1))
                .thenReturn(r1);

        boolean success = dao.updateRestaurant(r1);

        verify(session, times(1))
                .get(Restaurant.class, r1.getId());
        verify(transaction, times(1))
                .commit();

        assertTrue(success, "Update does not return true");
    }

    @Test
    @DisplayName("update with exception")
    void updateException() {
        when(sessionFactory.openSession())
                .thenReturn(session);
        when(session.beginTransaction())
                .thenReturn(transaction);
        when(session.get(Restaurant.class, 1))
        	.thenThrow(NullPointerException.class);

        boolean success = dao.updateRestaurant(r1);

        assertFalse(success, "updateRestaurant with exception should return false");
    }


    @Test
    @DisplayName("delete")
    void delete() {
        when(sessionFactory.openSession())
                .thenReturn(session);
        when(session.beginTransaction())
                .thenReturn(transaction);
        when(session.get(Restaurant.class, 1))
                .thenReturn(r1);

        boolean success = dao.deleteRestaurant(1);
        verify(session, times(1))
                .delete(r1);
        verify(transaction, times(1))
                .commit();
        assertTrue(success, "deleteRestaurant returns false");
    }

    @Test
    @DisplayName("delete with exception")
    void deleteException() {
        when(sessionFactory.openSession())
                .thenReturn(session);
        when(session.beginTransaction())
                .thenReturn(transaction);
        when(session.get(Restaurant.class, 1))
        	.thenThrow(NullPointerException.class);

        boolean success = dao.deleteRestaurant(1);
        verify(session, times(0))
                .delete(r1);
        verify(transaction, times(0))
                .commit();
        assertFalse(success, "deleteRestaurant returns false");
    }
}
