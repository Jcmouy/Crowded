package com.example.crowded.database;

import android.app.Activity;
import android.content.Context;

import com.example.crowded.R;
import com.example.crowded.dto.PaisDto;
import com.example.crowded.helper.PrefManager;
import com.example.crowded.repository.PaisService;
import com.example.crowded.view.model.Injection;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

public class MockDataDatabase {
    private Activity activity;
    private PrefManager pref;
    private Context context;
    private PaisService paisService;


    public MockDataDatabase(Context context, final WeakReference<Activity> mainReference) {
        this.activity = mainReference.get();
        this.context = context;
        pref = new PrefManager(this.context);
    }

    public void initMock(){
        paisService = Injection.providePaisService(context);
    }
}
