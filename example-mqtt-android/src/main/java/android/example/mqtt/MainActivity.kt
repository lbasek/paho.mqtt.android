package android.example.mqtt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

private const val TAG = "MainActivity"

private const val MQTT_BROKER = "ws://powerful-river-12698.herokuapp.com"

class MainActivity : AppCompatActivity(), MqttCallback {

    override fun messageArrived(topic: String, message: MqttMessage) {

    }

    override fun connectionLost(cause: Throwable) {
        statusTextView.text = "Disconnected"
    }

    override fun deliveryComplete(token: IMqttDeliveryToken) {
    }

    lateinit var connectBtn: Button

    lateinit var publishMessageBtn: Button

    lateinit var statusTextView: TextView

    private lateinit var mqttClient: MqttAndroidClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectBtn = findViewById(R.id.connectBtn)
        statusTextView = findViewById(R.id.statusTextView)
        publishMessageBtn = findViewById(R.id.publishMessageBtn)

        mqttClient = MqttAndroidClient(this, MQTT_BROKER, "lbasek1337")
        mqttClient.setCallback(this)

        connectBtn.setOnClickListener {
            connect()
        }

        publishMessageBtn.setOnClickListener {
            val message = MqttMessage()
            message.payload = "Hello".toByteArray()
            mqttClient.publish("main", message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Toast.makeText(this@MainActivity, "Message sent!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                }
            })
        }

    }

    private fun connect() {
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = false
        mqttConnectOptions.keepAliveInterval = 1000

        mqttClient.connect(mqttConnectOptions, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.i(TAG, "onSuccess")
                statusTextView.text = "Connected"
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e(TAG, exception?.localizedMessage ?: "failure!")
                statusTextView.text = "Disconnected"
            }
        })
    }
}
