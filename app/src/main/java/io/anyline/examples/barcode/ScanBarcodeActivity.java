/*
 * Anyline
 * ScanBarcodeActivity.java
 *
 * Copyright (c) 2015 9yards GmbH
 *
 * Created by martin at 2015-07-03
 */

package io.anyline.examples.barcode;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import at.nineyards.anyline.camera.CameraController;
import at.nineyards.anyline.camera.CameraOpenListener;
import at.nineyards.anyline.modules.AnylineBaseModuleView;
import at.nineyards.anyline.modules.barcode.BarcodeResult;
import at.nineyards.anyline.modules.barcode.BarcodeResultListener;
import at.nineyards.anyline.modules.barcode.BarcodeScanView;
import io.anyline.examples.R;
import io.anyline.examples.ScanActivity;
import io.anyline.examples.ScanModuleEnum;

/**
 * Example activity for the Anyline-Barcode-Module
 */
public class ScanBarcodeActivity extends ScanActivity implements CameraOpenListener {

    private static final String TAG = ScanBarcodeActivity.class.getSimpleName();
    private BarcodeScanView barcodeScanView;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_scan_barcode,
                (ViewGroup) findViewById(R.id.scan_view_placeholder));

        resultText = (TextView) findViewById(R.id.text_result);

        barcodeScanView = (BarcodeScanView) findViewById(R.id.barcode_scan_view);
        // add a camera open listener that will be called when the camera is opened or an error occurred
        //  this is optional (if not set a RuntimeException will be thrown if an error occurs)
        barcodeScanView.setCameraOpenListener(this);
        // the view can be configured via a json file in the assets, and this config is set here
        // (alternatively it can be configured via xml, see the Energy Example for that)
        barcodeScanView.setConfigFromAsset("barcode_view_config.json");

        // optionally limit the barcode format to (multiple) specific types
        //barcodeScanView.setBarcodeFormats(BarcodeScanView.BarcodeFormat.QR_CODE,
        //        BarcodeScanView.BarcodeFormat.CODE_128);

        // initialize Anyline with the license key and a Listener that is called if a result is found
        barcodeScanView.initAnyline(getString(R.string.anyline_license_key), new BarcodeResultListener() {
            @Override
            public void onResult(BarcodeResult barcodeResult) {
                resultText.setText(barcodeResult.getResult());

                //setup the scan process
                setupScanProcessView(ScanBarcodeActivity.this, barcodeResult, getScanModule());

                //reset as the scanning will never stop (cancelOnResult = false)
                resetTime();
            }
        });
    }

    @Override
    public Rect getCutoutRect() {
        if (barcodeScanView != null) {
            return barcodeScanView.getCutoutRect();
        }
        return null;
    }

    @Override
    protected AnylineBaseModuleView getScanView() {
        return barcodeScanView;
    }

    @Override
    protected ScanModuleEnum.ScanModule getScanModule() {
        return ScanModuleEnum.ScanModule.BARCODE;
    }

    @Override
    protected void onResume() {
        super.onResume();
        resultText.setText("");
        //start the actual scanning
        barcodeScanView.startScanning();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop the scanning
        barcodeScanView.cancelScanning();
        //release the camera (must be called in onPause, because there are situations where
        // it cannot be auto-detected that the camera should be released)
        barcodeScanView.releaseCameraInBackground();
    }

    @Override
    public void onCameraOpened(CameraController cameraController, int width, int height) {
        //the camera is opened async and this is called when the opening is finished
        Log.d(TAG, "Camera opened successfully. Frame resolution " + width + " x " + height);
    }

    @Override
    public void onCameraError(Exception e) {
        //This is called if the camera could not be opened.
        // (e.g. If there is no camera or the permission is denied)
        // This is useful to present an alternative way to enter the required data if no camera exists.
        throw new RuntimeException(e);
    }
}
