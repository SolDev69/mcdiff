package net.minecraft.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.SharedConstants;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.info.BlockListReport;
import net.minecraft.data.info.CommandsReport;
import net.minecraft.data.info.RegistryDumpReport;
import net.minecraft.data.info.WorldgenRegistryDumpReport;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.structures.NbtToSnbt;
import net.minecraft.data.structures.SnbtToNbt;
import net.minecraft.data.structures.StructureUpdater;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ConfiguredStructureTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.obfuscate.DontObfuscate;

public class Main {
   @DontObfuscate
   public static void main(String[] p_129669_) throws IOException {
      SharedConstants.tryDetectVersion();
      OptionParser optionparser = new OptionParser();
      OptionSpec<Void> optionspec = optionparser.accepts("help", "Show the help menu").forHelp();
      OptionSpec<Void> optionspec1 = optionparser.accepts("server", "Include server generators");
      OptionSpec<Void> optionspec2 = optionparser.accepts("client", "Include client generators");
      OptionSpec<Void> optionspec3 = optionparser.accepts("dev", "Include development tools");
      OptionSpec<Void> optionspec4 = optionparser.accepts("reports", "Include data reports");
      OptionSpec<Void> optionspec5 = optionparser.accepts("validate", "Validate inputs");
      OptionSpec<Void> optionspec6 = optionparser.accepts("all", "Include all generators");
      OptionSpec<String> optionspec7 = optionparser.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated");
      OptionSpec<String> optionspec8 = optionparser.accepts("input", "Input folder").withRequiredArg();
      OptionSet optionset = optionparser.parse(p_129669_);
      if (!optionset.has(optionspec) && optionset.hasOptions()) {
         Path path = Paths.get(optionspec7.value(optionset));
         boolean flag = optionset.has(optionspec6);
         boolean flag1 = flag || optionset.has(optionspec2);
         boolean flag2 = flag || optionset.has(optionspec1);
         boolean flag3 = flag || optionset.has(optionspec3);
         boolean flag4 = flag || optionset.has(optionspec4);
         boolean flag5 = flag || optionset.has(optionspec5);
         DataGenerator datagenerator = createStandardGenerator(path, optionset.valuesOf(optionspec8).stream().map((p_129659_) -> {
            return Paths.get(p_129659_);
         }).collect(Collectors.toList()), flag1, flag2, flag3, flag4, flag5);
         datagenerator.run();
      } else {
         optionparser.printHelpOn(System.out);
      }
   }

   public static DataGenerator createStandardGenerator(Path p_129661_, Collection<Path> p_129662_, boolean p_129663_, boolean p_129664_, boolean p_129665_, boolean p_129666_, boolean p_129667_) {
      DataGenerator datagenerator = new DataGenerator(p_129661_, p_129662_);
      if (p_129663_ || p_129664_) {
         datagenerator.addProvider((new SnbtToNbt(datagenerator)).addFilter(new StructureUpdater()));
      }

      if (p_129663_) {
         datagenerator.addProvider(new ModelProvider(datagenerator));
      }

      if (p_129664_) {
         datagenerator.addProvider(new FluidTagsProvider(datagenerator));
         BlockTagsProvider blocktagsprovider = new BlockTagsProvider(datagenerator);
         datagenerator.addProvider(blocktagsprovider);
         datagenerator.addProvider(new ItemTagsProvider(datagenerator, blocktagsprovider));
         datagenerator.addProvider(new EntityTypeTagsProvider(datagenerator));
         datagenerator.addProvider(new RecipeProvider(datagenerator));
         datagenerator.addProvider(new AdvancementProvider(datagenerator));
         datagenerator.addProvider(new LootTableProvider(datagenerator));
         datagenerator.addProvider(new GameEventTagsProvider(datagenerator));
         datagenerator.addProvider(new BiomeTagsProvider(datagenerator));
         datagenerator.addProvider(new ConfiguredStructureTagsProvider(datagenerator));
      }

      if (p_129665_) {
         datagenerator.addProvider(new NbtToSnbt(datagenerator));
      }

      if (p_129666_) {
         datagenerator.addProvider(new BlockListReport(datagenerator));
         datagenerator.addProvider(new RegistryDumpReport(datagenerator));
         datagenerator.addProvider(new CommandsReport(datagenerator));
         datagenerator.addProvider(new WorldgenRegistryDumpReport(datagenerator));
      }

      return datagenerator;
   }
}