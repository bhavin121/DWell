package com.code_n_droid.dwell;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import com.code_n_droid.dwell.databinding.MarkerLayoutBinding;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MarkerBuilder {

    public static final int MARKER_TYPE_DELIVERED = 0;
    public static final int MARKER_TYPE_UN_DELIVERED = 1;
    public static final int MARKER_TYPE_CAN_NOT_DELIVERED = 2;

    private View markerView;
    private MarkerLayoutBinding binding;

    public MarkerBuilder(@NonNull LayoutInflater inflater) {
        binding = MarkerLayoutBinding.inflate(inflater);
        markerView = binding.getRoot();
    }

    public BitmapDescriptor getNumberedMarker(int no, int markerType){
        int color=0;
        int res=0;

        if(markerType == MARKER_TYPE_DELIVERED){
            color = Color.parseColor("#3DD598");
            res = R.drawable.ic_delivered;
        }else if(markerType == MARKER_TYPE_UN_DELIVERED){
            color = Color.parseColor("#FF575F");
            res = R.drawable.ic_un_delivered;
        }else if(markerType == MARKER_TYPE_CAN_NOT_DELIVERED){
            color = Color.parseColor("#FFC107");
            res = R.drawable.ic_cannot_deliver;
        }else{
            return null;
        }

        binding.imageView.setImageResource(res);
        binding.no.setText(String.valueOf(no));
        binding.no.setBackgroundTintList(ColorStateList.valueOf(color));

        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        markerView.layout(0,0, markerView.getMeasuredWidth(), markerView.getMeasuredHeight());
        markerView.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(markerView.getMeasuredWidth(), markerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        markerView.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public BitmapDescriptor getMyLocationMarker(){
        binding.imageView.setImageResource(R.drawable.ic_my_location);
        binding.no.setText("");
        binding.no.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));

        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        markerView.layout(0,0, markerView.getMeasuredWidth(), markerView.getMeasuredHeight());
        markerView.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(markerView.getMeasuredWidth(), markerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        markerView.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
