package DatabaseManipulation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by jmalasics on 10/21/2014.
 */
public abstract class DatabaseController {

    private EntityManagerFactory emf;
    private EntityManager em;

    public DatabaseController(String persistenceUnit) {
        emf = Persistence.createEntityManagerFactory(persistenceUnit);
        em = emf.createEntityManager();
    }

    protected EntityManager entityManager() {
        return em;
    }

    public void dispose() {
        em.close();
        emf.close();
    }

}
