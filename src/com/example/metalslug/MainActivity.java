package com.example.metalslug;

import android.app.Activity;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import static android.view.ViewGroup.LayoutParams.*;

public class MainActivity extends Activity
{
	// �����������ڵ�������FrameLayout
	public static FrameLayout mainLayout = null;
	// �����ֵĲ��ֲ���
	public static FrameLayout.LayoutParams mainLP = null;
	// ������Դ����ĺ�����
	public static Resources res = null;
	public static MainActivity mainActivity = null;
	// �����Ա������¼��Ϸ���ڵĿ�ȡ��߶�
	public static int windowWidth;
	public static int windowHeight;
	// ��Ϸ���ڵ�����Ϸ����
	public static GameView mainView = null;
	// ���ű������ֵ�MediaPlayer
	private MediaPlayer player;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mainActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		DisplayMetrics metric = new DisplayMetrics();
		// ��ȡ��Ļ�߶ȡ����
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		windowHeight = metric.heightPixels;  // ��Ļ�߶�
		windowWidth = metric.widthPixels;  // ��Ļ���
		getWindow().setSoftInputMode(
			WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		res = getResources();
		// ����main.xml��������ļ�
		setContentView(R.layout.activity_main);
		// ��ȡmain.xml��������ļ���IDΪmainLayout�����
		mainLayout = (FrameLayout) findViewById (R.id.mainLayout);
		// ����GameView���
		mainView = new GameView(this.getApplicationContext()
			, GameView.STAGE_INIT);
		mainLP = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
		mainLayout.addView(mainView, mainLP);
		// ���ű�������
		player = MediaPlayer.create(this, R.raw.background);
		player.setLooping(true);
		player.start();
	}
	@Override
	public void onResume()
	{
		super.onResume();
		// ����Ϸ��ͣʱ����ͣ���ű�������
		if(player != null && !player.isPlaying())
		{
			player.start();
		}
	}
	@Override
	public void onPause()
	{
		super.onPause();
		// ����Ϸ�ָ�ʱ�����û�в��ű������֣���ʼ���ű�������
		if(player != null && player.isPlaying())
		{
			player.pause();
		}
	}
}
