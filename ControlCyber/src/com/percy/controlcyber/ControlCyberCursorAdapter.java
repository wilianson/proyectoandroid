package com.percy.controlcyber;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ControlCyberCursorAdapter extends CursorAdapter
{
	
	private ControlCyberDbAdapter dbAdapter = null ;
	
		
	public ControlCyberCursorAdapter(Context context, Cursor c)
	{
		//colocar los colores
		
		super(context, c);
		dbAdapter = new ControlCyberDbAdapter(context);
		dbAdapter.abrir();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{      
		TextView tv = (TextView) view ;
		
		tv.setText(cursor.getString(cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_NOMBRE)));
		//int colorPos = (cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_NOMBRE)) % colors.length;  
		//view.setBackgroundColor(colors[colorPos]);
		//Log.i(this.getClass().toString(), "esto seria el id?"+cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_NOMBRE));

		
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		final LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
		
		
		return view;
	}
}
