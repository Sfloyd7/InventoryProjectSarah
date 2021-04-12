package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**This class creates our product class. */
public class Product {


    private final ObservableList<Part> associatedPart = FXCollections.observableArrayList();
    private static int nextId;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**Product Constructor*/
    public Product(int id, String name, double price, int stock, int min, int max) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the Stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the Min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**@param part adds associated part to observable list.*/
    public  void addAssociatedPart(Part part)
    {
        associatedPart.add(part);
    }

    /**@return returns associated part observable list. */
    public  ObservableList<Part> getAssociatedPart()
    {
        return associatedPart;
    }

    /**@param selectedAssociatedPart  deletes the selected associated part from the observable list.*/
    public  void deleteAssociatedPart(Part selectedAssociatedPart){ associatedPart.remove(selectedAssociatedPart);}

    /**@return returns next id for the product id.*/
    public static int getNextId()
    {
        nextId = 1;
        for(Product product : Inventory.getAllProducts())
        {
            if (product.getId() == nextId)
            {
                nextId++;
            }

        }

        return nextId;
    }

}
