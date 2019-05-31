package com.example.smartcollege;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

public class BitmapDecoder
{
    //should give us the bitmap from image url
    private static final String LOG_TAG = BitmapDecoder.class.getSimpleName();

    /**
     * Decode bitmap form resource id
     *
     * @param context
     * @param resId
     * @param scaleFactor
     * @param isCropRequired  bitmap must be cropped
     * @param isWidgetRequest bitmap requested by the app widget
     * @return bitmap
     */
    public static Bitmap decodeImage(Context context, int resId, float scaleFactor, boolean isCropRequired, boolean isWidgetRequest)
    {
        Point holderSize = new Point(20,20);
        // Create a bitmap using BitmapFactory option to make the system manage
        // the bitmap heap without crashing when there is not enough memory to
        // allocate the decoded bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap finalBitmap = null;
        int sampleSize = 1;
        int imageWidth, imageHeight = 0;
        int holderWidth = holderSize.x, holderHeight = holderSize.y;
        try
        {
            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            // Get default background size
            // Get specified image size
            BitmapFactory.decodeResource(context.getResources(), resId, options);
            imageWidth = options.outWidth;
            imageHeight = options.outHeight;
            // Reset inJustDecodeBounds
            options.inJustDecodeBounds = false;
            addDeprecatedOption(options);
            // If set, decode methods will always return a mutable Bitmap instead of an immutable one. 
            options.inMutable = true;
            // Get sample size
            if (isWidgetRequest)
            {
                //noinspection SuspiciousNameCombination
                sampleSize = calculateInSampleSize(imageHeight, (int) (holderHeight / scaleFactor));
            }
            else
            {
                sampleSize = calculateInSampleSize(imageWidth, (int) (holderWidth / scaleFactor));
            }
            // If a scale factor is explicitly requested, use previously calculated sample size
            if (scaleFactor > 1)
            {
                // Flag reducing memory consumption
                options.inSampleSize = sampleSize;
            }
            // Otherwise, calculate a scale factor to match one of the screen dimensions
            else
            {
                // Scale to the target width or height according to the image and screen ratios
                boolean scaleAccordingToWidth = (float) imageWidth / imageHeight < (float) holderWidth / holderHeight;
                options.inDensity = scaleAccordingToWidth ? imageWidth : imageHeight;
                options.inTargetDensity = scaleAccordingToWidth ? holderWidth : holderHeight;
            }
            // Decode bitmap
            Bitmap decodedBitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
            // Crop bitmap
            if (isCropRequired)
            {
                if (isWidgetRequest)
                {
                    finalBitmap = cropBitmap(decodedBitmap);
                }
                else
                {
                    finalBitmap = cropBitmap(decodedBitmap, holderWidth, holderHeight);
                }
                if (!decodedBitmap.equals(finalBitmap))
                {
                    decodedBitmap.recycle();
                    decodedBitmap = null;
                }
            }
            else
            {
                finalBitmap = decodedBitmap;
                decodedBitmap = null;
            }
        }
        catch (OutOfMemoryError oome)
        {
            Log.e(LOG_TAG, "Ran out of memory when decoding image. Resampling");
            // We force a ratio sampleSize * 2 sub-sampling
            try
            {
                // Flag reducing memory consumption
                options.inSampleSize = sampleSize * 2;
                // Decode bitmap
                Bitmap decodedBitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
                // Crop bitmap
                if (isCropRequired)
                {
                    if (isWidgetRequest)
                    {
                        finalBitmap = cropBitmap(decodedBitmap);
                    }
                    else
                    {
                        finalBitmap = cropBitmap(decodedBitmap, holderWidth, holderHeight);
                    }
                    if (!decodedBitmap.equals(finalBitmap))
                    {
                        decodedBitmap.recycle();
                        decodedBitmap = null;
                    }
                }
                else
                {
                    finalBitmap = decodedBitmap;
                    decodedBitmap = null;
                }
            }
            catch (Throwable th)
            {
            }
        }
        catch (Throwable th)
        {
        }
        System.gc();
        return finalBitmap;
    }

