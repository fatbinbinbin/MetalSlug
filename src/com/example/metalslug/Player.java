package com.example.metalslug;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.metalslug.Graphics;
import com.example.metalslug.ViewManager;


public class Player {
public static final int MAX_HP=500;
public static final int ACTION_STAND_RIGHT=1;
public static final int ACTION_STAND_LEFT=2;
public static final int ACTION_RUN_RIGHT=3;
public static final int ACTION_RUN_LEFT=4;
public static final int ACTION_JUMP_RIGHT=5;
public static final int ACTION_JUMP_LEFT=6;

public static final int DIR_RIGHT=1;
public static final int DIR_LEFT=2;
//���ƽ�ɫ��Ĭ������
public static int X_DEFAULT=0;
public static int Y_DEFAULT=0;
public static int Y_JUMP_MAX=0;
//�����ɫ���ֵĳ�Ա����
private String name;
private int HP;
private int gun;
private int action=ACTION_STAND_RIGHT;
private int x =-1;
private int y=-1;
//�����ɫ����������ӵ�
private final List<Bullet> bulletList=new ArrayList();
//������ƽ�ɫ�ƶ��ĳ���
public static final int MOVE_STAND=0;
public static final int MOVE_RIGHT=1;
public static final int MOVE_LEFT=2;
//�����ɫ�ƶ���ʽ�ĳ�Ա����
public int move =MOVE_STAND;
public static final int MAX_LEFT_SHOOT_TIME=6;
//�������״̬�ı���������
//ֻ��leftshoottime��Ϊ0�����ܷ�����һǹ
private int leftShootTime=0;
public boolean isJump=false;
public boolean isJumpMax=false;
//����������ߴ���ͣ��ʱ��
public int jumpStopCount=0;
private int indexLeg=0;
private int indexHead=0;
private int currentHeadDrawX=0;
private int currentHeadDrawY=0;
private Bitmap currentLegBitmap=null;
private Bitmap currentHeadBitmap=null;
private int drawCount=0;
public Player(String name, int HP)
{
	this.name = name;
	this.HP = HP;
}
public void initPosition()
{
	x = ViewManager.SCREEN_WIDTH * 15 / 100;
	y = ViewManager.SCREEN_HEIGHT * 75 / 100;
	X_DEFAULT = x;
	Y_DEFAULT = y;
	Y_JUMP_MAX = ViewManager.SCREEN_HEIGHT * 50 / 100;
}
//��ȡ��ɫ��ǰ����actionΪ����Ϊ����
public int getDir(){
	if(action %2==1){
		return DIR_RIGHT;
	}
	return DIR_LEFT;
}
//���ظý�ɫ����Ϸ�����ϵ�λ��
public int getShift(){
	if(x<=0||y<=0){
		initPosition();
	}
	return X_DEFAULT-x;
}


// �жϽ�ɫ�Ƿ��Ѿ�����
public boolean isDie()
{
	return HP<= 0;
}
// ��ȡ�ý�ɫ����������ӵ�
public List<Bullet> getBulletList()
{
	return bulletList;
}

//����ɫ�ķ���
	public void draw(Canvas canvas)
	{
		if (canvas == null)
		{
			return;
		}

		switch (action)
		{
			case ACTION_STAND_RIGHT:
				drawAni(canvas, ViewManager.legStandImage, ViewManager.headStandImage, DIR_RIGHT);
				break;
			case ACTION_STAND_LEFT:
				drawAni(canvas, ViewManager.legStandImage, ViewManager.headStandImage, DIR_LEFT);
				break;
			case ACTION_RUN_RIGHT:
				drawAni(canvas, ViewManager.legRunImage, ViewManager.headRunImage, DIR_RIGHT);
				break;
			case ACTION_RUN_LEFT:
				drawAni(canvas, ViewManager.legRunImage, ViewManager.headRunImage, DIR_LEFT);
				break;
			case ACTION_JUMP_RIGHT:
				drawAni(canvas, ViewManager.legJumpImage, ViewManager.headJumpImage, DIR_RIGHT);
				break;
			case ACTION_JUMP_LEFT:
				drawAni(canvas, ViewManager.legJumpImage, ViewManager.headJumpImage, DIR_LEFT);
				break;
			default:
				break;
		}
	}

