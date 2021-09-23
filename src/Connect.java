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
    public void insertBuyer(String name) {
        String sql = "INSERT INTO buyers(name, balance) VALUES(?,0)";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertBag(Connection conn, int buyerId, String date) throws Exception {
        // for inserting bags
        String sql = "INSERT INTO bags(buyer_id, date) VALUES(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, buyerId);
        pstmt.setString(2, date);
        pstmt.executeUpdate();
    }
    public void insertSale(Connection conn,int bag_id, String name, long quantity, long price, String lens, String groups) throws Exception{
        
        // for inserting sales
        String sql = "INSERT INTO sales(bag_id, name, quantity, price, lens, groups) VALUES(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, bag_id);
        pstmt.setString(2, name);
        pstmt.setLong(3, quantity);
        pstmt.setLong(4, price);
        pstmt.setString(5, lens);
        pstmt.setString(6, groups);
        pstmt.executeUpdate();
    }
    public void changeBuyerBalance(int newBalance, int rowId) {
        
    }
    public void queryBuyerTable() {
        String sql = "SELECT buyer_id, name, balance FROM buyers;";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" 
                        + rs.getString(2) + "\t"
                        + rs.getString(3));
            }
            System.out.println("\n");
            
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
    public void querySaleTable() {
        // String sql = "SELECT sale_id, bag_id, name, quantity, price, lens, groups" + 
                // "FROM bags;";
        String sql = "SELECT * FROM sales;";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t"
                                 + rs.getString(2) + "\t"
                                 + rs.getString(3) + "\t"
                                 + rs.getString(4) + "\t"
                                 + rs.getString(5) + "\t"
                                 + rs.getString(6) + "\t"
                                 + rs.getString(7));
                
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
    public void createSaleTable() {
        
        String sql = "CREATE TABLE IF NOT EXISTS sales (\n"
                + "	sale_id integer PRIMARY KEY,\n"
                + "	bag_id integer UNIQUE NOT NULL,\n"
                + "	name text NOT NULL,\n"
                + "	quantity integer NOT NULL,\n"
                + "	price integer NOT NULL,\n"
                + "	lens text NULL,\n"
                + "	groups text NULL,\n"
                + "	FOREIGN KEY (bag_id)\n"
                + "	    REFERENCES bags (bag_id)\n"
                + "	    ON DELETE CASCADE\n"
                + "	    ON UPDATE CASCADE\n"
                + ");";
        
        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void createBagTable() {

        String sql = "CREATE TABLE IF NOT EXISTS bags (\n"
                + "	bag_id integer PRIMARY KEY,\n"
                + "	buyer_id integer NOT NULL,\n"
                + "	date text NOT NULL,\n"
                + "	FOREIGN KEY (buyer_id)\n"
                + "	REFERENCES buyers (buyer_id)\n"
                + ");";
        
        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void createBuyerTable() {

        String sql = "CREATE TABLE IF NOT EXISTS buyers (\n"
                + "	buyer_id integer PRIMARY KEY,\n"
                + "	name text UNIQUE NOT NULL,\n"
                + "	balance integer NOT NULL"
                + ");";
        
        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void bagToDB(Bag bag) {
        String beginTrans = "BEGIN TRANSACTION;";
        try (Connection conn = this.connect();
            Statement transaction = conn.createStatement()){
            // excute transaction
            // transaction.execute(beginTrans);
            // insert bag
            insertBag(conn, getBuyerId(bag.getBuyer()), getDate());
            // insert all sales
            // for (int i = 0; i < bag.size(); i++) {
                // Sale sale = bag.getSale(i);
            // insertSale(conn, lastInsertKey(conn), sale.getName(), sale.getQuantity(),
            //             sale.getPrice(), sale.getLens().getSphereCyl(), sale.getGroupS());
            // }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public int lastInsertKey (Connection conn) throws Exception {
        String sql = "SELECT last_insert_rowid()";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        return result.getInt(1);
    }
    public int getBuyerId(String buyer) {
        String sql = "SELECT buyer_id FROM buyers WHERE name = ?";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,buyer);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " EXCEPTIOOON");
            return -1;
        }
    }
    public static void main(String[] args) {
        Connect te = new Connect();
        // System.out.println(te.lastInsertKey());
        // te.createBagTable();
        // te.createSaleTable();
        // te.createBuyerTable();
        // te.queryBuyerTable();
        // te.insertBag(2, te.getDate());
        // te.insertBuyer("mom");
        // System.out.println(te.getBuyerId("mom"));
        // te.getBuyerId("zal");
        // te.insertSale(2, "bluecut", 3, 3000, "4.25/6.5", "16/20");
        // te.deleteBag(2);
        // te.queryBagTable();
        // te.queryBuyerTable();
        // te.querySaleTable();


        ArrayList<Bag> foo = new ArrayList<Bag>();
        Bag test = new Bag("zal");
        Sale saleObject1 = new Sale(new Lens(3, 5, "blue cut"), 6500, 3, "2", "2");
        Sale saleObject2 = new Sale(new Lens(4.5F, 2.25F, "blue cut"), 200, 1, "4", "6");
        Sale saleObject3 = new Sale(new Lens(1, 1.75F, "blue cut"), 15250, 33, "10", "6");
        test.addSale(saleObject1);
        test.addSale(saleObject2);
        test.addSale(saleObject3);
        // System.out.println(test);
        // Sale saleObject2 = new Sale(price, quantity, group1, group2);
        // Sale saleObject3 = new Sale(price, quantity, name);
        te.bagToDB(test);
        te.queryBagTable();
        // te.queryBuyerTable();
        te.querySaleTable();

        Bag test2 = new Bag("eric");
        Bag test3 = new Bag("john");
        foo.add(test);
        foo.add(test2);
        foo.add(test3);
        for (int i = 0; i < 3; i++) {
            // bagToDB(foo.get(i));
        }
    }
}
