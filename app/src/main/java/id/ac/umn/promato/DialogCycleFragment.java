package id.ac.umn.promato;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogCycleFragment extends DialogFragment {

    private static final String TAG = "DialogCycleFragment";

    public interface OnInputCycleAdd {
        void SetCycle(int cycle);
    }

    private OnInputCycleAdd cycleInputCallback;
    private EditText cycleInput;
    private TextView ok, cancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_cycle, container, false);
        cycleInput = view.findViewById(R.id.input);
        ok = view.findViewById(R.id.action_ok);
        cancel = view.findViewById(R.id.action_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cycle = Integer.valueOf(cycleInput.getText().toString());

                if(cycle > 0)
                {
                    cycleInputCallback.SetCycle(cycle);
                    getDialog().dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            cycleInputCallback = (OnInputCycleAdd) getTargetFragment();
        }catch (ClassCastException e){

        }
    }
}
