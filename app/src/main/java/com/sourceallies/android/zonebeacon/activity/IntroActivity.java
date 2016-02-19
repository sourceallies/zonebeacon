package com.sourceallies.android.zonebeacon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TextInputLayout;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.AppIntroViewPager;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.fragment.AbstractSetupFragment;
import com.sourceallies.android.zonebeacon.fragment.GatewaySetupFragment;

import lombok.Getter;

/**
 * From GitHub project: https://github.com/PaoloRotolo/AppIntro/
 */
public class IntroActivity extends AppIntro2 {

    @Getter
    protected AppIntroViewPager viewPager;

    @Getter
    protected AbstractSetupFragment setupFragment;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        setResult(RESULT_CANCELED);

        // we will use the view pager for testing.
        viewPager = (AppIntroViewPager) findViewById(R.id.view_pager);

        addSlides();

        setFadeAnimation();
    }

    protected void addSlides() {
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.app_intro_title),
                getString(R.string.powered_by_sai),
                R.drawable.intro_image,
                getResources().getColor(R.color.appIntro1)
        ));

        setupFragment = new GatewaySetupFragment();
        addSlide(setupFragment);
    }

    @Override
    public void onDonePressed() {
        if (setupFragment.isComplete()) {
            save();

            setResult(RESULT_OK);
            finish();
        }
    }

    @VisibleForTesting
    protected void save() {
        setupFragment.save();
    }

    @Override public void onNextPressed() { }
    @Override public void onSlideChanged() { }
}
