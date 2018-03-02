package com.example.metalslug;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;


import com.example.metalslug.Graphics;
import com.example.metalslug.GameView;
import com.example.metalslug.ViewManager;


public class Monster
{
	// �������������͵ĳ��������������Ҫ���Ӹ�����ֻ���ڴ˴���ӳ������ɣ�
	public static final int TYPE_BOMB = 1;
	public static final int TYPE_FLY = 2;
	public static final int TYPE_MAN = 3;
	// ����������͵ĳ�Ա����
	private int type = TYPE_BOMB;
	// �������X��Y����ĳ�Ա����
	private int x = 0;
	private int y = 0;
	// ��������Ƿ��Ѿ����������
	private boolean isDie = false;
	// ���ƹ���ͼƬ�����Ͻǵ�X����
	private int startX = 0;
	// ���ƹ���ͼƬ�����Ͻǵ�Y����
	private int startY = 0;
	// ���ƹ���ͼƬ�����½ǵ�X����
	private int endX = 0;
	// ���ƹ���ͼƬ�����½ǵ�Y����
	private int endY = 0;
	// �ñ����������ڿ��ƶ���ˢ�µ��ٶ�
	int drawCount = 0;
	// ���嵱ǰ���ڻ��ƹ��ﶯ���ĵڼ�֡�ı���
	private int drawIndex = 0;
	// ���ڼ�¼��������ֻ����һ�Σ�����Ҫ�ظ�����
	// ÿ����������ʱ���ñ����ᱻ��ʼ��Ϊ����������������֡��
	// ���������������֡�������ʱ���ñ�����ֵ��Ϊ0��
	private int dieMaxDrawCount = Integer.MAX_VALUE;
	// �������������ӵ�
	private List<Bullet> bulletList = new ArrayList();

	public Monster(int type)
	{
		this.type = type;
		// -------���������ݹ�����������ʼ������X��Y����------
		// ���������ը����TYPE_BOMB������ˣ�TYPE_MAN��
		// �����Y��������ҿ��ƵĽ�ɫ��Y������ͬ
		if (type == TYPE_BOMB || type == TYPE_MAN)
		{
			y = Player.Y_DEFAULT;
		}
		// ��������Ƿɻ���������Ļ�߶�������ɹ����Y����
		else if (type == TYPE_FLY)
		{
			y = ViewManager.SCREEN_HEIGHT * 50 / 100
				- Util.rand((int) (ViewManager.scale * 100));
		}
		// �����������X���ꡣ
		x = ViewManager.SCREEN_WIDTH + Util.rand(ViewManager.SCREEN_WIDTH >> 1)
			- (ViewManager.SCREEN_WIDTH >> 2);
	}
	// ������ķ���
	public void draw(Canvas canvas)
	{
		if (canvas == null)
		{
			return;
		}
		switch (type)
		{
			case TYPE_BOMB:
				// �����Ĺ���������ͼƬ
				drawAni(canvas, isDie ? ViewManager.bomb2Image : ViewManager.bombImage);
				break;
			case TYPE_FLY:
				// �����Ĺ���������ͼƬ
				drawAni(canvas, isDie ? ViewManager.flyDieImage : ViewManager.flyImage);
				break;
			case TYPE_MAN:
				// �����Ĺ���������ͼƬ
				drawAni(canvas, isDie ? ViewManager.manDieImage : ViewManager.manImage);
				break;
			default:
				break;
		}
	}
	// ���ݹ���Ķ���֡ͼƬ�����ƹ��ﶯ��
	public void drawAni(Canvas canvas, Bitmap[] bitmapArr)
	{
		if (canvas == null)
		{
			return;
		}
		if (bitmapArr == null)
		{
			return;
		}
		// ��������Ѿ�������û�в��Ź���������
		//��dieMaxDrawCount���ڳ�ʼֵ����δ���Ź�����������
		if (isDie && dieMaxDrawCount == Integer.MAX_VALUE)
		{
			// ��dieMaxDrawCount������������������֡�����
			dieMaxDrawCount = bitmapArr.length;  // ��
		}
		drawIndex = drawIndex % bitmapArr.length;
		// ��ȡ��ǰ���ƵĶ���֡��Ӧ��λͼ
		Bitmap bitmap = bitmapArr[drawIndex];  // ��
		if (bitmap == null || bitmap.isRecycled())
		{
			return;
		}
		int drawX = x;
		// �Ի��ƹ��ﶯ��֡λͼ��X�������΢��
		if (isDie)
		{
			if (type == TYPE_BOMB)
			{
				drawX = x - (int) (ViewManager.scale * 50);
			}
			else if (type == TYPE_MAN)
			{
				drawX = x + (int) (ViewManager.scale * 50);
			}
		}
		// �Ի��ƹ��ﶯ��֡λͼ��Y�������΢��
		int drawY = y - bitmap.getHeight();
		// �����ﶯ��֡��λͼ
		Graphics.drawMatrixImage(canvas, bitmap, 0, 0, bitmap.getWidth(),
			bitmap.getHeight(), Graphics.TRANS_NONE, drawX, drawY, 0,
				Graphics.TIMES_SCALE);
		startX = drawX;
		startY = drawY;
		endX = startX + bitmap.getWidth();
		endY = startY + bitmap.getHeight();
		drawCount++;
		// ����6��4���ڿ����ˡ��ɻ��ķ����ӵ����ٶ�
		if (drawCount >= (type == TYPE_MAN ? 6 : 4))  // ��
		{
			// ����������ˣ�ֻ�ڵ�3֡�ŷ����ӵ�
			if (type == TYPE_MAN && drawIndex == 2)
			{
				addBullet();
			}
			// ��������Ƿɻ���ֻ�����һ֡�ŷ����ӵ�
			if (type == TYPE_FLY && drawIndex == bitmapArr.length - 1)
			{
				addBullet();
			}
			drawIndex++;  // ��
			drawCount = 0;  // ��
		}
		// ÿ��������������һ֡��dieMaxDrawCount��1��
		// ��dieMaxDrawCount����0ʱ��������������������ɣ�MonsterManger��ɾ���ù��
		if (isDie)
		{
			dieMaxDrawCount--;  // ��
		}
		// �����ӵ�
		drawBullet(canvas);
	}

