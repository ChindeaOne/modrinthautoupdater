package io.github.chindeaytb;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.io.File;
import java.util.List;

public abstract class UpdateAction {
    private UpdateAction() {
    }

    public abstract void encode(List<String> arguments);

    @Value
    @EqualsAndHashCode(callSuper = false)
    public static class DeleteFile extends UpdateAction {
        @NonNull
        File toDelete;

        @Override
        public void encode(List<String> arguments) {
            arguments.add("delete");
            arguments.add(toDelete.getAbsolutePath());
        }
    }

    @Value
    @EqualsAndHashCode(callSuper = false)
    public static class MoveDownloadedFile extends UpdateAction {
        @NonNull File sourceFile;
        @NonNull File destinationFile;

        @Override
        public void encode(List<String> arguments) {
            arguments.add("move");
            arguments.add(sourceFile.getAbsolutePath());
            arguments.add(destinationFile.getAbsolutePath());
        }
    }
}

