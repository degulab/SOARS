package role;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import env.Environment;
import env.EquippedObject;
import env.SpotVal;

/**
 * The RoleForExalge class implements role methods related to the exchange algebra.
 * @author H. Tanuma / SOARS project
 */
public class RoleForExalge extends RoleImpl {

	private static final long serialVersionUID = 8905330829533523500L;

	String immbase(EquippedObject self, String key) {
		if (key.startsWith("\"") && key.endsWith("\"")) {
			return key.substring(1, key.length() - 1);
		}
		return self.getProp(key);
	}
	String exbase(EquippedObject self, String equality) {
		String[] bases = equality.split("=", -1);
		StringBuilder base = new StringBuilder(immbase(self, bases[0]));
		for (int i = 1; i < bases.length; ++i) {
			base.append('-').append(immbase(self, bases[i]));
		}
		return base.toString();
	}
	static Class<?> factory;
	Method factory(String name, Class<?>...types) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
		if (factory == null) {
			factory = Environment.getCurrent().classForName("org.soars.exalge.util.ExalgeFactory");
		}
		return factory.getMethod(name, types);
	}
	void exalgeNew(EquippedObject self, String equality) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, ClassNotFoundException, NoSuchMethodException {
		String[] trio = equality.split("=", 3);
		if (trio.length == 1) {
			invokerSet(self, trio[0], factory("newExalge").invoke(null));
		}
		else if (trio.length == 3) {
			invokerSet(self, trio[0], factory("newExalge", String.class, String.class).invoke(null, immbase(self, trio[1]), exbase(self, trio[2])));
		}
		else throw new RuntimeException("Illegal Command - exalgeNew " + equality);
	}
	public void exalgeNew(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeNew(spot.getSpot(this), equality);
	}
	public void exalgeNew(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeNew(getSelf(), equality);
	}
	void exalgeAdd(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = splitPair(equality, "=");
		if (args.length == 2) {
			factory("add", String.class, String.class).invoke(null, immbase(self, args[0]), exbase(self, args[1]));
		}
		else throw new RuntimeException("Illegal Command - exalgeAdd " + equality);
	}
	public void exalgeAdd(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeAdd(spot.getSpot(this), equality);
	}
	public void exalgeAdd(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeAdd(getSelf(), equality);
	}
	void exalgeCreate(EquippedObject self, String key) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		invokerSet(self, key, factory("toExalge").invoke(null));
	}
	public void exalgeCreate(SpotVal spot, String key) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeCreate(spot.getSpot(this), key);
	}
	public void exalgeCreate(String key) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeCreate(getSelf(), key);
	}
	static Class<?> math;
	Method math(String name, Class<?>...types) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
		if (math == null) {
			math = Environment.getCurrent().classForName("org.soars.exalge.util.ExalgeMath");
		}
		return math.getMethod(name, types);
	}
	static Class<?> exalge;
	Class<?> exalge() throws ClassNotFoundException {
		if (exalge == null) {
			exalge = Environment.getCurrent().classForName("exalge2.Exalge");
		}
		return exalge;
	}
	void exalgePlus(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = equality.split("=", 4);
		if (args.length == 3) {
			invokerSet(self, args[0], math("plus", exalge(), exalge()).invoke(null, invokerGet(self, args[1]), invokerGet(self, args[2])));
		}
		else if (args.length == 4) {
			Object o = self.getEquip(args[2]);
			if (o instanceof Number) {
				invokerSet(self, args[0], math("plus", exalge(), Double.class, String.class).invoke(null, invokerGet(self, args[1]), ((Number) o).doubleValue(), self.getProp(args[3])));
			}
			else {
				invokerSet(self, args[0], math("plus", exalge(), String.class, String.class).invoke(null, invokerGet(self, args[1]), String.valueOf(o), self.getProp(args[3])));
			}
		}
		else throw new RuntimeException("Illegal Command - exalgePlus " + equality);
	}
	public void exalgePlus(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgePlus(spot.getSpot(this), equality);
	}
	public void exalgePlus(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgePlus(getSelf(), equality);
	}
	void exalgeMultiple(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = equality.split("=", 3);
		if (args.length == 3) {
			Object o = self.getEquip(args[2]);
			if (o instanceof Number) {
				invokerSet(self, args[0], math("multiple", exalge(), Double.class).invoke(null, invokerGet(self, args[1]), ((Number) o).doubleValue()));
			}
			else {
				invokerSet(self, args[0], math("multiple", exalge(), String.class).invoke(null, invokerGet(self, args[1]), String.valueOf(o)));
			}
		}
		else throw new RuntimeException("Illegal Command - exalgeMultiple " + equality);
	}
	public void exalgeMultiple(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeMultiple(spot.getSpot(this), equality);
	}
	public void exalgeMultiple(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeMultiple(getSelf(), equality);
	}
	void exalgeDivide(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = equality.split("=", 3);
		if (args.length == 3) {
			Object o = self.getEquip(args[2]);
			if (o instanceof Number) {
				invokerSet(self, args[0], math("divide", exalge(), Double.class).invoke(null, invokerGet(self, args[1]), ((Number) o).doubleValue()));
			}
			else {
				invokerSet(self, args[0], math("divide", exalge(), String.class).invoke(null, invokerGet(self, args[1]), String.valueOf(o)));
			}
		}
		else throw new RuntimeException("Illegal Command - exalgeDivide " + equality);
	}
	public void exalgeDivide(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeDivide(spot.getSpot(this), equality);
	}
	public void exalgeDivide(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeDivide(getSelf(), equality);
	}
	void exalgeGetValue(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = equality.split("=", 3);
		if (args.length == 3) {
			invokerSet(self, args[0], math("getValue", exalge(), String.class).invoke(null, invokerGet(self, args[1]), exbase(self, args[2])));
		}
		else throw new RuntimeException("Illegal Command - exalgeGetValue " + equality);
	}
	public void exalgeGetValue(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeGetValue(spot.getSpot(this), equality);
	}
	public void exalgeGetValue(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeGetValue(getSelf(), equality);
	}
	void exalgeNorm(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = splitPair(equality, "=");
		if (args.length == 2) {
			invokerSet(self, args[0], math("norm", exalge()).invoke(null, invokerGet(self, args[1])));
		}
		else throw new RuntimeException("Illegal Command - exalgeNorm " + equality);
	}
	public void exalgeNorm(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeNorm(spot.getSpot(this), equality);
	}
	public void exalgeNorm(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeNorm(getSelf(), equality);
	}
	void exalgeHat(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = splitPair(equality, "=");
		invokerSet(self, args[0], math("hat", exalge()).invoke(null, invokerGet(self, args[args.length - 1])));
	}
	public void exalgeHat(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeHat(spot.getSpot(this), equality);
	}
	public void exalgeHat(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeHat(getSelf(), equality);
	}
	void exalgeBar(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = splitPair(equality, "=");
		invokerSet(self, args[0], math("bar", exalge()).invoke(null, invokerGet(self, args[args.length - 1])));
	}
	public void exalgeBar(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeBar(spot.getSpot(this), equality);
	}
	public void exalgeBar(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeBar(getSelf(), equality);
	}
	void exalgeProjection(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = equality.split("=", 3);
		if (args.length == 3) {
			invokerSet(self, args[0], math("projection", exalge(), String.class).invoke(null, invokerGet(self, args[1]), exbase(self, args[2])));
		}
		else throw new RuntimeException("Illegal Command - exalgeProjection " + equality);
	}
	public void exalgeProjection(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeProjection(spot.getSpot(this), equality);
	}
	public void exalgeProjection(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeProjection(getSelf(), equality);
	}
	void exalgeCopy(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		String[] args = splitPair(equality, "=");
		invokerSet(self, args[0], math("copyExalge", exalge()).invoke(null, invokerGet(self, args[args.length - 1])));
	}
	public void exalgeCopy(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeCopy(spot.getSpot(this), equality);
	}
	public void exalgeCopy(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		exalgeCopy(getSelf(), equality);
	}
}
