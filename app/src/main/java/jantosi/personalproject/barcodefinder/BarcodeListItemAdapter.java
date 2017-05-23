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

import jantosi.personalproject.barcodefinder.model.BarcodeToFind;

/**
 * Created by Kuba on 01.05.2017.
 */

public class BarcodeListItemAdapter extends BaseAdapter implements ListAdapter {

    private List<BarcodeToFind> list;
    private Context context;

    public BarcodeListItemAdapter(List<BarcodeToFind> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BarcodeToFind getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BarcodeToFind item = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.barcodes_list_element, null);
        }

        TextView codeTextView = (TextView) convertView.findViewById(R.id.barcodes_list_element_text);
        codeTextView.setText(item.getMatchMode() + ": " + item.getText());

        TextView codeStatusView = (TextView) convertView.findViewById(R.id.barcodes_list_element_status);
        int count = item.getNumMatches();
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
                BarcodeToFind.delete(item);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public List<BarcodeToFind> getList() {
        return list;
    }

    public void setList(List<BarcodeToFind> list) {
        this.list = list;
    }
}
