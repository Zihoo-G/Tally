package com.gazikel.tally.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.gazikel.tally.R;

public class KeyboardUtils {

    //自定义键盘
    private final Keyboard keyboard;


    private KeyboardView keyboardView;
    private EditText editText;

    public interface  OnEnsureListener{
        public void onEnsure();
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyboardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        //取消弹出系统键盘
        this.editText.setInputType(InputType.TYPE_NULL);
        keyboard = new Keyboard(this.editText.getContext(), R.xml.key);

        /*设置要显示键盘的样式*/
        this.keyboardView.setKeyboard(keyboard);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);

        this.keyboardView.setOnKeyboardActionListener(listener);
    }


    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();

            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE://点击了删除
                    if (editable != null && editable.length()>0) {
                        if (start>0) {
                            editable.delete(start-1, start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL://点击了清零
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE:
                    /*通过接口回调方法，当点击确定可以调用这个方法*/
                    onEnsureListener.onEnsure();
                    break;
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    //显示键盘的方法
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }
}
