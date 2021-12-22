package com.mnw.stickyselection.infrastructure;

import com.mnw.stickyselection.model.PaintGroupDataBean;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Date;
import java.util.Random;

public class RandomPaintGroupData {

    private final static Random random;

    static {
        random = new Random((new Date()).getTime());

    }

    public static PaintGroupDataBean createBean() {
        final Color hsbColor = getRandomColor();
        final PaintGroupDataBean paintGroupDataBean = new PaintGroupDataBean();
        paintGroupDataBean.setColor(hsbColor);
        paintGroupDataBean.setMarkerNeeded(true);
        paintGroupDataBean.setHighlightLayer(random.nextInt(65) * 100);
        paintGroupDataBean.setFrameNeeded(random.nextBoolean());
        final String shortcut
                = random.nextFloat() < 0.278f
                ? String.valueOf(random.nextInt(10))
                : String.valueOf((char)(random.nextInt(26) + 'A'));
        paintGroupDataBean.setShortcut(shortcut);

        return paintGroupDataBean;
    }

    @NotNull
    public static Color getRandomColor() {
        return Color.getHSBColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
    }
}
