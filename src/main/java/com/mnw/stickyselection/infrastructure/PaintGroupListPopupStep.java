package com.mnw.stickyselection.infrastructure;

import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.util.text.StringUtil;
import com.mnw.stickyselection.model.PaintGroupDataBean;
import com.mnw.stickyselection.model.ValuesRepository;
import com.mnw.stickyselection.model.ValuesRepositoryImpl;
import com.mnw.stickyselection.preferences.ColorImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
public class PaintGroupListPopupStep implements ListPopupStep<PaintGroupDataBean> {


    private final List<PaintGroupDataBean> list;
    private final PerformRunnableFactory runnableFactory;
    private final String title;

    public PaintGroupListPopupStep(String title, PerformRunnableFactory runnableFactory) {
        this.title = title;
        this.runnableFactory = runnableFactory;
        final ValuesRepository valuesRepository = ValuesRepositoryImpl.getInstance();

        list = new ArrayList<>(valuesRepository.getPaintGroupCount());
        for (int i = 0; i < valuesRepository.getPaintGroupCount(); i++) {
            list.add(valuesRepository.getPaintGroupProperties(i));
        }

    }

    @NotNull
    @Override
    public List<PaintGroupDataBean > getValues() {
        return list;
    }

    @Override
    public boolean isSelectable(PaintGroupDataBean paintGroupDataBean) {
        return true;
    }

    @Nullable
    @Override
    public Icon getIconFor(PaintGroupDataBean paintGroupDataBean) {
        final ColorImage colorImage = new ColorImage(24, 24, paintGroupDataBean.getColor());
        return new ImageIcon(colorImage);
    }

    @NotNull
    @Override
    public String getTextFor(PaintGroupDataBean paintGroupDataBean) {
        return String.format(" %s - Paint group (layer: %d)",
                             paintGroupDataBean.getShortcut(),
                             paintGroupDataBean.getHighlightLayer());
    }

    @Nullable
    @Override
    public ListSeparator getSeparatorAbove(PaintGroupDataBean paintGroupDataBean) {
        return null;
    }

    @Override
    public int getDefaultOptionIndex() {
        return 0;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public PopupStep<?> onChosen(PaintGroupDataBean selectedValue, boolean finalChoice) {
        final Runnable performAction = runnableFactory.createPerformAction(list.indexOf(selectedValue));
        performAction.run();
        return PopupStep.FINAL_CHOICE;
    }

    @Override
    public boolean hasSubstep(PaintGroupDataBean paintGroupDataBean) {
        return false;
    }

    @Override
    public void canceled() {

    }

    @Override
    public boolean isMnemonicsNavigationEnabled() {
        return true;
    }

    @Nullable
    @Override
    public MnemonicNavigationFilter<PaintGroupDataBean> getMnemonicNavigationFilter() {
        return new MnemonicNavigationFilter<PaintGroupDataBean>() {
            @Override
            public int getMnemonicPos(PaintGroupDataBean paintGroupDataBean) {
                return 0;
            }

            @Override
            public String getTextFor(PaintGroupDataBean paintGroupDataBean) {
                return " " + (StringUtil.isEmpty(paintGroupDataBean.getShortcut()) ? "*" : paintGroupDataBean.getShortcut());
            }

            @NotNull
            @Override
            public List<PaintGroupDataBean> getValues() {
                return list;
            }
        };
    }

    @Override
    public boolean isSpeedSearchEnabled() {
        return false;
    }

    @Nullable
    @Override
    public SpeedSearchFilter<PaintGroupDataBean> getSpeedSearchFilter() {
        return null;
    }

    @Override
    public boolean isAutoSelectionEnabled() {
        return false;
    }

    @Nullable
    @Override
    public Runnable getFinalRunnable() {
        return null;
    }
}
