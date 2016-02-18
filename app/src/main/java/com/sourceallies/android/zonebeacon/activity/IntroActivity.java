package com.sourceallies.android.zonebeacon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.AppIntroViewPager;
import com.sourceallies.android.zonebeacon.R;

import lombok.Getter;

/**
 * From GitHub project: https://github.com/PaoloRotolo/AppIntro/
 */
public class IntroActivity extends AppIntro2 {

    @Getter
    private AppIntroViewPager viewPager;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        // we will use the view pager for testing.
        viewPager = (AppIntroViewPager) findViewById(R.id.view_pager);

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.app_intro_title),
                getString(R.string.powered_by_sai),
                R.drawable.intro_image,
                getResources().getColor(R.color.appIntro1)
        ));

        addSlide(AppIntroFragment.newInstance(
                "Let's get set up!",
                "We will make this our own fragment and add a gateway next.",
                R.drawable.intro_image,
                getResources().getColor(R.color.colorPrimary)
        ));

        setFadeAnimation();
    }

    @Override
    public void onDonePressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}
