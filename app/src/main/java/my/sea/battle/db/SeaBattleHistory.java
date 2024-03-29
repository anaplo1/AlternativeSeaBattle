package my.sea.battle.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Описание ячейки в таблице с историей игр
@Entity(tableName = "SeaBattleHistory")
public class SeaBattleHistory {

    public SeaBattleHistory(boolean isWin, String date, SeaBattleFieldHistory userBattleField, SeaBattleFieldHistory botBattleField, String email) {
        this.isWin = isWin;
        this.date = date;
        this.userBattleField = userBattleField;
        this.botBattleField = botBattleField;
        this.email = email;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    public boolean isWin;
    public String date;
    public SeaBattleFieldHistory userBattleField;
    public SeaBattleFieldHistory botBattleField;
    public String email;
}
