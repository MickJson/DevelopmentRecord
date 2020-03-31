package com.nianlun.advancedtextview.bean;

/**
 * @author zj
 * @date 2020/3/31.
 * description：
 */
public class TextItem {

    private float value;
    private String unit;

    public TextItem(float value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
