package com.watchtrading.watchtrade.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.watchtrading.watchtrade.interfaces.IConverter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;


public class ImageConverterTask extends AsyncTask<Void, Void, Void> {



    private Context context;
    private Uri picUri;
    private File file;
    private boolean gallery;
    private IConverter iConverter;



    public ImageConverterTask(Context context, Uri picUri, boolean gallery, IConverter iConverter){
        this.context=context;
        this.picUri=picUri;

        this.gallery=gallery;
        this.iConverter=iConverter;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Bitmap thumbnail = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);

            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            file = new File(new Utils().getPath(context, picUri));



            Log.e("orientation", file.toString());
            ExifInterface ei = new ExifInterface(file.toString());


            String orientation = ei.getAttribute(ExifInterface.TAG_ORIENTATION);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90 + "":
                    rotatedBitmap = rotateImage(thumbnail, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180 + "":
                    rotatedBitmap = rotateImage(thumbnail, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270 + "":
                    rotatedBitmap = rotateImage(thumbnail, 270);
                    break;


                case ExifInterface.ORIENTATION_NORMAL + "":
                default:
                    rotatedBitmap = thumbnail;
            }
            if(!gallery) {
                if (file.exists()) {
                    file.delete();


                }
            }
            final ByteArrayOutputStream bytesss = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytesss);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    "capture" + ".png");

            file = destination;


            destination.createNewFile();
            FileOutputStream fo = new FileOutputStream(destination);
            fo.write(bytesss.toByteArray());
            fo.close();

            BitmapFactory.Options options = new BitmapFactory.Options();

            Bitmap bitmap = BitmapFactory.decodeFile(file.toString(), options);


            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            if (file.exists()) {
                file.delete();
                file = new File(extStorageDirectory, "attachment" + ".png");
                Log.e("file exist", file + "");
            }
            OutputStream outStream = new FileOutputStream(file);
            bitmap=Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/3, bitmap.getHeight()/3, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception ex) {
            Log.e("Ex-Converter", ex.toString());

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        iConverter.getConvertedFile(file);

    }
}
