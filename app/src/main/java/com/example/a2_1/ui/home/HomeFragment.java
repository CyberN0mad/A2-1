package com.example.a2_1.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_1.OnItemClickListener;
import com.example.a2_1.R;

import java.util.Date;

import Models.Note;

import static android.widget.Toast.LENGTH_SHORT;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NoteAdapter(getContext());
        notes();
    }
    private void notes() {
        for (int i = 10; i > 0; i--) {
            String date = java.text.DateFormat.getDateTimeInstance().format(new Date());
            adapter.addItem(new Note("Write some notes here:  " + i, date));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.fab).setOnClickListener(v -> openForm());
        setFragmentListener();
        initList();
    }

    private  void  initList(){
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {

                Note note = adapter.getItem(position);

                Toast.makeText(requireContext(), note.getTitle(),  LENGTH_SHORT).show();

            }

            @Override
            public void longClick(int position) {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.dialog_layout, null);

                Button delete = view.findViewById(R.id.delete);
                Button cancel = view.findViewById(R.id.cancel);

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                        .setView(view);

                final AlertDialog dialog = alert.create();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.remove(position);
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }


        });
//        recyclerView.setAdapter(adapter);
    }

    private void setFragmentListener() {
        getParentFragmentManager().setFragmentResultListener("rk_form", getViewLifecycleOwner(),
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = (Note) result.getSerializable("note");
                        adapter.addItem(note);

                    }
                });
    }
    private void openForm() {
            NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
            navController.navigate(R.id.formFragment);
    }


}