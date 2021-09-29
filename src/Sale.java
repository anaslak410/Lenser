package src;


public class Sale {
    private Lens lens;
    private long price;
    private long quantity;
    private String name;
    private boolean custom;

    // for custom sales
    public Sale(long price, long quantity , String name) {
        this.price = price ;
        this.quantity = quantity;
        this.name = name;
        this.custom = true;
    }
    public Sale(Lens lens, long price, long quantity, String group1, String group2) {
        this.lens = lens;
        this.price = price ;
        this.name = group1 + "//" + group2;
        this.quantity = quantity;
        this.custom = false;
    }
    public Sale(long price, long quantity, String group1, String group2 , String name) {
        this.lens = new Lens(0, 0, name);
        this.price = price ;
        this.quantity = quantity;
        this.name = group1 + "//" + group2;
        this.custom = false;
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
        // if compared is custom then compare names only
        if (compare.isCustom() || this.isCustom()) {
            if (!compare.getName().equals(this.getName()))
                return false;
        }
        // else compare lens names AND group names 
        else {
            if(!compare.getLens().getType().equals(this.getLens().getType()))
                return false;
            if (!compare.getName().equals(this.getName()))
                return false;
        }
        if (compare.getPrice() != this.getPrice())
            return false;
        return true;
    }
    public Lens getLens() {
        return lens;
    }
    public long getPrice() {
        return price ;
    }
    public long getPriceQuant() {
        return price * quantity ;
    }
    public boolean isCustom() {
        return custom;
    }
    public long getQuantity() {
        return quantity;
    }
    public String getName() {
        return name;
    }
    public boolean hasLens() {
        return lens != null;
    }
    @Override
    public String toString() {
        if (!isCustom()){
            return "\n*****\nquant: " + quantity + "\nName: " + name + "\nprice: " + price;
        }
        return "\n*****\nquant: " + quantity + "\n" + lens + "\ngroup: " + getName() + "\nprice: " + price;  
    }
    public static void main(String[] args) {
        Sale test = new Sale(new Lens(0, 0, "type"),1,1,"5","2");
        Sale test2 = new Sale(new Lens(0, 0, "type"),1,9,"5","2");
        System.out.println(test.equals(test2));
    }
}