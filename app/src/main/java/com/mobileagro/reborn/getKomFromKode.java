package com.mobileagro.reborn;

/**
 * Created by ThinkPad T440s VPro on 19/12/2016.
 */

public class getKomFromKode {
    public String komoditasName(String komoditasKode) {
        String namaKomoditas;
        switch (komoditasKode) {
            case "SI":
                namaKomoditas = "Padi Sawah Irigasi";
                break;
            case "SL":
                namaKomoditas = "Padi Sawah Lebak";
                break;
            case "SH":
                namaKomoditas = "Padi Tadah Hujan";
                break;
            case "SP":
                namaKomoditas = "Padi Pasang Surut";
                break;
            case "PG":
                namaKomoditas = "Padi Ladang";
                break;
            case "KD":
                namaKomoditas = "Kedelai";
                break;
            case "JG":
                namaKomoditas = "Jagung";
                break;
            default:
                namaKomoditas = "";
        }
        return  namaKomoditas;
    }
}
