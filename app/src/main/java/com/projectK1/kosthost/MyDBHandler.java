package com.projectK1.kosthost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    //Deklarasi variabel konstanta untuk pembuatan database, tabel dan kolom yang diperlukan
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "datapenjaga.db";
    private static final String TABLE_NAME = "penjaga";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAMAKOST = "namakost";
    private static final String COLUMN_NAMAPENJAGA = "namapenjaga";
    private static final String COLUMN_TELEPONPENJAGA = "teleponpenjaga";

    //Constructor untuk Class MyDBHandler
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_PENJAGA = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMAKOST + " VARCHAR(50) NOT NULL, " +
                COLUMN_NAMAPENJAGA + " VARCHAR(50) NOT NULL, " +
                COLUMN_TELEPONPENJAGA + " VARCHAR(15) NOT NULL)";
        sqLiteDatabase.execSQL(CREATE_TABLE_PENJAGA);
    }

    //Method yang digunakan untuk Upgrade Tabel
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /*---- Insert, Select, Update, Delete ----*/
    private SQLiteDatabase database;

    //Method untuk Open Database Connection
    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    //Inisialisasi Semua Kolom di Tabel Database
    private String[] allColumns =
            {COLUMN_ID, COLUMN_NAMAKOST, COLUMN_NAMAPENJAGA, COLUMN_TELEPONPENJAGA};

    //Method untuk Memindahkan Isi Cursor ke Objek Penjaga
    private PenjagaKost cursorToPenjagaKost(Cursor cursor) {
        PenjagaKost penjagaKost = new PenjagaKost();
        penjagaKost.setID(cursor.getLong(0));
        penjagaKost.setNamaKost(cursor.getString(1));
        penjagaKost.setNamaPenjaga(cursor.getString(2));
        penjagaKost.setTeleponPenjaga(cursor.getString(3));
        return penjagaKost;
    }

    //Method untuk Menambahkan Penjaga Kost Baru
    public void createPenjagaKost(String namaKost, String namaPenjaga, String teleponPenjaga) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMAKOST, namaKost);
        values.put(COLUMN_NAMAPENJAGA, namaPenjaga);
        values.put(COLUMN_TELEPONPENJAGA, teleponPenjaga);
        database.insert(TABLE_NAME, null, values);
    }

    //Method untuk Mendapatkan Detail per Penjaga Kost
    public PenjagaKost getPenjagaKost(long id) {
        PenjagaKost penjagaKost = new PenjagaKost();
        Cursor cursor = database.query(TABLE_NAME, allColumns, "_id=" + id, null, null, null, null);
        cursor.moveToFirst();
        penjagaKost = cursorToPenjagaKost(cursor);
        cursor.close();
        return penjagaKost;
    }

    //Method untuk Mendapatkan Data Semua Penjaga Kost di Tabel Penjaga
    public ArrayList<PenjagaKost> getAllPenjagaKost() {
        ArrayList<PenjagaKost> daftarPenjagaKost = new ArrayList<PenjagaKost>();
        Cursor cursor = database.query(TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PenjagaKost penjagaKost = cursorToPenjagaKost(cursor);
            daftarPenjagaKost.add(penjagaKost);
            cursor.moveToNext();
        }
        cursor.close();
        return daftarPenjagaKost;
    }

    //Method untuk Memperbarui Data Penjaga Kost
    public void updatePenjagaKost(PenjagaKost penjagaKost) {
        String filter = "_id=" + penjagaKost.getID();
        ContentValues args = new ContentValues();
        args.put(COLUMN_NAMAKOST, penjagaKost.getNamaKost());
        args.put(COLUMN_NAMAPENJAGA, penjagaKost.getNamaPenjaga());
        args.put(COLUMN_TELEPONPENJAGA, penjagaKost.getTeleponPenjaga());
        database.update(TABLE_NAME, args, filter, null);
    }

    //Method untuk Menghapus Data Penjaga Kost
    public void deletePenjagaKost(long id) {
        String filter = "_id=" + id;
        database.delete(TABLE_NAME, filter, null);
    }
}