	// ���ƽ�ɫ�Ķ���֡
		public void drawAni(Canvas canvas, Bitmap[] legArr, Bitmap[] headArrFrom, int dir)
		{
			if (canvas == null)
			{
				return;
			}
			if (legArr == null)
			{
				return;
			}

			Bitmap[] headArr = headArrFrom;
			// ���״̬ͣ������ÿ�μ�1
			if (leftShootTime > 0)
			{
				headArr = ViewManager.headShootImage;
				leftShootTime--;
			}

			if (headArr == null)
			{
				return;
			}

			indexLeg = indexLeg % legArr.length;
			indexHead = indexHead % headArr.length;

			// �Ƿ���Ҫ��תͼƬ
			int trans = dir == DIR_RIGHT ? Graphics.TRANS_MIRROR : Graphics.TRANS_NONE;

			Bitmap bitmap = legArr[indexLeg];
			if (bitmap == null || bitmap.isRecycled())
			{
				return;
			}

			// �Ȼ���
			int drawX = X_DEFAULT;
			int drawY = y - bitmap.getHeight();
			Graphics.drawMatrixImage(canvas, bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), trans, drawX, drawY, 0, Graphics.TIMES_SCALE);
			currentLegBitmap = bitmap;


			// �ٻ�ͷ
			Bitmap bitmap2 = headArr[indexHead];
			if (bitmap2 == null || bitmap2.isRecycled())
			{
				return;
			}
			drawX = drawX - ((bitmap2.getWidth() - bitmap.getWidth()) >> 1);
			if (action == ACTION_STAND_LEFT)
			{
				drawX += (int) (6 * ViewManager.scale);
			}
			drawY = drawY - bitmap2.getHeight() + (int) (10 * ViewManager.scale);
			Graphics.drawMatrixImage(canvas, bitmap2, 0, 0, bitmap2.getWidth(),
					bitmap2.getHeight(), trans, drawX, drawY, 0, Graphics.TIMES_SCALE);
			currentHeadDrawX = drawX;
			currentHeadDrawY = drawY;
			currentHeadBitmap = bitmap2;

			// drawCount���Ƹ÷���ÿ����4�βŻ��л�����һ֡λͼ
			drawCount++;
			if (drawCount >= 4)
			{
				drawCount = 0;
				indexLeg++;
				indexHead++;
			}
			// ���ӵ�
			drawBullet(canvas);
		
			// �����ϽǵĽ�ɫ�����֡�Ѫ��
			drawHead(canvas);
		}
//�������ϽǵĽ�ɫͷ�����֡�����ֵ�ķ���
public void drawHead(Canvas canvas){
	if(ViewManager.head==null){
		return;
	}
	//��ͷ��
	Graphics.drawMatrixImage(canvas,ViewManager.head,0,0,ViewManager.head.getWidth(),ViewManager.head.getHeight(),
			Graphics.TRANS_MIRROR,0,0,0,Graphics.TIMES_SCALE);
	Paint p=new Paint();
	p.setTextSize(30);
	Graphics.drawBorderString(canvas,0xa33e11,0xffde00,name,ViewManager.head.getWidth(),(int)(ViewManager.scale*20),3,p);
	Graphics.drawBorderString(canvas,0x066a14, 0x91ff1d,"HP"+HP,ViewManager.head.getWidth(),(int)(ViewManager.scale*40),3,p);

}

