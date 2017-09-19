package hcl.numberverificationcodeinput;

/**
 * @创建者 hcl
 * @创时间 2017/7/13 15:51
 * @描述 ${TODO}
 * @包名 com.uyes.parttime.view
 * @更新者 huang
 * @更新时间 2017/7/13
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class VerificationCodeInput extends RelativeLayout {
    private Drawable     mBoxBg;//输入框边框drawable
    private int          mChildHPadding;//水平方向输入框padding
    private int          mBox;//输入框数量
    private Listener     listener;
    private boolean      mFull;//文本是否充满
    private int          mScreenWidth;
    private int          mBoxWidth;
    private int          mBoxHeight;
    private EditText     mEditText;
    private LinearLayout mLinearLayout;
    private int          mText_color;
    private int          mBox_bg_color;

    public VerificationCodeInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeInput);
        mBox = a.getInt(R.styleable.vericationCodeInput_box, 4);
        mChildHPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_h_padding, 0);
        mBoxBg = a.getDrawable(R.styleable.vericationCodeInput_box_bg);
        mBoxWidth = (int) a.getDimension(R.styleable.vericationCodeInput_child_width, mBoxWidth);
        mBoxHeight = (int) a.getDimension(R.styleable.vericationCodeInput_child_height, mBoxHeight);
        mText_color = a.getColor(R.styleable.vericationCodeInput_text_color, Color.BLACK);
        mBox_bg_color = a.getColor(R.styleable.vericationCodeInput_box_bg_color, 0);
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        if(mBoxWidth==0||mBoxHeight==0){
            mBoxWidth=mBoxHeight=(mScreenWidth- dp2Px(60) - (mBox - 1) * mChildHPadding) / mBox;
        }

        initView(context);
    }

    private void initView(Context context) {
        initLinearLayout(context);
        initTextViews(context, mLinearLayout);
        addView(mLinearLayout);
        initEdittext(context);
        addView(mEditText);
        initListener();

    }

    private void initLinearLayout(Context context) {
        mLinearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mBoxHeight);
        params.gravity=Gravity.CENTER;
        mLinearLayout.setLayoutParams(params);
        mLinearLayout.setGravity(Gravity.CENTER);
    }

    private void initListener() {

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mEditText.setSelection(s.length());//防止点击数据时点击到了中间摸个数字
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    if(s.length()==4){
                        if(listener!=null){
                            listener.onComplete(s.toString());
                        }
                        mFull=true;
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0) ;
                    }
                    TextView textview = (TextView) mLinearLayout.getChildAt(s.length()-1);
                    textview.setText(s.toString().substring(s.length()-1,s.length()));
                }
            }
        });
        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    mFull=false;
                    String s = mEditText.getText().toString();
                    if(s.length()<4){
                        TextView textview = (TextView) mLinearLayout.getChildAt(s.length());
                        textview.setText("");
                    }
                }
                return false;
            }
        });
    }

    private void initEdittext(Context context) {
        mEditText = new EditText(context);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mBoxHeight);
        mEditText.setCursorVisible(false);
        mEditText.setBackgroundColor(Color.TRANSPARENT);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,1);
        mEditText.setTextColor(Color.TRANSPARENT);
        mEditText.setLayoutParams(layoutParams);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mBox)});
        mEditText.setGravity(Gravity.LEFT);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void initTextViews(Context context, LinearLayout linearLayout) {
        mBoxWidth = mBoxHeight = (mScreenWidth - dp2Px(60) - (mBox-1) * mChildHPadding) / mBox;
        for (int i = 0; i < 4; i++) {
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mBoxWidth, mBoxHeight);
            layoutParams.leftMargin = mChildHPadding;
            layoutParams.gravity = Gravity.CENTER;

            textView.setTag(i);
            textView.setMaxLines(1);
            if(mBoxBg!=null){
                textView.setBackground(mBoxBg);
            }else {
                if(mBox_bg_color==0){
                    textView.setBackgroundResource(R.drawable.verification_box_bg);
                }else {
                    textView.setBackgroundColor(mBox_bg_color);
                }

            }
            textView.setTextColor(mText_color);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBoxWidth / 2);
            linearLayout.addView(textView,i);
        }
    }
    public interface Listener {
        void onComplete(String content);
    }
    public void setOnCompleteListener(Listener listener){
        this.listener = listener;
    }

    /**
     * 检查文本是否输入完全
     * @return
     */
    public boolean checkContent(){
        return mFull;
    }
    /**
     * dp-->px
     */
    public int dp2Px(int dp) {
        float density = getResources().getDisplayMetrics().density; // 1.5
        int px = (int) (dp * density + .5f);
        return px;
    }
}