package oliva.gianfranco.tiboxtest.domain.model;

import com.google.api.client.util.Key;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by gianfranco on 2/06/17.
 */

public class CustomerImage extends SugarRecord implements Serializable{

    @Key
    private Long id;
    @Key
    private Long customerId;
    @Key
    private String imageUrl;
    @Key
    private String imageEncode;

    public CustomerImage() {
    }

    public CustomerImage(Long customerId, String imageUrl) {
        this.customerId = customerId;
        this.imageUrl = imageUrl;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageEncode() {
        return imageEncode;
    }

    public void setImageEncode(String imageEncode) {
        this.imageEncode = imageEncode;
    }
}
