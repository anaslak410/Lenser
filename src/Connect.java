package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.lang.reflect.Array;
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
    public void insertSale(Connection conn,int bag_id, Sale sale) throws Exception{
        // for inserting sales

        // lenses
        if (sale.hasLens()){
            String sql = "INSERT INTO sales(bag_id, name, quantity, price, lens, groups) VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bag_id);
            pstmt.setString(2, sale.getLens().getType());
            pstmt.setLong(3, sale.getQuantity());
            pstmt.setLong(4, sale.getPrice());
            pstmt.setString(5, sale.getLens().getSphereCyl());
            pstmt.setString(6, sale.getName());
            pstmt.executeUpdate();
        }
        // custom slaes
        else{
            String sql = "INSERT INTO sales(bag_id, name, quantity, price) VALUES(?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bag_id);
            pstmt.setString(2, sale.getName());
            pstmt.setLong(3, sale.getQuantity());
            pstmt.setLong(4, sale.getPrice());
            pstmt.executeUpdate();
        }
    }
    public void insertCostListCell(Connection conn,int costListId, int sell,int purch) throws Exception{

        String sql = "INSERT INTO CostListCells(list_id, sell, purch) VALUES(?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, costListId);
        pstmt.setInt(2, sell);
        pstmt.setInt(3, purch);
        pstmt.executeUpdate();
    }
    public void insertCostList(Connection conn, String date ,String name) throws Exception{

        String sql = "INSERT INTO costLists(name, date) VALUES(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, date);
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
            System.out.println("\n");
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
    public boolean CostListExists(String name) {
        String sql = "SELECT EXISTS(SELECT name FROM CostLists WHERE name = ?);";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            return stmt.executeQuery().getBoolean(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public void createSaleTable() {
        
        String sql = "CREATE TABLE IF NOT EXISTS sales (\n"
                + "	sale_id integer PRIMARY KEY,\n"
                + "	bag_id integer NOT NULL,\n"
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
    public void createCostListsTable() {
        
        String sql = "CREATE TABLE IF NOT EXISTS costLists (\n"
                + "	list_id integer PRIMARY KEY,\n"
                + "	name text NOT NULL UNIQUE,\n"
                + "	date text NOT NULL\n"
                + ");";
        
        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void createCostListCellsTable() {
        
        String sql = "CREATE TABLE IF NOT EXISTS costListCells (\n"
                + "	row_id integer PRIMARY KEY,\n"
                + "	list_id integer NOT NULL,\n"
                + "	sell integer NOT NULL,\n"
                + "	purch integer NOT NULL,\n"
                + "	FOREIGN KEY (list_id)\n"
                + "	    REFERENCES CostLists (list_id)\n"
                + "	    ON DELETE CASCADE\n"
                + "	    ON UPDATE CASCADE\n"
                + ");";
        
        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
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
        String endTrans = "COMMIT;";
        try (Connection conn = this.connect();
            Statement transaction = conn.createStatement()){
            // excute transaction
            transaction.execute(beginTrans);
            // insert bag
            insertBag(conn, getBuyerId(bag.getBuyer()), getDate());
            // insert all sales
            final int lastKey = lastInsertBagKey(conn);
            for (int i = 0; i < bag.size(); i++) {
                Sale sale = bag.getSale(i);
                insertSale(conn, lastKey, sale);
            }
            transaction.execute(endTrans);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void costListToDb(CostList list) {
        String beginTrans = "BEGIN TRANSACTION;";
        String endTrans = "COMMIT;";
        try (Connection conn = this.connect();
            Statement transaction = conn.createStatement()){
            // excute transaction
            transaction.execute(beginTrans);
            // insert bag
            insertCostList(conn,getDate(),list.getname());
            // insert all sales
            final int lastKey = lastInsertCostListKey(conn);
            for (int row = 0, col = 0; row < 10; col++) {
                insertCostListCell(conn, lastKey,list.getSell(row, col) ,list.getPurch(row, col));
                if (col == 3) {
                    row++;
                    col = -1;
                }
            }
            transaction.execute(endTrans);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public int getCostListId(String name) {
        String sql = "SELECT list_id FROM CostLists WHERE name = ?;";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
    public String queryCostLists(int id , String col) {
        String sql = "SELECT * FROM CostLists WHERE list_id = ?;";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.getString(col);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
    public int[] queryCostListCells(int id, String col) {
        String sql = "SELECT * FROM CostListCells WHERE list_id = ?;";
        
        int[] result = new int[40];
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            for (int i = 0; i <= 40; i++) {
                // System.out.println(rs.getString(1) + "\t"
                //                     + rs.getString(2) + "\t"
                //                     + rs.getString(3) + "\t"
                //                     + rs.getString(4));
                rs.next();
                result[i] = rs.getInt(col);
            }
            System.out.println(rs.getArray(1).toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    public CostList dbToCostList(String name) {
        int id = getCostListId(name);
        try {
            CostList result = new CostList(queryCostLists(id, "name"));
            result.sellArrToCostList(queryCostListCells(id, "sell"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new CostList("NoName");
        }
    }
    public int lastInsertBagKey (Connection conn) throws Exception {
        String sql = "SELECT last_insert_rowid() FROM bags";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        return result.getInt(1);
    }
    public int lastInsertCostListKey (Connection conn) throws Exception {
        String sql = "SELECT last_insert_rowid() FROM costLists";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        return result.getInt(1);
    }
    public int getBuyerId(String buyer) {
        String sql = "SELECT buyer_id FROM buyers WHERE name = ?;";
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
    public void queryBag(int id) {
        String sql = "SELECT bag_id, buyer_id,date FROM bags WHERE bag_id = ?;";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void queryCostListsTable() {
        String sql = "SELECT * FROM CostLists;";
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
    public void queryCostListCellsTable() {
        
        String sql = "SELECT * FROM CostListCells;";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t"
                        + rs.getString(2) + "\t"
                        + rs.getString(3) + "\t"
                        + rs.getString(4));
            }
            System.out.println("\n");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void querySale(int id) {
        String sql = "SELECT * FROM sales WHERE bag_id = ?;";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Sales");
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
    public void queryLastBag() {
       String sql = "SELECT * FROM bags ORDER BY bag_id DESC LIMIT 1;";
       try (Connection conn = this.connect();
               Statement stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery(sql)) {
           int lastRecord = rs.getInt(1); 
           queryBag(lastRecord);
           querySale(lastRecord);

       } catch (SQLException e) {
           System.out.println(e.getMessage());
       }
   }
   public ArrayList<String> getCostListNames() {
        String sql = "SELECT name FROM CostLists;";
        ArrayList<String> names = new ArrayList<>();
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                names.add(rs.getString(1));
            }
            return names;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return names;
        }
   }
   public String rsToString(ResultSet set, int numOfCols) throws SQLException {
       String result = "";
       while (set.next()) {
           for (int i = 1; i < numOfCols; i++) {
               result = result + set.getString(i);
               System.out.println(set.getString(i) + "\t");
           }
       }
       return result;
   }
    public String getBuyer(int id) {
        
        String sql = "SELECT name FROM buyers WHERE buyer_id = ?;";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "null";
        }
    }
    public static void main(String[] args) {
        Connect te = new Connect();
        // System.out.println(te.lastInsertKey());
        CostList foo = new CostList("bluecut");
        foo.editSell(3, 3, 3000);
        foo.editSell(3, 1, 3000);
        foo.editSell(3, 2, 3000);
        foo.editSell(3, 0, 3000);
        System.out.println(te.getCostListNames().contains("cut"));
        // te.costListToDb(foo);
        // System.out.println(te.queryCostLists(1,"name"));
        // System.out.println(Arrays.toString(te.queryCostListCells(1, "sell")));
        // System.out.println(te.dbToCostList("NoName"));
        // te.createCostListsTable();
        // te.createCostListCellsTable();
        // te.queryCostListCellsTable();
        // te.queryCostListsTable();
        // te.queryCostListCellsTable();
        // te.queryCostListCellsTable();
        // te.queryLastBag();
        // te.queryCostListsTable();
        // System.out.println(te.CostListExists("blue cut"));
        // System.out.println(te.dbToCostList("name"));





        Bag test = new Bag("anas");
        Sale saleObject1 = new Sale(new Lens(3, 5, "blue cut"), 6500, 3, "2", "2");
        Sale saleObject2 = new Sale(200, 1, "glasses ");
        Sale saleObject3 = new Sale(new Lens(1, 1.75F, "blue cut"), 15250, 33, "10", "6");
        test.addSale(saleObject1);
        test.addSale(saleObject2);
        test.addSale(saleObject3);
        // te.bagToDB(test);
        // te.queryLastBag();
        // te.createCostListsTable();
        Bag test2 = new Bag("eric");
        Bag test3 = new Bag("john");
        for (int i = 0; i < 3; i++) {
            // bagToDB(foo.get(i));
        }
    }
}
