package algonquin.cst2335.finalproject.Currency;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrencyObject {


    @ColumnInfo(name = "convertfrom")
    String convertfrom;
    @ColumnInfo(name = "convertto")
    String converto;

    @ColumnInfo(name = "cfom")
    String cfrom;

    @ColumnInfo(name = "too")
    String too;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;


    public CurrencyObject() {
    }

    public CurrencyObject(String t, String f, String af, String at) {
        this.converto = t;
        this.convertfrom = f;
        this.cfrom = af;
        this.too = at;
    }
    public String getConvertfrom() {
        return convertfrom;
    }

    public String getConverto() {
        return converto;
    }

    public String getCfrom() {
        return cfrom;
    }

    public String getToo() {
        return too;
    }

    public int getId() {
        return id;
    }
}