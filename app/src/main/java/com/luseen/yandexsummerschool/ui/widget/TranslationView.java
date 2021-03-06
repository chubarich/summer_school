package com.luseen.yandexsummerschool.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.luseen.yandexsummerschool.R;
import com.luseen.yandexsummerschool.utils.AnimationUtils;
import com.luseen.yandexsummerschool.utils.FontUtils;
import com.luseen.yandexsummerschool.utils.KeyboardUtils;
import com.luseen.yandexsummerschool.utils.StringUtils;
import com.luseen.yandexsummerschool.utils.ViewUtils;

import static com.luseen.yandexsummerschool.utils.AppConstants.SANS_LIGHT;

/**
 * Created by Chatikyan on 20.03.2017.
 */

public class TranslationView extends RelativeLayout implements View.OnClickListener, Viewable {

    public static final int MAX_INPUT_LENGTH = 1000;

    private EditText translationEditText;
    private CloseIcon closeIcon;

    private boolean isEnable = false;
    private int inActiveBorderColor;
    private int inActiveBorderWidth;
    private int activeBorderColor;
    private int activeBorderWidth;
    private int hintColor;

    public TranslationView(Context context) {
        super(context);
        init(context);
    }

    public TranslationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public void init(Context context) {
        activeBorderColor = ContextCompat.getColor(context, R.color.colorPrimary);
        inActiveBorderColor = ContextCompat.getColor(context, R.color.gray);
        hintColor = ContextCompat.getColor(context, R.color.light_gray);
        activeBorderWidth = (int) getResources().getDimension(R.dimen.translation_view_active_border);
        inActiveBorderWidth = (int) getResources().getDimension(R.dimen.translation_view_inactive_border);
        setOnClickListener(this);
        addEditText(context);
        addCloseIcon(context);
        enable();
    }

    private void addEditText(Context context) {
        translationEditText = new EditText(context);
        addView(translationEditText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int editTextLeftMargin = (int) context.getResources().getDimension(R.dimen.translate_editext_left_margin);
        int editTextRightMargin = (int) context.getResources().getDimension(R.dimen.translate_editext_right_margin);
        int editTextBottomMargin = (int) context.getResources().getDimension(R.dimen.translate_editext_bottom_margin);
        LayoutParams params = (LayoutParams) translationEditText.getLayoutParams();
        params.addRule(ALIGN_PARENT_LEFT);
        params.addRule(ALIGN_PARENT_TOP);
        params.setMargins(editTextLeftMargin, 0, editTextRightMargin, editTextBottomMargin);

        translationEditText.setHint(context.getString(R.string.type_text));
        translationEditText.setBackground(null);
        translationEditText.setIncludeFontPadding(false);
        translationEditText.setOnClickListener(this);
        translationEditText.setHintTextColor(hintColor);
        translationEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        translationEditText.setSingleLine(false);
        translationEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        translationEditText.setTypeface(FontUtils.get(context,SANS_LIGHT));
        translationEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_INPUT_LENGTH)});
        ViewUtils.setEditTextDefaultCursorDrawable(translationEditText);
        translationEditText.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (count > 0) {
                    closeIcon.show();
                } else if (s.length() == 0) {
                    closeIcon.hide();
                }
            }
        });
    }

    private void addCloseIcon(Context context) {
        closeIcon = new CloseIcon(context);
        addView(closeIcon, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams params = (LayoutParams) closeIcon.getLayoutParams();
        params.addRule(ALIGN_PARENT_LEFT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        ViewUtils.setViewMargins(closeIcon, new int[]{5, 0, 0, 5});
    }

    private void setBackgroundShape(int borderWidth) {
        GradientDrawable shape = getGradientDrawable();
        shape.setStroke(borderWidth, activeBorderColor);
        setBackground(shape);
    }

    private void changeBackgroundShapeColor(int strokeColor) {
        GradientDrawable shape = getGradientDrawable();
        shape.setStroke(isEnable ? activeBorderWidth : inActiveBorderWidth, strokeColor);
        setBackground(shape);
    }

    private GradientDrawable getGradientDrawable() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.WHITE);
        shape.setCornerRadius(5);
        return shape;
    }

    private void changeBorderWidth(int fromWidth, int toWidth) {
        ValueAnimator animator = ValueAnimator.ofInt(fromWidth, toWidth);
        animator.setDuration(200);
        animator.setInterpolator(AnimationUtils.getFastOutSlowInInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            setBackgroundShape(animatedValue);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int targetColor = isEnable ? activeBorderColor : inActiveBorderColor;
                changeBackgroundShapeColor(targetColor);
            }
        });
        animator.start();
    }

    @Override
    public void onClick(View v) {
        if (!isEnable) {
            enable();
            KeyboardUtils.showKeyboard(this);
        }
    }

    private void disableEditText() {
        translationEditText.setCursorVisible(false);
        translationEditText.clearFocus();
    }

    private void enableEditText() {
        translationEditText.setCursorVisible(true);
        translationEditText.requestFocus();
    }

    public void disable() {
        disableEditText();
        changeBorderWidth(activeBorderWidth, inActiveBorderWidth);
        isEnable = false;
    }

    public void enable() {
        enableEditText();
        changeBorderWidth(inActiveBorderWidth, activeBorderWidth);
        isEnable = true;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public EditText getTranslationEditText() {
        return translationEditText;
    }

    public boolean hasText() {
        return !translationEditText.getText().toString().isEmpty();
    }

    public void reset() {
        resetTextAndIcon();
        if (!isEnable) enable();
    }

    public void resetTextAndIcon() {
        translationEditText.setText(StringUtils.EMPTY);
        closeIcon.hide();
    }

    public CloseIcon getCloseIcon() {
        return closeIcon;
    }
}
