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
    double convertfrom;
    @ColumnInfo(name = "convertto")
    double converto;



    public CurrencyObject(double name, double rate) {
        convertfrom = name;
        converto = rate;

    }

    public CurrencyObject(int id, double converto, double convertfrom) {
        this.id = id;
        this.converto  = converto;
        this.convertfrom = convertfrom;
    }

    public double getConvertfrom() {
        return convertfrom;
    }


    public double getConverto() {
        return converto;
    }

}