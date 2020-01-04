package pl.edu.wat.ml_application.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import android.media.ExifInterface
import androidx.loader.content.CursorLoader


class LoadImage {

    companion object RequestCodes {
        const val galleryRequestCode = 1000
        const val cameraRequestCode = 1001
        var uri: String = ""
    }

    object Camera {
        fun getCamera(fragment: Fragment) {

            val photo: File = CreateFile.createPhoto(fragment)
            uri = photo.absolutePath
            if (photo != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    fragment.context!!,
                    "pl.edu.wat.ml_application.fileprovider",
                    photo
                )
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                fragment.startActivityForResult(cameraIntent, cameraRequestCode)
            }
        }
    }

    object Gallery {
        fun getGallery(fragment: Fragment) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            fragment.startActivityForResult(galleryIntent, galleryRequestCode)
        }
    }

    object CreateFile {
        fun createPhoto(fragment: Fragment): File {
            val fileName: String = "file1"
            val storageDir =
                File(fragment.activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.toURI())
            return File.createTempFile(fileName, ".jpg", storageDir)
        }
    }

    object CreateBitmapFromUri {
        fun convert(uri: Uri, activity: Activity) : Bitmap {
            var bitmap: Bitmap
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(activity!!.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            return bitmap
        }
    }

    object RotateBitmap {
        fun rotateImageIfRequired(bitmap: Bitmap, imageUri: String): Bitmap {

            val exif = ExifInterface(imageUri)

            val orientation =
                exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                else -> bitmap
            }
        }

        private fun rotateImage(bitmap: Bitmap, degree: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(degree)
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

       fun getRealPathFromURI(contentUri: Uri, fragment: Fragment): String {
            val proj = arrayOf(MediaStore.Images.Media._ID)
            val loader = CursorLoader(fragment.context!!, contentUri, proj, null, null, null)
            val cursor = loader.loadInBackground()
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            cursor.moveToFirst()
            val result = cursor.getString(columnIndex)
            cursor.close()
            return result
        }
    }
}