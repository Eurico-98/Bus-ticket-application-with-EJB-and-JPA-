package book;
import book.data.MyUser;
import book.data.Locations;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EntityGenarator{
    public static void main( String[] args ){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tables");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        Locations[] locations = {
                    new Locations(1 , "Coimbra"),
                    new Locations(2 , "Lisboa"),
                    new Locations(3 , "Porto"),
                    new Locations(4 , "Braga"),
                    new Locations(5 , "Faro"),
                    new Locations(6 , "Beja"),
                    new Locations(7 , "Guimaraes"),
                    new Locations(8 , "Barcelos"),
                    new Locations(9 , "Tores vedras"),
                    new Locations(10, "Tondela"),
                    new Locations(11, "Treixedo"),
                    new Locations(12, "Tonda"),
                    new Locations(13, "Sabugosa"),
                    new Locations(14, "Figeiro"),
                    new Locations(15, "Parada de Gonta")};

        MyUser managers = new MyUser(1, "jorge jesus", "contaparatrabalhos93@gmail.com", "jj", 500.0, true);

        et.begin();

        for (Locations l : locations)
            em.persist(l);
        em.persist(managers);
        et.commit();
    }
}
