package tadakazu1972.dungeon40;

/**
 * Created by tadakazu on 2016/10/10.
 */

public class Damage {
    protected float x,y;
    protected int index;
    protected int visible;
    protected int point; //表示するダメージポイント
    protected float py; //表示するダメージポイントは上昇するため保存変数

    public Damage(){
        x = 0.0f;
        y = 0.0f;
        index = 0;
        visible = 0;
        point = 0;
        py = y;
    }

    public void reset(){
        x = 0.0f;
        y = 0.0f;
        index = 0;
        visible = 0;
        point = 0;
        py = y;
    }

    public void set(float dx, float dy){
        x = dx;
        y = dy;
        index = 0;
        visible = 1;
        py = y;
    }

    public void move(){
        index = index + 6;
        if ( index > 69 ){
            reset();
        }
        //ダメージポイント表示上昇する
        py = py - 2.0f;
    }
}
