package oliva.gianfranco.tiboxtest.rest.request;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.util.ArrayList;
import java.util.List;

import oliva.gianfranco.tiboxtest.domain.model.Customer;
import oliva.gianfranco.tiboxtest.rest.response.ExportResponse;

/**
 * Created by gianfranco on 4/06/17.
 */

public class ExportRequest extends GoogleHttpClientSpiceRequest<ExportResponse> {

    private String REST_URL = "http://besend.net/app/tibox/export";
    private List<Customer> customers;

    public ExportRequest(List<Customer> customers) {
        super(ExportResponse.class);
        this.customers = customers;
    }

    @Override
    public ExportResponse loadDataFromNetwork() throws Exception {

        HttpContent content = new JsonHttpContent(new JacksonFactory(), new ExportRequestContent(customers));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType("application/json");

        HttpRequest request = getHttpRequestFactory().buildPostRequest(new GenericUrl(REST_URL), content);
        request.setParser(new JacksonFactory().createJsonObjectParser());

        final HttpResponse httpResponse = request.execute();

        return httpResponse.parseAs(ExportResponse.class);
    }

    public class ExportRequestContent{
        @Key
        private List<Customer> customers;

        public ExportRequestContent(List<Customer> customers) {
            this.customers = customers;
        }

        public List<Customer> getCustomers() {
            return customers;
        }

        public void setCustomers(List<Customer> customers) {
            this.customers = customers;
        }
    }
}
