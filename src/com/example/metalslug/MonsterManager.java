package com.example.metalslug;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public class MonsterManager {
//�������������Ĺ���
	public static final List<Monster> dieMonsterList=new ArrayList();
	//�������л��� �Ĺ���
	public static final List<Monster> monsterList=new ArrayList();
	
	//������ɲ���ӹ���ķ���
public static void generateMonster(){
	if(monsterList.size()<3+Util.rand(3)){
		//�����¹���
		Monster monster=new Monster(1+Util.rand(3));
		monsterList.add(monster);
	}
}

//���¹������ӵ�����ķ���
public static void updatePosistion(int shift){
	Monster monster=null;
	List<Monster> delList=new ArrayList();
	//�������Ｏ��
	for(int i=0;i<monsterList.size();i++){
		monster=monsterList.get(i);
		if(monster==null){
			continue;
		}
		//���¹�����������ӵ���λ��
		monster.updateShift(shift);
		if(monster.getX()<0){
			delList.add(monster);
		}
	}
	monsterList.removeAll(delList);
	delList.clear();
	//�������������Ĺ���ļ���
	for(int i=0;i<dieMonsterList.size();i++){
		monster=dieMonsterList.get(i);
		if(monster==null){
			continue;
		}
		//���¹����ӵ���λ��
		monster.updateShift(shift);
		if(monster.getX()<0){
			delList.add(monster);
			
		}
	}
	dieMonsterList.removeAll(delList);
	GameView.player.updateBulletShift(shift);
	
	
}

public static void checkMonster(){
	List<Bullet> bulletList=new ArrayList();
	bulletList=GameView.player.getBulletList();
	if(bulletList==null){
		bulletList=new ArrayList();
	}
	Monster monster=null;
	List<Monster> delList=new ArrayList();
	List<Bullet> delBulletList=new ArrayList();
	//�������й���
	for(int i=0;i<monsterList.size();i++){
		monster=monsterList.get(i);
		if(monster==null){
			continue;
		}
	
	//���������ը��
	if(monster.getType()==Monster.TYPE_BOMB){
		if(GameView.player.isHurt(monster.getStartX(),monster.getEndX(),monster.getStartY(),monster.getEndY())){
			//����������Ϊ����״̬
			monster.setDie(true);
			//���ű�ը��Ч
			ViewManager.soundPool.play(
					ViewManager.soundMap.get(2),1,1,0,0,1);
			//������ը���ŵ�������
			delList.add(monster);
			GameView.player.setHp(GameView.player.getHp()-10);
		}
		continue;
	}
	
	for(Bullet bullet:bulletList){
		if(bullet==null||!bullet.isEffect()){
			continue;
		}
		if(monster.isHurt(bullet.getX(), bullet.getY())){
			//���ӵ�����Ϊ��Ч
			bullet.setEffect(false);
			//��������Ϊ����״̬
			monster.setDie(true);
			//��������Ƿɻ�
			if(monster.getType()==Monster.TYPE_FLY){
				//���ű�ը��Ч
				ViewManager.soundPool.play(
						ViewManager.soundMap.get(2),1,1,0,0,1);
			}
			if(monster.getType()==Monster.TYPE_MAN){
				//���ű�ը��Ч
				ViewManager.soundPool.play(
						ViewManager.soundMap.get(3),1,1,0,0,1);
			}
			delList.add(monster);
			delBulletList.add(bullet);
		}
	}
	//���������ӵ��Ƴ�
	bulletList.removeAll(monsterList);
	monster.checkBullet();
	}
	dieMonsterList.addAll(delList);
	monsterList.removeAll(delList);
}

public static void drawMonster(Canvas canvas){
	Monster monster=null;
	//�������л��� �Ĺ�����ƻ��ŵĹ���
	for(int i=0;i<monsterList.size();i++){
		monster=monsterList.get(i);
		if(monster==null){
			continue;
		}
		monster.draw(canvas);
		
	}
	List<Monster> delList=new ArrayList();
	for(int i=0;i<dieMonsterList.size();i++){
		monster=dieMonsterList.get(i);
		if(monster==null){
			continue;
		}
		monster.draw(canvas);
		if(monster.getDieMaxDrawCount()<=0){
			delList.add(monster);
		}
	}
	dieMonsterList.removeAll(delList);
}
}
