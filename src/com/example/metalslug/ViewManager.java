package com.example.metalslug;

import java.io.InputStream;
import java.util.HashMap;

import com.example.metalslug.Graphics;
import com.example.metalslug.MonsterManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;

// ����ͼƬ���غ�ͼƬ���ƵĹ�����
public class ViewManager
{
	// ����һ��SoundPool
	public static SoundPool soundPool;
	public static HashMap<Integer, Integer> soundMap = new HashMap();
	// ��ͼͼƬ
	public static Bitmap map = null;
	// �����ɫվ��ʱ�Ų�����֡��ͼƬ����
	public static Bitmap[] legStandImage = null;
	// �����ɫվ��ʱͷ������֡��ͼƬ����
	public static Bitmap[] headStandImage = null;
	// �����ɫ�ܶ�ʱ�Ȳ�����֡��ͼƬ����
	public static Bitmap[] legRunImage = null;
	// �����ɫ�ܶ�ʱͷ������֡��ͼƬ����
	public static Bitmap[] headRunImage = null;
	// �����ɫ����ʱ�Ȳ�����֡��ͼƬ����
	public static Bitmap[] legJumpImage = null;
	// �����ɫ����ʱͷ������֡��ͼƬ����
	public static Bitmap[] headJumpImage = null;
	// �����ɫ���ʱͷ������֡��ͼƬ����
	public static Bitmap[] headShootImage = null;
	// ���������ӵ���ͼƬ
	public static Bitmap[] bulletImage = null;
	// ���ƽ�ɫ��ͼƬ
	public static Bitmap head = null;
	// �����һ�ֹ��ը����δ��ըʱ����֡��ͼƬ
	public static Bitmap[] bombImage = null;
	// �����һ�ֹ��ը������ըʱ����֡��ͼƬ
	public static Bitmap[] bomb2Image = null;
	// ����ڶ��ֹ���ɻ����Ķ���֡��ͼƬ
	public static Bitmap[] flyImage = null;
	// ����ڶ��ֹ���ɻ�����ը�Ķ���֡��ͼƬ
	public static Bitmap[] flyDieImage = null;
	// ��������ֹ���ˣ��Ķ���֡��ͼƬ
	public static Bitmap[] manImage = null;
	// ��������ֹ���ˣ�������ʱ����֡��ͼƬ
	public static Bitmap[] manDieImage = null;
	// ������Ϸ��ͼƬ�����ű���
	public static float scale = 1f;

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	// ��ȡ��Ļ��ʼ��ȡ��߶ȵķ���
	public static void initScreen(int width, int height)
	{
		SCREEN_WIDTH = (short) width;
		SCREEN_HEIGHT = (short) height;
	}

	// �����Ļ�ķ���
	public static void clearScreen(Canvas c, int color)
	{
		color = 0xff000000 | color;
		c.drawColor(color);
	}

	// �����Ļ�ķ���
	public static void clearScreen(Canvas c)
	{
		c.drawColor(Color.BLACK);
	}
	// ����������ϷͼƬ�������ķ���
	public static void loadResource()
	{
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM , 5);
	//	AudioAttributes attr = new AudioAttributes.Builder()
	//		.setUsage(AudioAttributes.USAGE_GAME)
	//		.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
	//		.build();
	//	soundPool = new SoundPool.Builder()
	//			.setAudioAttributes(attr)
	//			.setMaxStreams(10).build();
		// load��������ָ����Ƶ�ļ��������������ص���ƵID
		// �˴�ʹ��HashMap��������Щ��Ƶ��
		soundMap.put(1, soundPool.load(MainActivity.mainActivity, R.raw.shot, 1));
		soundMap.put(2, soundPool.load(MainActivity.mainActivity, R.raw.bomb, 1));
		soundMap.put(3, soundPool.load(MainActivity.mainActivity, R.raw.oh, 1));

