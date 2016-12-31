package tadakazu1972.dungeon40;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Runnable, android.view.View.OnTouchListener{
    private MainActivity mainActivity;
    private View view;
    private GameView gameView;
    private SurfaceView surfaceView;
    private Thread thread;
    private volatile boolean isThreadRun;
    public int deviceWidth; //Device's size
    public int deviceHeight; //Device's size
    final float VIEW_WIDTH = 320.0f;
    final float VIEW_HEIGHT = 320.0f;
    private int width;
    private int height;
    protected float scale;
    private float scaleX;
    private float scaleY;
    protected Map map;
    protected float mapx;
    protected float mapy;
    protected float m;
    protected int baseX; // for drawing Map Array's baseX
    protected int baseY; // for drawing Map Array's baseY
    protected Bitmap[] sBg = new Bitmap[4];
    protected Bitmap[] sMap = new Bitmap[32];
    protected Bitmap[] sArthur = new Bitmap[8];
    protected Bitmap[] sDamage = new Bitmap[7];
    protected MyChara myChara;
    protected int touchDirection;
    boolean repeatFlg;
    private Button btnUp;
    private Button btnRight;
    private Button btnDown;
    private Button btnLeft;
    protected static final int DN = 4; //ダメージ表現最大数
    protected Damage[] damage = new Damage[DN]; //ダメージ表現
    protected Paint paintDamage = new Paint();
    protected Paint paintMonsterHp = new Paint();
    protected int damageIndex;
    //ダメージ時にモンスターHP表示して３秒後に消すタイマー
    private Timer monsterHpTimer = null;
    private TimerTask monsterHpTimerTask = null;
    private Handler monsterHpTimerHandler = new Handler();
    //開発中パラメータ表示用
    protected Paint paint0 = new Paint();

    public MainActivity(){
        super();
        // Create MAP DATA
        mapx = 0.0f;
        mapy = 0.0f;
        map = new Map();
        m = 0.0f;
        baseX = 0;
        baseY = 0;
        // Create MyChara
        myChara = new MyChara();
        touchDirection=0;
        repeatFlg=false;
        // Create Damage
        for (int i=0;i<damage.length;i++){
            damage[i] = new Damage();
        }
        damageIndex=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        view = this.getWindow().getDecorView();
        setContentView(R.layout.activity_main);
        //画面サイズ取得、拡大率決定
        getDisplaySize();
        createButton();
        //View生成
        gameView = (GameView)findViewById(R.id.gameView);

        //タイマー生成
        monsterHpTimer = new Timer();
        monsterHpTimerTask = new monsterHpTimerTask();
        monsterHpTimer.schedule(monsterHpTimerTask, 3000, 3000); //3秒後
    }
    
        @Override
    public void onResume(){
        super.onResume();
        // Create Sprite
        createSprite();
            isThreadRun = true;
            thread = new Thread(this);
            thread.start();
        }

    @Override
    public void onPause(){
        super.onPause();
        isThreadRun = false;
        while(true){
            try{
                thread.join();
                break;
            } catch(InterruptedException e){

            }
        }
        thread = null;
    }

    @Override
    public void run(){
        while(isThreadRun){
            long time1 = 0, time2 = 0; //スリープ用
            time1 = System.currentTimeMillis();

            //移動処理
            moveCharacters();

           //スリープ
            time2 = System.currentTimeMillis();
            if ( time2 - time1 < 16 ){ // 1000 / 60 = 16.6666
                try {
                    Thread.sleep( 16 - (time2 - time1));
                } catch (InterruptedException e){

                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent e){
        //onTouchは画面を押した時と離した時の両方のイベントを取得する
        int action = e.getAction();
        switch(action){
            //ボタンから指が離れた時
            case MotionEvent.ACTION_UP:
                //連続イベントフラグをfalse
                repeatFlg = false;
                touchDirection = 0;
                //ゲームスタート
                //if (mainSurfaceView.gs==0) mainSurfaceView.gs=1;
                break;
            case MotionEvent.ACTION_DOWN:
                switch(v.getId()){
                    case R.id.btnUp:
                        myChara.base_index = 6;
                        touchDirection = 1;
                        break;
                    case R.id.btnRight:
                        touchDirection = 2;
                        break;
                    case R.id.btnDown:
                        myChara.base_index = 0;
                        touchDirection = 3;
                        break;
                    case R.id.btnLeft:
                        touchDirection = 4;
                        break;
                }
        }
        return false;
    }

    public void getDisplaySize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        deviceHeight = size.y;
        scaleX = deviceWidth / VIEW_WIDTH;
        scaleY = deviceHeight / VIEW_HEIGHT;
        scale = scaleX > scaleY ? scaleY : scaleX;
    }

    public void createButton(){
        /*OnClickだと動きっぱなしになるので不採用
        view.findViewById(R.id.btnUp).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                myChara.base_index = 6;
                touchDirection = 1;
            }
        });
        view.findViewById(R.id.btnRight).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                touchDirection = 2;
            }
        });
        view.findViewById(R.id.btnDown).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                myChara.base_index = 0;
                touchDirection = 3;
            }
        });
        view.findViewById(R.id.btnLeft).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                touchDirection = 4;
            }
        });*/
        //OnTouchのほうがボタンを離すと止まる動きを実現できるのでこちらを採用
        // attach button from res/layout/activity_main.xml
        btnUp = (Button)findViewById(R.id.btnUp);
        btnRight = (Button)findViewById(R.id.btnRight);
        btnDown = (Button)findViewById(R.id.btnDown);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        // set OnTouchListener
        btnUp.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
    }

    public void createSprite(){
        Resources res = this.getResources();
        if (sBg[0] == null) {
            // Back Ground
            sBg[0] = BitmapFactory.decodeResource(res, R.drawable.black);
            sBg[1] = BitmapFactory.decodeResource(res, R.drawable.rock2);
            sBg[2] = BitmapFactory.decodeResource(res, R.drawable.back04);
            sBg[3] = BitmapFactory.decodeResource(res, R.drawable.back05);
            // Map
            sMap[0] = BitmapFactory.decodeResource(res, R.drawable.back05);
            sMap[1] = BitmapFactory.decodeResource(res, R.drawable.brickl);
            sMap[2] = BitmapFactory.decodeResource(res, R.drawable.descend);
            sMap[3] = BitmapFactory.decodeResource(res, R.drawable.greenbrick03);
            sMap[4] = BitmapFactory.decodeResource(res, R.drawable.rock);
            sMap[5] = BitmapFactory.decodeResource(res, R.drawable.gate_open);
            sMap[6] = BitmapFactory.decodeResource(res, R.drawable.black);
            sMap[7] = BitmapFactory.decodeResource(res, R.drawable.bluerock);
            sMap[8] = BitmapFactory.decodeResource(res, R.drawable.gate_open);
            sMap[9] = BitmapFactory.decodeResource(res, R.drawable.black);
            sMap[10] = BitmapFactory.decodeResource(res, R.drawable.wall0501);
            sMap[11] = BitmapFactory.decodeResource(res, R.drawable.door_open);
            sMap[12] = BitmapFactory.decodeResource(res, R.drawable.black);
            sMap[13] = BitmapFactory.decodeResource(res, R.drawable.wall0302);
            sMap[14] = BitmapFactory.decodeResource(res, R.drawable.door_open);
            sMap[15] = BitmapFactory.decodeResource(res, R.drawable.tree1_2);
            sMap[16] = BitmapFactory.decodeResource(res, R.drawable.tree1_3);
            sMap[17] = BitmapFactory.decodeResource(res, R.drawable.tree2_1);
            sMap[18] = BitmapFactory.decodeResource(res, R.drawable.tree2_2);
            sMap[19] = BitmapFactory.decodeResource(res, R.drawable.tree2_3);
            sMap[20] = BitmapFactory.decodeResource(res, R.drawable.tree3_1);
            sMap[21] = BitmapFactory.decodeResource(res, R.drawable.tree3_2);
            sMap[22] = BitmapFactory.decodeResource(res, R.drawable.tree3_3);
            sMap[23] = BitmapFactory.decodeResource(res, R.drawable.tree4_1);
            sMap[24] = BitmapFactory.decodeResource(res, R.drawable.tree4_2);
            sMap[25] = BitmapFactory.decodeResource(res, R.drawable.tree4_3);
            sMap[26] = BitmapFactory.decodeResource(res, R.drawable.tree5_1);
            sMap[27] = BitmapFactory.decodeResource(res, R.drawable.tree5_2);
            sMap[28] = BitmapFactory.decodeResource(res, R.drawable.tree5_3);
            sMap[29] = BitmapFactory.decodeResource(res, R.drawable.tree6_1);
            sMap[30] = BitmapFactory.decodeResource(res, R.drawable.tree6_2);
            sMap[31] = BitmapFactory.decodeResource(res, R.drawable.tree6_3);
            // Arthur
            sArthur[0] = BitmapFactory.decodeResource(res, R.drawable.arthur01);
            sArthur[1] = BitmapFactory.decodeResource(res, R.drawable.arthur02);
            sArthur[2] = BitmapFactory.decodeResource(res, R.drawable.arthur03);
            sArthur[3] = BitmapFactory.decodeResource(res, R.drawable.arthur04);
            sArthur[4] = BitmapFactory.decodeResource(res, R.drawable.arthur05);
            sArthur[5] = BitmapFactory.decodeResource(res, R.drawable.arthur06);
            sArthur[6] = BitmapFactory.decodeResource(res, R.drawable.arthur07);
            sArthur[7] = BitmapFactory.decodeResource(res, R.drawable.arthur08);
            //sDamage = new Bitmap[7];
            sDamage[0] = BitmapFactory.decodeResource(res,R.drawable.star01);
            sDamage[1] = BitmapFactory.decodeResource(res,R.drawable.star02);
            sDamage[2] = BitmapFactory.decodeResource(res,R.drawable.star03);
            sDamage[3] = BitmapFactory.decodeResource(res,R.drawable.star04);
            sDamage[4] = BitmapFactory.decodeResource(res,R.drawable.star05);
            sDamage[5] = BitmapFactory.decodeResource(res,R.drawable.star06);
            sDamage[6] = BitmapFactory.decodeResource(res,R.drawable.star07);
        }
        //ダメージポイント表示用Paint設定
        paintDamage.setTextSize(12);
        paintDamage.setColor(Color.WHITE);
        paintDamage.setTextAlign(Paint.Align.CENTER);
        paintDamage.setTypeface(Typeface.DEFAULT_BOLD);
        //ダメージ時モンスターHP表示用Paint設定
        paintMonsterHp.setTextSize(12);
        paintMonsterHp.setColor(Color.RED);
        paintMonsterHp.setTextAlign(Paint.Align.CENTER);
        //paintMonsterHp.setTypeface(Typeface.DEFAULT_BOLD);
        //開発時各種パラメーター確認用
        paint0.setTextSize(12);
        paint0.setColor(Color.WHITE);
        paint0.setTextAlign(Paint.Align.LEFT);
        paint0.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public class monsterHpTimerTask extends TimerTask {
        @Override
        public void run(){
            monsterHpTimerHandler.post( new Runnable(){
                public void run(){
                }
            });
        }
    }

    public void moveCharacters(){
        myChara.move( touchDirection, map, this);
        for (Damage aDamage : damage){
            aDamage.move();
        }
    }
}
