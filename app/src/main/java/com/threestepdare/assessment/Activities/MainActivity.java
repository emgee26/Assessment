package com.threestepdare.assessment.Activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.threestepdare.assessment.Fragments.AddOrEditDetailFragment;
import com.threestepdare.assessment.Fragments.DisplayDetailFragment;
import com.threestepdare.assessment.Helpers.PreferenceHelper;
import com.threestepdare.assessment.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    PreferenceHelper preferenceHelper;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceHelper = new PreferenceHelper(getApplicationContext());
        frameLayout = findViewById(R.id.frame_layout);

        if(!dataAvailable()){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AddOrEditDetailFragment()).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DisplayDetailFragment()).commit();
        }
    }

    private boolean dataAvailable() {
        return preferenceHelper.getIsDataSaved();
    }


}
