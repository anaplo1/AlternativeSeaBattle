package my.sea.battle.history;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import my.sea.battle.R;
import my.sea.battle.db.SeaBattleDB;
import my.sea.battle.db.SeaBattleHistory;
import my.sea.battle.db.SharedPrefsManager;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        super(R.layout.history_fragment);
    }

    private RecyclerView historyRecycler;
    private HistoryAdapter historyAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyRecycler = view.findViewById(R.id.historyRecycler);
        ImageView historyBackButton = view.findViewById(R.id.historyBackButton);
        //По нажатию назад навигируемся в меню
        historyBackButton.setOnClickListener(v -> {
            Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment
            ).navigate(R.id.menuFragment);
        });
        // В отдельном потоке ходим в БД
        new Thread(() -> {
            SeaBattleDB db = Room.databaseBuilder(requireActivity().getApplicationContext(),
                    SeaBattleDB.class, "sea-battle-database").build();
            SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(requireContext());
            if (sharedPrefsManager.getEmail().equals("")) {} else {
                historyAdapter = new HistoryAdapter(db.seaBattleDao().getFullHistory().stream().filter(dbModel -> {
                    return dbModel.email.equals(sharedPrefsManager.getEmail());
                }).collect(Collectors.toList()));
            }
            //Переключаемся на главный и передаем данные в Recycler
            new Handler(Looper.getMainLooper()).post(() -> {
                if(sharedPrefsManager.getEmail().equals("")){
                    ((TextView)view.findViewById(R.id.historyFragmentTitle)).setText("Для просмотра истории\nНеобходимо авторизоваться");
                }
                else {
                    ((TextView)view.findViewById(R.id.historyFragmentTitle)).setText("История игр");
                    historyRecycler.setAdapter(historyAdapter);
                }
            });
        }).start();

    }
}
