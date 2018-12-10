package com.codility.speechtext

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.content.Context.AUDIO_SERVICE
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaMetadataRetriever


class MainActivity : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private var position = 0
    private var contador = 0

    val play = arrayOf("play", "Play", "tocar", "Tocar", "vai", "toca")
    val pause = arrayOf("pause", "Pause", "parar", "Parar", "para", "chega", "stop")
    val volume_up = arrayOf("aumenta", "alto", "mais", "aumentar", "cima", "gritar", "auto")
    val volume_down = arrayOf("diminui", "baixo", "menos", "diminuir", "sussuro", "abaixa")
    val next = arrayOf("proxima", "próxima", "próximo", "proximo", "proxima_musica", "next", "passa", "passar")
    val previous = arrayOf("anterior", "previous", "volta", "voltar")
    val shakira = arrayOf("Shakira", "shakira", "hips", "rebolar", "my hips don't lie")

    val musicas_id = arrayListOf<Int>(R.raw.hips_dont_lie, R.raw.atrasadinha, R.raw.amor_falso, R.raw.amor_falso, R.raw.refem, R.raw.medicina, R.raw.taki_taki)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        startSpeechToText()

        position = 0
        contador = 0

        val mediaPath = Uri.parse("android.resource://" + packageName + "/" + musicas_id[contador])
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(applicationContext, mediaPath)
        val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

        val image_array = mmr.embeddedPicture
        if (image_array != null) {
            val imagem_bit = BitmapFactory.decodeByteArray(image_array, 0, image_array.size)
            capa_musica.setImageBitmap(imagem_bit)
            capa_musica.setColorFilter(Color.argb(0, 255, 255, 255))
            capa_musica.adjustViewBounds = true
        } else {
            //capa_musica.setImageResource(R.id.an)
        }
        mp = MediaPlayer.create(this, musicas_id[contador])
        nome_musica.setText(title + " - " + artist)
    }

    private fun startSpeechToText() {
        //val editText = findViewById<EditText>(R.id.editText)

        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        //speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(v: Float) {}

            override fun onBufferReceived(bytes: ByteArray) {}

            override fun onEndOfSpeech() {}

            override fun onError(i: Int) {}

            override fun onResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)//getting all the matches
                //displaying the first match
                if (matches != null) {

                    if (matches[0] in play) {
                        if (mp.isPlaying() == false) {
                            mp.seekTo(position)
                            mp.start()
                            Toast.makeText(applicationContext, "Play", Toast.LENGTH_LONG).show()
                        }
                    } else if (matches[0] in pause) {
                        if (mp.isPlaying()) {
                            position = mp.getCurrentPosition()
                            mp.pause()
                            Toast.makeText(applicationContext, "Pause", Toast.LENGTH_LONG).show()
                        }
                    } else if (matches[0] in volume_up) {
                        val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
                        Toast.makeText(applicationContext, "Volume up", Toast.LENGTH_LONG).show()
                    } else if (matches[0] in volume_down) {
                        val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
                        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
                        Toast.makeText(applicationContext, "Volume down", Toast.LENGTH_LONG).show()
                    } else if (matches[0] in next) {
                        contador++
                        if (contador == musicas_id.size) {
                            contador = 0
                        }
                        Toast.makeText(applicationContext, "Música: " + (contador + 1).toString(), Toast.LENGTH_LONG).show()
                        val mediaPath = Uri.parse("android.resource://" + packageName + "/" + musicas_id[contador])
                        val mmr = MediaMetadataRetriever()
                        mmr.setDataSource(applicationContext, mediaPath)
                        val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

                        val image_array = mmr.embeddedPicture
                        if (image_array != null) {
                            val imagem_bit = BitmapFactory.decodeByteArray(image_array, 0, image_array.size)
                            capa_musica.setImageBitmap(imagem_bit)
                            capa_musica.setColorFilter(Color.argb(0, 255, 255, 255))
                            capa_musica.adjustViewBounds = true
                        } else {
                            //capa_musica.setImageResource(R.id.an)
                        }
                        mp.reset()
                        mp = MediaPlayer.create(this@MainActivity, musicas_id[contador])
                        mp.seekTo(0)
                        mp.start()
                        nome_musica.setText(title + " - " + artist)

                        Toast.makeText(applicationContext, "Next", Toast.LENGTH_LONG).show()
                    } else if (matches[0] in previous) {
                        contador--
                        if (contador < 0) {
                            contador = musicas_id.size - 1
                        }
                        Toast.makeText(applicationContext, "Música: " + (contador + 1).toString(), Toast.LENGTH_LONG).show()
                        val mediaPath = Uri.parse("android.resource://" + packageName + "/" + musicas_id[contador])
                        val mmr = MediaMetadataRetriever()
                        mmr.setDataSource(applicationContext, mediaPath)
                        val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

                        val image_array = mmr.embeddedPicture
                        if (image_array != null) {
                            val imagem_bit = BitmapFactory.decodeByteArray(image_array, 0, image_array.size)
                            capa_musica.setImageBitmap(imagem_bit)
                            capa_musica.setColorFilter(Color.argb(0, 255, 255, 255))
                            capa_musica.adjustViewBounds = true
                        } else {
                            //capa_musica.setImageResource(R.id.an)
                        }
                        mp.reset()
                        mp = MediaPlayer.create(this@MainActivity, musicas_id[contador])
                        mp.seekTo(0)
                        mp.start()
                        nome_musica.setText(title + " - " + artist)

                        Toast.makeText(applicationContext, "Previous", Toast.LENGTH_LONG).show()
                    } else if (matches[0] in shakira) {
                        contador = 0
                        Toast.makeText(applicationContext, "Música: " + (contador + 1).toString(), Toast.LENGTH_LONG).show()
                        val mediaPath = Uri.parse("android.resource://" + packageName + "/" + musicas_id[contador])
                        val mmr = MediaMetadataRetriever()
                        mmr.setDataSource(applicationContext, mediaPath)
                        val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

                        val image_array = mmr.embeddedPicture
                        if (image_array != null) {
                            val imagem_bit = BitmapFactory.decodeByteArray(image_array, 0, image_array.size)
                            capa_musica.setImageBitmap(imagem_bit)
                            capa_musica.setColorFilter(Color.argb(0, 255, 255, 255))
                            capa_musica.adjustViewBounds = true
                        } else {
                            //capa_musica.setImageResource(R.id.an)
                        }
                        mp.reset()
                        mp = MediaPlayer.create(this@MainActivity, musicas_id[contador])
                        mp.seekTo(0)
                        mp.start()
                        nome_musica.setText(title + " - " + artist)

                        Toast.makeText(applicationContext, "Shakira", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "Nenhum comando reconhecido", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onPartialResults(bundle: Bundle) {}

            override fun onEvent(i: Int, bundle: Bundle) {}
        })

        btSpeech.setOnTouchListener(View.OnTouchListener
        { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    speechRecognizer.stopListening()
                    //editText.hint = getString(R.string.text_hint)
                }

                MotionEvent.ACTION_DOWN -> {
                    speechRecognizer.startListening(speechRecognizerIntent)
                    //editText.setText("")
                    //editText.hint = "Listening..."
                }
            }
            false
        })
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + packageName))
                startActivity(intent)
                finish()
                Toast.makeText(this, "Ative a permissão para usar o microfone!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}