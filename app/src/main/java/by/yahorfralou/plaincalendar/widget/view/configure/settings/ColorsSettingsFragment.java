package by.yahorfralou.plaincalendar.widget.view.configure.settings;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.helper.PrefHelper;
import by.yahorfralou.plaincalendar.widget.view.customviews.ColorSingleOptionView;

public class ColorsSettingsFragment extends Fragment {
    private static final String ARG_COLORS_LIST = "arg_colors_list";
    private static final String ARG_COLOR_SELECTED = "arg_color_selected";

    private Context ctx;
    private LinearLayout root;
    private HorizontalScrollView scrollView;
    private View selectedView = null;

    private SettingClickListener listener;
    private int[] colorsList;
    private int colorSelected;

    private int screeWidth;

    public static ColorsSettingsFragment getNewInstance(int [] colors, int colorSelected) {
        ColorsSettingsFragment fragment = new ColorsSettingsFragment();

        Bundle args = new Bundle();
        args.putIntArray(ARG_COLORS_LIST, colors);
        args.putInt(ARG_COLOR_SELECTED, colorSelected);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.frag_colors_settings, container, false);
        }
        ctx = getActivity();

        scrollView = v.findViewById(R.id.blockScroll);
        root = v.findViewById(R.id.root);
        colorsList = getArguments().getIntArray(ARG_COLORS_LIST);
        colorSelected = getArguments().getInt(ARG_COLOR_SELECTED);

        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        screeWidth = size.x;

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawColorSettings();
    }

    public void setSettingsListener(SettingClickListener listener) {
        this.listener = listener;
    }

    private void drawColorSettings() {
        root.removeAllViews();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) root.getLayoutParams();
        params.setMarginStart(4);
        params.setMarginEnd(4);

        for (int color : colorsList) {
            ColorSingleOptionView viewColorOption = new ColorSingleOptionView(ctx);
            viewColorOption.setColor(color);
            viewColorOption.setLayoutParams(params);
            viewColorOption.setOnClickListener(this::onOptionSelected);
            if (color == colorSelected) {
                viewColorOption.setChecked(true);
                selectedView = viewColorOption;
            }

            root.addView(viewColorOption);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
// TODO fix right side
        if (selectedView != null) {
            scrollView.post(() -> {
                int scrollX = selectedView.getLeft() - (screeWidth / 2 - selectedView.getWidth() / 2);
                scrollView.scrollTo(scrollX, 0);
            });
        }
    }

    private void onOptionSelected(View v) {
        int colorSelected = PrefHelper.getDefaultBackColor(ctx);
        for (int i = 0; i < root.getChildCount(); i++) {
            ColorSingleOptionView child = (ColorSingleOptionView) root.getChildAt(i);

            if (child.equals(v)) {
                child.setChecked(true);
                colorSelected = colorsList[i];
            } else {
                child.setChecked(false);
            }
        }

        if (listener != null) {
            listener.onColorClick(colorSelected);
        }
    }

    public interface SettingClickListener {
        void onColorClick(int colorValue);
    }
}
