package com.bhex.wallet.mnemonic.ui.item;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/6
 * Time: 20:55
 */
public class MnemonicItem {
    private String word;
    private int index;
    private boolean isSelected;

    public MnemonicItem(String word, int index, boolean isSelected) {
        this.word = word;
        this.index = index;
        this.isSelected = isSelected;
    }

    public MnemonicItem(int index) {
        this.index = index;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
