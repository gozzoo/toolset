import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class SqareImagePreview {
	static int SIZE = 480,
			   PADDING = 30,
			   INNER_SIZE = SIZE - 2 * PADDING;
	
	int sx1, sx2, sy1, sy2,
		dx1, dx2, dy1, dy2;
	
	BufferedImage simg;
	
	public SqareImagePreview(File f) throws Exception {
		simg =  ImageIO.read(f);
        removeWhiteMargin();
	}
	
	static boolean isBlue(int rgb) {
		Color c = new Color(rgb);
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		
		return (b - r > 20 & b - g > 20);
	}
	
	void removeWhiteMargin() {
        int w = simg.getWidth();
        int h = simg.getHeight();
        
		int rgbWhite = Color.WHITE.getRGB();
	
		l1: for (sy1 = 0; sy1 < h; sy1++) {
			for (int i = 0; i < w; i++)
				if (simg.getRGB(i, sy1) != rgbWhite) 
					break l1;
		}	
		l2: for (sy2 = h - 1; sy2 >= 0; sy2--) {
			for (int i = 0; i < w; i++)
				if (simg.getRGB(i, sy2) != rgbWhite) 
					break l2;
		}
		l3: for (sx1 = 0; sx1 < w; sx1++) {
			for (int i = 0; i < h; i++)
				if (simg.getRGB(sx1, i) != rgbWhite) 
					break l3;
		}		
		l4:	for (sx2 = w - 1; sx2 >= 0; sx2--) {
			for (int i = 0; i < h; i++)
				if (simg.getRGB(sx2, i) != rgbWhite)
					break l4;
		}
	}
	
	int sl, ss;
	static float MAX_SCALE = 2.0f;
	
	void scaleSize(int longSide, int shortSide) {
		float scale = (float) INNER_SIZE / longSide;
		if (scale > MAX_SCALE) {
			scale = MAX_SCALE;
			sl =  (int) (scale * longSide);
		} else
			sl = INNER_SIZE;
    	ss =  (int) (shortSide * scale);
	}

	void setDestRect(int w, int h) {
    	dx1 = (SIZE - w) / 2;
    	dx2 = SIZE - dx1;
    	dy1 = (SIZE - h) / 2;
    	dy2 = SIZE - dy1;
	}

	void scaleImage() {
		int w = sx2 - sx1;
		int h = sy2 - sy1;
	
		if (w > h) {
			scaleSize(w, h);
			setDestRect(sl, ss);
        } else {
        	scaleSize(h, w);
        	setDestRect(ss, sl);
        }		
	}
	
	public void save(File f) throws IOException {
		BufferedImage dimg = new BufferedImage(SIZE, SIZE, simg.getType());
        Graphics2D graphics2D = dimg.createGraphics();
        
        graphics2D.setPaint(Color.WHITE);
        graphics2D.fillRect(0, 0, SIZE, SIZE);
        
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
    	scaleImage();

        graphics2D.drawImage(simg, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        graphics2D.dispose();
                
        ImageIO.write(dimg, "jpg", f);  				
	}
}