package io.github.chindeaytb;

import java.io.File;
import java.util.List;

public interface UpdateTarget {
    List<UpdateAction> generateUpdateActions(PotentialUpdate update);

    /**
     * Create
     *
     * @param containedClass
     * @return
     */
    static UpdateTarget replaceJar(Class<?> containedClass) {
        File file = UpdateUtils.getJarFileContainingClass(containedClass);
        return new ReplaceJarUpdateTarget(file);
    }


    /**
     * Create an update target that deletes the Jar containing the specified class, and saves the new Jar in the same
     */
    static UpdateTarget deleteAndSaveInTheSameFolder(Class<?> containedClass) {
        File file = UpdateUtils.getJarFileContainingClass(containedClass);
        return new DeleteAndSaveInSameFolderUpdateTarget(file);
    }

}