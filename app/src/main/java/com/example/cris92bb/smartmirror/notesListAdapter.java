package com.example.cris92bb.smartmirror;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alessio on 21/03/2017.
 */

public class notesListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Nota> note;
	private int size;
	LayoutInflater inflater;

    public notesListAdapter(Context context, ArrayList<Nota> note){
        this.context = context;
        this.note = note;
	    this.size = note.size();
	    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return note.size();
    }

    @Override
    public Nota getItem(int position) {
        return note.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	    Log.v("notesListAdapter","Position:"+position+" count:"+this.getCount());
	    View noteView = convertView;
	    if(noteView == null)
		    noteView = inflater.inflate(R.layout.nota, parent, false);
        if(position<this.getCount()){
	        TextView text_date = (TextView) noteView.findViewById(R.id.text_date);
	        TextView text_title = (TextView) noteView.findViewById(R.id.text_title);
	        TextView text_description = (TextView) noteView.findViewById(R.id.text_description);
	        text_date.setText(note.get(position).getDate());
	        text_title.setText(note.get(position).getTitle());
	        text_description.setText(note.get(position).getDescription());
        }

        return noteView;
    }
}
