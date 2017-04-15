package tadakazu1972.dungeon40;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by tadakazu on 2017/04/15.
 */

public class BattleActivity extends AppCompatActivity {
    protected BattleActivity mActivity = null;
    protected View mView = null;
    //モンスター画像表示用ImageView
    private ImageView mImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //自分保存
        mActivity = this;
        mView = this.getWindow().getDecorView();
        //レイアウト紐付け
        setContentView(R.layout.battle);
        //ボタン初期化
        initButtons();
        //モンスター画像初期化
        mImageView = (ImageView)findViewById(R.id.monsterimage);
        Random r = new Random();
        switch (r.nextInt(11)){
            case 0:
                mImageView.setImageResource(R.drawable.arcdamon);
                break;
            case 1:
                mImageView.setImageResource(R.drawable.evilmonkey);
                break;
            case 2:
                mImageView.setImageResource(R.drawable.geble);
                break;
            case 3:
                mImageView.setImageResource(R.drawable.ghost);
                break;
            case 4:
                mImageView.setImageResource(R.drawable.ghoul);
                break;
            case 5:
                mImageView.setImageResource(R.drawable.imgur);
                break;
            case 6:
                mImageView.setImageResource(R.drawable.lich);
                break;
            case 7:
                mImageView.setImageResource(R.drawable.lizardknight);
                break;
            case 8:
                mImageView.setImageResource(R.drawable.orcleader);
                break;
            case 9:
                mImageView.setImageResource(R.drawable.skeltonwarrior);
                break;
            case 10:
                mImageView.setImageResource(R.drawable.zombieknight);
                break;
            default:
                mImageView.setImageResource(R.drawable.skeltonwarrior);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    //ボタン初期化
    private void initButtons(){
        mView.findViewById(R.id.btnWeapon).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}
