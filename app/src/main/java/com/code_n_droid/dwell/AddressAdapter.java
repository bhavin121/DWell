package com.code_n_droid.dwell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.code_n_droid.dwell.databinding.AddressCardBinding;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressVH> {

    public Data data;

    public AddressAdapter ( Data data ) {
        this.data=data;
    }

    @NonNull
    @Override
    public AddressVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_card, parent, false);
        return new AddressVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressVH holder, int position) {
        CustomerDetail customerDetail = data.getCustomerDetails().get(position);
        holder.binding.name.setText( customerDetail.getCustomerName() );
        holder.binding.latLong.setText(
                "Latitude: "+customerDetail.getCustomerAddress().getLatLong().getLatitude()+
                        "\nLongitude: "+customerDetail.getCustomerAddress().getLatLong().getLongitude()
        );
    }

    @Override
    public int getItemCount() {
        return data.getCustomerDetails().size();
    }
}

class AddressVH extends RecyclerView.ViewHolder{

    AddressCardBinding binding;

    public AddressVH(@NonNull View itemView) {
        super(itemView);
        binding = AddressCardBinding.bind(itemView);
    }
}
