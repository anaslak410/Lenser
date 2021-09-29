package src;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bag {
    private String buyer;
    private Date date;
    private ArrayList<Sale> sales;
    private long total;

    public Bag(String buyer) {
        this.buyer = buyer;
        date = new Date();
        sales = new ArrayList<>();
    }
    public void editBuyer(String input) {
        this.buyer = input;
    }
    public Sale getSale(int index) {
        return sales.get(index);
    }
    public long getTotal() {
        return total;
    }
    public int getSaleIndex(Sale sale) {
        return sales.indexOf(sale);
    }
    public long getSaleQuant(Sale sale) {
        try {
            Sale found = getSale(getSaleIndex(sale));
            return found.getQuantity();
        } catch (Exception e) {
            System.out.println("not in bag!");
            throw new IndexOutOfBoundsException();
        }
    }
    public String getDateString() {
        return date.toString();
    }
    public String getBuyer() {
        return buyer;
    }
    public ArrayList<Sale> getSalesArrayList() {
        return sales;
    }
    public boolean contains(Sale input) {
        return sales.contains(input);
    }
    public void addSale(Sale sale) {
        total += sale.getPriceQuant();
        sales.add(sale);
    }
    public void removeSale(Sale sale) {
        if (contains(sale)){
            sales.remove(sale);
            calcTotal();
        }
    }
    public void changeSale(Sale old , Sale nue) {
        if (!sales.contains(old))
            return;
        sales.set(sales.indexOf(old), nue);
        total += ( nue.getPriceQuant() - old.getPriceQuant() ); 
    }
    public void incSaleQuant(Sale sale) {
        if (!sales.contains(sale)){
            System.out.println("not found!");
            return;
        }
        int old = sales.indexOf(sale);
        Sale nue = sales.get(old);
        nue.addQuant(1);
        calcTotal();
    }
    public void changeSaleQuant(Sale sale , long quant) {
        if (!sales.contains(sale) && quant < 1)
            return;
        int oldSaleIndex = sales.indexOf(sale);
        Sale nue = sales.get(oldSaleIndex);
        nue.changeQuant(quant);
        calcTotal();
    }
    public void changeSalePrice(Sale sale , long price) {
        if (!sales.contains(sale) && price < 1)
            return;
        int oldSaleIndex = sales.indexOf(sale);
        Sale nue = sales.get(oldSaleIndex);
        nue.editprice(price);
        calcTotal();
    }
    private void calcTotal() {
        long result = 0;
        for (int i = 0; i < sales.size(); i++) {
            result += sales.get(i).getPriceQuant();
        }
        total = result;
    }
    public int size() {
        return sales.size();
    }
    public void clearBag() {
        sales.clear();
        total = 0;
    }
    public void incTotal(long input) {
        total += input;
    }
    public void editDate(Date date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "--------------\nbuyer: " + buyer + "\n" +
             "total: " + total + "\n" + date + "\n" + sales.toString() + "\n/////////////////";
    }
    public static void main(String[] args) {
        Bag bag = new Bag("buyer");
        System.out.println(bag);
        // test.addQuant(2);
        // bag.incSaleQuant(test);
        // System.out.println(bag.getTotal() + "\n");
        // System.out.println(bag);
        // bag.addSale(test);
        // System.out.println(test.getPriceQuant());
        // System.out.println(bag.getSale(test2));
    }
}