	// �жϹ����Ƿ��ӵ����еķ���
	public boolean isHurt(int x, int y)
	{
		return x >= startX && x <= endX
				&& y >= startY && y <= endY;
	}

	// ���ݹ������ͻ�ȡ�ӵ����ͣ���ͬ���﷢�䲻ͬ���ӵ�
	// return 0�������ֹ��ﲻ�����ӵ�
	public int getBulletType()
	{
		switch (type)
		{
			case TYPE_BOMB:
				return 0;
			case TYPE_FLY:
				return Bullet.BULLET_TYPE_3;
			case TYPE_MAN:
				return Bullet.BULLET_TYPE_2;
			default:
				return 0;
		}
	}

	// ���巢���ӵ��ķ���
	public void addBullet()
	{
		int bulletType = getBulletType();
		// ���û���ӵ�
		if (bulletType <= 0)
		{
			return;
		}
		// �����ӵ���X��Y����
		int drawX = x;
		int drawY = y - (int) (ViewManager.scale * 60);
		// ��������Ƿɻ������¼���ɻ�������ӵ���Y����
		if (type == TYPE_FLY)
		{
			drawY = y - (int) (ViewManager.scale * 30);
		}
		// �����ӵ�����
		Bullet bullet = new Bullet(bulletType, drawX, drawY, Player.DIR_LEFT);
		// ���ӵ���ӵ��ù��﷢����ӵ�������
		bulletList.add(bullet);
	}

	// ���½�ɫ��λ�ã�����ɫ��X�������shift���루��ɫ���ƣ�
	// ���������ӵ���λ�ã��������ӵ���X�������shift���루�ӵ����ƣ�
	public void updateShift(int shift)
	{
		x -= shift;
		for (Bullet bullet : bulletList)
		{
			if (bullet == null)
			{
				continue;
			}
			bullet.setX(bullet.getX() - shift);
		}
	}

// �����ӵ��ķ���
public void drawBullet(Canvas canvas)
{
	// ����һ��deleteList���ϣ��ü��ϱ���������Ҫɾ�����ӵ�
	List<Bullet> deleteList = new ArrayList();
	Bullet bullet = null;
	for (int i = 0; i < bulletList.size(); i++)
	{
		bullet = bulletList.get(i);
		if (bullet == null)
		{
			continue;
		}
		// ����ӵ��Ѿ�Խ����Ļ
		if (bullet.getX() < 0 || bullet.getX() > ViewManager.SCREEN_WIDTH)
		{
			// ����Ҫ������ӵ���ӵ�deleteList������
			deleteList.add(bullet);
		}
	}
	// ɾ��������Ҫ������ӵ�
	bulletList.removeAll(deleteList);  // ��
	// ��������ӵ���λͼ
	Bitmap bitmap;
	// �����ù��﷢��������ӵ�
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
		// �����ӵ���λͼ
		Graphics.drawMatrixImage(canvas, bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), bullet.getDir() == Player.DIR_RIGHT ?
				Graphics.TRANS_MIRROR : Graphics.TRANS_NONE,
				bullet.getX(), bullet.getY(), 0, Graphics.TIMES_SCALE);
	}
}

	// �ж��ӵ��Ƿ�����ҿ��ƵĽ�ɫ��ײ���ж��ӵ��Ƿ���н�ɫ��
	public void checkBullet()
	{
		// ����һ��delBulletList���ϣ��ü��ϱ�����н�ɫ���ӵ������ǽ�Ҫ��ɾ��
		List<Bullet> delBulletList = new ArrayList();
		// ���������ӵ�
		for (Bullet bullet : bulletList)
		{
			if (bullet == null || !bullet.isEffect())
			{
				continue;
			}
			// �����ҿ��ƵĽ�ɫ���ӵ���
			if (GameView.player.isHurt(bullet.getX(), bullet.getX()
				, bullet.getY(), bullet.getY()))
			{
				// �ӵ���Ϊ��Ч
				bullet.setEffect(false);
				// ����ҵ�����ֵ��5
				GameView.player.setHp(GameView.player.getHp() - 5);
				// ���ӵ���ӵ�delBulletList������
				delBulletList.add(bullet);
			}
		}
		// ɾ�����д��н�ɫ���ӵ�
		bulletList.removeAll(delBulletList);
	}

	// ----------�����Ǹ���Ա������setter��getter����----------
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}

	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}

	public int getStartX()
	{
		return startX;
	}
	public void setStartX(int startX)
	{
		this.startX = startX;
	}

	public int getStartY()
	{
		return startY;
	}
	public void setStartY(int startY)
	{
		this.startY = startY;
	}

	public int getEndX()
	{
		return endX;
	}
	public void setEndX(int endX)
	{
		this.endX = endX;
	}

	public int getEndY()
	{
		return endY;
	}
	public void setEndY(int endY)
	{
		this.endY = endY;
	}

	public boolean isDie()
	{
		return isDie;
	}
	public void setDie(boolean isDie)
	{
		this.isDie = isDie;
	}

	public int getDieMaxDrawCount()
	{
		return dieMaxDrawCount;
	}
	public void setDieMaxDrawCount(int dieMaxDrawCount)
	{
		this.dieMaxDrawCount = dieMaxDrawCount;
	}
}
