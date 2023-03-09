package at.kessaft.getdown.utils;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class Position implements ConfigurationSerializable {
    private double x = Double.NaN, y = Double.NaN, z = Double.NaN;
    private float yaw = Float.NaN, pitch = Float.NaN;

    public Position(Location location, boolean face) {
        this.x = location.getBlockX() + 0.5;
        this.y = location.getX();
        this.z = location.getBlockZ() + 0.5;

        if (face){
            this.yaw = location.getYaw();
            this.pitch = location.getPitch();
        }
    }

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
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

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        map.put("yaw", yaw == Float.NaN ? yaw : null);
        map.put("pitch", pitch == Float.NaN ? pitch : null);
        return map;
    }

    public static Position deserialize(Map<String, Object> map) {
        return new Position((double)map.get("x"),
                (double)map.get("y"),
                (double)map.get("z"),
                map.get("yaw") == null ? Float.NaN : (float)map.get("yaw"),
                map.get("pitch") == null ? Float.NaN : (float)map.get("pitch"));
    }
}
