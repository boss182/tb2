package oliva.gianfranco.tiboxtest.view.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import oliva.gianfranco.tiboxtest.R;
import oliva.gianfranco.tiboxtest.TiboxActivity;
import oliva.gianfranco.tiboxtest.domain.model.Customer;

/**
 * Created by gianfranco on 2/06/17.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private List<Customer> mCustomers;
    private Activity mActivity;

    public CustomerListAdapter(List<Customer> customers, Activity activity) {
        setCustomers(customers);
        mActivity = activity;
    }

    public void setCustomers(List<Customer> customers) {
        mCustomers = new ArrayList<>(customers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Customer customer = mCustomers.get(position);

        holder.getDni().setText(customer.getDni());
        holder.getName().setText(customer.getFullName());

        if(customer.getImages().size()>0 && !customer.getImages().get(0).getImageUrl().isEmpty()) {
            setImageFromPath(customer.getImages().get(0).getImageUrl(), holder.getImage());
        }else{
            holder.getImage().setImageResource(R.drawable.default_photo);
        }

        View view = holder.getImage().getRootView();

        if(view instanceof LinearLayout){

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((TiboxActivity) mActivity).callCustomerEdit(customer);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mDni;
        private TextView mName;

        public ViewHolder(View view) {
            super(view);

            mDni = (TextView) view.findViewById(R.id.item_dni);
            mName = (TextView) view.findViewById(R.id.item_name);
            mImage = (ImageView) view.findViewById(R.id.item_image);
        }

        public ImageView getImage() {
            return mImage;
        }

        public void setImage(ImageView mImage) {
            this.mImage = mImage;
        }

        public TextView getDni() {
            return mDni;
        }

        public void setDni(TextView mDni) {
            this.mDni = mDni;
        }

        public TextView getName() {
            return mName;
        }

        public void setName(TextView mName) {
            this.mName = mName;
        }
    }

    public void setImageFromPath(String path, ImageView imageView){

        File imgFile = new  File(path);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageView.setImageBitmap(myBitmap);

        }
    }
}
