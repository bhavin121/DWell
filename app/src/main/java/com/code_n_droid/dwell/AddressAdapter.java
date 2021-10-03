package com.code_n_droid.dwell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.code_n_droid.dwell.databinding.AddressCardBinding;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressVH> {

    public List<String> data;

    @NonNull
    @Override
    public AddressVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_card, parent, false);
        return new AddressVH(view).link(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

class AddressVH extends RecyclerView.ViewHolder{

    AddressCardBinding binding;
    private AddressAdapter adapter;

    public AddressVH(@NonNull View itemView) {
        super(itemView);
        binding = AddressCardBinding.bind(itemView);

        binding.delete.setOnClickListener(view -> {
            adapter.data.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
        });
    }

    public AddressVH link(AddressAdapter adapter){
        this.adapter = adapter;
        return this;
    }
}
