import java.util.Iterator;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.*;

/**
 *
 * @author eyvind
 */
public class JPQLExample {

    public static void main(String[] args) {
        // Open a database connection
        // (create a new database if it doesn't exist yet):
        JPQLExample j = new JPQLExample();
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("testPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        // Store 1000 Point objects in the database:
        em.getTransaction().begin();
        for (int i = 0; i < 1000; i++) {
            Point p = new Point(i, i);
            em.persist(p);
        }
        em.getTransaction().commit();
        j.getPoints(em);
        System.out.println("-----testing------");
        Point p = j.getPointbyX(721, em);
        System.out.println(p);

        // Find the number of Point objects in the database:
        System.out.println("Find the number of Point objects in the database:-");
        Query q1 = em.createQuery("SELECT COUNT(p) FROM Point p");
        System.out.println("Total Points: " + q1.getSingleResult());

        // Find the average X value:
        Query q2 = em.createQuery("SELECT AVG(p.x) FROM Point p");
        System.out.println("Average X: " + q2.getSingleResult());
        j.getAll(em);
        j.getByX(em, 225);

        // Close the database connection:
        em.close();
        emf.close();


    }

    Point getPointbyX(int id, EntityManager em) {
        Point p=null;
        TypedQuery<Point> query
                = em.createQuery("SELECT o FROM Point o where o.id=:id", Point.class);
        query.setParameter("id", id);
        try{
            p = query.getSingleResult();
        }
        catch(NoResultException |NonUniqueResultException nr)
        {
        }

        return p;
    }

    // Retrieve all the Point objects from the database:
    private void getPoints(EntityManager em) {
        System.out.println("get points called in tha hood");
        TypedQuery<Point> query
                = em.createQuery("SELECT o FROM Point o", Point.class);
        List<Point> results = query.getResultList();
        for (Point p : results) {
            System.out.println(p);
        }

    }

    void getAll(EntityManager em) {

        try {
            EntityTransaction entr = em.getTransaction();
            if (!em.getTransaction().isActive())
                entr.begin();
            Query query = em.createNamedQuery("Point.findAll");
            List list = query.getResultList();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Point point = (Point) iterator.next();
                System.out.print(point);
                System.out.println();
            }
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    void getByX(EntityManager em, int x) {

        try {
            EntityTransaction entr = em.getTransaction();
            if (!em.getTransaction().isActive())
                entr.begin();
            Query query = em.createNamedQuery("Point.findByX");
            query.setParameter("x", 3);
            List list = query.getResultList();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Point point = (Point) iterator.next();
                System.out.print(point);
                System.out.println();
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }

    }
}