package technici4n.advdebug.mixin;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.advancement.AdvancementFile;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.SimpleAdvancement;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(AdvancementFile.class)
public abstract class AdvancementFileMixin {
    @Shadow @Final private Set<SimpleAdvancement> field_16372;
    @Shadow @Final private Set<SimpleAdvancement> field_16373;
    @Shadow @Final private Set<SimpleAdvancement> field_16374;
    @Shadow @Final private Map<SimpleAdvancement, AdvancementProgress> field_16371;
    @Shadow protected abstract boolean method_14933(SimpleAdvancement advancement);

    private void updateDisplayDfs(SimpleAdvancement advancement, Set<SimpleAdvancement> visited) {
        if(visited.add(advancement)) {
            boolean bl = this.method_14933(advancement);
            boolean bl2 = this.field_16372.contains(advancement);
            if (bl && !bl2) {
                this.field_16372.add(advancement);
                this.field_16373.add(advancement);
                if (this.field_16371.containsKey(advancement)) {
                    this.field_16374.add(advancement);
                }
            } else if (!bl && bl2) {
                this.field_16372.remove(advancement);
                this.field_16373.add(advancement);
            }

            for(SimpleAdvancement child : advancement.getChilds()) {
                updateDisplayDfs(child, visited);
            }

            SimpleAdvancement parent = advancement.getParent();
            if (bl != bl2 && parent != null) {
                updateDisplayDfs(parent, visited);
            }
        }
    }

    @Overwrite
    private void method_14931(SimpleAdvancement advancement) {
        updateDisplayDfs(advancement, new ReferenceOpenHashSet<>());
    }
}
