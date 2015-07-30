package se.parkourspots.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import se.parkourspots.R;

/**
 * Created by Gabriel on 30/07/2015.
 */
public class CustomActionBarInflater {

    private static CustomActionBarInflater instance;

    private CustomActionBarInflater() {
    }

    public static CustomActionBarInflater getInstance() {
        if (instance == null) {
            instance = new CustomActionBarInflater();
        }
        return instance;
    }

    public void inflateCustomActionBar(AppCompatActivity activity) {
        android.support.v7.app.ActionBar mActionBar = activity.getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);

        View customView = activity.getLayoutInflater().inflate(R.layout.action_bar_custom, null);
        mActionBar.setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }
}
