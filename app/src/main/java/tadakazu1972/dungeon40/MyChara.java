package tadakazu1972.dungeon40;

/**
 * Created by tadakazu on 2016/10/10.
 */

public class MyChara {
    protected float x, y;   // display x, y
    protected float wx, wy; // world x, y
    protected float vx, vy; // move x, y
    protected float ay;     // gravity
    protected float l, r, t, b; // collision detection
    protected int index;    // animation index
    protected int base_index; //animation base index
    protected int status;   // character status

    public MyChara(){
        x = 1 * 32.0f;
        y = 1 * 32.0f;
        wx = 1 * 32.0f;
        wy = 1 * 32.0f;
        vx = 0.0f;
        vy = 0.0f;
        ay = 1.0f;
        l = x;
        r = x + 31.0f;
        t = y;
        b = y + 31.0f;
        index = 0;
        base_index = 0;
        status = 0;
    }

    public void resetMyChara(){
        x = 1 * 32.0f;
        y = 1 * 32.0f;
        wx = 1 * 32.0f;
        wy = 1 * 32.0f;
        vx = 0.0f;
        vy = 0.0f;
        ay = 1.0f;
        l = x;
        r = x + 31.0f;
        t = y;
        b = y + 31.0f;
        index = 0;
        base_index = 0;
        status = 0;
    }

    public void move(int touchDirection, Map map, MainActivity ac) {
        int x1, x2, y1, y2, dx1, dx2, dy1;
        if (touchDirection == 1) { //上
            vx = 0.0f;
            vy = -2.0f;
            base_index = 6;
        } else if (touchDirection == 3){ //下
            vx = 0.0f;
            vy = 2.0f;
            base_index = 0;
        } else if (touchDirection == 2) {
            vx = 2.0f;
            vy = 0.0f;
            base_index = 2;
        } else if  (touchDirection == 4) {
            vx = -2.0f;
            vy = 0.0f;
            base_index = 4;
        } else if (touchDirection == 0) {
            vy = 0.0f;
            vx = 0.0f;
        }
        //当たり判定用マップ座標算出
        x1 = (int) (wx + 4.0f + vx) / 32; if (x1 < 0) x1 = 0; if (x1 > 29) x1 = 29;
        y1 = (int) (wy + 4.0f + vy) / 32; if (y1 < 0) y1 = 0; if (y1 > 29) y1 = 29;
        x2 = (int) (wx + 28.0f + vx) / 32; if (x2 > 29) x2 = 29; if (x2 < 0) x2 = 0;
        y2 = (int) (wy + 28.0f + vy) / 32; if (y2 > 29) y2 = 29; if (y2 < 0) y2 = 0;
        //カベ判定
        if (map.MAP[y1][x1] > 0 || map.MAP[y1][x2] > 0 || map.MAP[y2][x1] > 0 || map.MAP[y2][x2] > 0) {
            //上または下に行こうとしたけど微妙に幅が合っておらず行けない。移動スムーズ処理
            if (touchDirection == 1 || touchDirection == 3){
                if ((wx+15.0f)%32.0f<15.0f){
                    //右へずらす
                    vx = 1.0f; vy = 0.0f;
                } else {
                    //左へずらす
                    vx = -1.0f; vy = 0.0f;
                }
            } else if (touchDirection == 2 || touchDirection == 4) { //右または左へ行こうとしたけど・・・略
                if ((wy+15.0f)%32.0f<15.0f){
                    //下へずらす
                    vx = 0.0f; vy = 1.0f;
                } else {
                    //上へずらす
                    vx = 0.0f; vy = -1.0f;
                }
            } else {
                vx = 0.0f;
                vy = 0.0f;
            }
            //出口当たり判定
            if (map.MAP[y1][x1] == 2 || map.MAP[y1][x2] == 2 || map.MAP[y2][x1] == 2 || map.MAP[y2][x2] == 2) {
                //マップ再構築
                ac.map.makeMap();
                //自キャラ位置リセット
                this.resetMyChara();
                //マップ変数初期化
                ac.mapx = 0.0f;
                ac.mapy = 0.0f;
                ac.m = 0.0f;
                ac.baseX = 0;
                ac.baseY = 0;
            }
        }
        //ワールド座標更新
        wx = wx + vx;
        if (wx < 0.0f) wx = 0.0f;
        if (wx > 928.0f) wx = 928.0f;
        wy = wy + vy;
        if (wy < 0.0f) wy = 0.0f;
        if (wy > 928.0f) wy = 928.0f;
        //ワールド当たり判定移動
        l = wx + 4.0f + vx;
        r = wx + 28.0f + vx;
        t = wy + 4.0f + vy;
        b = wy + 30.0f + vy;
        //画面座標更新
        x = x + vx;
        if ( x > 224.0f ) {
            if ( ac.baseX != 20) {
                x = 224.0f;
                ac.mapx = ac.mapx - vx;
                if ( wx / 32.0f > ac.baseX + 8 ) { //画面表示限界の１つ分前を超えたら代入
                    ac.baseX = ac.baseX + 1; if (ac.baseX > 20) { ac.baseX = 20; }// because baseX + (0 to 9) = Map Array's Maximum
                    ac.mapx = 0.0f;
                }
            } else if (ac.mapx != -32.0f) { //右端の限界まで引っ張り出していなければmapxを動かす
                x = 224.0f;
                ac.mapx = ac.mapx - vx;
            } else {
                if ( x > 288.0f ) x = 288.0f; //画面外へ飛び出し防止
            }
        }
        if (x < 64.0f) {
            if ( ac.baseX != 0) {
                x = 64.0f;
                ac.mapx = ac.mapx - vx;
                if ( wx / 32.0f < ac.baseX + 1 ) {
                    ac.baseX = ac.baseX - 1; if (ac.baseX < 0) { ac.baseX = 0; }
                    ac.mapx = 0.0f;
                }
            } else {
                if (x < 0.0f) x = 0.0f;
            }
        }
        y = y + vy;
        if (y > 224.0f) {
            if ( ac.baseY !=20) {
                y = 224.0f;
                ac.mapy = ac.mapy - vy; if (ac.mapy < -32.0f) { ac.mapy = -32.0f; }
                if ( wy / 32.0f > ac.baseY + 8 ) {
                    ac.baseY = ac.baseY + 1; if (ac.baseY > 20) { ac.baseY = 20; }
                    ac.mapy = 0.0f;
                }
            } else if ( ac.mapy != -32.0f ) { //最下段の限界まで引っ張りだしていなければmapyを動かす
                y = 224.0f;
                ac.mapy = ac.mapy - vy;
            } else {
                if ( y > 288.0f ) y = 288.0f; //画面外へ飛び出し防止
            }
        }
        if (y < 64.0f) {
            if ( ac.baseY != 0) {
                y = 64.0f;
                ac.mapy = ac.mapy - vy;
                if ( wy / 32.0f < ac.baseY + 1 ) {
                    ac.baseY = ac.baseY - 1; if (ac.baseY < 0) { ac.baseY = 0; }
                    ac.mapy = 0.0f;
                }
            } else { //最上段まで移動可能
                if ( y < 0.0f ) y = 0.0f; //画面外へ飛び出し防止
            }
        }
        //アニメーションインデックス変更処理
        index++;
        if (index > 19) index = 0;
    }
}
