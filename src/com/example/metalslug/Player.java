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
//控制角色的默认坐标
public static int X_DEFAULT=0;
public static int Y_DEFAULT=0;
public static int Y_JUMP_MAX=0;
//保存角色名字的成员变量
private String name;
private int HP;
private int gun;
private int action=ACTION_STAND_RIGHT;
private int x =-1;
private int y=-1;
//保存角色射出的所有子弹
private final List<Bullet> bulletList=new ArrayList();
//定义控制角色移动的常量
public static final int MOVE_STAND=0;
public static final int MOVE_RIGHT=1;
public static final int MOVE_LEFT=2;
//保存角色移动方式的成员变量
public int move =MOVE_STAND;
public static final int MAX_LEFT_SHOOT_TIME=6;
//控制设计状态的保留计数器
//只有leftshoottime变为0，才能发射下一枪
private int leftShootTime=0;
public boolean isJump=false;
public boolean isJumpMax=false;
//控制跳到最高处的停留时间
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
//获取角色当前朝向，action为奇数为向右
public int getDir(){
	if(action %2==1){
		return DIR_RIGHT;
	}
	return DIR_LEFT;
}
//返回该角色在游戏界面上的位移
public int getShift(){
	if(x<=0||y<=0){
		initPosition();
	}
	return X_DEFAULT-x;
}


// 判断角色是否已经死亡
public boolean isDie()
{
	return HP<= 0;
}
// 获取该角色发射的所有子弹
public List<Bullet> getBulletList()
{
	return bulletList;
}

//画角色的方法
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

	// 绘制角色的动画帧
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
			// 射击状态停留次数每次减1
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

			// 是否需要翻转图片
			int trans = dir == DIR_RIGHT ? Graphics.TRANS_MIRROR : Graphics.TRANS_NONE;

			Bitmap bitmap = legArr[indexLeg];
			if (bitmap == null || bitmap.isRecycled())
			{
				return;
			}

			// 先画脚
			int drawX = X_DEFAULT;
			int drawY = y - bitmap.getHeight();
			Graphics.drawMatrixImage(canvas, bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), trans, drawX, drawY, 0, Graphics.TIMES_SCALE);
			currentLegBitmap = bitmap;


			// 再画头
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

			// drawCount控制该方法每调用4次才会切换到下一帧位图
			drawCount++;
			if (drawCount >= 4)
			{
				drawCount = 0;
				indexLeg++;
				indexHead++;
			}
			// 画子弹
			drawBullet(canvas);
		
			// 画左上角的角色、名字、血量
			drawHead(canvas);
		}
//绘制左上角的角色头像、名字、生命值的方法
public void drawHead(Canvas canvas){
	if(ViewManager.head==null){
		return;
	}
	//画头像
	Graphics.drawMatrixImage(canvas,ViewManager.head,0,0,ViewManager.head.getWidth(),ViewManager.head.getHeight(),
			Graphics.TRANS_MIRROR,0,0,0,Graphics.TIMES_SCALE);
	Paint p=new Paint();
	p.setTextSize(30);
	Graphics.drawBorderString(canvas,0xa33e11,0xffde00,name,ViewManager.head.getWidth(),(int)(ViewManager.scale*20),3,p);
	Graphics.drawBorderString(canvas,0x066a14, 0x91ff1d,"HP"+HP,ViewManager.head.getWidth(),(int)(ViewManager.scale*40),3,p);

}

//判断该角色是否被子弹打中的方法
	public boolean isHurt(int startX, int endX, int startY, int endY)
	{
		if (currentHeadBitmap == null || currentLegBitmap == null)
		{
			return false;
		}
		// 计算角色的图片所覆盖的矩形区域
		int playerStartX = currentHeadDrawX;
		int playerEndX = playerStartX + currentHeadBitmap.getWidth();
		int playerStartY = currentHeadDrawY;
		int playerEndY = playerStartY + currentHeadBitmap.getHeight()
			+ currentLegBitmap.getHeight();
		// 如果子弹出现的位置与角色图片覆盖的矩形区域有重叠，即可判断角色被子弹打中
		return ((startX >= playerStartX && startX <= playerEndX) ||
			(endX >= playerStartX && endX <= playerEndX))
			&& ((startY >= playerStartY && startY <= playerEndY) ||
			(endY >= playerStartY && endY <= playerEndY));
	}

	// 画子弹
	public void drawBullet(Canvas canvas)
	{
		List<Bullet> deleteList = new ArrayList();
		Bullet bullet;
		// 遍历角色发射的所有子弹
		for (int i = 0; i < bulletList.size(); i++)
		{
			bullet = bulletList.get(i);
			if (bullet == null)
			{
				continue;
			}
			// 将所有越界的子弹收集到deleteList集合中
			if (bullet.getX() < 0 || bullet.getX() > ViewManager.SCREEN_WIDTH)
			{
				deleteList.add(bullet);
			}
		}
		Bitmap bitmap;
		// 清除所有越界的子弹
		bulletList.removeAll(deleteList);
		// 遍历用户发射的所有子弹
		for (int i = 0; i < bulletList.size(); i++)
		{
			bullet = bulletList.get(i);
			if (bullet == null)
			{
				continue;
			}
			// 获取子弹对应的位图
			bitmap = bullet.getBitmap();
			if (bitmap == null)
			{
				continue;
			}
			// 子弹移动
			bullet.move();
			// 画子弹
			Graphics.drawMatrixImage(canvas, bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), bullet.getDir() == Player.DIR_LEFT ?
				Graphics.TRANS_MIRROR : Graphics.TRANS_NONE,
				bullet.getX(), bullet.getY(), 0, Graphics.TIMES_SCALE);
		}
	}


