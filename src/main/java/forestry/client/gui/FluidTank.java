package forestry.client.gui;

import io.github.astrarre.gui.v1.api.component.AHoverableComponent;
import io.github.astrarre.gui.v1.api.listener.cursor.Cursor;
import io.github.astrarre.rendering.v1.api.plane.Texture;
import io.github.astrarre.rendering.v1.api.plane.icon.Icon;
import io.github.astrarre.rendering.v1.api.space.Render3d;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.minecraft.client.texture.Sprite;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings("UnstableApiUsage")
public class FluidTank extends AHoverableComponent {

    private final long capacity;
    private ResourceAmount<FluidVariant> fluid;

    public FluidTank(long capacity, ResourceAmount<FluidVariant> fluid) {
        this.capacity = capacity;
        this.fluid = fluid;

        tooltipDirect(tooltipBuilder -> {
            for (Text text : FluidVariantRendering.getTooltip(this.fluid.resource())) {
                tooltipBuilder.text(text, false);
            }

            tooltipBuilder.text(new LiteralText(this.fluid.amount() + " dp"), false);
        });
    }

    public void setFluid(ResourceAmount<FluidVariant> fluid) {
        this.fluid = fluid;
    }

    @Override
    protected void render0(Cursor cursor, Render3d render) {
        Icon.slot(getWidth(), getHeight()).render(render);

        FluidVariant variant = fluid.resource();
        FluidVariantRenderHandler handler = FluidVariantRendering.getHandlerOrDefault(variant.getFluid());
        Sprite[] sprites = handler.getSprites(variant);

        if (sprites != null) {
            int color = handler.getColor(variant, null, null);
            float ratio = (float) MathHelper.clamp(fluid.amount() / (double) capacity, 0, 1);

            Texture texture = Texture.sprite(sprites[0]);

            try (var ignored0 = render.translate(1, 1 + (handler.fillsFromTop(variant) ? 0 : getHeight() * (1 - ratio)));
                 var ignored1 = render.scale(1 - 2 / getWidth(), 1 - 2 / getHeight())) {
                Icon.repeat(Icon.tex(texture, 16, 16), getWidth() / 16, getHeight() / 16 * ratio)
                    .colored(color & 0xDDFFFFFF)
                    .render(render);
            }
        }
    }
}
