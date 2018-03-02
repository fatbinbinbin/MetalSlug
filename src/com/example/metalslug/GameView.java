package com.example.metalslug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.metalslug.MonsterManager;
import com.example.metalslug.Player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	public static final Player player = new Player("�����", Player.MAX_HP);
	// ���浱ǰAndroidӦ�õ���Context
	private Context mainContext = null;
	// ��ͼ����Ҫ��Paint��Canvas����
	private Paint paint = null;
	private Canvas canvas = null;
	// SurfaceHolder����ά��SurfaceView�ϻ��Ƶ�����
	private SurfaceHolder surfaceHolder;
	// ���������ı�ĳ���
	public static final int STAGE_NO_CHANGE = 0;
	// �����ʼ�������ĳ���
	public static final int STAGE_INIT = 1;
	// �����¼�����ĳ���
	public static final int STAGE_LOGIN = 2;
	// ������Ϸ�����ĳ���
	public static final int STAGE_GAME = 3;
	// ����ʧ�ܳ����ĳ���
	public static final int STAGE_LOSE = 4;
	// �����˳������ĳ���
	public static final int STAGE_QUIT = 99;
	// ������󳡾��ĳ���
	public static final int STAGE_ERROR = 255;
	// �������Ϸ��ǰ���ں��ֳ����ı���
	private int gStage = 0;
	// ����һ���������������Ϸ�Ѿ����ص����г���
	public static final List<Integer> stageList =
		Collections.synchronizedList(new ArrayList<Integer>());
	// ����GameView�Ĺ�����
	public GameView(Context context, int firstStage)
	{
		super(context);
		mainContext = context;
		paint = new Paint();
		// ���ÿ����
		paint.setAntiAlias(true);
		// ���ø�����ᱣ����Ļ������������Ϸ�����г��ֺ�����
		setKeepScreenOn(true);
		// ���ý��㣬��Ӧ�¼�����
		setFocusable(true);
		// ��ȡSurfaceHolder
		surfaceHolder = getHolder();
		// ����thisΪSurfaceHolder�Ļص�����Ҫ�����ʵ��SurfaceHolder.Callback�ӿ�
		surfaceHolder.addCallback(this);
		//��ʼ����Ļ��С
		ViewManager.initScreen(MainActivity.windowWidth
			, MainActivity.windowHeight);
		gStage = firstStage;
	}

	public Context getMainContext()
	{
		return mainContext;
	}

	public void setMainContext(Context mainContext)
	{
		this.mainContext = mainContext;
	}

	public Paint getPaint()
	{
		return paint;
	}

	public void setPaint(Paint paint)
	{
		this.paint = paint;
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	public void setCanvas(Canvas canvas)
	{
		this.canvas = canvas;
	}

	public int getgStage()
	{
		return gStage;
	}

	public void setgStage(int gStage)
	{
		this.gStage = gStage;
	}

	public SurfaceHolder getSurfaceHolder()
	{
		return surfaceHolder;
	}

	public void setSurfaceHolder(SurfaceHolder surfaceHolder)
	{
		this.surfaceHolder = surfaceHolder;
	}

	// ���裺��ʼ��
	private static final int INIT = 1;
	//���裺�߼�
	private static final int LOGIC = 2;
	// ���裺���
	private static final int CLEAN = 3;
	// ���裺��
	private static final int PAINT = 4;

	// ������Ϸ����
	public int doStage(int stage, int step)
	{
		int nextStage;
		switch (stage)
		{
			case STAGE_INIT:
				nextStage = doInit(step);
				break;
			case STAGE_LOGIN:
				nextStage = doLogin(step);
				break;
			case STAGE_GAME:
				nextStage = doGame(step);
				break;
			case STAGE_LOSE:
				nextStage = doLose(step);
				break;
			default:
				nextStage = STAGE_ERROR;
				break;
		}
		return nextStage;
	}

	public void stageLogic()
	{
		int newStage = doStage(gStage, LOGIC);
		if (newStage != STAGE_NO_CHANGE && newStage != gStage)
		{
			doStage(gStage, CLEAN); // ����ɵĳ���
			gStage = newStage & 0xFF;
			doStage(gStage, INIT);
		}
		else if (stageList.size() > 0)
		{
			newStage = STAGE_NO_CHANGE;
			synchronized (stageList)
			{
				newStage = stageList.get(0);
				stageList.remove(0);
			}
			if (newStage == STAGE_NO_CHANGE)
			{
				return;
			}
			doStage(gStage, CLEAN); // ����ɵĳ���
			gStage = newStage & 0xFF;
			doStage(gStage, INIT);
		}
	}

	// ִ�г�ʼ���ķ���
	public int doInit(int step)
	{
		// ��ʼ����ϷͼƬ
		ViewManager.loadResource();
		// ��ת����¼����
		return STAGE_LOGIN;
	}

	public Handler setViewHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			RelativeLayout layout = (RelativeLayout) msg.obj;
			if (layout != null)
			{
				RelativeLayout.LayoutParams params = new RelativeLayout
					.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				MainActivity.mainLayout.addView(layout, params);
			}
		}
	};

	public Handler delViewHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			RelativeLayout layout = (RelativeLayout) msg.obj;
			if (layout != null)
			{
				MainActivity.mainLayout.removeView(layout);
			}
		}
	};

	// ������Ϸ����
	RelativeLayout gameLayout = null;
	private static final int ID_LEFT = 9000000;
	private static final int ID_FIRE = ID_LEFT + 1;

	public int doGame(int step)
	{
		switch (step)
		{
			case INIT:
				// ��ʼ����Ϸ����
				if (gameLayout == null)
				{
					gameLayout = new RelativeLayout(mainContext);
					// ��������ƶ��İ�ť
					Button button = new Button(mainContext);
					button.setId(ID_LEFT);
					// ���ð�ť�ı���ͼƬ
					button.setBackgroundDrawable(getResources().getDrawable(R.drawable.left));
					RelativeLayout.LayoutParams params = new RelativeLayout
						.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					params.setMargins((int) (ViewManager.scale * 20),
						0, 0, (int) (ViewManager.scale * 10));
					// ����Ϸ�������������İ�ť
					gameLayout.addView(button, params);
					// Ϊ��ť����¼�������
					button.setOnTouchListener(new OnTouchListener()
					{
						@Override
						public boolean onTouch(View v, MotionEvent event)
						{
							switch (event.getAction())
							{
								case MotionEvent.ACTION_DOWN:
									player.setMove(Player.MOVE_LEFT);
									break;
								case MotionEvent.ACTION_UP:
									player.setMove(Player.MOVE_STAND);
									break;
								case MotionEvent.ACTION_MOVE:
									break;
							}
							return false;
						}
					});
					// ��������ƶ��İ�ť
					button = new Button(mainContext);
					// ���ð�ť�ı���ͼƬ
					button.setBackgroundDrawable(getResources().getDrawable(R.drawable.right));
					params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.RIGHT_OF, ID_LEFT);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					params.setMargins((int) (ViewManager.scale * 20),
						0, 0, (int) (ViewManager.scale * 10));
					// ����Ϸ������������ҵİ�ť
					gameLayout.addView(button, params);
					// Ϊ��ť����¼�������
					button.setOnTouchListener(new OnTouchListener()
					{
						public boolean onTouch(View v, MotionEvent event)
						{
							switch (event.getAction())
							{
								case MotionEvent.ACTION_DOWN:
									player.setMove(Player.MOVE_RIGHT);
									break;
								case MotionEvent.ACTION_UP:
									player.setMove(Player.MOVE_STAND);
									break;
								case MotionEvent.ACTION_MOVE:
									break;
							}
							return false;
						}
					});
					// ��������ť
					button = new Button(mainContext);
					button.setId(ID_FIRE);
					// ���ð�ť�ı���ͼƬ
					button.setBackgroundDrawable(getResources().getDrawable(R.drawable.fire));
					params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					params.setMargins(0, 0, (int) (ViewManager.scale * 20),
						(int) (ViewManager.scale * 10));
					// ����Ϸ�������������İ�ť
					gameLayout.addView(button, params);
					// Ϊ��ť����¼�������
					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							// ����ɫ��leftShootTimeΪ0ʱ����һǹ�������������ɫ���ܷ�����һǹ��
							if(player.getLeftShootTime() <= 0)
							{
								player.addBullet();
							}
						}
					});
					// ������İ�ť
					button = new Button(mainContext);
					// ���ð�ť�ı���ͼƬ
					button.setBackgroundDrawable(getResources().getDrawable(R.drawable.jump));
					params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.LEFT_OF, ID_FIRE);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					params.setMargins(0, 0, (int) (ViewManager.scale * 20),
						(int) (ViewManager.scale * 10));
					// ����Ϸ������������İ�ť
					gameLayout.addView(button, params);
					// Ϊ��ť����¼�������
					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							player.setJump(true);
						}
					});
					setViewHandler.sendMessage(setViewHandler
							.obtainMessage(0, gameLayout));  // ��
				}
				break;
			case LOGIC:
				// ������ɹ���
				MonsterManager.generateMonster();
				// �����ײ
				MonsterManager.checkMonster();
				// ��ɫ�����ƶ�
				player.logic();
				// ��ɫ����
				if (player.isDie())
				{
					stageList.add(STAGE_LOSE);
				}
				break;
			case CLEAN:
				// �����Ϸ����
				if (gameLayout != null)
				{
					delViewHandler.sendMessage(delViewHandler
							.obtainMessage(0, gameLayout));  // ��
					gameLayout = null;
				}
				break;
			case PAINT:
				// ����ϷԪ��
				ViewManager.clearScreen(canvas);
				ViewManager.drawGame(canvas);
				break;
		}
		return STAGE_NO_CHANGE;
	}


	// �����¼����
	private RelativeLayout loginView;
	public int doLogin(int step)
	{
		switch (step)
		{
			case INIT:
				// ��ʼ����ɫѪ��
				player.setHp(Player.MAX_HP);
				// ��ʼ����¼����
				if (loginView == null)
				{
					loginView = new RelativeLayout(mainContext);
					loginView.setBackgroundResource(R.drawable.game_back);
					// ������ť
					Button button = new Button(mainContext);
					// ���ð�ť�ı���ͼƬ
					button.setBackgroundResource(R.drawable.button_selector);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.CENTER_IN_PARENT);
					// ��Ӱ�ť
					loginView.addView(button, params);
					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							// ����Ϸ�����ĳ�����ӵ�stageList������
							stageList.add(STAGE_GAME);
						}
					});
					// ͨ��Handler֪ͨ���������loginView���
					setViewHandler.sendMessage(setViewHandler
							.obtainMessage(0, loginView));  // ��
				}
				break;
			case LOGIC:
				break;
			case CLEAN:
				// �����¼����
				if (loginView != null)
				{
					// ͨ��Handler֪ͨ������ɾ��loginView���
					delViewHandler.sendMessage(delViewHandler
							.obtainMessage(0, loginView));  // ��
					loginView = null;
				}
				break;
			case PAINT:
				break;
		}
		return STAGE_NO_CHANGE;
	}

	// ������Ϸʧ�ܽ���
	private RelativeLayout loseView;
	public int doLose(int step)
	{
		switch (step)
		{
			case INIT:
				// ��ʼ��ʧ�ܽ���
				if (loseView == null)
				{
					// ����ʧ�ܽ���
					loseView = new RelativeLayout(mainContext);
					loseView.setBackgroundResource(R.drawable.game_back);
					Button button = new Button(mainContext);
					button.setBackgroundResource(R.drawable.again);
					RelativeLayout.LayoutParams params = new RelativeLayout
						.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.CENTER_IN_PARENT);
					loseView.addView(button, params);
					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							// ��ת��������Ϸ�Ľ���
							stageList.add(STAGE_GAME);
							// �ý�ɫ������ֵ�ص����ֵ
							player.setHp(Player.MAX_HP);
						}
					});
					setViewHandler.sendMessage(setViewHandler
						.obtainMessage(0, loseView));
				}
				break;
			case LOGIC:
				break;
			case CLEAN:
				// �������
				if (loseView != null)
				{
					delViewHandler.sendMessage(delViewHandler
						.obtainMessage(0, loseView));
					loseView = null;
				}
				break;
			case PAINT:
				break;
		}
		return STAGE_NO_CHANGE;
	}

	// ���ε���֮��Ĭ�ϵ���ͣʱ��
	public static final int SLEEP_TIME = 40;
	// ��С����ͣʱ��
	public static final int MIN_SLEEP = 5;
	class GameThread extends Thread
	{
		public SurfaceHolder surfaceHolder = null;
		public boolean needStop = false;
		public GameThread(SurfaceHolder holder)
		{
			this.surfaceHolder = holder;
		}

		public void run()
		{
			long t1, t2;
			Looper.prepare();
			synchronized (surfaceHolder)
			{
				// ��Ϸδ�˳�
				while (gStage != STAGE_QUIT && needStop == false)
				{
					try
					{
						// ������Ϸ�ĳ����߼�
						stageLogic();
						t1 = System.currentTimeMillis();
						canvas = surfaceHolder.lockCanvas();
						if (canvas != null)
						{
							// ������Ϸ����
							doStage(gStage, PAINT);
						}
						t2 = System.currentTimeMillis();
						int paintTime = (int) (t2 - t1);
						long millis = SLEEP_TIME - paintTime;
						if (millis < MIN_SLEEP)
						{
							millis = MIN_SLEEP;
						}
						// ���߳���ͣmillis������ٴε���doStage()����
						sleep(millis);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						try
						{
							if (canvas != null)
							{
								surfaceHolder.unlockCanvasAndPost(canvas);
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			Looper.loop();
			try
			{
				sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	// ��Ϸ�߳�
	private GameThread thread = null;

@Override
public void surfaceCreated(SurfaceHolder holder)
{
	// �������߳�ִ�в���
	paint.setTextSize(15);
	if (thread != null)
	{
		thread.needStop = true;
	}
	thread = new GameThread(surfaceHolder);
	thread.start();
}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height)
	{
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
	}
}
