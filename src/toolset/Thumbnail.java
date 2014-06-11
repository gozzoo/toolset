package toolset;

import java.awt.Graphics2D;

import static java.awt.RenderingHints.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Thumbnail {
    int w, h, tw, th;
    float scale;
    BufferedImage simg, dimg;
    
    public Thumbnail(String name) throws Exception {
        this(new File(name));
    }
    
    public Thumbnail(File f) throws Exception {
        simg = ImageIO.read(f);
    }
    
    private void scaleSize(int width, boolean sqare) {
        boolean proportionalOrHorizontal = !sqare || (w > h);
        int base = proportionalOrHorizontal ? w : h;
        scale = (float)width / base;
        if (proportionalOrHorizontal) {
            tw = width;
            th = (int)(h * scale);
        } else {
            tw = (int)(w * scale);
            th = width;
        }
    }
    
    private void createScaledInstance(boolean fast) {
        while (h > th || w > tw) {  // multi-step technique
            w /= 2;
            h /= 2;
            if (th > h || tw > w) {
                w = tw;
                h = th;
            }
            scaleImage(fast);
        } 
    }
    
    private void scaleImage(boolean fast) {
        if (w == 0 || h == 0)
            return;
        
        BufferedImage timg = new BufferedImage(w, h,  BufferedImage.TYPE_INT_RGB);
        Graphics2D g = timg.createGraphics();
        //g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!fast) {
            g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
        }
        g.drawImage(dimg, 0, 0, w, h, null);
        g.dispose();
        dimg = timg;
    }
    
    public void save(String name, int width, boolean sqare) throws IOException {
        w = simg.getWidth();
        h = simg.getHeight();
        dimg = simg;        

        scaleSize(width, sqare);
        createScaledInstance(false);
        ImageIO.write(dimg, "jpeg", new File(name));        
    }
    
    public void saveThumb(String name, int width, boolean sqare) throws IOException {
        w = simg.getWidth();
        h = simg.getHeight();        
        dimg = simg;        
        scaleSize(width, sqare);
        createScaledInstance(true);
        ImageIO.write(dimg, "jpeg", new File(name));                  
    }
}