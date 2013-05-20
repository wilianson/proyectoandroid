package com.percy.controlcyber;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ControlCyberDbHelper extends SQLiteOpenHelper {

	private static int version = 2;
	private static String name = "ControlCyberDb" ;
	private static CursorFactory factory = null;
	
	public ControlCyberDbHelper(Context context)	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)	{
		Log.i(this.getClass().toString(), "Creando base de datos");
		db.execSQL( "CREATE TABLE CYBER(" +
					" _id INTEGER PRIMARY KEY," +
					" maq_nombre TEXT NOT NULL, " +
					" maq_condiciones TEXT, " +
					" maq_hor_ent TEXT," +
					" maq_hor_sal TEXT," +
					" maq_acumulado TEXT,");
		
		db.execSQL( "CREATE UNIQUE INDEX maq_nombre ON CYBER(maq_nombre ASC)" );
		
		Log.i(this.getClass().toString(), "Tabla CYBER creada");
		
		/*
		 * Insertamos datos iniciales
		 */
		db.execSQL("INSERT INTO CYBER(_id, maq_nombre) VALUES(1,'PC1')");
		db.execSQL("INSERT INTO CYBER(_id, maq_nombre) VALUES(2,'PC2')");
		db.execSQL("INSERT INTO CYBER(_id, maq_nombre) VALUES(3,'PC3')");
		db.execSQL("INSERT INTO CYBER(_id, maq_nombre) VALUES(4,'PC4')");
		db.execSQL("INSERT INTO CYBER(_id, maq_nombre) VALUES(5,'PC5')");
		db.execSQL("INSERT INTO CYBER(_id, maq_nombre) VALUES(6,'PC6')");
		db.execSQL("INSERT INTO CYBER(_id, maq_nombre) VALUES(7,'PC7')");
		
		Log.i(this.getClass().toString(), "Datos iniciales CYBER insertados");
		
		Log.i(this.getClass().toString(), "Base de datos creada");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)	{
		if (newVersion == 2)		{
			db.execSQL("UPDATE CYBER SET maq_hora_ent = ' '," +
		   			   "					maq_hor_sal= '12'," + 
		   			   " WHERE _id = 1");
		}
	
	}
}
