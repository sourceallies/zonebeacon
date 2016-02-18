package com.sourceallies.android.zonebeacon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.AppIntroViewPager;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.fragment.InitialGatewaySetup;

import lombok.Getter;

/**
 * From GitHub project: https://github.com/PaoloRotolo/AppIntro/
 */
public class IntroActivity extends AppIntro2 {

    @Getter
    private AppIntroViewPager viewPager;

    @Getter
    private InitialGatewaySetup setupFragment;

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

        setupFragment = new InitialGatewaySetup();
        addSlide(setupFragment);

        setFadeAnimation();
    }

    @Override
    public void onDonePressed() {
        if (setupFragment.isComplete()) {
            saveGateway();

            setResult(RESULT_OK);
            finish();
        }
    }

    @Override public void onNextPressed() { }
    @Override public void onSlideChanged() { }

    public void saveGateway() {
        String name = getText(setupFragment.getName());
        String ipAddress = getText(setupFragment.getIpAddress());
        int port =  Integer.parseInt(getText(setupFragment.getPort()));

        DataSource source = DataSource.getInstance(this);
        source.open();
        source.insertNewGateway(name, ipAddress, port);
        source.close();
    }

    private String getText(TextInputLayout input) {
        return input.getEditText().getText().toString();
    }
}
