package components;

import org.joml.Vector2f;
import render.Texture;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites) {
        this.sprites = new ArrayList<>();
        this.texture = texture;

        int currentX = 0;
        int currentY = 0;

        for (int i = 0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoord = {
                    new Vector2f(rightX, bottomY),
                    new Vector2f(rightX, topY),
                    new Vector2f(leftX, topY),
                    new Vector2f(leftX, bottomY),
            };
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoord);

            this.sprites.add(sprite);

            currentX += spriteWidth;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY += spriteHeight;
            }
        }
    }
    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }
}
