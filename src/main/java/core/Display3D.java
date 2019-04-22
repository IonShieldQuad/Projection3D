package core;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Display3D extends JPanel {
    private static final Color GRID_COLOR = Color.WHITE;
    private static final Color MODEL_COLOR = new Color(0xff5599);
    
    private static final Model AXIS = Model.axis(10000);
    
    private Map<Model, Transform3D> models = new HashMap<>();
    
    private double scale = 1.0;
    private boolean parallelMode = false;
    private double angleA = 0.0;
    private double factorL = 0.5;
    private double factorD = 10;
    
    private boolean warpX = true;
    private boolean warpY = true;
    private boolean warpZ = true;
    
    //All angles are in radians!
    
    public Display3D() {
        super();
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        drawModel(g, AXIS, new Transform3D(), GRID_COLOR, false);
        for (Model m : models.keySet()) {
            Transform3D t = models.get(m);
            drawModel(g, m, t, MODEL_COLOR, true);
        }
    }
    
    private void drawModel(Graphics g, Model model, Transform3D transform, Color color, boolean warp) {
        for (Pair<Integer, Integer> edge : model.getEdges()) {
            try {
                Point3D a = model.getVertices().get(edge.a).copy();
                Point3D b = model.getVertices().get(edge.b).copy();
                
                Matrix am = a.toMatrix();
                Matrix bm = b.toMatrix();
                
                am = am.multiply(Matrix.scaleMatrix3D(transform.scale.getX(), transform.scale.getY(), transform.scale.getZ()));
                bm = bm.multiply(Matrix.scaleMatrix3D(transform.scale.getX(), transform.scale.getY(), transform.scale.getZ()));
    
                am = am.multiply(Matrix.rotationMatrix3D(transform.rotation.getX(), transform.rotation.getY(), transform.rotation.getZ()));
                bm = bm.multiply(Matrix.rotationMatrix3D(transform.rotation.getX(), transform.rotation.getY(), transform.rotation.getZ()));
    
                am = am.multiply(Matrix.offsetMatrix3D(transform.offset.getX(), transform.offset.getY(), transform.offset.getZ()));
                bm = bm.multiply(Matrix.offsetMatrix3D(transform.offset.getX(), transform.offset.getY(), transform.offset.getZ()));
                
                a.setX(am.get(0, 0));
                a.setY(am.get(1, 0));
                a.setZ(am.get(2, 0));
    
                b.setX(bm.get(0, 0));
                b.setY(bm.get(1, 0));
                b.setZ(bm.get(2, 0));
                
                
                if (isParallelMode()) {
                    double l = getFactorL();
                    double angle = getAngleA();
                    double x;
                    double y;
                    
                    x = a.getX() + a.getZ() * (l * Math.cos(angle));
                    y = a.getY() + a.getZ() * (l * Math.sin(angle));
                    
                    a.setX(x);
                    a.setY(y);
    
                    x = b.getX() + b.getZ() * (l * Math.cos(angle));
                    y = b.getY() + b.getZ() * (l * Math.sin(angle));
    
                    b.setX(x);
                    b.setY(y);
                }
                else {
                    if (warp) {
                        double d = getFactorD();
                        double x;
                        double y;
    
                        x = a.getX() / (1 + (Math.abs(a.getX()) * (isWarpX() ? 1 : 0) / d) + (Math.abs(a.getY()) * (isWarpY() ? 1 : 0) / d) + ((a.getZ()) * (isWarpZ() ? 1 : 0) / d));
                        y = a.getY() / (1 + (Math.abs(a.getX()) * (isWarpX() ? 1 : 0) / d) + (Math.abs(a.getY()) * (isWarpY() ? 1 : 0) / d) + ((a.getZ()) * (isWarpZ() ? 1 : 0) / d));
    
                        a.setX(x);
                        a.setY(y);
    
                        x = b.getX() / (1 + (Math.abs(b.getX()) * (isWarpX() ? 1 : 0) / d) + (Math.abs(b.getY()) * (isWarpY() ? 1 : 0) / d) + ((b.getZ()) * (isWarpZ() ? 1 : 0) / d));
                        y = b.getY() / (1 + (Math.abs(b.getX()) * (isWarpX() ? 1 : 0) / d) + (Math.abs(b.getY()) * (isWarpY() ? 1 : 0) / d) + ((b.getZ()) * (isWarpZ() ? 1 : 0) / d));
    
                        b.setX(x);
                        b.setY(y);
                    }
                }
                
                g.setColor(color);
                g.drawLine(normX(a.getX()), normY(a.getY()), normX(b.getX()), normY(b.getY()));
            }
            catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    private double interpolate(double a, double b, double alpha) {
        return b * alpha + a * (1 - alpha);
    }
    
    private Color interpolate(Color c1, Color c2, double alpha) {
        double gamma = 2.2;
        int r = (int)Math.round(255 * Math.pow(Math.pow(c2.getRed() / 255.0, gamma) * alpha + Math.pow(c1.getRed() / 255.0, gamma) * (1 - alpha), 1 / gamma));
        int g = (int)Math.round(255 * Math.pow(Math.pow(c2.getGreen() / 255.0, gamma) * alpha + Math.pow(c1.getGreen() / 255.0, gamma) * (1 - alpha), 1 / gamma));
        int b = (int)Math.round(255 * Math.pow(Math.pow(c2.getBlue() / 255.0, gamma) * alpha + Math.pow(c1.getBlue() / 255.0, gamma) * (1 - alpha), 1 / gamma));
        
        return new Color(r, g, b);
    }
    
    
    public int normX(double x) {
        return (int)Math.round((x / getScale()) + 0.5 * getWidth());
    }
    public int normY(double y) {
        return (int)Math.round(0.5 * getHeight() - (y / getScale()));
    }
    
    public double getScale() {
        return scale;
    }
    
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    public Map<Model, Transform3D> getModels() {
        return models;
    }
    
    public boolean isParallelMode() {
        return parallelMode;
    }
    
    public void setParallelMode(boolean parallelMode) {
        this.parallelMode = parallelMode;
    }
    
    public double getAngleA() {
        return angleA;
    }
    
    public void setAngleA(double angleA) {
        this.angleA = angleA;
    }
    
    public double getFactorL() {
        return factorL;
    }
    
    public void setFactorL(double factorL) {
        this.factorL = factorL;
    }
    
    public double getFactorD() {
        return factorD;
    }
    
    public void setFactorD(double factorD) {
        this.factorD = factorD;
    }
    
    public boolean isWarpX() {
        return warpX;
    }
    
    public void setWarpX(boolean warpX) {
        this.warpX = warpX;
    }
    
    public boolean isWarpY() {
        return warpY;
    }
    
    public void setWarpY(boolean warpY) {
        this.warpY = warpY;
    }
    
    public boolean isWarpZ() {
        return warpZ;
    }
    
    public void setWarpZ(boolean warpZ) {
        this.warpZ = warpZ;
    }
}

