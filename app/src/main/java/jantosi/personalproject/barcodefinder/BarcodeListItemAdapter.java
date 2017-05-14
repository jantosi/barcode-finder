package jantosi.personalproject.barcodefinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kuba on 01.05.2017.
 */

public class BarcodeListItemAdapter<T> extends BaseAdapter implements ListAdapter {

    private List<T> list;
    private Context context;
    private SharedPreferences sharedPref;

    public BarcodeListItemAdapter(List<T> list, Context context, SharedPreferences sharedPref) {
        this.list = list;
        this.context = context;
        this.sharedPref = sharedPref;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final T item = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.barcodes_list_element, null);
        }

        TextView codeTextView = (TextView) convertView.findViewById(R.id.barcodes_list_element_text);
        codeTextView.setText(item.toString());

        TextView codeStatusView = (TextView) convertView.findViewById(R.id.barcodes_list_element_status);
        int count = sharedPref.getInt(item.toString(), 0);
        if (count <= 0) {
            codeStatusView.setText(R.string.barcode_list_element_notfound_status);
        } else {
            codeStatusView.setText(
                    String.format(context.getString(R.string.barcodes_list_element_found_status), count)
            );
        }

        Button deleteButton = (Button) convertView.findViewById(R.id.barcodes_list_element_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                sharedPref.edit().remove(item.toString()).apply();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
