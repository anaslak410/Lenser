package src;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;


public class Connect {

    Connect(){

    }
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:/home/russom/projects/Lens/db/sales.db";
        String foreignKey = "PRAGMA foreign_keys = true;";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            // enable foreign keys
            Statement stmt = conn.createStatement();
            stmt.execute(foreignKey);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public static void bagToDB(Bag bag) {
        
    }
    public void queryBuyerTable() {
        String sql = "SELECT buyer_id, buyer_name FROM buyers;";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                System.out.println(rs.getString(1) + "\t"
                                    + rs.getString(2));
            }
            System.out.println("\n");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertBuyer(String name) {
        String sql = "INSERT INTO buyers(buyer_name) VALUES(?)";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertBag(int buyerId, String date) {
        // for inserting bags
        String sql = "INSERT INTO bags(buyer_id, date) VALUES(?,?)";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buyerId);
            pstmt.setString(2, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void queryBagTable() {
        String sql = "SELECT bag_id, buyer_id,date FROM bags;";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
            }
            System.out.println("\n");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteBuyer(int rowId) {
        String sql = "DELETE FROM buyers WHERE buyer_id = ?";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rowId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteBag(int rowId) {
        String sql = "DELETE FROM bags WHERE bag_id = ?";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rowId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date).toString();
    }

    public static void main(String[] args) {
        Connect te = new Connect();
        te.queryBagTable();
        te.queryBuyerTable();
        te.deleteBuyer(2);
        te.queryBuyerTable();
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
