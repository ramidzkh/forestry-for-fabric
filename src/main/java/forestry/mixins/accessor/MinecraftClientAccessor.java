package forestry.mixins.accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
}
