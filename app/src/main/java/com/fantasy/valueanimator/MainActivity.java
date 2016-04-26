package com.fantasy.valueanimator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView safe_arrow;
    private LinearLayout safe_content;
    private TextView desc_content;
    private ImageView desc_arrow;

    private boolean safe ;//安全信息默认收起
    private boolean desc ;//描述信息默认收起

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        safe_arrow = (ImageView) findViewById(R.id.safe_arrow);
        safe_content = (LinearLayout) findViewById(R.id.safe_content);
        desc_content = (TextView) findViewById(R.id.des_content);
        desc_arrow = (ImageView) findViewById(R.id.des_arrow);

        safe_arrow.setOnClickListener(this);
        desc_arrow.setOnClickListener(this);

        init();
    }

    private void init() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) safe_content.getLayoutParams();
        layoutParams.height = 0;
        safe_content.setLayoutParams(layoutParams);
        safe_arrow.setImageResource(R.drawable.arrow_down);

        ViewGroup.LayoutParams layoutParam = desc_content.getLayoutParams();
        layoutParam.height = getShortMeasureHeight();
        desc_content.setLayoutParams(layoutParam);
        desc_arrow.setImageResource(R.drawable.arrow_down);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.safe_arrow:
                safeAnimator();
                break;
            case R.id.des_arrow:
                descAnimator();
                break;
        }
    }

    private void descAnimator() {
        int startHeight;
        int targetHeight;
        if (!desc) {
            desc = true;
            startHeight = getShortMeasureHeight();
            targetHeight = getLongMeasureHeight();
        } else {
            desc = false;
            startHeight = getLongMeasureHeight();
            targetHeight = getShortMeasureHeight();
        }
        final ViewGroup.LayoutParams layoutParams = desc_content.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, targetHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                layoutParams.height = value;
                desc_content.setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {  // 监听动画执行
            //当动画开始执行的时候调用
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (desc) {
                    desc_arrow.setImageResource(R.drawable.arrow_up);
                } else {
                    desc_arrow.setImageResource(R.drawable.arrow_down);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.setDuration(500);//设置动画持续时间
        animator.start();
    }

    private void safeAnimator() {
        int startHeight;
        int targetHeight;
        if (!safe) {    //  展开的动画
            startHeight = 0;
            targetHeight = getMeasureHeight();

            safe = true;
            safe_content.getMeasuredHeight();
        } else {
            safe = false;
            startHeight = getMeasureHeight();
            targetHeight = 0;
        }
        // 值动画
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, targetHeight);
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) safe_content.getLayoutParams();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  // 监听值的变化

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int value = (Integer) animator.getAnimatedValue();// 运行当前时间点的一个值
                layoutParams.height = value;
                safe_content.setLayoutParams(layoutParams);// 刷新界面
            }
        });

        animator.addListener(new Animator.AnimatorListener() {  // 监听动画执行
            //当动画开始执行的时候调用
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (safe) {
                    safe_arrow.setImageResource(R.drawable.arrow_up);
                } else {
                    safe_arrow.setImageResource(R.drawable.arrow_down);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

        animator.setDuration(500);
        animator.start();
    }

    private int getMeasureHeight() {
        //获取控件的宽度
        int width = safe_content.getMeasuredWidth();
        //让控件的高度为内容包裹
        safe_content.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //测量控件的宽度 参数1：大小 参数2：测量控件的mode
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        //因控件高度会发生变化 故高度最大为1000，以实际为准
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
        safe_content.measure(widthMeasureSpec, heightMeasureSpec);
        return safe_content.getMeasuredHeight();
    }

    /**
     * 获取7行的高度
     *
     * @return
     */
    public int getShortMeasureHeight() {
        // 复制一个新的TextView 用来测量,最好不要在之前的TextView测量 有可能影响其它代码执行
        TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);//设置字体大小14dp
        textView.setMaxLines(7);
        textView.setLines(7);// 强制有7行
        int width = desc_content.getMeasuredWidth(); // 开始宽度

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    /**
     * 获取TextView 自己本身的高度
     *
     * @return
     */
    public int getLongMeasureHeight() {
        int width = desc_content.getMeasuredWidth(); // 开始宽度
        desc_content.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;// 高度包裹内容

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
        desc_content.measure(widthMeasureSpec, heightMeasureSpec);//
        return desc_content.getMeasuredHeight();
    }
}
