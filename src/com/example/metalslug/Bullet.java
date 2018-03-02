package com.example.metalslug;

import com.example.metalslug.ViewManager;

import android.graphics.Bitmap;


public class Bullet {
public static final int BULLET_TYPE_1=1;
public static final int BULLET_TYPE_2=2;
public static final int BULLET_TYPE_3=3;
public static final int BULLET_TYPE_4=4;
//�����ӵ�����
private int type;
private int x;
private int y;
private int dir;
private int yAccelate=0;;
private boolean isEffect=true;
public Bullet(int type,int x,int y,int dir){
	this.type=type;
	this.x=x;
	this.y=y;
	this.dir=dir;
	
}
public Bitmap getBitmap(){
	switch (type){
	case BULLET_TYPE_1:
		return ViewManager.bulletImage[0];
	case BULLET_TYPE_2:
		return ViewManager.bulletImage[1];
	case BULLET_TYPE_3:
		return ViewManager.bulletImage[2];
	case BULLET_TYPE_4:
		return ViewManager.bulletImage[3];
	default:
		return null;
	}
	
	
}
public int getSpeedX(){
	//������ҵķ����������ӵ�������ƶ��ٶ�
	int sign =dir ==Player.DIR_RIGHT?1:-1;
	switch (type){
	//���ڵ�һ���ӵ�����12Ϊ�������������ٶ�
	case BULLET_TYPE_1:
	return (int)(ViewManager.scale*12)*sign;
	case BULLET_TYPE_2:
	return (int)(ViewManager.scale*8)*sign;
	case BULLET_TYPE_3:
		return (int)(ViewManager.scale*8)*sign;
	case BULLET_TYPE_4:
		return (int)(ViewManager.scale*8)*sign;
	default:
		return (int)(ViewManager.scale*8)*sign;
		
		
	
	}
	
}
public int getSpeedY(){
	if(yAccelate!=0){
		return yAccelate;
	}
	switch (type){
	//���ڵ�һ���ӵ�����12Ϊ�������������ٶ�
	case BULLET_TYPE_1:
	return 0;
	case BULLET_TYPE_2:
	return 0;
	case BULLET_TYPE_3:
		return (int)(ViewManager.scale*6);
	case BULLET_TYPE_4:
		return 0;
	default:
		return 0;
		
		
	
	}
	
}
//�����ӵ��ƶ����
public void move(){
	x+=getSpeedX();
	y+=getSpeedY();
}


// �����Ǹ���Ա������setter��getter����
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

public int getDir()
{
	return dir;
}

public void setDir(int dir)
{
	this.dir = dir;
}

public boolean isEffect()
{
	return isEffect;
}

public void setEffect(boolean isEffect)
{
	this.isEffect = isEffect;
}

public int getyAccelate()
{
	return yAccelate;
}

public void setyAccelate(int yAccelate)
{
	this.yAccelate = yAccelate;
}
}


