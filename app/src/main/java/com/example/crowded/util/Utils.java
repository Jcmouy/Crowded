package com.example.crowded.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.crowded.R;
import com.example.crowded.transformersUtil.AntiClockSpinTransformation;
import com.example.crowded.transformersUtil.ClockSpinTransformation;
import com.example.crowded.transformersUtil.CubeInDepthTransformation;
import com.example.crowded.transformersUtil.CubeInRotationTransformation;
import com.example.crowded.transformersUtil.CubeOutDepthTransformation;
import com.example.crowded.transformersUtil.CubeOutRotationTransformation;
import com.example.crowded.transformersUtil.CubeOutScalingTransformation;
import com.example.crowded.transformersUtil.DepthPageTransformer;
import com.example.crowded.transformersUtil.DepthTransformation;
import com.example.crowded.transformersUtil.FadeOutTransformation;
import com.example.crowded.transformersUtil.FanTransformation;
import com.example.crowded.transformersUtil.GateTransformation;
import com.example.crowded.transformersUtil.HingeTransformation;
import com.example.crowded.transformersUtil.HorizontalFlipTransformation;
import com.example.crowded.transformersUtil.PopTransformation;
import com.example.crowded.transformersUtil.SimpleTransformation;
import com.example.crowded.transformersUtil.SpinnerTransformation;
import com.example.crowded.transformersUtil.TossTransformation;
import com.example.crowded.transformersUtil.VerticalFlipTransformation;
import com.example.crowded.transformersUtil.VerticalShutTransformation;
import com.example.crowded.transformersUtil.ZoomOutPageTransformer;

import androidx.viewpager2.widget.ViewPager2;

public class Utils {
    @SuppressLint("NewApi")
    public static ViewPager2.PageTransformer getTransformer(int id) {
        switch (id) {
            case R.id.action_anti_clock_spin:
                return new AntiClockSpinTransformation();
            case R.id.action_clock_spin:
                return new ClockSpinTransformation();
            case R.id.action_cube_in_depth:
                return new CubeInDepthTransformation();
            case R.id.action_cube_in_rotate:
                return new CubeInRotationTransformation();
            case R.id.action_cube_out_depth:
                return new CubeOutDepthTransformation();
            case R.id.action_cube_out_rotate:
                return new CubeOutRotationTransformation();
            case R.id.action_cube_out_scaling:
                return new CubeOutScalingTransformation();
            case R.id.action_depth_page:
                return new DepthPageTransformer();
            case R.id.action_depth:
                return new DepthTransformation();
            case R.id.action_fade_out:
                return new FadeOutTransformation();
            case R.id.action_fan:
                return new FanTransformation();
            case R.id.action_gate:
                return new GateTransformation();
            case R.id.action_hinge:
                return new HingeTransformation();
            case R.id.action_horizontal_flip:
                return new VerticalFlipTransformation();
            case R.id.action_pop:
                return new PopTransformation();
            case R.id.action_simple_transformation:
                return new SimpleTransformation();
            case R.id.action_spinner:
                return new SpinnerTransformation();
            case R.id.action_toss:
                return new TossTransformation();
            case R.id.action_vertical_flip:
                return new HorizontalFlipTransformation();
            case R.id.action_vertical_shut:
                return new VerticalShutTransformation();
            case R.id.action_zoom_out:
                return new ZoomOutPageTransformer();
        }

        return null;
    }

    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\n"+context.getResources().getString(R.string.pref_feedback_email_body_one)+ "\n"
                    +context.getResources().getString(R.string.pref_feedback_email_body_two)+ "\n"+
                    context.getResources().getString(R.string.pref_feedback_email_body_three)+
                    Build.VERSION.RELEASE + "\n"+ context.getResources().getString(R.string.pref_feedback_email_body_four)+" " + body + "\n"+
                    context.getResources().getString(R.string.pref_feedback_email_body_five)+" " + Build.BRAND +
                    "\n"+ context.getResources().getString(R.string.pref_feedback_email_body_six)+" "+ Build.MODEL + "\n"+
                    context.getResources().getString(R.string.pref_feedback_email_body_seven)+" " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.pref_feedback_email_contact)});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.pref_feedback_email_contact));
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.pref_feedback_email_subject)));
    }

    public static String randomStringGenerator(int n){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
