package oliva.gianfranco.tiboxtest.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.model.Image;

import oliva.gianfranco.tiboxtest.R;
import oliva.gianfranco.tiboxtest.TiboxActivity;
import oliva.gianfranco.tiboxtest.domain.model.Customer;
import oliva.gianfranco.tiboxtest.domain.model.CustomerImage;
import oliva.gianfranco.tiboxtest.domain.usecase.CustomerUseCase;

public class CustomerDetailFragment extends Fragment implements View.OnClickListener{

    private static final String CUSTOMER_OBJECT = "CUSTOMER_OBJECT";

    private Customer mCustomer;

    private OnFragmentCustomerDetailListener mListener;

    private TextInputLayout mDNITextInputEditText;
    private TextInputLayout mNameTextInputEditText;
    private TextInputLayout mLastNameTextInputEditText;

    private EditText mDNIEditText;
    private EditText mNameEditText;
    private EditText mLastNameEditText;

    private Button mSaveCustomerButton;
    private Button mTakePhotoButton;
    private Button mDeleteCustomerButton;

    private LinearLayout mPhotoGalleryLinearLayout;
    private List<ImageView> mPhotosView = new ArrayList<>();
    private List<CustomerImage> mImages = new ArrayList<CustomerImage>();

    private boolean isEditMode = false;

    public CustomerDetailFragment() {
        // Required empty public constructor
    }

    public static CustomerDetailFragment newInstance() {
        CustomerDetailFragment fragment = new CustomerDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static CustomerDetailFragment newInstance(Customer customer) {
        CustomerDetailFragment fragment = new CustomerDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(CUSTOMER_OBJECT, customer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomer = (Customer) getArguments().getSerializable(CUSTOMER_OBJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_detail, container, false);

        if(mCustomer!=null){
            isEditMode = true;
        }

        initView(view);

        return view;
    }

    private void initView(View view){

        mDNITextInputEditText = (TextInputLayout) view.findViewById(R.id.ti_dni);
        mNameTextInputEditText = (TextInputLayout) view.findViewById(R.id.ti_name);
        mLastNameTextInputEditText = (TextInputLayout) view.findViewById(R.id.ti_lastname);

        mDNIEditText = (EditText) view.findViewById(R.id.et_dni);
        mNameEditText = (EditText) view.findViewById(R.id.et_name);
        mLastNameEditText = (EditText) view.findViewById(R.id.et_lastname);

        mPhotoGalleryLinearLayout = (LinearLayout) view.findViewById(R.id.ll_photo_gallery);

        mSaveCustomerButton = (Button) view.findViewById(R.id.btn_save_customer);
        mSaveCustomerButton.setText(getString((isEditMode)? R.string.btn_update_customer : R.string.btn_save_customer));
        mSaveCustomerButton.setOnClickListener(this);

        mTakePhotoButton = (Button) view.findViewById(R.id.btn_take_photo);
        mTakePhotoButton.setOnClickListener(this);

        mDeleteCustomerButton = (Button) view.findViewById(R.id.btn_delete_customer);
        mDeleteCustomerButton.setVisibility((isEditMode)? View.VISIBLE : View.GONE);
        mDeleteCustomerButton.setOnClickListener(this);

        if(isEditMode){
            setCustomerData(mCustomer);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCustomerDetailListener) {
            mListener = (OnFragmentCustomerDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_save_customer:
                saveCustomer();
                break;

            case R.id.btn_take_photo:
                openGallery();
                break;

            case R.id.btn_delete_customer:
                deleteCustomer();
                break;

            default:
                break;
        }
    }

    public interface OnFragmentCustomerDetailListener {
        // TODO: Update argument type and name
        void onCustomerRegister();

        void onCustomerDelete();
    }

    private void saveCustomer(){

        if(!validateCustomerData()){

            String dni = mDNIEditText.getText().toString();
            String name = mNameEditText.getText().toString();
            String lastname = mLastNameEditText.getText().toString();

            if (isEditMode) {
                CustomerUseCase.updateCustomer(mCustomer.getId(), dni, name, lastname, mImages);
            } else {
                CustomerUseCase.registerCustomer(dni, name, lastname, mImages);
            }

            if (mListener != null) {
                mListener.onCustomerRegister();
            }
        }
    }

    private void deleteCustomer(){

        CustomerUseCase.deleteCustomer(mCustomer);

        if (mListener != null) {
            mListener.onCustomerDelete();
        }
    }

    private void openGallery(){

        ImagePicker.create(getActivity())
                .folderMode(true) // folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select") // image selection title
                //.single() // single mode
                .multi() // multi mode (default mode)
                .limit(3) // max images can be selected (999 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                //.origin(images) // original selected images, used in multi mode
                .start(TiboxActivity.REQUEST_CODE_PICKER); // start image picker activity with request code
    }

    public void setImagesFromGallery(ArrayList<Image> images){

        List<CustomerImage> customerImages = new ArrayList<>();

        for (Image img : images) {

            CustomerImage customerImage = new CustomerImage();
            customerImage.setImageUrl(img.getPath());
            customerImages.add(customerImage);
        }

        setImagesToView(customerImages);

    }

    private void setImagesToView(List<CustomerImage> customerImage){

        mImages = new ArrayList<>();
        mPhotosView = new ArrayList<>();
        mPhotoGalleryLinearLayout.removeAllViews();

        for (CustomerImage img : customerImage) {

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            setImageFromPath(img.getImageUrl(), imageView);

            mPhotosView.add(imageView);
            mPhotoGalleryLinearLayout.addView(imageView);

            mImages.add(img);
        }
    }

    public void setImageFromPath(String path, ImageView imageView){

        File imgFile = new  File(path);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            /*float ratio = Math.min((float) 80 / myBitmap.getWidth(), (float) 80 / myBitmap.getHeight());
            int width = Math.round((float) ratio * myBitmap.getWidth());
            int height = Math.round((float) ratio * myBitmap.getHeight());

            Bi tmap newBitmap = Bitmap.createScaledBitmap(myBitmap, width, height, true);*/

            imageView.setImageBitmap(myBitmap);

        }
    }

    private boolean validateCustomerData(){

        boolean error = false;

        if(mDNIEditText.getText().toString().length() != 8){
            error = true;
            mDNITextInputEditText.setError(getString(R.string.validate_dni_error));
        }else{
            mDNITextInputEditText.setError(null);
        }

        if(mNameEditText.getText().toString().length() == 0){
            error = true;
            mNameTextInputEditText.setError(getString(R.string.validate_empy_field));
        }else{
            mNameTextInputEditText.setError(null);
        }

        if(mLastNameEditText.getText().toString().length() == 0){
            error = true;
            mLastNameTextInputEditText.setError(getString(R.string.validate_empy_field));
        }else{
            mLastNameTextInputEditText.setError(null);
        }

        return error;
    }


    private void setCustomerData(Customer customer){

        mDNIEditText.setText(customer.getDni());
        mNameEditText.setText(customer.getName());
        mLastNameEditText.setText(customer.getLastname());
        setImagesToView(customer.getImages());
    }
}
