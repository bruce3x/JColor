package me.brucezz.ideaplugin.jcolor;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by bruce on 2017/9/23.
 * Email: im.brucezz@gmail.com
 * Github: https://github.com/brucezz
 */
public class JColorFinder {


    @Nullable
    public static Color findColor(PsiElement element) {

        Color color = null;

        //noinspection ConstantConditions
        if (color == null) color = findInMethodCall(element, ColorARGB);
        if (color == null) color = findInMethodCall(element, ColorRGB);
        if (color == null) color = findInMethodCall(element, ColorParseColor);

        return color;
    }

    // ---------------------------


    private static final ColorMethod ColorARGB = new ColorMethod("android.graphics.Color", "argb") {
        @Nullable
        @Override
        public Color getColor(String... args) throws Exception {
            int a = Integer.valueOf(args[0]);
            int r = Integer.valueOf(args[1]);
            int g = Integer.valueOf(args[2]);
            int b = Integer.valueOf(args[3]);
            return new JColor(r, g, b, a);
        }
    };

    private static final ColorMethod ColorRGB = new ColorMethod("android.graphics.Color", "rgb") {
        @Nullable
        @Override
        public Color getColor(String... args) throws Exception {
            int r = Integer.valueOf(args[0]);
            int g = Integer.valueOf(args[1]);
            int b = Integer.valueOf(args[2]);
            return new JColor(r, g, b);
        }
    };

    private static final ColorMethod ColorParseColor = new ColorMethod("android.graphics.Color", "parseColor") {
        private final Pattern REGEX = Pattern.compile("(#|0X)([0-9A-H]{3,8})");

        @Nullable
        @Override
        Color getColor(String... args) throws Exception {
            // TODO: 2017/9/23 换一种优雅的姿势
            String colorStr = args[0].replace("\"", "").toUpperCase();
            Matcher matcher = REGEX.matcher(colorStr);
            if (matcher.matches()) {
                String txt = matcher.group(2);
                int len = txt.length();
                if (len == 3) {
                    return new JColor(
                        Integer.parseInt(txt.substring(0, 1), 16),
                        Integer.parseInt(txt.substring(1, 2), 16),
                        Integer.parseInt(txt.substring(2, 3), 16),
                        255
                    );
                } else if (len == 6) {
                    return new JColor(
                        Integer.parseInt(txt.substring(0, 2), 16),
                        Integer.parseInt(txt.substring(2, 4), 16),
                        Integer.parseInt(txt.substring(4, 6), 16),
                        255
                    );
                } else if (len == 8) {
                    return new JColor(
                        Integer.parseInt(txt.substring(2, 4), 16),
                        Integer.parseInt(txt.substring(4, 6), 16),
                        Integer.parseInt(txt.substring(6, 8), 16),
                        Integer.parseInt(txt.substring(0, 2), 16)
                    );
                }
            }
            return null;
        }
    };


    @Nullable
    private static Color findInMethodCall(PsiElement psiElement, ColorMethod colorMethod) {

        if (!(psiElement instanceof PsiCallExpression)) return null;

        PsiCallExpression expression = (PsiCallExpression)psiElement;
        PsiMethod method = expression.resolveMethod();

        if (method == null) return null;

        String methodName = method.getName();
        PsiElement parent = method.getParent();

        // Color.argb(1, 2, 3, 4)
        if (!colorMethod.methodName.equals(methodName)) return null;
        if (!(parent instanceof PsiClass) || !colorMethod.classQualifiedName.equals(((PsiClass)parent).getQualifiedName()))
            return null;

        PsiExpressionList argumentList = expression.getArgumentList();
        if (argumentList == null) return null;

        PsiExpression[] argsExpressions = argumentList.getExpressions();

        String[] args = new String[argsExpressions.length];
        for (int i = 0; i < argsExpressions.length; i++) {
            args[i] = argsExpressions[i].getText();
        }
        try {
            return colorMethod.getColor(args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static abstract class ColorMethod {
        public final String methodName;
        public final String classQualifiedName;

        public ColorMethod(@NotNull String classQualifiedName, @NotNull String methodName) {
            this.classQualifiedName = classQualifiedName;
            this.methodName = methodName;
        }

        @Nullable
        abstract Color getColor(String... args) throws Exception;

    }

}
