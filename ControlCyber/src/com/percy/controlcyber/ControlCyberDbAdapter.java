package com.percy.controlcyber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class ControlCyberDbAdapter {

	/**
	 * Definimos constante con el nombre de la tabla
	 */
	public static final String C_TABLA = "CYBER" ;
	
	/**
	 * Definimos constantes con el nombre de las columnas de la tabla
	 */
	public static final String C_COLUMNA_ID	= "_id";
	public static final String C_COLUMNA_NOMBRE = "maq_nombre";
	public static final String C_COLUMNA_CONDICIONES = "maq_condiciones";
	public static final String C_COLUMNA_HORA_ENTRADA = "maq_hor_ent";
	public static final String C_COLUMNA_HORA_SALIDA = "maq_hor_sal";
	public static final String C_COLUMNA_ACUMULADO = "maq_acumulado";
	private Context contexto;
	private  ControlCyberDbHelper dbHelper;
	private SQLiteDatabase db;
	
	/**
	 * Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
	 */
	private String[] columnas = new String[]{ C_COLUMNA_ID, C_COLUMNA_NOMBRE, C_COLUMNA_CONDICIONES, C_COLUMNA_HORA_ENTRADA, C_COLUMNA_HORA_SALIDA, C_COLUMNA_ACUMULADO /*, C_COLUMNA_OBSERVACIONES*/} ;

	public  ControlCyberDbAdapter(Context context)	{
		this.contexto = context;
	}
	public  ControlCyberDbAdapter abrir() throws SQLException	{
		dbHelper = new ControlCyberDbHelper(contexto);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void cerrar(){
		dbHelper.close();
	}
	/**
	 * Devuelve cursor con todos los registros y columnas de la tabla
	 */
	public Cursor getCursor() throws SQLException	{
		Cursor c = db.query( true, C_TABLA, columnas, null, null, null, null, null, null);
		return c;
	}
	
	/**
	 * Devuelve cursor con todos las columnas del registro
	 */
	public Cursor getRegistro(long id) throws SQLException	{
		Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" + id, null, null, null, null, null);
		
		//Nos movemos al primer registro de la consulta
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	/**
	 * Inserta los valores en un registro de la tabla
	 */
	public long insert(ContentValues reg)	{
		if (db == null)
			abrir();
		return db.insert(C_TABLA, null, reg);
	}
	
	/**
	 * Eliminar el registro con el identificador indicado
	 */
	public long delete(long id)	{
		if (db == null)
			abrir();
		return db.delete(C_TABLA, "_id=" + id, null);
	}
	
/**
 * Modificar el registro
 */
public long update(ContentValues reg){
	long result = 0;
	if (db == null)
		abrir();
	if (reg.containsKey(C_COLUMNA_ID))	{
		// Obtenemos el id y lo borramos de los valores
		long id = reg.getAsLong(C_COLUMNA_ID);
		reg.remove(C_COLUMNA_ID);
		// Actualizamos el registro con el identificador que hemos extraido 
		result = db.update(C_TABLA, reg, "_id=" + id, null); 
	}
	return result;
}
}