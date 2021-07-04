package com.example.crowded.view.fragment;

import com.example.crowded.R;
import com.example.crowded.util.constants.ConstantMenuFrag;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class HandleFragment {
    private FragmentManager fragmentManager;
    private String currentFragment = "";

    private static List<String> listFragments;

    public HandleFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragmentToList(String nameFrag){
        if (!listFragments.contains(nameFrag)){
            listFragments.add(nameFrag);
        }
    }

    public void loadInitialFragments() {
        listFragments  = new ArrayList<>();
        listFragments.add(ConstantMenuFrag.MAIN_FRAG_DASH);
        listFragments.add(ConstantMenuFrag.MAIN_FRAG_PROF);
    }

    public void setFragment(Fragment fragment, String nameFrag) {
        currentFragment = nameFrag;
        if (fragmentManager.findFragmentByTag(nameFrag) != null) {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(nameFrag)).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.main_frame_container, fragment, nameFrag).commit();
        }
        hideFragment(nameFrag);
    }

    public void hideFragment(String nameFrag) {
        for (String frag: listFragments) {
            if (!frag.equals(nameFrag)){
                if (fragmentManager.findFragmentByTag(frag) != null) {
                    /*
                    if (frag.equals(ConstantMenuFrag.CHILD_OBRA_DETAIL) || frag.equals(ConstantMenuFrag.CHILD_OBRA_MESSAGE)){
                        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(frag)).commit();
                        fragmentManager.popBackStack();
                        listFragments.remove(frag);
                    }else{
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(frag)).commit();
                    }
                     */
                }
            }
        }
    }
}
