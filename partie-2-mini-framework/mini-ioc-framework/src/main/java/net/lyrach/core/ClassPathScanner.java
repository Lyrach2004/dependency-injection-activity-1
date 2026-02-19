package net.lyrach.core;

import net.lyrach.annotation.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassPathScanner {

    public List<Class<?>> scan(String basePackage) {
        List<Class<?>> classes = new ArrayList<>();

        String path = "src/main/java/" + basePackage.replace('.', '/');
        File root = new File(path);

        if (!root.exists()) {
            throw new RuntimeException("Package introuvable : " + basePackage);
        }

        scanDirectory(root, basePackage, classes);

        return classes;
    }

    private void scanDirectory(File directory, String packageName, List<Class<?>> classes) {
        for (File file : directory.listFiles()) {

            if (file.isDirectory()) {
                // Sous-package → récursion
                scanDirectory(file, packageName + "." + file.getName(), classes);
            }

            else if (file.getName().endsWith(".java")) {
                String className = packageName + "." + file.getName().replace(".java", "");

                try {
                    Class<?> clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(Component.class)) {
                        classes.add(clazz);
                    }

                } catch (Exception ignored) {}
            }
        }
    }
}
