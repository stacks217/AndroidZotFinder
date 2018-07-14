package edu.uci.ZotFinder.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.model.ImageLabelData;
import edu.uci.ZotFinder.model.ImageLabelModel;

public class ImageLabelAdapter extends BaseAdapter{
    private Context mContext;
    private List<? extends ImageLabelModel> data;
    private int layoutID;

    public ImageLabelAdapter(Context context, ImageLabelData imageLabelData, int layoutID) {
        mContext = context;
        data = imageLabelData.getData();
        this.layoutID = layoutID;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutID, parent, false);
        }
        ((ImageView) convertView.findViewById(R.id.gridIcon)).setImageResource(data.get(position).getImage());
        ((TextView) convertView.findViewById(R.id.gridLabel)).setText(data.get(position).getLabel());
        return convertView;
    }
}
