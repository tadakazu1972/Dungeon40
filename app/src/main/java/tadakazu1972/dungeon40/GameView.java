package tadakazu1972.dungeon40;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tadakazu on 2016/12/29.
 */

public class GameView extends View {

    private MainActivity ac;
    private Paint mPaint;

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        ac = (MainActivity)context; //メモリリークの可能性に留意すべし
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas){
        //まずは画面サイズに応じて拡大
        canvas.scale(ac.scale, ac.scale);

        // Draw Back
        for (int y=0;y<11;y++){
            for (int x=0;x<10;x++){
                canvas.drawBitmap(ac.sBg[0], x*32.0f, y*32.0f, null);
            }
        }
        //マップ描画
        //まずは見えていない次の部分を上下左右描く
        // 上辺
        if (ac.baseY>0){
            for (int x=0;x<10;x++) canvas.drawBitmap(ac.sMap[ac.map.MAP[ac.baseY-1][ac.baseX+x]+ac.map.a], x*32.0f+ac.mapx, -32.0f+ac.mapy, null);
        }
        // 右辺
        if (ac.baseX<19){
            for (int y=0;y<10;y++) canvas.drawBitmap(ac.sMap[ac.map.MAP[ac.baseY+y][ac.baseX+10]+ac.map.a], 320.0f+ac.mapx, y*32.0f+ac.mapy, null);
        }
        // 左辺
        if (ac.baseX>0){
            for (int y=0;y<10;y++) canvas.drawBitmap(ac.sMap[ac.map.MAP[ac.baseY+y][ac.baseX-1]+ac.map.a], -32.0f+ac.mapx, y*32.0f+ac.mapy, null);
        }
        // 下辺
        if (ac.baseY<19){
            for (int x=0;x<10;x++) canvas.drawBitmap(ac.sMap[ac.map.MAP[ac.baseY+10][ac.baseX+x]+ac.map.a], x*32.0f+ac.mapx, 320.0f+ac.mapy, null);
        }
        // Draw Main Map
        for (int y=0;y<10;y++){
            for (int x=0;x<10;x++){
                canvas.drawBitmap(ac.sMap[ac.map.MAP[ac.baseY+y][ac.baseX+x]+ac.map.a], x*32.0f+ac.mapx, y*32.0f+ac.mapy, null);
            }
        }
        //自キャラ描画
        int i = ac.myChara.base_index + ac.myChara.index / 10;
        if (i > 7) i = 0;
        canvas.drawBitmap(ac.sArthur[i], ac.myChara.x, ac.myChara.y, null);
        // Draw Damage
        for (Damage aDamage : ac.damage){
            if ( aDamage.visible ==1 ){
                int di = aDamage.index / 10;
                if ( di > 6 ) di = 6;
                //ダメージ表現
                canvas.drawBitmap(ac.sDamage[di], aDamage.x, aDamage.y, null);
                //ダメージポイント表示
                canvas.drawText(String.valueOf(aDamage.point), aDamage.x + 16.0f, aDamage.py, ac.paintDamage);
            }
        }

        /*// Draw Text
        canvas.drawText("mapx="+String.valueOf(ac.mapx), 20, 10, ac.paint0);
        canvas.drawText("mapy="+String.valueOf(ac.mapy), 20, 20, ac.paint0);
        canvas.drawText("x="+String.valueOf(ac.myChara.x), 20, 30, ac.paint0);
        canvas.drawText("y="+String.valueOf(ac.myChara.y), 20, 40, ac.paint0);
        canvas.drawText("wx="+String.valueOf(ac.myChara.wx), 20, 50, ac.paint0);
        canvas.drawText("wy="+String.valueOf(ac.myChara.wy), 20, 60, ac.paint0);
        canvas.drawText("wx/32="+String.valueOf((ac.myChara.wx+15.0f)%32.0f), 20, 70, ac.paint0);
        canvas.drawText("wy/32="+String.valueOf((ac.myChara.wy+15.0f)%32.0f), 20, 80, ac.paint0);
        canvas.drawText("baseX="+String.valueOf(ac.baseX), 20, 90, ac.paint0);
        canvas.drawText("baseY="+String.valueOf(ac.baseY), 20, 100, ac.paint0);*/

        postInvalidateDelayed(1000/60); //60fps
    }
}
