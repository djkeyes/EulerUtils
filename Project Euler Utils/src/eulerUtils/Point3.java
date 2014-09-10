package eulerUtils;
 
// maybe we should parameterize this so it can use doubles, longs, bigintegers, etc

// this is immutable, as far as i can tell
public class Point3 {

	public int x, y, z;
	
	public Point3(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int dot(Point3 that){
		return this.x*that.x + this.y*that.y + this.z*that.z;
	}
	public static int dot(Point3 a, Point3 b){
		return a.dot(b);
	}
	
	public Point3 cross(Point3 that){
		return new Point3(
				this.y*that.z - this.z*that.y,
				this.z*that.x - this.x*that.z,
				this.x*that.y - this.y*that.x);
	}
	
	public static Point3 cross(Point3 a, Point3 b){
		return a.cross(b);
	}
	
	public Point3 subtract(Point3 that){
		return new Point3(this.x - that.x, this.y - that.y, this.z - that.z);
	}
	
	public static Point3 subtract(Point3 a, Point3 b){
		return a.subtract(b);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
}
