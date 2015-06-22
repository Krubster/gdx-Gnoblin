package ru.alastar.graphics.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class DepthShader extends BaseShader {
    public static class Config extends DefaultShader.Config {
        public boolean depthBufferOnly = false;

        public Config () {
            super();
            defaultCullFace = GL20.GL_FRONT;
        }

        public Config (String vertexShader, String fragmentShader) {
            super(vertexShader, fragmentShader);
        }
    }

    public static String createPrefix (final Renderable renderable, final Config config) {
        String prefix = DefaultShader.createPrefix(renderable, config);
        if (!config.depthBufferOnly) prefix += "#define PackedDepthFlag\n";
        return prefix;
    }
    public Renderable	renderable;

    public DepthShader(final Renderable renderable, final ShaderProgram shaderProgramModelBorder)   {                  this.renderable = renderable;
        this.program = shaderProgramModelBorder;

        register(DefaultShader.Inputs.worldTrans, DefaultShader.Setters.worldTrans);
        register(DefaultShader.Inputs.projViewTrans, DefaultShader.Setters.projViewTrans);
        register(DefaultShader.Inputs.normalMatrix, DefaultShader.Setters.normalMatrix);

    }

    @Override
    public void begin (Camera camera, RenderContext context) {
        super.begin(camera, context);
        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
    }

    @Override
    public void end () {
        super.end();
    }

    @Override
    public void render(final Renderable renderable)
    {
        if (!renderable.material.has(BlendingAttribute.Type))
        {
            context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        else
        {
            context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        super.render(renderable);
    }

    @Override
    public void init()
    {
        final ShaderProgram program = this.program;
        this.program = null;
        init(program, renderable);
        renderable = null;
    }

    @Override
    public int compareTo(final Shader other)
    {
        return 0;
    }

    @Override
    public boolean canRender(final Renderable instance)
    {
        return true;
    }

    @Override
    public void render(final Renderable renderable, final Attributes combinedAttributes)
    {
        super.render(renderable, combinedAttributes);
    }
}
