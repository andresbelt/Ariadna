package com.oncreate.ariadna.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.R;

/**
 * Created by azulandres92 on 8/15/16.
 */
public class UnidadesFragment extends AppFragment {


//    public int getMenuId() {
//        return R.id.navigation_action_home;
//    }
//
//
//    public boolean isMenuEnabled() {
//        return true;
//    }
//
//    public boolean isEntryPoint() {
//        return true;
//    }
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        CourseManager course = getApp().getCourseManager();
//        this.adapter = new  (getContext(), course.getCourse().getId(), course.getCourse().getModules());
//        this.adapter.setListener(this);
//    }
//
//
//    public void onDestroy() {
//        super.onDestroy();
//        this.adapter = null;
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
//
//
//        btn_ing = (Button) rootView.findViewById(R.id.btn_ing);
//        email = (EditText) rootView.findViewById(R.id.et_email);
//        btn_ing.setOnClickListener(this);
//
//
//
//        return rootView;
//    }
//
//    public void onDestroyView() {
//        super.onDestroyView();
//        getApp().getProgressManager().removeListener(this.progressListener);
//        this.progressListener = null;
//    }
//
//    public void onDestroyViewAfterAnimation() {
//        super.onDestroyViewAfterAnimation();
//        if (this.recyclerView != null) {
//            this.recyclerView.setAdapter(null);
//            this.recyclerView.removeItemDecoration(this.moduleDecoration);
//            this.moduleDecoration = null;
//            this.recyclerView = null;
//        }
//        if (this.layoutManager != null) {
//            this.layoutManager.setSpanSizeLookup(null);
//        }
//    }

}
