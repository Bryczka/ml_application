package pl.edu.wat.ml_application.firebase

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import com.google.firebase.ml.custom.*
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HCRRecognition {

    fun recognize(bitmap: Bitmap) {
        val bitmapMutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val localModel =
            FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("tensorflowModels/converted_model.tflite")
                .build()

        val options =
            FirebaseModelInterpreterOptions.Builder(localModel).build()

        val interpreter =
            FirebaseModelInterpreter.getInstance(options)

        val inputOutputOptions =
            FirebaseModelInputOutputOptions.Builder()
                .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 28, 28, 1))
                .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 47))
                .build()

        val inputs =
            FirebaseModelInputs.Builder()
                .add(scaleAndProcessBitmap(bitmapMutable))
                .build()

        interpreter!!.run(inputs, inputOutputOptions)
            .addOnSuccessListener { result ->
                val output = result.getOutput<Array<FloatArray>>(0)
                val probabilities = output[0]
                val result = probabilities.max()
            }
            .addOnFailureListener { e ->
                print("Errors occurred")
            }
    }

    private fun scaleAndProcessBitmap(bitmap: Bitmap): ByteBuffer {

        val imgData = ByteBuffer.allocateDirect(1 * 28 * 28 * 1 * 4)
        val mat = Mat()

        Utils.bitmapToMat(bitmap, mat)


        //Imgproc.resize(mat,mat, Size(28.0,28.0))
        //Core.rotate(mat,mat, Core.ROTATE_90_CLOCKWISE)
        //Core.flip(mat,mat, 1)
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, Size(15.0, 15.0))
        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_TOPHAT, kernel)
        Imgproc.threshold(mat, mat, 17.0, 255.0, Imgproc.THRESH_BINARY)
        val bitmapA = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, bitmapA)
        val c = bitmapA
        Imgproc.resize(mat, mat, Size(28.0, 28.0))
        Core.rotate(mat, mat, Core.ROTATE_90_CLOCKWISE)
        Core.flip(mat, mat, 1)
        val bitmapB = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, bitmapB)
        val D = bitmapB
        imgData.order(ByteOrder.nativeOrder())
        imgData.rewind()

        for (i in 0..27) {
            for (j in 0..27) {
                imgData.putFloat((mat.get(i, j)[0]).toFloat())
            }
        }
        return imgData
    }
}