package me.brucezz.ideaplugin.jcolor;

import java.awt.Color;
import java.util.Collection;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bruce on 2017/9/23.
 * Email: im.brucezz@gmail.com
 * Github: https://github.com/brucezz
 */
public class JColorLineMarkerProvider extends RelatedItemLineMarkerProvider {


    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        Color color = JColorFinder.findColor(element);
        if (color == null) return;

        RelatedItemLineMarkerInfo<PsiElement> lineMarkerInfo = NavigationGutterIconBuilder
            .create(new JColorIcon(color))
            .setTargets(element)
            .setTooltipText("This is a color")
            .createLineMarkerInfo(element);

        result.add(lineMarkerInfo);
    }

}

