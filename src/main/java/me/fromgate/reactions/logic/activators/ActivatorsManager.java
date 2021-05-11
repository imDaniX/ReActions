package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.ReActionsPlugin;
import me.fromgate.reactions.util.RaGenerator;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.collections.CaseInsensitiveMap;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public class ActivatorsManager {
    private final Map<Class<? extends Activator>, ActivatorType> types;
    private final Map<String, ActivatorType> aliasTypes;
    private final Map<String, Activator> activatorByName;
    private final Logger logger;

    public ActivatorsManager(ReActionsPlugin plugin) {
        types = new HashMap<>();
        aliasTypes = new HashMap<>();
        activatorByName = new CaseInsensitiveMap<>();
        logger = plugin.getLogger();
    }

    public List<String> registerType(@NotNull ActivatorType handler) {
        if (types.containsKey(handler.getType())) {
            throw new IllegalArgumentException("Activator type '" + handler.getType().getSimpleName() + "' is already registered!");
        }
        String name = handler.getName().toUpperCase(Locale.ENGLISH);
        if (aliasTypes.containsKey(name)) {
            ActivatorType preserved = aliasTypes.get(name);
            if (preserved.getName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("Activator type name '" + name + "' is already used for '" + preserved.getType().getSimpleName() + "'!");
            } else {
                logger.warning("Activator type name '" + name + "' is already used as an alias for '" + preserved.getType().getSimpleName() + "', overriding it.");
            }
        }
        List<String> registeredNames = new ArrayList<>();
        registeredNames.add(name);
        aliasTypes.put(name, handler);
        for (String alias : handler.getAliases()) {
            aliasTypes.computeIfAbsent(alias.toUpperCase(Locale.ENGLISH), key -> {
                registeredNames.add(key);
                return handler;
            });
        }
        return registeredNames;
    }

    public void loadActivators(File file, String group) {
        if (!file.exists()) return;
        if (file.getName().endsWith(".yml")) {
            FileConfiguration cfg = new YamlConfiguration();
            try {
                cfg.load(file);
            } catch (InvalidConfigurationException | IOException e) {
                logger.warning("Cannot load '" + file.getName() + "' file!");
                e.printStackTrace();
                return;
            }
            String localGroup = file.getName().substring(0, file.getName().length() - 4);
            group = group.isEmpty() ?
                    localGroup :
                    group + File.separator + localGroup;
            for (String strType : cfg.getKeys(false)) {
                ActivatorType type = aliasTypes.get(strType);
                if (type == null) {
                    logger.warning("Failed to load activators with the unknown type '" + strType + "' in the group '"+ group + "'.");
                    // TODO Move failed activators to backup
                    continue;
                }
                // TODO Replace with some simpler null-safe method
                ConfigurationSection cfgType = Objects.requireNonNull(cfg.getConfigurationSection(strType));
                for (String name : cfgType.getKeys(false)) {
                    ConfigurationSection cfgActivator = Objects.requireNonNull(cfg.getConfigurationSection(name));
                    Activator activator = type.loadActivator(new ActivatorLogic(name, group, cfgActivator), cfgActivator);
                    if (activator == null || !activator.isValid()) {
                        logger.warning("Failed to load activator '" + name + "' in the group '" + group + "'.");
                        continue;
                    }
                    addActivator(activator);
                }
            }
        } else if (file.isDirectory()) {
            group = group.isEmpty() ?
                        file.getName() :
                        group + File.separator + file.getName();
            for (File inner : Objects.requireNonNull(file.listFiles())) {
                loadActivators(inner, group);
            }
        }
    }

    private void addActivator(@NotNull Activator activator) {
        types.get(activator.getClass()).addActivator(activator);
        activatorByName.put(activator.getLogic().getName(), activator);
    }

    @Nullable
    public ActivatorType getType(@NotNull String name) {
        return aliasTypes.get(name.toUpperCase(Locale.ENGLISH));
    }

    @NotNull
    public static ActivatorType typeOf(@NotNull Class<? extends Activator> type, @NotNull String name, @NotNull RaGenerator<Parameters> creator, @NotNull RaGenerator<ConfigurationSection> loader) {
        return new SimpleType(type, name, creator, loader);
    }

    private static class SimpleType implements ActivatorType {
        private final Class<? extends Activator> type;
        private final RaGenerator<Parameters> creator;
        private final RaGenerator<ConfigurationSection> loader;
        private final boolean locatable;
        private final String name;
        private final List<String> aliases;
        private final Set<Activator> activators;

        public SimpleType(Class<? extends Activator> type, String name, RaGenerator<Parameters> creator, RaGenerator<ConfigurationSection> loader) {
            this.type = type;
            this.creator = creator;
            this.loader = loader;
            this.locatable = type.isAssignableFrom(Locatable.class);
            this.name = name;
            this.aliases = List.of(Utils.getAliases(type));
            this.activators = new HashSet<>();
        }

        @NotNull
        @Override
        public Class<? extends Activator> getType() {
            return type;
        }

        @NotNull
        @Override
        public String getName() {
            return name;
        }

        @NotNull
        @Override
        public List<String> getAliases() {
            return aliases;
        }

        @NotNull
        @Override
        public Set<Activator> getActivators() {
            return activators;
        }

        @Nullable
        @Override
        public Activator createActivator(@NotNull ActivatorLogic logic, @NotNull Parameters params) {
            return creator.generate(logic, params);
        }

        @Nullable
        @Override
        public Activator loadActivator(@NotNull ActivatorLogic logic, @NotNull ConfigurationSection cfg) {
            return loader.generate(logic, cfg);
        }

        @Override
        public boolean isLocatable() {
            return locatable;
        }

        @Override
        public void addActivator(@NotNull Activator activator) {
            activators.add(activator);
        }

        @Override
        public void removeActivator(@NotNull Activator activator) {
            activators.remove(activator);
        }

        @Override
        public void clearActivators() {
            activators.clear();
        }

        @NotNull
        @Override
        public List<Activator> getActivatorsAt(@NotNull World world, int x, int y, int z) {
            if (!isLocatable()) return Collections.emptyList();
            List<Activator> located = new ArrayList<>();
            for (Activator activator : getActivators()) {
                if (((Locatable) activator).isLocatedAt(world, x, y, z)) located.add(activator);
            }
            return located;
        }

        @Override
        public void activate(@NotNull Storage storage) {
            for (Activator activator : getActivators()) {
                activator.executeActivator(storage);
            }
        }
    }
}
