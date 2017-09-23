package me.brucezz.ideaplugin.jcolor;

import java.awt.Color;

import com.intellij.ui.JBColor;

/**
 * Created by bruce on 2017/9/23.
 * Email: im.brucezz@gmail.com
 * Github: https://github.com/brucezz
 */
public class JColor extends JBColor {

    public JColor(int r, int g, int b, int a) {
        super(new Color(r, g, b, a), new Color(r, g, b, a));
    }

    public JColor(int r, int g, int b) {
        this(r, g, b, 255);
    }
}
