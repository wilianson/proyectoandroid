package com.percy.controlcyber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ControlCyberFormulario extends Activity {
	private ControlCyberDbAdapter dbAdapter;
    private Cursor cursor; 
	//
    // Modo del formulario
    //
	private int modo ;
	//
	// Identificador del registro que se edita cuando la opción es MODIFICAR
	//
	private long id ;
	//
	// Elementos de la vista
	//
	private EditText nombre;
	private EditText condiciones;
	private EditText hora_ent;
	private EditText hora_sal;
	private EditText acumulado;
	private Button boton_guardar;
	private Button boton_cancelar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiti_control_cyber_formulario);
		
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();

		if (extra == null) return;
		
		//
		// Obtenemos los elementos de la vista
		//
		nombre = (EditText) findViewById(R.id.nombre);
		condiciones = (EditText) findViewById(R.id.condiciones);
		hora_ent = (EditText) findViewById(R.id.hora_ent);
		hora_sal = (EditText) findViewById(R.id.hora_sal);
		acumulado = (EditText) findViewById(R.id.acumulado);
		boton_guardar = (Button) findViewById(R.id.boton_guardar);
		boton_cancelar = (Button) findViewById(R.id.boton_cancelar);
		
		//
		// Creamos el adaptador  
		//
		dbAdapter = new ControlCyberDbAdapter(this);
		dbAdapter.abrir();
		
		//
		// Obtenemos el identificador del registro si viene indicado
		//
		if (extra.containsKey(ControlCyberDbAdapter.C_COLUMNA_ID))
		{
			id = extra.getLong(ControlCyberDbAdapter.C_COLUMNA_ID);
			consultar(id);
		}
		
		//
		// Establecemos el modo del formulario
		//
		establecerModo(extra.getInt(ControlCyber.C_MODO));
		
		//
		// Definimos las acciones para los dos botones
		//
		boton_guardar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				guardar();
			}
		});
		
		boton_cancelar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				cancelar();	
			}
		});
		
	}
	
private void establecerModo(int m)
{
	this.modo = m ;
	
	if (modo == ControlCyber.C_VISUALIZAR)
	{
		this.setTitle(nombre.getText().toString());
		this.setEdicion(false);
	}
	else if (modo == ControlCyber.C_CREAR)
	{
		this.setTitle(R.string.control_cyber_crear_titulo);
		this.setEdicion(true);
	}
	else if (modo == ControlCyber.C_EDITAR)
	{
		this.setTitle(R.string.control_cyber_editar_titulo);
		this.setEdicion(true);
	}
}
	
	private void consultar(long id)
	{
		//
		// Consultamos el centro por el identificador
		//
		cursor = dbAdapter.getRegistro(id);
		
		nombre.setText(cursor.getString(cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_NOMBRE)));
		condiciones.setText(cursor.getString(cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_CONDICIONES)));
		hora_ent.setText(cursor.getString(cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_HORA_ENTRADA)));
		hora_sal.setText(cursor.getString(cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_HORA_SALIDA)));
		acumulado.setText(cursor.getString(cursor.getColumnIndex(ControlCyberDbAdapter.C_COLUMNA_ACUMULADO)));		
	}
	
	private void setEdicion(boolean opcion)
	{
		nombre.setEnabled(opcion);
		condiciones.setEnabled(opcion);
		hora_ent.setEnabled(opcion);
		hora_sal.setEnabled(opcion);
		acumulado.setEnabled(opcion);
	}
	
private void guardar()
{
	//
	// Obtenemos los datos del formulario
	// 
	ContentValues reg = new ContentValues();
	
	//
	// Si estamos en modo edición añadimos el identificador del registro que se utilizará en el update
	//
	if (modo == ControlCyber.C_EDITAR)
		reg.put(ControlCyberDbAdapter.C_COLUMNA_ID, id);
	
	reg.put(ControlCyberDbAdapter.C_COLUMNA_NOMBRE, nombre.getText().toString());
	reg.put(ControlCyberDbAdapter.C_COLUMNA_CONDICIONES, condiciones.getText().toString());
	reg.put(ControlCyberDbAdapter.C_COLUMNA_HORA_ENTRADA, hora_ent.getText().toString());
	reg.put(ControlCyberDbAdapter.C_COLUMNA_HORA_SALIDA, hora_sal.getText().toString());
	reg.put(ControlCyberDbAdapter.C_COLUMNA_ACUMULADO, acumulado.getText().toString());
	
	if (modo == ControlCyber.C_CREAR)
	{
		dbAdapter.insert(reg);
		Toast.makeText(ControlCyberFormulario.this, R.string.control_cyber_crear_confirmacion, Toast.LENGTH_SHORT).show();
	}
	else if (modo == ControlCyber.C_EDITAR)
	{
		Toast.makeText(ControlCyberFormulario.this, R.string.control_cyber_editar_confirmacion, Toast.LENGTH_SHORT).show();
		dbAdapter.update(reg);
	}
	
	//
	// Devolvemos el control
	//
	setResult(RESULT_OK);
	finish();
}
	
	private void cancelar()
	{
		setResult(RESULT_CANCELED, null);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
	    if (modo == ControlCyber.C_VISUALIZAR)
			getMenuInflater().inflate(R.menu.control_cyber_formulario_ver, menu);
		else
			getMenuInflater().inflate(R.menu.control_cyber_formulario_editar, menu);
		return true;
	}
	
@Override
public boolean onMenuItemSelected(int featureId, MenuItem item) {
	
	switch (item.getItemId())
	{
		case R.id.menu_eliminar:
			borrar(id);
			return true;
			
		case R.id.menu_cancelar:
			cancelar();
			return true;
			
		case R.id.menu_guardar:
			guardar();
			return true;
			
		case R.id.menu_editar:
			establecerModo(ControlCyber.C_EDITAR);
			return true;
	}
	
	return super.onMenuItemSelected(featureId, item);
}
	
	private void borrar(final long id)
	{
		/*
		 * Borramos el registro con confirmación
		 */
		AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);
		
		dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
		dialogEliminar.setTitle(getResources().getString(R.string.control_cyber_eliminar_titulo));
		dialogEliminar.setMessage(getResources().getString(R.string.control_cyber_eliminar_mensaje));
		dialogEliminar.setCancelable(false);
		
		dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int boton) {
				dbAdapter.delete(id);
				Toast.makeText(ControlCyberFormulario.this, R.string.control_cyber_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
				/*
				 * Devolvemos el control
				 */
				setResult(RESULT_OK);
				finish();
			}
		});
		
		dialogEliminar.setNegativeButton(android.R.string.no, null);
		
		dialogEliminar.show();
		
	}

	
}