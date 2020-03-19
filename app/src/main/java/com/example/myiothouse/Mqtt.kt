package com.example.myiothouse

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

private lateinit var mqttAndroidClient: MqttAndroidClient
val CLINET_ID: String = MqttClient.generateClientId()

fun connect(applicationContext : Context) {
    val context: Context = applicationContext
    mqttAndroidClient = MqttAndroidClient ( context.applicationContext,"tcp://13.124.231.98:1883", CLINET_ID )
    try {
        val token = mqttAndroidClient.connect()
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken)                        {
                Log.i("Connection", "success ")
                //connectionStatus = true
                // Give your callback on connection established here

//                    publish("test", "open")

            }
            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                //connectionStatus = false
                Log.i("Connection", "failure")
                // Give your callback on connection failure here
                exception.printStackTrace()
            }
        }
    } catch (e: MqttException) {
        // Give your callback on connection failure here
        e.printStackTrace()
    }
}

fun subscribe(topic: String) {
    val qos = 2 // Mention your qos value
    try {
        mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                // Give your callback on Subscription here
            }
            override fun onFailure(
                asyncActionToken: IMqttToken,
                exception: Throwable
            ) {
                // Give your subscription failure callback here
            }
        })
    } catch (e: MqttException) {
        // Give your subscription failure callback here
    }
}

fun unSubscribe(topic: String) {
    try {
        val unsubToken = mqttAndroidClient.unsubscribe(topic)
        unsubToken.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                // Give your callback on unsubscribing here
            }
            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                // Give your callback on failure here
            }
        }
    } catch (e: MqttException) {
        // Give your callback on failure here
    }
}

fun receiveMessages() {
    mqttAndroidClient.setCallback(object : MqttCallback {
        override fun connectionLost(cause: Throwable) {
            //connectionStatus = false
            // Give your callback on failure here
        }
        override fun messageArrived(topic: String, message: MqttMessage) {
            try {
                val data = String(message.payload, charset("UTF-8"))
                // data is the desired received message
                // Give your callback on message received here
            } catch (e: Exception) {
                // Give your callback on error here
            }
        }
        override fun deliveryComplete(token: IMqttDeliveryToken) {
            // Acknowledgement on delivery complete
        }
    })
}

fun publish(topic: String, data: String) {
    Log.i("Connection_T", topic)
    Log.i("Connection_D", data)
    val encodedPayload : ByteArray
    try {
        encodedPayload = data.toByteArray(charset("UTF-8"))
        val message = MqttMessage(encodedPayload)
        message.qos = 1
        message.isRetained = false
        mqttAndroidClient.publish(topic, message)
    } catch (e: Exception) {
        // Give Callback on error here
    } catch (e: MqttException) {
        // Give Callback on error here
    }
}

fun disconnect() {
    try {
        val disconToken = mqttAndroidClient.disconnect()
        disconToken.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                //connectionStatus = false
                // Give Callback on disconnection here
            }
            override fun onFailure(
                asyncActionToken: IMqttToken,
                exception: Throwable
            ) {
                // Give Callback on error here
            }
        }
    } catch (e: MqttException) {
        // Give Callback on error here
    }
}