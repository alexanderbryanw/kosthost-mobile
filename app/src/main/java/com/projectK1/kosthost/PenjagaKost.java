package com.projectK1.kosthost;

public class PenjagaKost {
    private long _id;
    private String _namakost;
    private String _namapenjaga;
    private String _teleponpenjaga;

    //Constructor untuk class PenjagaKost
    public PenjagaKost() {
    }

    //Method untuk set id PenjagaKost
    public void setID(long id) {
        this._id = id;
    }

    //Method untuk get id PenjagaKost
    public long getID() {
        return this._id;
    }

    //Method untuk set Nama Kost
    public void setNamaKost(String namaKost) {
        this._namakost = namaKost;
    }

    //Method untuk get Nama Kost
    public String getNamaKost() {
        return this._namakost;
    }

    //Method untuk set Nama Penjaga
    public void setNamaPenjaga(String namaPenjaga) {
        this._namapenjaga = namaPenjaga;
    }

    //Method untuk get Nama Penjaga
    public String getNamaPenjaga() {
        return this._namapenjaga;
    }

    //Method untuk set Telepon Penjaga
    public void setTeleponPenjaga(String teleponPenjaga) {
        this._teleponpenjaga = teleponPenjaga;
    }

    //Method untuk get Telepon Penjaga
    public String getTeleponPenjaga() {
        return this._teleponpenjaga;
    }

    //Method yang digunakan untuk mengubah objek Penjaga menjadi String
    @Override
    public String toString() {
        return "Nama Kost\t\t\t\t\t\t: " + _namakost + " \nNama Penjaga\t\t\t: " +
                _namapenjaga + " \nTelepon Penjaga\t: " + _teleponpenjaga;
    }
}
