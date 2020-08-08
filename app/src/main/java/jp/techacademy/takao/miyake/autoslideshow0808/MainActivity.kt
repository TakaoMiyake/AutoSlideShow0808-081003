package jp.techacademy.takao.miyake.autoslideshow0808

import android.os.Bundle
import android.Manifest
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.view.View
import android.widget.Toast

import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Button


import java.util.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(),View.OnClickListener{

    private var mTimer: Timer? = null
    private var mTimerSec = 0.0
    private var mHandler = Handler()

    private lateinit var grantResults: IntArray

    private val PERMISSIONS_REQUEST_CODE = 100
    private var cursor: Cursor?= null
    private var imageUri:Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("ANDROID", "許可されている")

            } else {
                Log.d("ANDROID", "許可されていない")
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }else{

        }

            //進むボタン時の処理
            go_button.setOnClickListener{

            }


            //戻るボタンの処理
            back_button.setOnClickListener{

            }


            //再生／停止ボタンの処理
            playandstop_button.setOnClickListener{

            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
       // Log.d("ANDROID", PERMISSIONS_REQUEST_CODE.toString())

        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ANDROID", "許可された")
                     getContentsInfo()
                } else {
                    Log.d("ANDROID", "許可されなかった")
                    Toast.makeText(this, "許可してください", Toast.LENGTH_SHORT).show()

                }
        }
    }
    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )

        if (cursor!!.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            Log.d("ANDROID", "URI : " + imageUri.toString())
            imageView.setImageURI(imageUri)

        }
        cursor.close()
    }


    override fun onClick(v:View?) {

         //進むボタンクリック時
            if (v!!.id == R.id.go_button) {

                //次の画像がなかったら先頭の画像取得
                if (!cursor!!.moveToNext()) {
                    cursor!!.moveToFirst()
                    getContentsInfo()
                }
                //次の画像を取得
                else {
                    cursor!!.moveToNext()
                    getContentsInfo()
                }

            }
            //戻るボタンクリック時
            else if (v!!.id == R.id.back_button) {

                //前の画像がなかったら最後の画像取得
                if (!cursor!!.moveToPrevious()) {
                    cursor!!.moveToLast()
                    getContentsInfo()
                }

                else {
                    cursor!!.moveToPrevious()
                    getContentsInfo()
                }

                imageView.setImageURI(imageUri)
                Log.d("ANDROID", "URI : " + imageUri.toString())
            }

            //再生／停止ボタンクリック時
            else if (v!!.id == R.id.playandstop_button) {
                //I have no ideas to indicate around this area so far
            }

            else {
                cursor!!.close()
            }

    }



}



