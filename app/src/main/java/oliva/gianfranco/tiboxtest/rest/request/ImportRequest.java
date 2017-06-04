package oliva.gianfranco.tiboxtest.rest.request;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import oliva.gianfranco.tiboxtest.rest.response.ImportResponse;

/**
 * Created by gianfranco on 4/06/17.
 */

public class ImportRequest extends GoogleHttpClientSpiceRequest<ImportResponse> {

    private String REST_URL = "http://besend.net/app/tibox/import";
    public ImportRequest() {
        super(ImportResponse.class);
    }

    @Override
    public ImportResponse loadDataFromNetwork() throws Exception {

        HttpHeaders headers = new HttpHeaders();

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(REST_URL));
        request.setHeaders(headers);
        request.setParser(new JacksonFactory().createJsonObjectParser());

        final HttpResponse httpResponse = request.execute();

        return httpResponse.parseAs(ImportResponse.class);
    }
}
