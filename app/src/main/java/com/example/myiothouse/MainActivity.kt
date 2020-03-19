package com.example.myiothouse

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private val executor = Executor { }
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    var permission_list = arrayOf(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.USE_BIOMETRIC
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val context: Context = this

        biometricPrompt = createBiometricPrompt()


        val promptInfo = createPromptInfo()
        if (BiometricManager.from(this)
                .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            Log.i("finger", "NG")
        }
    }

    fun checkPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }

        for (permission: String in permission_list){
            var chk = checkCallingOrSelfPermission(permission)

            if (chk == PackageManager.PERMISSION_DENIED){
                requestPermissions(permission_list, 0)
                break;
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var idx =0;
        for (idx in grantResults.indices){
            if(grantResults[idx] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "All permissions allowed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Not All permissions allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createBiometricPrompt(): androidx.biometric.BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.i("finger", "$errorCode :: $errString")
                if (errorCode == androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON) {

                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.i("finger", "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("finger", "Authentication was successful")
                connect(this@MainActivity)
                val door_lock_intent = Intent(this@MainActivity, Door_lock_Activity::class.java)
                startActivity(door_lock_intent)
            }
        }

        val biometricPrompt = androidx.biometric.BiometricPrompt(this, executor, callback)
        return biometricPrompt
    }


    fun createPromptInfo(): androidx.biometric.BiometricPrompt.PromptInfo {
        val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Please Check your FingerPrint")
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setNegativeButtonText("Cancle")
            .build()
        return promptInfo
    }


}
