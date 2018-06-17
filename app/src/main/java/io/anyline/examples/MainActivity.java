package io.anyline.examples;


import android.os.Bundle;
import android.widget.TextView;


import io.anyline.examples.baseactivities.BaseToolbarActivity;
import io.anyline.examples.basefragments.OthersFragment;
import io.anyline.examples.basefragments.ViewPagerFragment;

/**
 * Main Activity
 */

public class MainActivity extends BaseToolbarActivity {

    private TextView messageCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_no_menu_bundle);


        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {

            OthersFragment fragment = new OthersFragment();
            Bundle identityDocArgs = new Bundle();
            identityDocArgs.putBoolean(OthersFragment.DOCUMENT_IDENTITY_FRAGMENT, true);
            fragment.setArguments(identityDocArgs);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }


        //add the view pager
//        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
//            getSupportFragmentManager().beginTransaction()
//                                       .add(R.id.fragment_container,
//                                            new ViewPagerFragment()).commit();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
