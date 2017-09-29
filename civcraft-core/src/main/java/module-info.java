module nl.civcraft.core {
    requires log4j.api;
    requires joml;
    requires rxjava;
    requires javax.inject;
    requires statefulj.fsm;
    requires guice;
    requires guava;
    requires guice.multibindings;
    exports nl.civcraft.core;
    exports nl.civcraft.core.managers;
    exports nl.civcraft.core.model;
    exports nl.civcraft.core.rendering;
    exports nl.civcraft.core.gamecomponents;
    exports nl.civcraft.core.worldgeneration;
    exports nl.civcraft.core.interaction;
    exports nl.civcraft.core.utils;
    exports nl.civcraft.core.interaction.util;
    exports nl.civcraft.core.modules;
    exports nl.civcraft.core.tasks;
    exports nl.civcraft.core.pathfinding;
}