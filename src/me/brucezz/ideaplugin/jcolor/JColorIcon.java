package me.brucezz.ideaplugin.jcolor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.EmptyIcon;

/**
 * Created by bruce on 2017/9/23.
 * Email: im.brucezz@gmail.com
 * Github: https://github.com/brucezz
 */
public class JColorIcon extends EmptyIcon {

    private static final Icon REFER = EmptyIcon.ICON_8;

    private final Color mColor;

    public JColorIcon(Color color) {
        super(REFER.getIconWidth(), REFER.getIconHeight());
        mColor = color;
    }

    @Override
    public void paintIcon(Component component, Graphics g, int i, int j) {
        JBColor c = new JBColor(mColor, mColor);
        g.setColor(c);
        g.fillRect(i, j, getIconWidth(), getIconHeight());
    }
}
