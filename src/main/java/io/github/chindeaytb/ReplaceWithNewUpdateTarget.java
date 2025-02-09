package io.github.chindeaytb;

import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Getter
public final class ReplaceWithNewUpdateTarget implements UpdateTarget {
    private final File targetFile;

    @Override
    public List<UpdateAction> generateUpdateActions(PotentialUpdate update) {
        return Arrays.asList(
                new UpdateAction.DeleteFile(targetFile),
                new UpdateAction.MoveDownloadedFile(update.getTempJarFile(), new File(targetFile.getParentFile(), update.getFileName()))
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
