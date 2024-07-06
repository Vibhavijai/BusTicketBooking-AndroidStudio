package com.example.busticketbooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements BusRVAdapter.BusClickInterface {
    public ProgressBar loadingPB;
    public ArrayList<BusRVModel> BusRVModelArrayList;
    public RelativeLayout bottomSheetRL;
    public BusRVAdapter busRVAdapter;
    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView busRV = findViewById(R.id.idRVBuses);
        loadingPB = findViewById(R.id.idPBLoading);
        FloatingActionButton addFAB = findViewById(R.id.idAddFAB);
        bottomSheetRL = findViewById(R.id.idRLBSheet);
        dbHelper = new DBHelper(this);
        BusRVModelArrayList = new ArrayList<>();
        busRVAdapter = new BusRVAdapter(BusRVModelArrayList, this, this);
        busRV.setLayoutManager(new LinearLayoutManager(this));
        busRV.setAdapter(busRVAdapter);
        addFAB.setOnClickListener(view -> startActivity(new Intent(MainActivity2.this, Add_Bus.class)));
        getAllBuses();
    }

    private void getAllBuses() {
        BusRVModelArrayList.clear();
        //BusRVModelArrayList.addAll(dbHelper.getAllBuses());
        busRVAdapter.notifyDataSetChanged();
        loadingPB.setVisibility(View.GONE);
    }

    @Override
    public void onBusClick(int position) {
        displayBottomSheet(BusRVModelArrayList.get(position));
    }

    private void displayBottomSheet(BusRVModel busRVModel) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, bottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView busNumTV = layout.findViewById(R.id.idBusNum);
        TextView SrcTV = layout.findViewById(R.id.idSrc);
        TextView DestTV = layout.findViewById(R.id.idDest);
        TextView TimingsTV = layout.findViewById(R.id.idTimings);
        Button editBtn = layout.findViewById(R.id.idBtnEditBus);

        busNumTV.setText("Number Plate: " + busRVModel.getBusNum());
        SrcTV.setText("From: " + busRVModel.getSrc());
        DestTV.setText("To: " + busRVModel.getDest());
        TimingsTV.setText("Time: " + busRVModel.getTimings());

        editBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity2.this, Edit_Bus.class);
            i.putExtra("Bus", busRVModel);
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.idLogOut) {
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            // Handle logout
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