//�жϸý�ɫ�Ƿ��ӵ����еķ���
	public boolean isHurt(int startX, int endX, int startY, int endY)
	{
		if (currentHeadBitmap == null || currentLegBitmap == null)
		{
			return false;
		}
		// �����ɫ��ͼƬ�����ǵľ�������
		int playerStartX = currentHeadDrawX;
		int playerEndX = playerStartX + currentHeadBitmap.getWidth();
		int playerStartY = currentHeadDrawY;
		int playerEndY = playerStartY + currentHeadBitmap.getHeight()
			+ currentLegBitmap.getHeight();
		// ����ӵ����ֵ�λ�����ɫͼƬ���ǵľ����������ص��������жϽ�ɫ���ӵ�����
		return ((startX >= playerStartX && startX <= playerEndX) ||
			(endX >= playerStartX && endX <= playerEndX))
			&& ((startY >= playerStartY && startY <= playerEndY) ||
			(endY >= playerStartY && endY <= playerEndY));
	}

	// ���ӵ�
	public void drawBullet(Canvas canvas)
	{
		List<Bullet> deleteList = new ArrayList();
		Bullet bullet;
		// ������ɫ����������ӵ�
		for (int i = 0; i < bulletList.size(); i++)
		{
			bullet = bulletList.get(i);
			if (bullet == null)
			{
				continue;
			}
			// ������Խ����ӵ��ռ���deleteList������
			if (bullet.getX() < 0 || bullet.getX() > ViewManager.SCREEN_WIDTH)
			{
				deleteList.add(bullet);
			}
		}
		Bitmap bitmap;
		// �������Խ����ӵ�
		bulletList.removeAll(deleteList);
		// �����û�����������ӵ�
		for (int i = 0; i < bulletList.size(); i++)
		{
			bullet = bulletList.get(i);
			if (bullet == null)
			{
				continue;
			}
			// ��ȡ�ӵ���Ӧ��λͼ
			bitmap = bullet.getBitmap();
			if (bitmap == null)
			{
				continue;
			}
			// �ӵ��ƶ�
			bullet.move();
			// ���ӵ�
			Graphics.drawMatrixImage(canvas, bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), bullet.getDir() == Player.DIR_LEFT ?
				Graphics.TRANS_MIRROR : Graphics.TRANS_NONE,
				bullet.getX(), bullet.getY(), 0, Graphics.TIMES_SCALE);
		}
	}


//�����ӵ�����
public int getLeftShootTime(){
	return leftShootTime;
}
public void addBullet(){
	int bulletX=getDir()==DIR_RIGHT?X_DEFAULT+(int)(ViewManager.scale*50):X_DEFAULT-(int)(ViewManager.scale*50);
	Bullet bullet=new Bullet(Bullet.BULLET_TYPE_1,bulletX,y-(int)(ViewManager.scale*60),getDir());
    //���ӵ���ӵ��û�������ӵ�������
	bulletList.add(bullet);
	leftShootTime=MAX_LEFT_SHOOT_TIME;
	ViewManager.soundPool.play(ViewManager.soundMap.get(1),1,1,0,0,1);
}

public void move(){
	if(move==MOVE_RIGHT){
		//���¹����λ��
		MonsterManager.updatePosistion((int)(ViewManager.scale));
		//���½�ɫ��λ��
		setX(getX()+(int)(6*ViewManager.scale));
		if(!isJump){
			//�������Ӻ���Ҫ���ö���
			setAction(Player.ACTION_RUN_RIGHT);
		}
	}else if(move==MOVE_LEFT){
		if(getX()-(int)(6*ViewManager.scale)<Player.X_DEFAULT){
			//���¹����λ��
			MonsterManager.updatePosistion(-(getX()-Player.X_DEFAULT));
		}else{
			MonsterManager.updatePosistion(-(int)(6*ViewManager.scale));
		}
		//���½�ɫ��λ��
		setX(getX()-(int)(6*ViewManager.scale));
		if(!isJump()){
			//������ʱ����Ҫɫ֯����
			setAction(Player.ACTION_RUN_LEFT);
		}
	}else if(getAction()!=Player.ACTION_JUMP_RIGHT
			&& getAction()!=Player.ACTION_JUMP_LEFT){
		if(!isJump){
			setAction(Player.ACTION_STAND_RIGHT);
		}
	}
}



