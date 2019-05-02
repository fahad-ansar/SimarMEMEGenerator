package me.kindeep.simarmemegenerator;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setProminentFaceOnly(true)
                .build();

        ImageView myImageView = (ImageView) findViewById(R.id.imgview);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap base_image_temp = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.raw.lead_720_405,
//                R.drawable.praam,
                options);

        Bitmap resultBitmap = Bitmap.createBitmap(base_image_temp.getWidth(), base_image_temp.getHeight(), Bitmap.Config.RGB_565);

        Canvas resultCanvas = new Canvas(resultBitmap);
        resultCanvas.drawBitmap(base_image_temp, 0, 0, null);

        Bitmap simar_temp = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.simar,
                options);

        Bitmap simarBitmap = Bitmap.createBitmap(simar_temp.getWidth(), simar_temp.getHeight(), Bitmap.Config.RGB_565);

        FaceDetector faceDetector = new
                FaceDetector.Builder(this).setTrackingEnabled(false)
                .build();
        if (!faceDetector.isOperational()) {
//            new AlertDialog.Builder(getApplicationContext()).setMessage("Could not set up the face detector!").show();d\\
            Toast.makeText(this, "Could not set up face detector!", Toast.LENGTH_SHORT).show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(base_image_temp).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        Paint myRectPaint = new Paint();
//        myRectPaint.setColor(70);

        for (int i = 0; i < faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();

            float x_left = x1 - thisFace.getWidth() / 2;
            float y_top = x1 - thisFace.getHeight() / 2;
            Bitmap temp_bitmap_simar = Bitmap.createBitmap((int) thisFace.getWidth(), (int) thisFace.getHeight(), Bitmap.Config.RGB_565);

            resultCanvas.drawBitmap(getResizedBitmap(simar_temp, (int)thisFace.getWidth(), (int)thisFace.getHeight()), x1, y1, null);
        }

        myImageView.setImageDrawable(new BitmapDrawable(getResources(), resultBitmap));

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
        return resizedBitmap;
    }
}
