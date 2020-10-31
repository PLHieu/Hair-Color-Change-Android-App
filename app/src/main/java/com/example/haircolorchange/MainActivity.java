package com.example.haircolorchange;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import ai.fritz.core.Fritz;
import ai.fritz.vision.FritzVision;
import ai.fritz.vision.FritzVisionImage;
import ai.fritz.vision.FritzVisionModels;
import ai.fritz.vision.FritzVisionOrientation;
import ai.fritz.vision.ImageRotation;
import ai.fritz.vision.ModelVariant;
import ai.fritz.vision.PredictorStatusListener;
import ai.fritz.vision.imagesegmentation.BlendMode;
import ai.fritz.vision.imagesegmentation.FritzVisionSegmentationPredictor;
import ai.fritz.vision.imagesegmentation.FritzVisionSegmentationResult;
import ai.fritz.vision.imagesegmentation.MaskClass;
import ai.fritz.vision.imagesegmentation.SegmentationManagedModel;
import ai.fritz.vision.imagesegmentation.SegmentationOnDeviceModel;

public class MainActivity extends AppCompatActivity {

    private ImageView _imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init Image view
        _imageView = findViewById(R.id.imageView);
        Fritz.configure(this, "a4e85980765c4b0880545930411334cd");
        startfromImageBitmap();
    }

    private void startfromImageBitmap() {

        // load a bitmap from drawable
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);

        // Initialize the model included with the app
        SegmentationOnDeviceModel onDeviceModel = FritzVisionModels.getHairSegmentationOnDeviceModel(ModelVariant.FAST);

        // Create the predictor with the Hair Segmentation model.
        FritzVisionSegmentationPredictor predictor = FritzVision.ImageSegmentation.getPredictor(onDeviceModel);


//        final FritzVisionSegmentationPredictor[] predictor = new FritzVisionSegmentationPredictor[1];
//
//        SegmentationManagedModel managedModel = FritzVisionModels.getHairSegmentationManagedModel(ModelVariant.FAST);
//
//        FritzVision.ImageSegmentation.loadPredictor(managedModel, new PredictorStatusListener<FritzVisionSegmentationPredictor>() {
//            @Override
//            public void onPredictorReady(FritzVisionSegmentationPredictor segmentationPredictor) {
//                Log.d("success", "Segment predictor is ready");
//                predictor[0] = segmentationPredictor;
//            }
//        } ) ;

        // Create a FritzVisionImage object from BitMap object
        FritzVisionImage visionImage = FritzVisionImage.fromBitmap(bitmap);

        /*        // Get the system service for the camera manager
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Gets the first camera id
        String cameraId = manager.getCameraIdList().get(0);*/

        // Run the image through the model to identify pixels representing hair.
        FritzVisionSegmentationResult segmentationResult = predictor.predict(visionImage);

        /*        //Extract the mask for which we detected hair in the image , (maxAlpha 255, clippingScoresAbove .5f, zeroingScoresBelow .5f)
                Bitmap personMask = segmentationResult.buildSingleClassMask(MaskClass.HAIR, 255, .5f, .5f);

                // This image will have the same dimensions as visionImage
                Bitmap imageWithMask = visionImage.mask(personMask);

                // To trim the transparent pixels, set the optional trim parameter to true
                Bitmap imageWithMask = visionImage.mask(personMask, true);*/


        // Color Blend
        BlendMode colorBlend = BlendMode.COLOR;

        // Extract the mask for which we detected hair in the image
        Bitmap maskBitmap = segmentationResult.buildSingleClassMask(MaskClass.HAIR, 180, .5f, .5f, Color.BLUE);

        // blend maskBitmap with the original image.
        Bitmap blendedBitmap = visionImage.blend(maskBitmap, colorBlend);

        _imageView.setImageBitmap(blendedBitmap);
    }
}