package com.botsydroid.controltarjeta;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jofl on 22/10/2015.
 */
public class Record extends SQLiteOpenHelper {
    private static int version = 1;
    private static String name = "RecordDb";
    private static SQLiteDatabase.CursorFactory factory = null;


    public Record(Context context) {
        super(context, name, factory, version);
    }
    public void onCreate(SQLiteDatabase db) {
        Log.i(this.getClass().toString(), "Creando base de datos");
        db.execSQL("CREATE TABLE valores( _id INTEGER PRIMARY KEY AUTOINCREMENT, numero  TEXT NOT NULL, nombre TEXT  )");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertar(ContentValues values )
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //ContentValues values = new ContentValues();
        //values.put("StudentName", queryValues.get("StudentName"));
        database.insert("valores", null, values);
        database.close();
        Log.d(this.getClass().toString(), "Insertado");

    }
    public void modificar(ContentValues values, int id )
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //ContentValues values = new ContentValues();
        //values.put("StudentName", queryValues.get("StudentName"));
        //database.insert("valores", null, values);

        database.update("valores",values,"_id="+id, null);
        database.close();
        Log.d(this.getClass().toString(), "Insertado");

    }
    /*
    public int cantidad()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCount= db.rawQuery("select count(*) from valores", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;
    }
    public int cantidad(String tipo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCount= db.rawQuery("select count(*) from valores where nombre="+tipo, null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;
    }
    public float promedio(String tipo){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCount= db.rawQuery("select AVG(valor) from valores where nombre="+tipo , null);
        mCount.moveToFirst();
        float count= mCount.getFloat(0);
        mCount.close();
        return count;
    }
    public float promedio()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCount= db.rawQuery("select AVG(valor) from valores", null);
        mCount.moveToFirst();
        float count= mCount.getFloat(0);
        mCount.close();
        return count;
    }*/
    public Cursor datos(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCount= db.rawQuery("select  _id, numero, nombre from valores order by nombre DESC limit 20", null);
        return mCount;
    }

    public Cursor datos(String tipo){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] campos = new String[] {"valor"};
        String []args = new String[] {tipo};
        Cursor mCount = db.query("valores",campos,"nombre =?",args,null,null,"_id DESC","20");
        return mCount;
    }



}
