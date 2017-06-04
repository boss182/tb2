package oliva.gianfranco.tiboxtest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.listener.RequestListener;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.octo.android.robospice.persistence.exception.SpiceException;

import java.util.ArrayList;
import java.util.List;

import oliva.gianfranco.tiboxtest.domain.model.Customer;
import oliva.gianfranco.tiboxtest.domain.usecase.CustomerUseCase;
import oliva.gianfranco.tiboxtest.rest.request.ExportRequest;
import oliva.gianfranco.tiboxtest.rest.request.ImportRequest;
import oliva.gianfranco.tiboxtest.rest.response.ExportResponse;
import oliva.gianfranco.tiboxtest.rest.response.ImportResponse;
import oliva.gianfranco.tiboxtest.view.fragment.CustomerDetailFragment;
import oliva.gianfranco.tiboxtest.view.fragment.CustomerListFragment;

public class TiboxActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener,
                                                                CustomerListFragment.OnFragmentCustomerListListener,
                                                                CustomerDetailFragment.OnFragmentCustomerDetailListener{

    private SpiceManager spiceManager = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);

    private Toolbar mToolbar;

    public static ProgressDialog mDialog;

    public static int REQUEST_CODE_PICKER = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tibox);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle(getString(R.string.app_name));

        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getResources().getString(R.string.msg_progress_dialog));
        mDialog.setCancelable(false);

        Fragment fragment = CustomerListFragment.newInstance();
        updateFragmentContent(fragment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }
    }

    @Override
    public void onCustomerListUpdate() {

    }

    @Override
    public void onStart() {
        if(!spiceManager.isStarted()) {
            spiceManager.start(this);
        }
        super.onStart();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_new_customer:
                fragment = CustomerDetailFragment.newInstance();
                break;

            case R.id.action_back_list:
                fragment = CustomerListFragment.newInstance();
                break;
            case R.id.action_export:
                exportData();
                break;
            case R.id.action_import:
                importData();
                break;

        }

        if(fragment!=null) {
            updateFragmentContent(fragment);
        }
        return false;
    }

    private void updateFragmentContent(Fragment fragment){

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_fragment, fragment)
                .commit();
    }

    public void callCustomerEdit(Customer customer){

        Fragment fragment = CustomerDetailFragment.newInstance(customer);
        updateFragmentContent(fragment);
    }

    private void refreshListCustomerFragment(){

        Fragment fragment = CustomerListFragment.newInstance();
        updateFragmentContent(fragment);
        ((CustomerListFragment) fragment).refreshGrid();
    }

    @Override
    public void onCustomerRegister() {
        refreshListCustomerFragment();
    }

    @Override
    public void onCustomerDelete() {
        refreshListCustomerFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_fragment);
            if(fragment instanceof CustomerDetailFragment) {
                ((CustomerDetailFragment) fragment).setImagesFromGallery(images);
            }
        }
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }

    private void importData(){

        mDialog.show();
        ImportRequest request = new ImportRequest();
        getSpiceManager().execute(request, new ImportListener() );
    }

    public final class ImportListener implements RequestListener<ImportResponse> {

        @Override
        public void onRequestFailure( SpiceException spiceException ) {
            mDialog.dismiss();
        }

        @Override
        public void onRequestSuccess( final ImportResponse result ) {

            if(result.getCustomers().size() > 0){

                List<Customer> customersLocal = CustomerUseCase.listAllCustomer();

                for (Customer  customerImported : result.getCustomers()){

                    int position = CustomerUseCase.findPositionCustomer(customersLocal, customerImported);

                    if(position==-1){
                        CustomerUseCase.registerCustomer(customerImported.getDni(),customerImported.getName(),customerImported.getLastname(), customerImported.getImages());
                    }else{
                        Customer customerLocal = customersLocal.get(position);
                        CustomerUseCase.updateCustomer(customerLocal.getId(), customerImported.getDni(), customerImported.getName(), customerImported.getLastname(), customerImported.getImages());
                    }

                }
            }

            refreshListCustomerFragment();
            mDialog.dismiss();
        }
    }

    private void exportData(){

        mDialog.show();
        List<Customer> customers = CustomerUseCase.exportCustomers();
        ExportRequest request = new ExportRequest(customers);
        getSpiceManager().execute(request, new ExportListener() );
    }

    public final class ExportListener implements RequestListener<ExportResponse> {

        @Override
        public void onRequestFailure( SpiceException spiceException ) {
            mDialog.dismiss();
        }

        @Override
        public void onRequestSuccess( final ExportResponse result ) {

            mDialog.dismiss();
        }
    }
}
