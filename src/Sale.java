package src;


public class Sale {
    private Lens lens;
    private long price;
    private long quantity;
    private String group1;
    private String group2;
    private String name;

    // for custom sales
    public Sale(long price, long quantity , String name) {
        this.price = price ;
        this.quantity = quantity;
        group1 = "";
        group2 = "";
        this.name = name;
    }
    public Sale(Lens lens, long price, long quantity, String group1, String group2) {
        this.lens = lens;
        this.price = price ;
        this.group1 = group1;
        this.group2 = group2;
        this.quantity = quantity;
        this.name = "";
    }
    public Sale(long price, long quantity, String group1, String group2) {
        this.price = price ;
        this.group1 = group1;
        this.group2 = group2;
        this.quantity = quantity;
        this.name = "";
    }
    public void changeQuant(long input) {
        if (input > 0)
            quantity = input;
        else
            System.out.println("cant change to zero or below");
    }
    public void addQuant(long input) {
        quantity += input;
    }
    public void editSphere(float sphere) {
        lens.editSphere(sphere);
    }
    public void editCyl(float cyl) {
        lens.editCyl(cyl);
    }
    public void editName(String input) {
        this.name = input;
    }
    public void editprice(long price) {
        this.price = price;
    }
    @Override
    public boolean equals(Object input) {
        if (this == input)
            return true;
        if (input == null)
            return false;
        if (getClass() != input.getClass())
            return false;
        Sale compare = (Sale) input;
        // if compared has no lenses then dont compare lenses
        if ((compare.hasLens() || this.hasLens()) && !compare.getLens().equals(this.getLens()))
            return false;
        // if compared has group then compare group
        if ((compare.hasGroup() || this.hasGroup())){
            if(!compare.getGroupS().equals(this.getGroupS()))
            return false;
        }
        // else compare names since its custom
        else {  
            if(!compare.getName().equals(this.getName()))
            return false;
        }
        if (compare.getPrice() != this.getPrice())
            return false;
        return true;
    }
    public Lens getLens() {
        return lens;
    }
    public boolean hasGroup() {
        if (group1.isEmpty() && group2.isEmpty())
            return false;
        return true;
    }
    public long getPrice() {
        return price ;
    }
    public boolean hasName() {
        return name.isEmpty();
    }
    public long getPriceQuant() {
        return price * quantity ;
    }

    public long getQuantity() {
        return quantity;
    }
    public String getName() {
        return name;
    }

    public String getGroupOne() {
        return group1;
    }

    public String getGroupS() {
        return group1 + "/" + group2;
    }
    public boolean hasLens() {
        return lens != null;
    }
    public String getGroupTwo() {
        return group2;
    }
    @Override
    public String toString() {
        if (!hasName()){
            return "\n*****\nquant: " + quantity + "\nName: " + name + "\nprice: " + price;
        }
        return "\n*****\nquant: " + quantity + "\n" + lens + "\ngroup: " + getGroupS() + "\nprice: " + price;  
    }
    public static void main(String[] args) {
        Sale saleObject1 = new Sale(new Lens(3, 5, "blue cut"), 6500, 3, "2", "2");
        System.out.println(saleObject1);
    }
}