package com.percy.controlcyber;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class ControlCyber extends ListActivity {
	
	
	public static final String C_MODO  = "modo" ;
	public static final int C_VISUALIZAR = 551 ;
	public static final int C_CREAR = 552 ;
	public static final int C_EDITAR = 553 ;
	public static final int C_ELIMINAR = 554 ;
		
	private ControlCyberDbAdapter dbAdapter;
    private Cursor cursor; 
    private ControlCyberCursorAdapter ControlCyberAdapter ;
    private ListView lista;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_cyber);
		lista = (ListView) findViewById(android.R.id.list);
		dbAdapter = new ControlCyberDbAdapter(this);
		dbAdapter.abrir();
		consultar();
		registerForContextMenu(this.getListView());
	}

	private void consultar()
	{
		cursor = dbAdapter.getCursor();
		startManagingCursor(cursor);
		ControlCyberAdapter = new ControlCyberCursorAdapter(this, cursor);
		lista.setAdapter(ControlCyberAdapter);
		
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control_cyber, menu);
		return true;
	}
	
	private void visualizar(long id)
	{
		// Llamamos a la Actividad Formulario indicando el modo visualización y el identificador del registro 
		Intent i = new Intent(ControlCyber.this, ControlCyberFormulario.class);
		i.putExtra(C_MODO, C_VISUALIZAR);
		i.putExtra(ControlCyberDbAdapter.C_COLUMNA_ID, id);
										
		startActivityForResult(i, C_VISUALIZAR);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		
		visualizar(id);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		Intent i;
		
		switch (item.getItemId())
		{
			case R.id.menu_crear:
				i = new Intent(ControlCyber.this, ControlCyberFormulario.class);
				i.putExtra(C_MODO, C_CREAR);
				startActivityForResult(i, C_CREAR);
				return true;
			case R.id.menu_salir:
				Intent intent = new Intent(Intent.ACTION_MAIN); 
				finish();
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//
		// Nos aseguramos que es la petición que hemos realizado
		//
		switch(requestCode)
		{
			case C_CREAR:
				if (resultCode == RESULT_OK)
					consultar();
				
			case C_VISUALIZAR:
				if (resultCode == RESULT_OK)
					consultar();
				
			default:
				super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private void borrar(final long id)
	{
		/*
		 * Borramos el registro y refrescamos la lista
		 */
		AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);
		
		dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
		dialogEliminar.setTitle(getResources().getString(R.string.control_cyber_eliminar_titulo));
		dialogEliminar.setMessage(getResources().getString(R.string.control_cyber_eliminar_mensaje));
		dialogEliminar.setCancelable(false);
		dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int boton) {
				dbAdapter.delete(id);
				Toast.makeText(ControlCyber.this, R.string.control_cyber_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
				consultar();				
			}
		});
		
		dialogEliminar.setNegativeButton(android.R.string.no, null);
		
		dialogEliminar.show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.setHeaderTitle(cursor.getString(cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_NOMBRE)));
		menu.add(Menu.NONE, C_VISUALIZAR, Menu.NONE, R.string.menu_visualizar);
		menu.add(Menu.NONE, C_EDITAR, Menu.NONE, R.string.menu_editar);
		menu.add(Menu.NONE, C_ELIMINAR, Menu.NONE, R.string.menu_eliminar);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Intent i;
		
		switch(item.getItemId())
		{
	    	case C_ELIMINAR:
	    		borrar(info.id);
	    		return true;
	 
	    	case C_VISUALIZAR:
	    		visualizar(info.id);
				return true;
				
	    	case C_EDITAR:
	    		i = new Intent(ControlCyber.this, ControlCyberFormulario.class);
	    		i.putExtra(C_MODO, C_EDITAR);
	    		i.putExtra(ControlCyberDbAdapter.C_COLUMNA_ID, info.id);
	    		startActivityForResult(i, C_EDITAR);
				return true;
	    }
	    return super.onContextItemSelected(item);
	}
}
