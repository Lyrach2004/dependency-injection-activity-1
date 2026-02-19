package net.lyrach.core;

import net.lyrach.annotation.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassPathScanner {

    public List<Class<?>> scan(String basePackage) {
        List<Class<?>> classes = new ArrayList<>();

        String path = basePackage.replace('.', '/');
        File directory = new File("src/main/java/" + path);

        if (!directory.exists()) {
            throw new RuntimeException("Package introuvable : " + basePackage);
        }

        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".java")) {
                String className = basePackage + "." + file.getName().replace(".java", "");
                try {
                    Class<?> clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(Component.class)) {
                        classes.add(clazz);
                    }

                } catch (Exception ignored) {}
            }
        }

        return classes;
    }
}
