package br.com.comquesamba.factories;

import android.support.v4.app.Fragment;

import br.com.comquesamba.constants.Constants;
import br.com.comquesamba.fragments.AddEventFragment;
import br.com.comquesamba.fragments.HomeFragment;
import br.com.comquesamba.fragments.LoginFragment;
import br.com.comquesamba.fragments.LogoutFragment;
import br.com.comquesamba.fragments.MapsFragment;

public class FragmentFactory {

    public Fragment getFragment(int fragmentType){

        Fragment fragment = null;


        if (fragmentType == Constants.HOME_SCREEN){
            fragment = new HomeFragment();
        }

        if (fragmentType == Constants.LOGIN_SCREEN){
            fragment = new LoginFragment();
        }

        if (fragmentType == Constants.LOGOUT_SCREEN){
            fragment = new LogoutFragment();
        }

        if (fragmentType == Constants.MAPS_SCREEN){
            fragment = new MapsFragment();
        }

        if (fragmentType == Constants.ADD_EVENT_SCREEN){
            fragment = new AddEventFragment();
        }

        return fragment;

    }

}
