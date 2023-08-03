package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrencyObject {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "convertfrom")
    String convertfrom;
    @ColumnInfo(name = "convertto")
    String converto;

    @ColumnInfo(name = "amountfrom")
    String cfrom;

    @ColumnInfo(name = "amountto")
    String too;


    public CurrencyObject() {
    }

    public CurrencyObject(String t, String f, String af, String at) {
        this.converto = t;
        this.convertfrom = f;
        this.cfrom = af;
        this.too = at;
    }
}