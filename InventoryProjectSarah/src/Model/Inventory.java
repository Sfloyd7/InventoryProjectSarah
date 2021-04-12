package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**This is the Inventory class.
 * This creates  Observable lists.*/
public class Inventory {

    /**
     * create observable lists
     */
    public static ObservableList<Part> allParts = FXCollections.observableArrayList();
    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    public static ObservableList<Part> searchPart = FXCollections.observableArrayList();
    public static ObservableList<Product> searchProduct = FXCollections.observableArrayList();

    public static int nextId;

/**Method to add test data.*/

    static {
        addTestData();
    }

    public static void addTestData() {
        allParts.add(new InHousePart(1, "Wheel", 10.0, 10, 0, 20, 10));
        allParts.add(new InHousePart(2, "Seat", 5.0, 5, 0, 10, 1));
        allParts.add(new InHousePart(3, "Tire", 3.0, 10, 0, 10, 3));
        allParts.add(new OutsourcedPart(4, "Horn", 1.0, 3, 0, 10, "HornsRUs"));
        allParts.add(new OutsourcedPart(5, "Bell", 1.0, 5, 0, 5, "BikeBells.com"));
        allParts.add(new OutsourcedPart(6, "Basket", 10.0, 3, 0, 5, "BikeBaskets"));

        allProducts.add(new Product(1, "Cruiser Bike", 150.0, 5, 0, 15));
        allProducts.add(new Product(2, "Mountain Bike", 300.0, 3, 0, 20));
        allProducts.add(new Product(3, "Hybrid", 200.0, 2, 0, 5));

    }

    /**
     * @param part and product to observable list
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * @param product adds product observable list
     */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**@param partID  looks up the part id.
     * @return part from part id.*/
    public static Part lookUpPartId(int partID) {
        Part q = getPartNumber(partID);
        return q;

    }

    /**@param productID  looks up the product id.
     * @return returns product from product id*/
    public static Product lookUpProductId(int productID) {
        Product q = getProductNumber(productID);
        return q;

    }

    /**@param partialName looks up part by partial name.
     * @return returns a part list from searching the name.*/
    public static ObservableList<Part> lookupPartName(String partialName) {
        ObservableList<Part> allPart = getAllParts();
        ObservableList<Part> namedPart = FXCollections.observableArrayList();


        for (Part p : allPart) {
            if (p.getName().contains(partialName)) {
                namedPart.add(p);
            }
        }
        return namedPart;
    }

    /**@param partialName  looks up product by partial name.
     * @return returns a product list from searching the name.*/
    public static ObservableList<Product> lookupProductName(String partialName){
        ObservableList<Product> allProduct = getAllProducts();
        ObservableList<Product> namedProduct = FXCollections.observableArrayList();

        for(Product p : allProduct){
            if(p.getName().contains(partialName)){
                namedProduct.add(p);
            }
        }
        return namedProduct;
    }

    /**@param id uses the id to select part and update it.*/
    public static void updatePart(int id, Part selectedPart)
    {
        int index = -1;

        for(Part part : getAllParts())
        {
            index++;
            if(part.getId() == id)
            {
                getAllParts().set(index, selectedPart);
            }

        }
    }

    /**@param id uses the id to select product and update it.*/
    public static void updateProduct(int id, Product selectedProduct)
    {
        int index = -1;

        for(Product product: getAllProducts())
        {
            index++;
            if(product.getId() == id)
            {
                getAllProducts().set(index,selectedProduct);
            }
        }
    }

    /**@param deleted removes part from all parts observable list*/
    public static void deletePart(Part deleted) {
        allParts.remove(deleted);
    }
    /**@param delete removes product from all products observable list*/
    public static void deleteProduct(Product delete) {
        allProducts.remove(delete);
    }

    //return observable lists in full

    /**
     * @return the Observable list for parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return the observable list for products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**@param id gets the part number using the id.
     * @return returns part from that part number.*/
    private static Part getPartNumber(int id) {

        for (int i = 0; i < allParts.size(); i++) {
            Part part = allParts.get(i);
            if (part.getId() == id) {
                return part;
            }

        }
        return null;
    }



    /**@param id gets product number using id.
     * @return returns product from that product id.*/
    private static Product getProductNumber(int id) {


        for (int i = 0; i < allProducts.size(); i++) {
            Product product = allProducts.get(i);
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    /**@return Returns the next id for part number*/
    public static int getNextId()
    {
        nextId = 1;
        for(Part part : Inventory.getAllParts())
        {
            if (part.getId() == nextId)
            {
                nextId++;
            }

        }

        return nextId;
    }
}


