package com.solang.maprecord.widgets

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.Nullable
import com.solang.maprecord.R
import com.solang.maprecord.utils.dp2px
import com.solang.maprecord.utils.sp2px


/**
 * @author Burning
 * @description:
 * @date :2020/8/13 7:28 PM
 */
/**
 * splash跳转倒计时控件
 * 建议xml布局中使用wrap_content设置长宽
 */

@SuppressLint("AppCompatCustomView")
class SplashTimerTextView : TextView {
    private var hasDraw: Boolean = false
    /**
     * 获取倒计时的时长，进行界面更新或逻辑
     * @return
     */
    // 倒计时动画时间
    var duration = 0

    //倒计时文字
    private var textStr: String? = null

    //文字与圆环的padding
    private var textPadding = 0

    // 动画扫过的角度
    private var mSweepAngle = 360f

    // 属性动画
    private  var animator: ValueAnimator?=null

    // 矩形用来保存位置大小信息
    private val mRect = RectF()

    // 圆弧的画笔
    private lateinit var mArcPaint: Paint

    //圆环的宽
    private var mArcStrokeWidth = 0

    //圆环默认的颜色
    private var mArcColor = 0xFFFFFF

    //圆环文字的颜色
    private var mTextColor = 0xFFFFFF

    //文字的大小
    private var mTextSize = 0

    // 文字画笔
    private lateinit var mTextPaint: Paint

    //控件的最终宽
    private var mWidth = 0

    //控件的最终高
    private var mHeight = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SplashTimer)
        duration = typedArray.getInteger(R.styleable.SplashTimer_timer_duration, 0)
        textPadding = typedArray.getDimension(
            R.styleable.SplashTimer_timer_padding,
            dp2px(context,8f).toFloat()

        ).toInt()
        mArcStrokeWidth = typedArray.getDimension(
            R.styleable.SplashTimer_timer_arcStrokeWidth,
            dp2px(context, 3f)
        ).toInt()
        mArcColor = typedArray.getColor(R.styleable.SplashTimer_timer_arcColor, mArcColor)
        mTextSize = typedArray.getDimensionPixelSize(
            R.styleable.SplashTimer_android_textSize,
            sp2px(context,15f).toInt()
        )
        mTextColor = typedArray.getColor(R.styleable.SplashTimer_android_textColor, mTextColor)
        textStr = typedArray.getString(R.styleable.SplashTimer_android_text)
        if (TextUtils.isEmpty(textStr)) {
            textStr = "跳过"
        }
        typedArray.recycle()
        init()
    }

    /**
     * 初始化画笔
     */
    private fun init() {
        //设置画笔平滑
        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        //设置画笔颜色
        mArcPaint.setColor(mArcColor)
        //设置画笔样式:边框类型
        mArcPaint.setStyle(Paint.Style.STROKE)
        //设置画笔的宽
        mArcPaint.setStrokeWidth(mArcStrokeWidth.toFloat())

        //设置文字画笔
        mTextPaint = Paint()
        //去锯齿
        mTextPaint.setAntiAlias(true)
        //设置字体大小
        mTextPaint.setTextSize(mTextSize.toFloat())
        //设置画笔颜色
        mTextPaint.setColor(mTextColor)
        //设置画笔样式:填充类型
        mTextPaint.setStyle(Paint.Style.FILL)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //计算控件的实际宽
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        mWidth = if (widthMode == View.MeasureSpec.AT_MOST) {
            val desire =
                (mTextPaint.measureText(textStr) + textPadding + mArcPaint.getStrokeWidth() + paddingLeft + paddingRight)
            Math.min(desire.toInt(), widthSize)
        } else {
            widthSize
        }

        //计算控件的实际高
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        mHeight = if (heightMode == View.MeasureSpec.AT_MOST) {
            val desire =
                (mTextPaint.measureText(textStr) + textPadding + mArcPaint.getStrokeWidth() + paddingTop + paddingBottom)
            Math.min(desire.toInt(), heightSize)
        } else {
            heightSize
        }

        //最终决定当前控件的宽高
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        //绘制圆环
        mRect.top = paddingTop.toFloat()+mArcStrokeWidth
        mRect.left = paddingLeft.toFloat()+mArcStrokeWidth
        mRect.bottom = height - paddingBottom.toFloat()-mArcStrokeWidth
        mRect.right = width - paddingRight.toFloat()-mArcStrokeWidth
        canvas.drawArc(mRect, -90f, mSweepAngle, false, mArcPaint)

        //绘制文字，计算文字居中绘制的位置
        val fontMetrics: Paint.FontMetrics = mTextPaint.getFontMetrics()
        val x: Float = (mWidth - mTextPaint.measureText(textStr)) / 2
        val y: Float =
            mHeight / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2
        textStr?.let { canvas.drawText(it, x, y, mTextPaint) }
        if (!hasDraw) {
            startAnim()
            hasDraw = true
        }
    }

    /**
     * 开始属性动画，通过属性动画，去更新圆环的绘制
     */
    private fun startAnim() {
        if (mSweepAngle != 360f || duration <= 0) return
        animator = ValueAnimator.ofInt(360)
        animator?.setDuration(duration.toLong())
        animator?.addUpdateListener(AnimatorUpdateListener { animation: ValueAnimator ->
            mSweepAngle = (animation.animatedValue as Int).toFloat()
            invalidate()
        })
        animator?.start()
    }

    /**
     * 设置圆环中间显示的文字
     * @param textStr
     * @return
     */
    fun setText(textStr: String): String {
        return textStr.also { this.textStr = it }
    }

    fun getDurationTime() = duration.toLong()
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        animator?.cancel()
    }
}