    /**
     * Decode, scale and compress a bitmap from the gallery
     *
     * @param context
     * @param uri
     * @param scaleFactor
     * @param isCropRequired  bitmap must be cropped
     * @param isWidgetRequest bitmap requested by the app widget
     * @return bitmap
     */
    public static Bitmap decodeImage(Context context, Uri uri, float scaleFactor, boolean isCropRequired, boolean isWidgetRequest)
    {
        Point holderSize = new Point(20,20);
        // Create a bitmap using BitmapFactory option to make the system manage
        // the bitmap heap without crashing when there is not enough memory to
        // allocate the decoded bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap finalBitmap = null;
        InputStream is = null;
        int sampleSize = 1;
        int imageWidth, imageHeight = 0;
        int holderWidth = holderSize.x, holderHeight = holderSize.y;
        int orientation = 0;
        try
        {
            // Get input stream
            is = context.getContentResolver().openInputStream(uri);
            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            // Get default background size
            // Get specified image size
            BitmapFactory.decodeStream(is, null, options);
            if(is != null)
            {
                is.close();
            }
            // Orientation of the image
            orientation = getOrientation(context, uri);
            // Get width and height after rotation
            if (orientation == 90 || orientation == 270)
            {
                //noinspection SuspiciousNameCombination
                imageWidth = options.outHeight;
                //noinspection SuspiciousNameCombination
                imageHeight = options.outWidth;
            }
            else
            {
                imageWidth = options.outWidth;
                imageHeight = options.outHeight;
            }
            is = context.getContentResolver().openInputStream(uri);
            // Reset inJustDecodeBounds
            options.inJustDecodeBounds = false;
            addDeprecatedOption(options);
            // If set, decode methods will always return a mutable Bitmap instead of an immutable one. 
            options.inMutable = true;
            // Which is the biggest dimension
            boolean isWidthBiggest = imageWidth >= imageHeight;
            // Get sample size
            if ((isWidgetRequest) || isWidthBiggest)
            {
                //noinspection SuspiciousNameCombination
                sampleSize = calculateInSampleSize(imageHeight, (int) (holderHeight / scaleFactor));
            }
            else
            {
                sampleSize = calculateInSampleSize(imageWidth, (int) (holderWidth / scaleFactor));
            }
            // If a scale factor is explicitly requested, use previously calculated sample size
            if (scaleFactor > 1)
            {
                // Flag reducing memory consumption
                options.inSampleSize = sampleSize;
            }
            // Otherwise, calculate a scale factor to obtain the same dimensions as the screen
            else
            {
                // Scale to the target width or height according to the image and screen ratios
                boolean scaleAccordingToWidth = (float) imageWidth / imageHeight < (float) holderWidth / holderHeight;
                options.inDensity = scaleAccordingToWidth ? imageWidth : imageHeight;
                options.inTargetDensity = scaleAccordingToWidth ? holderWidth : holderHeight;
            }
            // Decode bitmap
            Bitmap decodedBitmap = BitmapFactory.decodeStream(is, null, options);
            // Rotate bitmap
            Bitmap rotatedBitmap = rotateBitmap(context, uri, decodedBitmap, orientation);
            if (!decodedBitmap.equals(rotatedBitmap))
            {
                decodedBitmap.recycle();
            }
            decodedBitmap = null;
            // Crop bitmap
            if (isCropRequired)
            {
                if (isWidgetRequest)
                {
                    finalBitmap = cropBitmap(rotatedBitmap);
                }
                else
                {
                    finalBitmap = cropBitmap(rotatedBitmap, holderWidth, holderHeight);
                }
                if (!rotatedBitmap.equals(finalBitmap))
                {
                    rotatedBitmap.recycle();
                    rotatedBitmap = null;
                }
            }
            else
            {
                finalBitmap = rotatedBitmap;
                rotatedBitmap = null;
            }
        }
        catch (OutOfMemoryError oome)
        {
            Log.e(LOG_TAG, "Ran out of memory when decoding image. Resampling");
            oome.printStackTrace();
            // We force a ratio sampleSize * 2 sub-sampling
            try
            {
                // Flag reducing memory consumption
                options.inSampleSize = sampleSize * 8;
                // Decode bitmap
                Bitmap decodedBitmap = BitmapFactory.decodeStream(is, null, options);
                // Rotate bitmap
                Bitmap rotatedBitmap = rotateBitmap(context, uri, decodedBitmap, orientation);
                if (!decodedBitmap.equals(rotatedBitmap))
                {
                    decodedBitmap.recycle();
                }
                decodedBitmap = null;
                // Crop bitmap
                if (isCropRequired)
                {
                    if (isWidgetRequest)
                    {
                        finalBitmap = cropBitmap(rotatedBitmap);
                    }
                    else
                    {
                        finalBitmap = cropBitmap(rotatedBitmap, holderWidth, holderHeight);
                    }
                    if (!rotatedBitmap.equals(finalBitmap))
                    {
                        rotatedBitmap.recycle();
                        rotatedBitmap = null;
                    }
                }
                else
                {
                    finalBitmap = rotatedBitmap;
                    rotatedBitmap = null;
                }
            }
            catch (Throwable th)
            {
            }
        }
        catch (Throwable th)
        {
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                    is = null;
                }
                catch (IOException e)
                {
                }
            }
        }
        System.gc();
        return finalBitmap;
    }

    /**
     * Decode snapshot for the camera mosaic
     *
     * @param context
     * @param resId
     * @param width
     * @param height
     * @return bitmap
     */
    public static Bitmap decodeSnapshot(Context context, int resId, int width, int height)
    {
        // Create a bitmap using BitmapFactory option to make the system manage
        // the bitmap heap without crashing when there is not enough memory to
        // allocate the decoded bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap finalBitmap = null;
        int sampleSize = 1;
        int imageWidth = 0;
        try
        {
            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            // Get specified image size
            BitmapFactory.decodeResource(context.getResources(), resId, options);
            imageWidth = options.outWidth;
            // Reset inJustDecodeBounds
            options.inJustDecodeBounds = false;
            addDeprecatedOption(options);
            // Get sample size
            sampleSize = calculateInSampleSize(imageWidth, width);
            // Flag reducing memory consumption
            options.inSampleSize = sampleSize;
            // Decode bitmap
            finalBitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        }
        catch (OutOfMemoryError oome)
        {
            // We force a ratio sampleSize * 2 sub-sampling
            try
            {
                // Flag reducing memory consumption
                options.inSampleSize = sampleSize * 2;
                // Decode bitmap
                finalBitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
            }
            catch (Throwable th)
            {
            }
        }
        catch (Throwable th)
        {
        }
        System.gc();
        return finalBitmap;
    }

    /**
     * Decode snapshot for the camera mosaic
     *
     * @param url
     * @param width
     * @param height
     * @return bitmap
     */
    public static Bitmap decodeSnapshot(URL url, int width, int height, String basicAuth)
    {
        // Create a bitmap using BitmapFactory option to make the system manage
        // the bitmap heap without crashing when there is not enough memory to
        // allocate the decoded bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap finalBitmap = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        int sampleSize = 1;
        int imageWidth = 0;
        try
        {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", basicAuth);
            conn.connect();
            is = conn.getInputStream();
            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            // Get specified image size
            BitmapFactory.decodeStream(is, null, options);
            imageWidth = options.outWidth;
            conn.disconnect();
            conn = (HttpURLConnection) conn.getURL().openConnection();
            conn.setRequestProperty("Authorization", basicAuth);
            conn.connect();
            is = conn.getInputStream();
            // Reset inJustDecodeBounds
            options.inJustDecodeBounds = false;
            addDeprecatedOption(options);
            // Get sample size
            sampleSize = calculateInSampleSize(imageWidth, width);
            // Flag reducing memory consumption
            options.inSampleSize = sampleSize;
            // Decode bitmap
            finalBitmap = BitmapFactory.decodeStream(is, null, options);
        }
        catch (OutOfMemoryError oome)
        {
            // We force a ratio sampleSize * 2 sub-sampling
            try
            {
                // Flag reducing memory consumption
                options.inSampleSize = sampleSize * 2;
                // Decode bitmap
                finalBitmap = BitmapFactory.decodeStream(is, null, options);
            }
            catch (Throwable th)
            {
            }
        }
        catch (Throwable th)
        {
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                    is = null;
                }
                catch (IOException e)
                {
                }
            }
            if (conn != null)
            {
                conn.disconnect();
            }
        }
        System.gc();
        return finalBitmap;
    }

    /**
     * Get a low quality scale factor for decoding images to store in cache
     *
     * @param context
     * @return
     */
    public static float getLowQualityScaleFactor(Context context)
    {
        float scaleFactor = 2.0f;
            int density = (int) (context.getResources().getDisplayMetrics().density * DisplayMetrics.DENSITY_DEFAULT);
            if (density >= DisplayMetrics.DENSITY_XXHIGH)
            {
                scaleFactor = 4.0f;
            }
        return scaleFactor;
    }

    /**
     * Get the orientation of the image
     *
     * @param context
     * @param uri
     * @return
     */
    public static int getOrientation(Context context, Uri uri)
    {
        // It's on the external media.
        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);
        if (cursor.getCount() != 1)
        {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    /**
     * Get orientation form EXIF
     *
     * @param filePath
     * @return
     */
    public static int getExifOrientation(String filePath)
    {
        int orientation = 0;
        try
        {
            ExifInterface exif = new ExifInterface(filePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (exifOrientation)
            {
                case 1:
                    break; // top left
                case 2:
                    break; // top right
                case 3:
                    orientation = 180;
                    break; // bottom right
                case 4:
                    orientation = 180;
                    break; // bottom left
                case 5:
                    orientation = 90;
                    break; // left top
                case 6:
                    orientation = 90;
                    break; // right top
                case 7:
                    orientation = 270;
                    break; // right bottom
                case 8:
                    orientation = 270;
                    break; // left bottom
                default:
                    break; // Unknown
            }
        }
        catch (IOException e)
        {
        }
        return orientation;
    }

    /**
     * Calculate sample size to have an optimal bitmap dimensions which fit the screen
     *
     * @param width
     * @param reqWidth
     * @return
     */
    public static int calculateInSampleSize(int width, int reqWidth)
    {
        int inSampleSize = 1;
        if (width > reqWidth)
        {
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps width larger than the 90 % of requested width.
            while ((halfWidth / inSampleSize) > (reqWidth * 0.9))
            {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /////////////////////
    // Private methods //
    /////////////////////

    /**
     * Rotate bitmap
     *
     * @param context
     * @param uri
     * @param bitmap
     * @param orientation
     * @return
     * @throws IOException
     */
    private static Bitmap rotateBitmap(Context context, Uri uri, Bitmap bitmap, int orientation) throws IOException
    {
        Bitmap rotatedBitmap = null;
        // If the orientation is not 0 (or -1, which means we don't know), we have to do a rotation.
        if (orientation > 0)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        else
        {
            return bitmap;
        }
        return rotatedBitmap;
    }

    /**
     * Crop to keep the center of the bitmap
     *
     * @param bitmap
     * @return
     */
    private static Bitmap cropBitmap(Bitmap bitmap)
    {
        if (bitmap != null)
        {
            if (bitmap.getWidth() > bitmap.getHeight())
            {
                bitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2) - (bitmap.getHeight() / 2), 0, bitmap.getHeight(), bitmap.getHeight());
            }
            else
            {
                bitmap = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() / 2) - (bitmap.getWidth() / 2), bitmap.getWidth(), bitmap.getWidth());
            }
        }
        return bitmap;
    }

    /**
     * Crop to keep the center of the bitmap
     *
     * @param bitmap
     * @return
     */
    private static Bitmap cropBitmap(Bitmap bitmap, int reqWidth, int reqHeight)
    {
        Bitmap croppedBitmap = null;
        if (bitmap != null)
        {
            int centerX = 0, centerY = 0;
            // The bitmap is smaller than the expected dimensions
            if (bitmap.getWidth() <= reqWidth && bitmap.getHeight() <= reqHeight)
            {
                return bitmap;
            }
            // Calculate center X
            if (bitmap.getWidth() >= reqWidth)
            {
                centerX = (bitmap.getWidth() / 2) - (reqWidth / 2);
            }
            else
            {
                reqWidth = bitmap.getWidth();
            }
            // Calculate center Y
            if (bitmap.getHeight() >= reqHeight)
            {
                centerY = (bitmap.getHeight() / 2) - (reqHeight / 2);
            }
            else
            {
                reqHeight = bitmap.getHeight();
            }
            // Crop the bitmap
            croppedBitmap = Bitmap.createBitmap(bitmap, centerX, centerY, reqWidth, reqHeight);
        }
        return croppedBitmap;
    }

    /**
     * Apply factor for decoding image from gallery to reduce memory consumption
     *
     * @param context
     * @param value
     * @return
     */
    private static int applyFactor(Context context, int value)
    {
        float factor = 2.0f;
        //      if (AndroidUtils.isTablet(context.getResources()))
        //      {
        //         factor = 2.0f;
        //      }
        //      else
        //      {
        //         int density = (int)(context.getResources().getDisplayMetrics().density * DisplayMetrics.DENSITY_DEFAULT);
        //         if (density >= DisplayMetrics.DENSITY_XXHIGH)
        //         {
        //            factor = 4.0f;
        //         }
        //      }
        return (int) (factor * value);
    }

    @SuppressWarnings("deprecation")
    private static void addDeprecatedOption(BitmapFactory.Options options)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            // Flag telling that the bitmap will be purged if the system needs to reclaim memory.
            options.inPurgeable = true;
            // Flag telling that the bitmap will share a reference to the input data
            options.inInputShareable = true;
        }
    }
}
