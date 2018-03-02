package com.example.metalslug;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

// ���廭ͼ�Ĺ�����
public class Graphics
{
	public static final int HCENTER = 1;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;

	// �������ֱ�ߵķ���
	public static void drawLine(Canvas canvas, float startX,
		float startY, float endX, float endY, Paint paint)
	{
		paint.setStyle(Style.STROKE);
		canvas.drawLine(startX, startY, endX, endY, paint);
	}
	// ������ƾ��εķ���
	public static void drawRect(Canvas canvas, float x,
		float y, float w, float h, Paint paint)
	{
		paint.setStyle(Style.STROKE);
		RectF rect = new RectF(x, y, x + w, y + h);
		canvas.drawRect(rect, paint);
	}
	// ������ƻ��ķ���
	public static void drawArc(Canvas canvas, float x,
		float y, float width, float height,
		float startAngle, float sweepAngle, Paint paint)
	{
		paint.setStyle(Style.STROKE);
		RectF rect = new RectF(x, y, x + width, y + height);
		canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
	}

	// ��������ַ����ķ���
	public static void drawString(Canvas canvas, String text,
		float textSize, float x, float y, int anchor, Paint paint)
	{
		// ��̬�����ַ����Ķ��뷽ʽ
		if ((anchor & LEFT) != 0)
		{
			paint.setTextAlign(Align.LEFT);
		}
		else if ((anchor & RIGHT) != 0)
		{
			paint.setTextAlign(Align.RIGHT);
		}
		else if ((anchor & HCENTER) != 0)
		{
			paint.setTextAlign(Align.CENTER);
		}
		else
		{
			paint.setTextAlign(Align.CENTER);
		}
		paint.setTextSize(textSize);
		// �����ַ���
		canvas.drawText(text, x, y, paint);
		// ���ı����뷽ʽ�ָ���Ĭ�������
		paint.setTextAlign(Align.CENTER);
	}

	/**
	 * ���ư����ַ����ķ���
	 *
	 * @param c           ���ư����ַ����Ļ���
	 * @param borderColor ���ư����ַ����ı߿���ɫ
	 * @param textColor   ���ư����ַ������ı���ɫ
	 * @param text        ָ��Ҫ���Ƶ��ַ���
	 * @param x           �����ַ�����X����
	 * @param y           �����ַ�����Y����
	 * @param borderWidth ���ư����ַ����ı߿���
	 * @param mPaint      ���ư����ַ����Ļ���
	 */
	public static void drawBorderString(Canvas c, int borderColor,
		int textColor, String text, int x, int y, int borderWidth, Paint mPaint)
	{
		mPaint.setAntiAlias(true);
		// ��ʹ��STROKE�������ַ����ı߿�
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(borderWidth);
		// ���û��Ʊ߿����ɫ
		mPaint.setColor(Color.rgb((borderColor & 0xFF0000) >> 16,
			(borderColor & 0x00ff00) >> 8, (borderColor & 0x0000ff)));
		c.drawText(text, x, y, mPaint);
		// ��ʹ��FILL�������ַ���
		mPaint.setStyle(Paint.Style.FILL);
		// ���û����ı�����ɫ
		mPaint.setColor(Color.rgb((textColor & 0xFF0000) >> 16,
				(textColor & 0x00ff00) >> 8, (textColor & 0x0000ff)));
		c.drawText(text, x, y, mPaint);
	}

	private static final Rect src = new Rect();
	private static final Rect dst = new Rect();

	// ����Android��ת�����ĳ���
	public static final int TRANS_MIRROR = 2;
	public static final int TRANS_MIRROR_ROT180 = 1;
	public static final int TRANS_MIRROR_ROT270 = 4;
	public static final int TRANS_MIRROR_ROT90 = 7;
	public static final int TRANS_NONE = 0;
	public static final int TRANS_ROT180 = 3;
	public static final int TRANS_ROT270 = 6;
	public static final int TRANS_ROT90 = 5;

	public static final float INTERVAL_SCALE = 0.05f; // ÿ�����ŵ��ݶ�
	// ÿ�����ŵ��ݶ���0.05�����������20�Ժ�ת������
	public static final int TIMES_SCALE = 20;
	private static final float[] pts = new float[8];
	private static final Path path = new Path();
	private static final RectF srcRect = new RectF();

