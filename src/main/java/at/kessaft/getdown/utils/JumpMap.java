package at.kessaft.getdown.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JumpMap implements ConfigurationSerializable {
    private Position spawn = null, bottomCenter = null;
    private int height = 0, radius = 0, bottomSpace = 0;
    private LinkedHashMap<Material, Integer> blocks = new LinkedHashMap<>();

    public JumpMap(){
    }

    public JumpMap(Position spawn, Position bottomCenter, int height, int radius, int bottomSpace, LinkedHashMap<Material, Integer> blocks) {
        this.spawn = spawn;
        this.bottomCenter = bottomCenter;
        this.height = height;
        this.radius = radius;
        this.bottomSpace = bottomSpace;
        this.blocks = blocks;
    }

    public Position getSpawn() {
        return spawn;
    }

    public void setSpawn(Position spawn) {
        this.spawn = spawn;
    }

    public Position getBottomCenter() {
        return bottomCenter;
    }

    public void setBottomCenter(Position bottomCenter) {
        this.bottomCenter = bottomCenter;
    }

    public void setBottomCenter(Location bottom) {
        this.bottomCenter = bottomCenter;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getBottomSpace() {
        return bottomSpace;
    }

    public void setBottomSpace(int bottomSpace) {
        this.bottomSpace = bottomSpace;
    }

    public LinkedHashMap<Material, Integer> getBlocks() {
        return blocks;
    }

    public void addBlock(Material block, int percentage) {
        if (percentage <= 0){
            removeBlock(block);
            return;
        }

        blocks.put(block, percentage);
    }

    public void setBlocks(LinkedHashMap<Material, Integer> blocks) {
        this.blocks = blocks;
    }

    public void removeBlock(Material block) {
        if (blocks.containsKey(block))
            blocks.remove(block);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("spawn", spawn);
        map.put("bottomCenter", bottomCenter);
        map.put("bottomSpace", bottomSpace);
        map.put("height", height);
        map.put("radius", radius);
        map.put("blocks", blocks.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue())));
        return map;
    }

    public static JumpMap deserialize(Map<String, Object> map) {
        LinkedHashMap<String, Integer> bs = new LinkedHashMap<>();
        if (map.get("blocks") != null)
            bs.putAll((LinkedHashMap<String, Integer>)map.get("blocks"));


        return new JumpMap((Position)map.get("spawn"), (Position)map.get("bottomCenter"), (int)map.get("height"), (int)map.get("radius"), (int)map.get("bottomSpace"), new LinkedHashMap<Material, Integer>(bs.entrySet().stream().collect(Collectors.toMap(e -> Material.getMaterial(e.getKey()), e -> e.getValue()))));
    }
}
