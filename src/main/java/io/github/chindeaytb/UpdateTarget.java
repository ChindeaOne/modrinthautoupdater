package io.github.chindeaytb;

import java.io.File;
import java.util.List;

public interface UpdateTarget {
    List<UpdateAction> generateUpdateActions(PotentialUpdate update);

    static UpdateTarget deleteAndSaveInTheSameFolder(Class<?> containedClass) {
        File file = UpdateUtils.getJarFileContainingClass(containedClass);
        return new ReplaceWithNewUpdateTarget(file);
    }

}