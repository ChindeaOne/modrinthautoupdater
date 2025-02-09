/*
 * Based on libautoupdate by Linnea Gräf (c) 2022.
 * Modified by Chindea Eduard (c) 2025.
 *
 * Licensed under BSD-2-Clause. See LICENSE file for details.
 */
package io.github.chindeaytb;

import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Getter
public final class ReplaceWithNewUpdateTarget implements UpdateTarget {
    private final File targetFile;

    @Override
    public List<UpdateAction> createActions(UpdateSetup update) {
        return Arrays.asList(
                new UpdateAction.DeleteFile(targetFile),
                new UpdateAction.MoveDownloadedFile(update.getTempJarFile(), new File(targetFile.getParentFile(), update.getUpdate().filename))
        );
    }

    public ReplaceWithNewUpdateTarget(final File targetFile) {
        this.targetFile = targetFile;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReplaceWithNewUpdateTarget)) return false;
        final ReplaceWithNewUpdateTarget other = (ReplaceWithNewUpdateTarget) o;
        return this.targetFile.equals(other.targetFile);
    }

    @Override
    public int hashCode() {
        return this.targetFile.hashCode();
    }

    @Override
    public String toString() {
        return "ReplaceWithNewUpdateTarget(targetFile=" + this.getTargetFile() + ")";
    }
}
