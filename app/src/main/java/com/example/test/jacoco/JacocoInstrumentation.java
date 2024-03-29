package com.example.test.jacoco;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * author: beitingsu
 * created on: 2021/7/12 4:12 下午
 */
public class JacocoInstrumentation extends Instrumentation implements FinishListener {
    public static String TAG = "JacocoInstrumentation:";
    private static String DEFAULT_COVERAGE_FILE_PATH = "/mnt/sdcard/coverage.ec";
    private final Bundle mResults = new Bundle();
    private Intent mIntent;
    private static final boolean LOGD = true;
    private boolean mCoverage = true;
    private String mCoverageFilePath;

    public JacocoInstrumentation() {

    }

    @Override
    public void onCreate(Bundle arguments) {
        Log.e(TAG, "onCreate(" + arguments + ")");
        super.onCreate(arguments);
        DEFAULT_COVERAGE_FILE_PATH = getContext().getFilesDir().getPath() + "/coverage.ec";

        File file = new File(DEFAULT_COVERAGE_FILE_PATH);
        if (file.isFile() && file.exists()) {
            if (file.delete()) {
                Log.e(TAG, "file del successs");
            } else {
                Log.e(TAG, "file del fail !");
            }
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "异常 : " + e);
                e.printStackTrace();
            }
        }
        if (arguments != null) {
            Log.e(TAG, "arguments不为空 : " + arguments);
            mCoverageFilePath = arguments.getString("coverageFile");
            Log.e(TAG, "mCoverageFilePath = " + mCoverageFilePath);
        }

        mIntent = new Intent(getTargetContext(), InstrumentedActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start();
    }

    @Override
    public void onStart() {
        Log.e(TAG, "onStart def");
        if (LOGD) {
            Log.e(TAG, "onStart()");
        }
        super.onStart();

        Looper.prepare();
        InstrumentedActivity activity = (InstrumentedActivity) startActivitySync(mIntent);
        activity.setFinishListener(this);
    }

    private boolean getBooleanArgument(Bundle arguments, String tag) {
        String tagString = arguments.getString(tag);
        return tagString != null && Boolean.parseBoolean(tagString);
    }

    private void generateCoverageReport() {
        OutputStream out = null;
        try {
            out = new FileOutputStream(getCoverageFilePath(), false);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);
            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getCoverageFilePath() {
        if (mCoverageFilePath == null) {
            return DEFAULT_COVERAGE_FILE_PATH;
        } else {
            return mCoverageFilePath;
        }
    }

    private boolean setCoverageFilePath(String filePath) {
        if (filePath != null && filePath.length() > 0) {
            mCoverageFilePath = filePath;
            return true;
        }
        return false;
    }

    private void reportEmmaError(Exception e) {
        reportEmmaError("", e);
    }

    private void reportEmmaError(String hint, Exception e) {
        String msg = "Failed to generate emma coverage. " + hint;
        Log.e(TAG, msg);
        mResults.putString(Instrumentation.REPORT_KEY_STREAMRESULT, "\nError: "
                + msg);
    }

    @Override
    public void onActivityFinished() {
        if (LOGD) {
            Log.e(TAG, "onActivityFinished()");
        }
        if (mCoverage) {
            Log.e(TAG, "onActivityFinished mCoverage true");
            generateCoverageReport();
        }
        finish(Activity.RESULT_OK, mResults);
    }

    @Override
    public void dumpIntermediateCoverage(String filePath) {
        // TODO Auto-generated method stub
        if (LOGD) {
            Log.e(TAG, "Intermidate Dump Called with file name :" + filePath);
        }
        if (mCoverage) {
            if (!setCoverageFilePath(filePath)) {
                if (LOGD) {
                    Log.e(TAG, "Unable to set the given file path:" + filePath + " as dump target.");
                }
            }
            generateCoverageReport();
            setCoverageFilePath(DEFAULT_COVERAGE_FILE_PATH);
        }
    }
}
