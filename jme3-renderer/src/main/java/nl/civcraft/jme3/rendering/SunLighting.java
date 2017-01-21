package nl.civcraft.jme3.rendering;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import nl.civcraft.core.model.events.Tick;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SunLighting {

    private static final float SUN_SPEED = 0.0f;

    private final DirectionalLight sunLight;
    private final DirectionalLightShadowRenderer dlsr;
    private float sunCount = 90f;

    @Autowired
    public SunLighting(Spatial rootNode, AssetManager assetManager, ViewPort mainViewPort) {
        sunLight = new DirectionalLight();
        sunLight.setColor(ColorRGBA.White);
        rootNode.addLight(sunLight);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        final int SHADOWMAP_SIZE = 2048;
        dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sunLight);
        mainViewPort.addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sunLight);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        mainViewPort.addProcessor(fpp);
    }

    @EventListener
    public void handleTick(Tick tick) {
        sunCount += SUN_SPEED;
        Vector3f sunPosition = new Vector3f();
        sunPosition.setX(FastMath.sin(sunCount));
        sunPosition.setY(FastMath.cos(sunCount));
        sunPosition.setZ(-0.2f);
        sunPosition.mult(100f);
        sunLight.setDirection(sunPosition.subtract(new Vector3f(0, 0, 0)));
        float sunHeight = sunPosition.y * -1;
        sunLight.setColor(new ColorRGBA(0.8f, MathUtil.valueOrMin(sunHeight, 0.01f), MathUtil.valueOrMin(sunHeight, 0.01f), MathUtil.valueOrMin(sunHeight, 0.01f)));
        if (sunCount > 360) {
            sunCount = 0;
        }
    }
}
