package book;
import book.data.MyUser;
import book.data.Locations;
import book.data.RegistUsers;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.math.BigInteger;

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
        new Locations(15, "Parada de Gonta")
        };

        String hashedpass = "";
        try {
            hashedpass = generateStrongPasswordHash("jj");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {}

        MyUser managers = new MyUser(1, "jorge jesus", "contaparatrabalhos93@gmail.com", hashedpass, 500.0, true);
        RegistUsers ru = new RegistUsers(1, "contaparatrabalhos93@gmail.com");

        et.begin();

        for (Locations l : locations)
            em.persist(l);
        em.persist(managers);
        em.persist(ru);
        et.commit();
    }

    // ------------------------------------------------- password encryption methods --------------------------------------------------
    private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
    
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64*8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
    // ------------------------------------------------- password encryption methods --------------------------------------------------

}
