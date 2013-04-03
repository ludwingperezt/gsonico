package com.example.musica;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
//import android.os.Bundle;
//import android.content.res.AssetFileDescriptor;
//import android.content.res.AssetManager;

@SuppressLint("SdCardPath")
public class MainActivity extends Activity implements OnCompletionListener{
	MediaPlayer player;
	int estado=0;
	int num_track=0;
	final String[] listado = ListadoArchivos.devolverListadoArchivosDirectorios("/mnt/sdcard/music/");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button pausa = (Button) findViewById(R.id.play_pause);
		Button siguiente = (Button) findViewById(R.id.next);
		Button detener = (Button) findViewById(R.id.stop);
		
		pausa.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (player.isPlaying()) {
					if (estado == 1) {
						player.start();
						estado = 0;
					}
					else{ 
						player.pause();
						estado = 1;
					};
				}
				else
				{
					player.reset();
					num_track=0;
					try{					
						player.setDataSource("/mnt/sdcard/music/" + listado[num_track]);
						player.prepare();
						player.start();
					}catch(Exception e){	
					}
				};
			}
		});
		
		siguiente.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				player.reset();
				num_track=num_track+1;
				try{					
					player.setDataSource("/mnt/sdcard/music/" + listado[num_track]);
					player.prepare();
					player.start();
				}catch(Exception e){	
				}
			}
		});
		
		detener.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				player.stop();
			}
		});
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		player = new MediaPlayer();
		
		try{
			player.setDataSource("/mnt/sdcard/music/" + listado[num_track]);
			player.prepare();
			player.start();
			player.setOnCompletionListener(this);
		}catch(Exception e){	
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Button sig = (Button) findViewById(R.id.next);
		if (num_track==listado.length-1)
			player.stop();
		else
			sig.performClick();
	}

}
