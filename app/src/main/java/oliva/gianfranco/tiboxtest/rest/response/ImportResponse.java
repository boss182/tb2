package oliva.gianfranco.tiboxtest.rest.response;

import com.google.api.client.util.Key;

import java.util.List;

import oliva.gianfranco.tiboxtest.domain.model.Customer;

/**
 * Created by gianfranco on 4/06/17.
 */

public class ImportResponse {

    @Key
    private int error;

    @Key
    private List<Customer> customers;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