//�����ɫ�ƶ��������߼���ϵ
	public void logic()
	{
		if (!isJump())
		{
			move();
			return;
		}

		// �����û��������ߵ�
		if (!isJumpMax)
		{
			setAction(getDir() == Player.DIR_RIGHT ?
				Player.ACTION_JUMP_RIGHT : Player.ACTION_JUMP_LEFT);
			// ����Y����
			setY(getY() - (int) (8 * ViewManager.scale));
			// �����ӵ���Y�����Ͼ������ϵļ��ٶ�
			setBulletYAccelate(-(int) (2 * ViewManager.scale));
			// �Ѿ��ﵽ��ߵ�
			if (getY() <= Player.Y_JUMP_MAX)
			{
				isJumpMax = true;
			}
		}
		else
		{
			jumpStopCount--;
			// �������ߵ�ͣ�������Ѿ�ʹ����
			if (jumpStopCount <= 0)
			{
				// ����Y����
				setY(getY() + (int) (8 * ViewManager.scale));
				// �����ӵ���Y�����Ͼ������µļ��ٶ�
				setBulletYAccelate((int) (2 * ViewManager.scale));
				// �Ѿ����䵽��͵�
				if (getY() >= Player.Y_DEFAULT)
				{
					// �ָ�Y����
					setY(Player.Y_DEFAULT);
					isJump = false;
					isJumpMax = false;
					setAction(Player.ACTION_STAND_RIGHT);
				}
				else
				{
					// δ���䵽��͵㣬����ʹ�����Ķ���
					setAction(getDir() == Player.DIR_RIGHT ?
						Player.ACTION_JUMP_RIGHT : Player.ACTION_JUMP_LEFT);
				}
			}
		}
		// ���ƽ�ɫ�ƶ�
		move();
	}
	// �����ӵ���λ�ã��ӵ�λ��ͬ�����ܵ���ɫ��λ�Ƶ�Ӱ�죩
		public void updateBulletShift(int shift)
		{
			for (Bullet bullet : bulletList)
			{
				if (bullet == null)
				{
					continue;
				}
				bullet.setX(bullet.getX() - shift);
			}
		}

		// ���ӵ����ô�ֱ�����ϵļ��ٶ�
		// ��Ϸ������ǣ�����ɫ����ʱ���ӵ�����д�ֱ�����ϵļ��ٶ�
		public void setBulletYAccelate(int accelate)
		{
			for (Bullet bullet : bulletList)
			{
				if (bullet == null || bullet.getyAccelate() != 0)
				{
					continue;
				}
				bullet.setyAccelate(accelate);
			}
		}

		public int getX()
		{
			if (x <= 0 || y <= 0)
			{
				initPosition();
			}

			return x;
		}

		public void setX(int x)
		{
			this.x = x % (ViewManager.map.getWidth() + X_DEFAULT);
			// �����ɫ�ƶ�����Ļ�����
			if (this.x < X_DEFAULT)
			{
				this.x = X_DEFAULT;
			}
		}

		public int getY()
		{
			if (x <= 0 || y <= 0)
			{
				initPosition();
			}
			return y;
		}

		public void setY(int y)
		{
			this.y = y;
		}

		public int getHp()
		{
			return HP;
		}

		public void setHp(int HP)
		{
			this.HP = HP;
		}

		public int getAction()
		{
			return action;
		}

		public void setAction(int action)
		{
			this.action = action;
		}

		public int getMove()
		{
			return move;
		}

		public void setMove(int move)
		{
			this.move = move;
		}

		public boolean isJump()
		{
			return isJump;
		}

		public void setJump(boolean isJump)
		{
			this.isJump = isJump;
			jumpStopCount = 6;
		}
	}





