package net.minecraft.data.tags;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTagsProvider extends TagsProvider<Fluid> {
   public FluidTagsProvider(DataGenerator p_126523_) {
      super(p_126523_, Registry.FLUID);
   }

   protected void addTags() {
      this.tag(FluidTags.WATER).add(Fluids.WATER, Fluids.FLOWING_WATER);
      this.tag(FluidTags.LAVA).add(Fluids.LAVA, Fluids.FLOWING_LAVA);
   }

   public String getName() {
      return "Fluid Tags";
   }
}