package com.bhex.lib.uikit.widget.keyborad;

import android.inputmethodservice.KeyboardView;

public class SimpleKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

    public SimpleKeyboardActionListener() {
    }

    /**
     * Called when the user presses a key. This is sent before the {@link #onKey} is called.
     * For keys that repeat, this is only called once.
     *
     * @param primaryCode the unicode of the key being pressed. If the touch is not on a valid
     *                    key, the value will be zero.
     */
    @Override
    public void onPress(int primaryCode) {

    }

    /**
     * Called when the user releases a key. This is sent after the {@link #onKey} is called.
     * For keys that repeat, this is only called once.
     *
     * @param primaryCode the code of the key that was released
     */
    @Override
    public void onRelease(int primaryCode) {

    }

    /**
     * Send a key press to the listener.
     *
     * @param primaryCode this is the key that was pressed
     * @param keyCodes    the codes for all the possible alternative keys
     *                    with the primary code being the first. If the primary key code is
     *                    a single character such as an alphabet or number or symbol, the alternatives
     *                    will include other characters that may be on the same key or adjacent keys.
     *                    These codes are useful to correct for accidental presses of a key adjacent to
     */
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

    }

    /**
     * Sends a sequence of characters to the listener.
     *
     * @param text the sequence of characters to be displayed.
     */
    @Override
    public void onText(CharSequence text) {

    }

    /**
     * Called when the user quickly moves the finger from right to left.
     */
    @Override
    public void swipeLeft() {

    }

    /**
     * Called when the user quickly moves the finger from left to right.
     */
    @Override
    public void swipeRight() {

    }

    /**
     * Called when the user quickly moves the finger from up to down.
     */
    @Override
    public void swipeDown() {

    }

    /**
     * Called when the user quickly moves the finger from down to up.
     */
    @Override
    public void swipeUp() {

    }
}
