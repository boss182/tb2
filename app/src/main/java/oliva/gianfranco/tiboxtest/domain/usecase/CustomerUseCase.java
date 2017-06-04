package oliva.gianfranco.tiboxtest.domain.usecase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.orm.query.Select;

import java.io.ByteArrayOutputStream;
import java.util.List;

import oliva.gianfranco.tiboxtest.domain.model.Customer;
import oliva.gianfranco.tiboxtest.domain.model.CustomerImage;

/**
 * Created by gianfranco on 2/06/17.
 */

public class CustomerUseCase {

    public static List<Customer> listAllCustomer(){

        List<Customer> customers = Customer.listAll(Customer.class);

        for(Customer customer : customers){

            List<CustomerImage> images = CustomerImage.find(CustomerImage.class,"customer_id = " + customer.getId());
            customer.setImages(images);
        }

        return customers;
    }

    public static void registerCustomer(String dni, String name, String lastname, List<CustomerImage> images){

        Customer customer = new Customer(name,lastname,dni,images);
        long customerId = customer.save();

        for(CustomerImage image : images) {
            CustomerImage customerImage = new CustomerImage(customerId, image.getImageUrl());
            customerImage.save();
        }
    }

    public static void updateCustomer(Long id, String dni, String name, String lastname, List<CustomerImage> images){

        Customer customer = Customer.findById(Customer.class, id);
        customer.setDni(dni);
        customer.setName(name);
        customer.setLastname(lastname);
        customer.setImages(images);
        customer.save();

        CustomerImage.executeQuery("Delete from customer_image where customer_id = ?", String.valueOf(id));

        for(CustomerImage image : images) {
            CustomerImage customerImage = new CustomerImage(id, image.getImageUrl());
            customerImage.save();
        }
    }

    public static void deleteCustomer(Customer customerForDelete){

        Customer customer = Customer.findById(Customer.class, customerForDelete.getId());
        customer.delete();

        CustomerImage.executeQuery("Delete from customer_image where customer_id = ?", String.valueOf(customerForDelete.getId()));
    }

    public static List<Customer> exportCustomers(){

        List<Customer> customers = Customer.listAll(Customer.class);

        for(Customer customer : customers){

            List<CustomerImage> images = CustomerImage.find(CustomerImage.class,"customer_id = " + customer.getId());

            /**for (CustomerImage image : images ){

                Bitmap bm = BitmapFactory.decodeFile(image.getImageUrl());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                //image.setImageEncode(encodedImage);

            }*/

            customer.setImages(images);
        }

        return customers;
    }

    public static int findPositionCustomer(List<Customer> customers, Customer customer) {

        if(customers.size() == 0){
            return -1;
        }

        for (int i=0; i<customers.size(); i++){
            if(customers.get(i).getDni().equals(customer.getDni()))
                return i;

        }

        return -1;
    }
}
