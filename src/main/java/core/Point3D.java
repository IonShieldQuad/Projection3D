package core;

public class Point3D {
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    
    public Point3D(){
    
    }
    
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getZ() {
        return z;
    }
    
    public void setZ(double z) {
        this.z = z;
    }
    
    public Point3D copy() {
        return new Point3D(x, y, z);
    }
    
    public Matrix toMatrix() {
        Matrix m = Matrix.makeEmptyMatrix(1, 4);
        return m.fill(new double[][]{{x, y, z, 1}}).transpose();
    }
}
