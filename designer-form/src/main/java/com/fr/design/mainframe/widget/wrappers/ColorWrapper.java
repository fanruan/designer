package com.fr.design.mainframe.widget.wrappers;

import java.awt.Color;
import java.util.StringTokenizer;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class ColorWrapper implements Encoder, Decoder {

    public ColorWrapper() {
    }

    @Override
    public Object decode(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return null;
        }
        if (txt.equals("black")) {
            return Color.black;
        } else if (txt.equals("blue")) {
            return Color.blue;
        } else if (txt.equals("cyan")) {
            return Color.cyan;
        } else if (txt.equals("darkGray")) {
            return Color.darkGray;
        } else if (txt.equals("gray")) {
            return Color.gray;
        } else if (txt.equals("green")) {
            return Color.green;
        } else if (txt.equals("lightGray")) {
            return Color.lightGray;
        } else if (txt.equals("magenta")) {
            return Color.magenta;
        } else if (txt.equals("orange")) {
            return Color.orange;
        } else if (txt.equals("pink")) {
            return Color.pink;
        } else if (txt.equals("red")) {
            return Color.red;
        } else if (txt.equals("white")) {
            return Color.WHITE;
        } else if (txt.equals("yellow")) {
            return Color.yellow;
        }
        txt = txt.trim();
        txt = txt.substring(1, txt.length() - 1).trim();

        StringTokenizer tokenizer = new StringTokenizer(txt, ",");

        return new Color(Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()));
    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }

        Color c = (Color) v;
        if (c.equals(Color.black)) {
            return "black";
        } else if (c.equals(Color.blue)) {
            return "blue";
        } else if (c.equals(Color.cyan)) {
            return "cyan";
        } else if (c.equals(Color.darkGray)) {
            return "darkGray";
        } else if (c.equals(Color.gray)) {
            return "gray";
        } else if (c.equals(Color.green)) {
            return "green";
        } else if (c.equals(Color.lightGray)) {
            return "lightGray";
        } else if (c.equals(Color.magenta)) {
            return "magenta";
        } else if (c.equals(Color.orange)) {
            return "orange";
        } else if (c.equals(Color.pink)) {
            return "pink";
        } else if (c.equals(Color.red)) {
            return "red";
        } else if (c.equals(Color.WHITE)) {
            return "white";
        } else if (c.equals(Color.yellow)) {
            return "yellow";
        }
        return "[" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + "]";
    }

    @Override
    public void validate(String txt) throws ValidationException {
        if (StringUtils.isEmpty(txt)) {
            return;
        }
        if (txt.equals("null")) {
            return;
        } else if (txt.equals("black")) {
            return;
        } else if (txt.equals("blue")) {
            return;
        } else if (txt.equals("cyan")) {
            return;
        } else if (txt.equals("darkGray")) {
            return;
        } else if (txt.equals("gray")) {
            return;
        } else if (txt.equals("green")) {
            return;
        } else if (txt.equals("lightGray")) {
            return;
        } else if (txt.equals("magenta")) {
            return;
        } else if (txt.equals("orange")) {
            return;
        } else if (txt.equals("pink")) {
            return;
        } else if (txt.equals("red")) {
            return;
        } else if (txt.equals("white")) {
            return;
        } else if (txt.equals("yellow")) {
            return;
        }

        WrapperUtils.validateIntegerTxtFomat(txt, 3, newValidateException());
    }

    private ValidationException newValidateException(){
        return new ValidationException("Color string takes form like: [red, green, blue], or null, black, white, red, green, blue etc.!");
    }
}