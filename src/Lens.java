package src;
import java.util.ArrayList;

import javax.lang.model.util.ElementScanner6;

// lens for databse
public class Lens{
    private float sphere;
    private float cylinder;
    private String type;

    public Lens(float sphere, float cylinder, String type){
        if (isCyl(cylinder) && isSphere(sphere)){
            this.sphere = sphere;
            this.cylinder = cylinder;
            this.type = type.toLowerCase();
        }
        else {throw new IllegalArgumentException();}
    }
    @Override
    public boolean equals(Object input) {
        if (this == input)
            return true;
        if (input == null)
            return false;
        if (getClass() != input.getClass())
            return false;
        Lens compare = (Lens) input;


        if (compare.getCylinder() != this.getCylinder())
            return false;
        if (compare.getSphere() != this.getSphere())
            return false;
        if (!this.getType().equals(compare.getType()))
            return false;
        return true;
    }
    public float getSphere(){
        return sphere;
    }
    public float getCylinder() {
        return cylinder;
    }
    public String getType() {
        return type;
    }
    public void editSphere(float input) {
        if (isSphere(input))
            sphere = input;
        else
            throw new IllegalArgumentException();
    }
    public void editCyl(float input) {
        if (isCyl(input))
            cylinder = input;
        else
            throw new IllegalArgumentException();
    }
    public boolean isSphere(float input) {
        ArrayList<Float> allowedSphere = new ArrayList<>();
        float sph = 0;

        allowedSphere.add(sph);
        for (int i = 0; i < 40;i++) {
            if (i < 24){
                sph += 0.25;
                allowedSphere.add(sph);                    
            }
            else if (i < 32){
                sph += 0.50;
                allowedSphere.add(sph);                    
            }
            else if (i < 38) {
                sph += 1.00;
                allowedSphere.add(sph);                    
            }
            else {
                sph += 2.00;
                allowedSphere.add(sph);                    
            }
        }

        if (allowedSphere.contains(input))
            return true;
        else
            return false;
    }
    public boolean isCyl(float input) {
        ArrayList<Float> allowedCyl = new ArrayList<>();
        float cyl = 0;
        allowedCyl.add(cyl);
        for (int i = 0; i < 20;i++) {
            if (i < 16){
                cyl += 0.25;
                allowedCyl.add(cyl);                    
            }
            else if (i < 20) {
                cyl += 0.50;
                allowedCyl.add(cyl);                    
            }
        }
        if (allowedCyl.contains(input))
            return true;
        else
            return false;
    }
    public String getSphereCyl() {
        return sphere + "/" + cylinder;
    }
    @Override
    public String toString() {
        return "type: " + type + "\n" + "sphere: " + sphere + "\n" + "cylinder: " + cylinder + "\n" ;
    }
    
    public static void main(String[] args) {
        // Lens test = new Lens(20.00F,5.50F,"blue cut");
        // Lens test2 = new Lens(15.50F,5.50F,"blue cut");
        // System.out.println(test2);
    }

}