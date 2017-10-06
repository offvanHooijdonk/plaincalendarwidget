package by.yahorfralou.plaincalendar.widget.view.configure.settings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.view.customviews.ColorSingleOptionView;

public class ColorsSettingsFragment extends Fragment {
    private static final String ARG_COLORS_LIST = "arg_colors_list";

    private Context ctx;
    private LinearLayout root;

    private int[] colorsList;

    public static ColorsSettingsFragment getNewInstance(int [] colors) {
        ColorsSettingsFragment fragment = new ColorsSettingsFragment();

        Bundle args = new Bundle();
        args.putIntArray(ARG_COLORS_LIST, colors);
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

        root = v.findViewById(R.id.root);
        colorsList = getArguments().getIntArray(ARG_COLORS_LIST);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawColorSettings();
    }

    private void drawColorSettings() {
        root.removeAllViews();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) root.getLayoutParams();
        params.setMarginStart(4);
        params.setMarginEnd(4);

        boolean first = true;
        for (int color : colorsList) {
            ColorSingleOptionView viewColorOption = new ColorSingleOptionView(ctx);
            viewColorOption.setColor(color);
            viewColorOption.setLayoutParams(params);
            viewColorOption.setOnClickListener(this::onOptionSelected);
            if (first) {
                viewColorOption.setChecked(true);
                first = false;
            }

            root.addView(viewColorOption);
        }
    }

    private void onOptionSelected(View v) {
        for (int i = 0; i < root.getChildCount(); i++) {
            ColorSingleOptionView child = (ColorSingleOptionView) root.getChildAt(i);
            child.setChecked(child.equals(v));
        }
    }

}
