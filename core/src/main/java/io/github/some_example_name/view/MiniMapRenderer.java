package io.github.some_example_name.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class MiniMapRenderer {
    private final FrameBuffer frameBuffer;
    private final SpriteBatch batch;
    private final TextureRegion textureRegion;

    public MiniMapRenderer(int width, int height) {
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        batch = new SpriteBatch();
        textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        textureRegion.flip(false, true); // بسیار مهم!
    }

    public void render(Runnable drawLogic) {
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        drawLogic.run();
        batch.end();

        frameBuffer.end();
    }

    public TextureRegion getTexture() {
        return textureRegion;
    }

    public Batch getBatch() {
        return batch;
    }

    public void dispose() {
        frameBuffer.dispose();
        batch.dispose();
    }
}