//发射子弹代码
public int getLeftShootTime(){
	return leftShootTime;
}
public void addBullet(){
	int bulletX=getDir()==DIR_RIGHT?X_DEFAULT+(int)(ViewManager.scale*50):X_DEFAULT-(int)(ViewManager.scale*50);
	Bullet bullet=new Bullet(Bullet.BULLET_TYPE_1,bulletX,y-(int)(ViewManager.scale*60),getDir());
    //将子弹添加到用户发射的子弹集合中
	bulletList.add(bullet);
	leftShootTime=MAX_LEFT_SHOOT_TIME;
	ViewManager.soundPool.play(ViewManager.soundMap.get(1),1,1,0,0,1);
}

public void move(){
	if(move==MOVE_RIGHT){
		//更新怪物的位置
		MonsterManager.updatePosistion((int)(ViewManager.scale));
		//更新角色的位置
		setX(getX()+(int)(6*ViewManager.scale));
		if(!isJump){
			//不跳的视乎需要设置动作
			setAction(Player.ACTION_RUN_RIGHT);
		}
	}else if(move==MOVE_LEFT){
		if(getX()-(int)(6*ViewManager.scale)<Player.X_DEFAULT){
			//更新挂物的位置
			MonsterManager.updatePosistion(-(getX()-Player.X_DEFAULT));
		}else{
			MonsterManager.updatePosistion(-(int)(6*ViewManager.scale));
		}
		//更新角色的位置
		setX(getX()-(int)(6*ViewManager.scale));
		if(!isJump()){
			//不跳的时候需要色织动作
			setAction(Player.ACTION_RUN_LEFT);
		}
	}else if(getAction()!=Player.ACTION_JUMP_RIGHT
			&& getAction()!=Player.ACTION_JUMP_LEFT){
		if(!isJump){
			setAction(Player.ACTION_STAND_RIGHT);
		}
	}
}



//处理角色移动与跳的逻辑关系
	public void logic()
	{
		if (!isJump())
		{
			move();
			return;
		}

		// 如果还没有跳到最高点
		if (!isJumpMax)
		{
			setAction(getDir() == Player.DIR_RIGHT ?
				Player.ACTION_JUMP_RIGHT : Player.ACTION_JUMP_LEFT);
			// 更新Y坐标
			setY(getY() - (int) (8 * ViewManager.scale));
			// 设置子弹在Y方向上具有向上的加速度
			setBulletYAccelate(-(int) (2 * ViewManager.scale));
			// 已经达到最高点
			if (getY() <= Player.Y_JUMP_MAX)
			{
				isJumpMax = true;
			}
		}
		else
		{
			jumpStopCount--;
			// 如果在最高点停留次数已经使用完
			if (jumpStopCount <= 0)
			{
				// 更新Y坐标
				setY(getY() + (int) (8 * ViewManager.scale));
				// 设置子弹在Y方向上具有向下的加速度
				setBulletYAccelate((int) (2 * ViewManager.scale));
				// 已经掉落到最低点
				if (getY() >= Player.Y_DEFAULT)
				{
					// 恢复Y坐标
					setY(Player.Y_DEFAULT);
					isJump = false;
					isJumpMax = false;
					setAction(Player.ACTION_STAND_RIGHT);
				}
				else
				{
					// 未掉落到最低点，继续使用跳的动作
					setAction(getDir() == Player.DIR_RIGHT ?
						Player.ACTION_JUMP_RIGHT : Player.ACTION_JUMP_LEFT);
				}
			}
		}
		// 控制角色移动
		move();
	}
	// 更新子弹的位置（子弹位置同样会受到角色的位移的影响）
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

		// 给子弹设置垂直方向上的加速度
		// 游戏的设计是：当角色跳动时，子弹会具有垂直方向上的加速度
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
			// 如果角色移动到屏幕最左边
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