		Bitmap temp = createBitmapByID(MainActivity.res, R.drawable.map);
		if (temp != null && !temp.isRecycled())
		{
			int height = temp.getHeight();
			if (height != SCREEN_HEIGHT && SCREEN_HEIGHT != 0)
			{
				scale = (float) SCREEN_HEIGHT / (float) height;
				map = Graphics.scale(temp, temp.getWidth() * scale, height * scale);
				temp.recycle();
			}
			else
			{
				map = temp;
			}
		}
		// ���ؽ�ɫվ��ʱ�Ȳ�����֡��ͼƬ
		legStandImage = new Bitmap[1];
		legStandImage[0] = createBitmapByID(MainActivity.res, R.drawable.leg_stand, scale);
		// ���ؽ�ɫվ��ʱͷ������֡��ͼƬ
		headStandImage = new Bitmap[3];
		headStandImage[0] = createBitmapByID(MainActivity.res, R.drawable.head_stand_1, scale);
		headStandImage[1] = createBitmapByID(MainActivity.res, R.drawable.head_stand_2, scale);
		headStandImage[2] = createBitmapByID(MainActivity.res, R.drawable.head_stand_3, scale);
		// ���ؽ�ɫ�ܶ�ʱ�Ȳ�����֡��ͼƬ
		legRunImage = new Bitmap[3];
		legRunImage[0] = createBitmapByID(MainActivity.res, R.drawable.leg_run_1, scale);
		legRunImage[1] = createBitmapByID(MainActivity.res, R.drawable.leg_run_2, scale);
		legRunImage[2] = createBitmapByID(MainActivity.res, R.drawable.leg_run_3, scale);
		// ���ؽ�ɫ�ܶ�ʱͷ������֡��ͼƬ
		headRunImage = new Bitmap[3];
		headRunImage[0] = createBitmapByID(MainActivity.res, R.drawable.head_run_1, scale);
		headRunImage[1] = createBitmapByID(MainActivity.res, R.drawable.head_run_2, scale);
		headRunImage[2] = createBitmapByID(MainActivity.res, R.drawable.head_run_3, scale);
		// ���ؽ�ɫ��Ծʱ�Ȳ�����֡��ͼƬ
		legJumpImage = new Bitmap[5];
		legJumpImage[0] = createBitmapByID(MainActivity.res, R.drawable.leg_jum_1, scale);
		legJumpImage[1] = createBitmapByID(MainActivity.res, R.drawable.leg_jum_2, scale);
		legJumpImage[2] = createBitmapByID(MainActivity.res, R.drawable.leg_jum_3, scale);
		legJumpImage[3] = createBitmapByID(MainActivity.res, R.drawable.leg_jum_4, scale);
		legJumpImage[4] = createBitmapByID(MainActivity.res, R.drawable.leg_jum_5, scale);
		// ���ؽ�ɫ��Ծʱͷ������֡��ͼƬ
		headJumpImage = new Bitmap[5];
		headJumpImage[0] = createBitmapByID(MainActivity.res, R.drawable.head_jump_1, scale);
		headJumpImage[1] = createBitmapByID(MainActivity.res, R.drawable.head_jump_2, scale);
		headJumpImage[2] = createBitmapByID(MainActivity.res, R.drawable.head_jump_3, scale);
		headJumpImage[3] = createBitmapByID(MainActivity.res, R.drawable.head_jump_4, scale);
		headJumpImage[4] = createBitmapByID(MainActivity.res, R.drawable.head_jump_5, scale);
		// ���ؽ�ɫ���ʱͷ������֡��ͼƬ
		headShootImage = new Bitmap[6];
		headShootImage[0] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_1, scale);
		headShootImage[1] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_2, scale);
		headShootImage[2] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_3, scale);
		headShootImage[3] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_4, scale);
		headShootImage[4] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_5, scale);
		headShootImage[5] = createBitmapByID(MainActivity.res, R.drawable.head_shoot_6, scale);
		// �����ӵ���ͼƬ
		bulletImage = new Bitmap[4];
		bulletImage[0] = createBitmapByID(MainActivity.res, R.drawable.bullet_1, scale);
		bulletImage[1] = createBitmapByID(MainActivity.res, R.drawable.bullet_2, scale);
		bulletImage[2] = createBitmapByID(MainActivity.res, R.drawable.bullet_3, scale);
		bulletImage[3] = createBitmapByID(MainActivity.res, R.drawable.bullet_4, scale);
		head = createBitmapByID(MainActivity.res, R.drawable.head, scale);
		// ���ص�һ�ֹ��ը����δ��ըʱ����֡��ͼƬ
		bombImage = new Bitmap[2];
		bombImage[0] = createBitmapByID(MainActivity.res, R.drawable.bomb_1, scale);
		bombImage[1] = createBitmapByID(MainActivity.res, R.drawable.bomb_2, scale);
		// ���ص�һ�ֹ��ը������ըʱ��ͼƬ
		bomb2Image = new Bitmap[13];
		bomb2Image[0] = createBitmapByID(MainActivity.res, R.drawable.bomb2_1, scale);
		bomb2Image[1] = createBitmapByID(MainActivity.res, R.drawable.bomb2_2, scale);
		bomb2Image[2] = createBitmapByID(MainActivity.res, R.drawable.bomb2_3, scale);
		bomb2Image[3] = createBitmapByID(MainActivity.res, R.drawable.bomb2_4, scale);
		bomb2Image[4] = createBitmapByID(MainActivity.res, R.drawable.bomb2_5, scale);
		bomb2Image[5] = createBitmapByID(MainActivity.res, R.drawable.bomb2_6, scale);
		bomb2Image[6] = createBitmapByID(MainActivity.res, R.drawable.bomb2_7, scale);
		bomb2Image[7] = createBitmapByID(MainActivity.res, R.drawable.bomb2_8, scale);
		bomb2Image[8] = createBitmapByID(MainActivity.res, R.drawable.bomb2_9, scale);
		bomb2Image[9] = createBitmapByID(MainActivity.res, R.drawable.bomb2_10, scale);
		bomb2Image[10] = createBitmapByID(MainActivity.res, R.drawable.bomb2_11, scale);
		bomb2Image[11] = createBitmapByID(MainActivity.res, R.drawable.bomb2_12, scale);
		bomb2Image[12] = createBitmapByID(MainActivity.res, R.drawable.bomb2_13, scale);
		// ���صڶ��ֹ���ɻ����Ķ���֡��ͼƬ
		flyImage = new Bitmap[6];
		flyImage[0] = createBitmapByID(MainActivity.res, R.drawable.fly_1, scale);
		flyImage[1] = createBitmapByID(MainActivity.res, R.drawable.fly_2, scale);
		flyImage[2] = createBitmapByID(MainActivity.res, R.drawable.fly_3, scale);
		flyImage[3] = createBitmapByID(MainActivity.res, R.drawable.fly_4, scale);
		flyImage[4] = createBitmapByID(MainActivity.res, R.drawable.fly_5, scale);
		flyImage[5] = createBitmapByID(MainActivity.res, R.drawable.fly_6, scale);
		// ���صڶ��ֹ���ɻ�����ըʱ�Ķ���֡��ͼƬ
		flyDieImage = new Bitmap[13];
		flyDieImage[0] = createBitmapByID(MainActivity.res, R.drawable.fly_die_1, scale);
		flyDieImage[1] = createBitmapByID(MainActivity.res, R.drawable.fly_die_2, scale);
		flyDieImage[2] = createBitmapByID(MainActivity.res, R.drawable.fly_die_3, scale);
		flyDieImage[3] = createBitmapByID(MainActivity.res, R.drawable.fly_die_4, scale);
		flyDieImage[4] = createBitmapByID(MainActivity.res, R.drawable.fly_die_5, scale);
		flyDieImage[5] = createBitmapByID(MainActivity.res, R.drawable.fly_die_6, scale);
		flyDieImage[6] = createBitmapByID(MainActivity.res, R.drawable.fly_die_7, scale);
		flyDieImage[7] = createBitmapByID(MainActivity.res, R.drawable.fly_die_8, scale);
		flyDieImage[8] = createBitmapByID(MainActivity.res, R.drawable.fly_die_9, scale);
		flyDieImage[9] = createBitmapByID(MainActivity.res, R.drawable.fly_die_10, scale);
		// ���ص����ֹ���ˣ�����ʱ�Ķ���֡��ͼƬ
		manImage = new Bitmap[3];
		manImage[0] = createBitmapByID(MainActivity.res, R.drawable.man_1, scale);
		manImage[1] = createBitmapByID(MainActivity.res, R.drawable.man_2, scale);
		manImage[2] = createBitmapByID(MainActivity.res, R.drawable.man_3, scale);
		// ���ص����ֹ���ˣ�����ʱ�Ķ���֡��ͼƬ
		manDieImage = new Bitmap[13];
		manDieImage[0] = createBitmapByID(MainActivity.res, R.drawable.man_die_1, scale);
		manDieImage[1] = createBitmapByID(MainActivity.res, R.drawable.man_die_2, scale);
		manDieImage[2] = createBitmapByID(MainActivity.res, R.drawable.man_die_3, scale);
		manDieImage[3] = createBitmapByID(MainActivity.res, R.drawable.man_die_4, scale);
		manDieImage[4] = createBitmapByID(MainActivity.res, R.drawable.man_die_5, scale);
	}
	// ������Ϸ����ķ������÷����Ȼ�����Ϸ������ͼ���ٻ�����Ϸ��ɫ�����������й���
	public static void drawGame(Canvas canvas)
	{
		if (canvas == null)
		{
			return;
		}
		// ����ͼ
		if (map != null && !map.isRecycled())
		{
			int width = map.getWidth() + GameView.player.getShift();
			// ����mapͼƬ��Ҳ���ǻ��Ƶ�ͼ
			Graphics.drawImage(canvas, map, 0, 0, -GameView.player.getShift()
				, 0 ,width, map.getHeight());
			int totalWidth = width;
			// ����ѭ������֤��ͼǰ�����ƴ������
			while (totalWidth < ViewManager.SCREEN_WIDTH)
			{
				int mapWidth = map.getWidth();
				int drawWidth = ViewManager.SCREEN_WIDTH - totalWidth;
				if (mapWidth < drawWidth)
				{
					drawWidth = mapWidth;
				}
				Graphics.drawImage(canvas, map, totalWidth, 0, 0, 0,
						drawWidth, map.getHeight());
				totalWidth += drawWidth;
			}
		}
		// ����ɫ
		GameView.player.draw(canvas);
		// ������
		MonsterManager.drawMonster(canvas);
	}

	// ���߷���������ͼƬid����ȡʵ�ʵ�λͼ
	private static Bitmap createBitmapByID(Resources res, int resID)
	{
		try
		{
			InputStream is = res.openRawResource(resID);
			return BitmapFactory.decodeStream(is, null, null);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	// ���߷���������ͼƬid����ȡʵ�ʵ�λͼ������scale��������
	private static Bitmap createBitmapByID(Resources res, int resID, float scale)
	{
		try
		{
			InputStream is = res.openRawResource(resID);
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, null);
			if (bitmap == null || bitmap.isRecycled())
			{
				return null;
			}
			if (scale <= 0 || scale == 1f)
			{
				return bitmap;
			}
			int wdith = (int) (bitmap.getWidth() * scale);
			int height = (int) (bitmap.getHeight() * scale);
			Bitmap newBitmap = Graphics.scale(bitmap, wdith, height);
			if (!bitmap.isRecycled() && !bitmap.equals(newBitmap))
			{
				bitmap.recycle();
			}
			return newBitmap;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// ���߷���������ͼƬ���ļ�������ȡʵ�ʵ�λͼ��
	private static Bitmap createBitmapByFile(String pathName)
	{
		try
		{
			InputStream fin = MainActivity.mainActivity.getAssets().open(pathName);
			return BitmapFactory.decodeStream(fin, null, null);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
