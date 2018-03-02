package com.example.metalslug;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public class MonsterManager {
//保存所有死掉的怪物
	public static final List<Monster> dieMonsterList=new ArrayList();
	//保存所有活着 的怪物
	public static final List<Monster> monsterList=new ArrayList();
	
	//随机生成并添加怪物的方法
public static void generateMonster(){
	if(monsterList.size()<3+Util.rand(3)){
		//创建新怪物
		Monster monster=new Monster(1+Util.rand(3));
		monsterList.add(monster);
	}
}

//更新怪物于子弹坐标的方法
public static void updatePosistion(int shift){
	Monster monster=null;
	List<Monster> delList=new ArrayList();
	//便利怪物集合
	for(int i=0;i<monsterList.size();i++){
		monster=monsterList.get(i);
		if(monster==null){
			continue;
		}
		//更新怪物、怪物所有子弹的位置
		monster.updateShift(shift);
		if(monster.getX()<0){
			delList.add(monster);
		}
	}
	monsterList.removeAll(delList);
	delList.clear();
	//遍历所有已死的怪物的集合
	for(int i=0;i<dieMonsterList.size();i++){
		monster=dieMonsterList.get(i);
		if(monster==null){
			continue;
		}
		//更新怪物子弹的位置
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
	//遍历所有怪物
	for(int i=0;i<monsterList.size();i++){
		monster=monsterList.get(i);
		if(monster==null){
			continue;
		}
	
	//如果怪物是炸弹
	if(monster.getType()==Monster.TYPE_BOMB){
		if(GameView.player.isHurt(monster.getStartX(),monster.getEndX(),monster.getStartY(),monster.getEndY())){
			//将怪物设置为死亡状态
			monster.setDie(true);
			//播放爆炸音效
			ViewManager.soundPool.play(
					ViewManager.soundMap.get(2),1,1,0,0,1);
			//将挂物炸弹放到集合中
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
			//将子弹设置为无效
			bullet.setEffect(false);
			//将怪物设为死亡状态
			monster.setDie(true);
			//如果怪物是飞机
			if(monster.getType()==Monster.TYPE_FLY){
				//播放爆炸音效
				ViewManager.soundPool.play(
						ViewManager.soundMap.get(2),1,1,0,0,1);
			}
			if(monster.getType()==Monster.TYPE_MAN){
				//播放爆炸音效
				ViewManager.soundPool.play(
						ViewManager.soundMap.get(3),1,1,0,0,1);
			}
			delList.add(monster);
			delBulletList.add(bullet);
		}
	}
	//将包含的子弹移除
	bulletList.removeAll(monsterList);
	monster.checkBullet();
	}
	dieMonsterList.addAll(delList);
	monsterList.removeAll(delList);
}

public static void drawMonster(Canvas canvas){
	Monster monster=null;
	//遍历所有活着 的怪物，绘制活着的怪物
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