	// ���ڴ�Դλͼ�е�srcX��srcY�㿪ʼ����ȡ��width����height�����򣬲��Ը�ͼƬ����trans�任��
	// ����scale����scaleΪ20ʱ��ʾ�����ţ�������תdegree�ǶȺ���Ƶ�Canvas��drawX��drawY����
	public synchronized static void drawMatrixImage(Canvas canvas, Bitmap src,
		int srcX, int srcY, int width, int height, int trans, int drawX,
		int drawY, int degree, int scale)
	{
		if (canvas == null)
		{
			return;
		}
		if (src == null || src.isRecycled())
		{
			return;
		}
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		if (srcX + width > srcWidth)
		{
			width = srcWidth - srcX;
		}
		if (srcY + height > srcHeight)
		{
			height = srcHeight - srcY;
		}

		if (width <= 0 || height <= 0)
		{
			return;
		}
		// ����ͼƬ�ں�����������������
		int scaleX = scale;
		int scaleY = scale;
		int rotate = 0;
		// ���ݳ�����Ҫ���е�ͼ��任������scaleX��scaleY�Լ�rotate��ת��
		switch (trans)
		{
			case TRANS_MIRROR_ROT180:
				scaleX = -scale;
				rotate = 180;
				break;
			case TRANS_MIRROR:
				scaleX = -scale;
				break;
			case TRANS_ROT180:
				rotate = 180;
				break;
			case TRANS_MIRROR_ROT270:
				scaleX = -scale;
				rotate = 270;
				break;
			case TRANS_ROT90:
				rotate = 90;
				break;
			case TRANS_ROT270:
				rotate = 270;
				break;
			case TRANS_MIRROR_ROT90:
				scaleX = -scale;
				rotate = 90;
				break;
			default:
				break;
		}
		// ���rotate��degreeΪ0���������漰��ת��
		// ���scaleX����TIMES_SCALE���������漰����
		if (rotate == 0 && degree == 0
			&& scaleX == TIMES_SCALE)
		{    // ��scale=1�����ţ� rotate=0����ת
			drawImage(canvas, src, drawX, drawY, srcX, srcY, width, height);
		}
		else
		{
			Matrix matrix = new Matrix();
			matrix.postScale(scaleX * INTERVAL_SCALE, scaleY * INTERVAL_SCALE);
			matrix.postRotate(rotate); // ��Matrix��תrotate
			matrix.postRotate(degree); // ��Matrix��תdegree
			srcRect.set(srcX, srcY, srcX + width, srcY + height);
			matrix.mapRect(srcRect);
			matrix.postTranslate(drawX - srcRect.left, drawY - srcRect.top);
			pts[0] = srcX;
			pts[1] = srcY;
			pts[2] = srcX + width;
			pts[3] = srcY;
			pts[4] = srcX + width;
			pts[5] = srcY + height;
			pts[6] = srcX;
			pts[7] = srcY + height;
			matrix.mapPoints(pts);
			canvas.save();
			path.reset();
			path.moveTo(pts[0], pts[1]);
			path.lineTo(pts[2], pts[3]);
			path.lineTo(pts[4], pts[5]);
			path.lineTo(pts[6], pts[7]);
			path.close();
			canvas.clipPath(path);
			// ʹ��matrix�任�������λͼ
			canvas.drawBitmap(src, matrix, null);
			canvas.restore();
		}
	}
	// ���߷���������λͼ
	// �����ǽ�Դλͼimage�����Ͻ�ΪsrcX��srcY����width����height��������Ƶ�canvas��
	public synchronized static void drawImage(Canvas canvas, Bitmap image,
		int destX, int destY, int srcX, int srcY, int width, int height)
	{
		if (canvas == null)
		{
			return;
		}
		if (image == null || image.isRecycled())
		{
			return;
		}
		// ���Դλͼ���������Ҫ���Ƶ�Ŀ�������С
		if (srcX == 0 && srcY == 0 && image.getWidth() <= width
			&& image.getHeight() <= height)
		{
			canvas.drawBitmap(image, destX, destY, null);
			return;
		}
		src.left = srcX;
		src.right = srcX + width;
		src.top = srcY;
		src.bottom = srcY + height;
		dst.left = destX;
		dst.right = destX + width;
		dst.top = destY;
		dst.bottom = destY + height;
		// ��image��src������ȡ������������canvas��dst������
		canvas.drawBitmap(image, src, dst, null);
	}


	// ����ͼƬ���õ�ָ����ߵ���ͼƬ
	// ʹ�ø��������㣬���ٳ�������ʱ�����
	public static Bitmap scale(Bitmap img, float newWidth, float newHeight)
	{
		if (img == null || img.isRecycled())
		{
			return null;
		}

		float height = img.getHeight();
		float width = img.getWidth();
		if (height == 0 || width == 0 || newWidth == 0 || newHeight == 0)
		{
			return null;
		}
		// ��������ͼƬ�����Matrix
		Matrix matrix = new Matrix();
		matrix.postScale(newWidth / width, newHeight / height);
		try
		{
			// ���ɶ�img����֮���ͼƬ
			return Bitmap.createBitmap(img, 0, 0,
				(int) width, (int) height, matrix, true);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// ��ԭ��λͼִ�о���任
	public static Bitmap mirror(Bitmap img)
	{
		if (img == null || img.isRecycled())
		{
			return null;
		}
		// ������ͼƬִ�о��������Matrix
		Matrix matrix = new Matrix();
		matrix.postScale(-1f, 1f);
		try
		{
			// ���ɶ�imgִ�о���任֮���ͼƬ
			return Bitmap.createBitmap(img, 0, 0, img.getWidth(),
				img.getHeight(), matrix, true);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
