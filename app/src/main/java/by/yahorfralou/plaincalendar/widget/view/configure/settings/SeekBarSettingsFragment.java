package by.yahorfralou.plaincalendar.widget.view.configure.settings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import by.yahorfralou.plaincalendar.widget.R;

public class SeekBarSettingsFragment extends Fragment {
    private static final String ARG_MIN_VALUE = "arg_min_value";
    private static final String ARG_MAX_VALUE = "arg_max_value";
    private static final String ARG_CURR_VALUE = "arg_curr_value";
    private static final String ARG_STEP = "arg_step";
    private static final String ARG_UNIT_TEXT = "arg_unit_text";

    private static final int DEFAULT_STEP = 1;

    private Context ctx;
    private SeekBar seekBar;
    private TextView txtProgress;

    private OnValueChangeListener listener;
    private int minValue;
    private String unitName;
    private int step;

    public static SeekBarSettingsFragment newInstance(int minValue, int maxValue, int currentValue, int step, String unitText) {
        SeekBarSettingsFragment fragment = new SeekBarSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MIN_VALUE, minValue);
        args.putInt(ARG_MAX_VALUE, maxValue);
        args.putInt(ARG_CURR_VALUE, currentValue);
        args.putInt(ARG_STEP, step);
        args.putString(ARG_UNIT_TEXT, unitText);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.frag_seek_settings, container, false);
        }

        ctx = getActivity();
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        minValue = getArguments().getInt(ARG_MIN_VALUE);
        int maxValue = getArguments().getInt(ARG_MAX_VALUE);
        int currValue = getArguments().getInt(ARG_CURR_VALUE);
        step = getArguments().getInt(ARG_STEP);
        step = step <= 0 ? DEFAULT_STEP : step;
        unitName = getArguments().getString(ARG_UNIT_TEXT);

        seekBar = v.findViewById(R.id.seekBar);
        txtProgress = v.findViewById(R.id.txtProgress);

        seekBar.setMax((maxValue - minValue) / step);
        seekBar.setProgress((currValue - minValue) / step);
        updateLabel(currValue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateProgress(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void setListener(OnValueChangeListener listener) {
        this.listener = listener;
    }

    private void updateProgress(int progress) {
        int value = progress * step + minValue;
        updateLabel(value);

        if (listener != null) {
            listener.onValueChanged(value);
        }
    }

    private void updateLabel(int value) {
        txtProgress.setText(String.format("%s%s", String.valueOf(value), unitName));
    }

    public interface OnValueChangeListener {
        void onValueChanged(int value);
    }
}
