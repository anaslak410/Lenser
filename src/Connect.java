package src;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Connect {
     public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/russom/projects/Lens/db/sales.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static void bagToDB(Bag bag) {
        
    }
    public static void main(String[] args) {
        connect();
        ArrayList<Bag> foo = new ArrayList<Bag>();
        Bag test = new Bag("anas");
        Bag test2 = new Bag("eric");
        Bag test3 = new Bag("john");
        foo.add(test);
        foo.add(test2);
        foo.add(test3);
        for (int i = 0; i < 3; i++) {
            bagToDB(foo.get(i));
        }
    }
}
