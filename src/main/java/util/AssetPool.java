package util;

import components.SpriteSheet;
import render.Shader;
import render.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    private static final Texture missing = new Texture();

    public static Shader getShader(String path) {
        File file = new File(path);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            try {
                System.out.println("creating shader " + path);
                Shader shader = new Shader(path);
                shader.compile();
                AssetPool.shaders.put(file.getAbsolutePath(), shader);
                return shader;
            } catch (Exception e) {
                System.err.println("could not compile shader " + path);
                return null;
            }
        }
    }
    public static Texture getTexture(String path) {
        File file = new File(path);
        if (AssetPool.textures.containsKey(file.getAbsolutePath())) {
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            try {
                System.out.println("creating texture " + path);
                Texture texture = new Texture();
                texture.init(path);
                AssetPool.textures.put(file.getAbsolutePath(), texture);
                return texture;
            } catch (Exception e) {
                System.err.println("could not create texture " + path);
                return missing;
            }
        }
    }

    public static void addSpriteSheet(String path, SpriteSheet spriteSheet) {
        File file = new File(path);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())) {
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String path) {
        File file = new File(path);
        assert AssetPool.spriteSheets.containsKey(file.getAbsolutePath()) : "no sprite sheet found: " + file.getAbsolutePath();
        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(), new SpriteSheet(missing, 0,0,0));
    }
}
