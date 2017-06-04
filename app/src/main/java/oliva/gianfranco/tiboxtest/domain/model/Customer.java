package oliva.gianfranco.tiboxtest.domain.model;

import com.google.api.client.util.Key;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gianfranco on 2/06/17.
 */

public class Customer extends SugarRecord implements Serializable{

    @Key
    private Long id;
    @Key
    private String name;
    @Key
    private String lastname;
    @Key
    private String dni;
    @Key
    @Ignore
    private List<CustomerImage> images;


    public Customer() {
    }

    public Customer(String name, String lastname, String dni, List<CustomerImage> images) {
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<CustomerImage> getImages() {
        return images;
    }

    public void setImages(List<CustomerImage> images) {
        this.images = images;
    }

    public String getFullName(){

        return this.name + " " + this.lastname;
    }
}
