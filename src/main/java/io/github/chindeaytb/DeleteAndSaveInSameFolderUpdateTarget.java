package io.github.chindeaytb;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import lombok.SneakyThrows;
import lombok.Value;

@Value
public class DeleteAndSaveInSameFolderUpdateTarget implements UpdateTarget {
    File file;

    @SneakyThrows
    @Override
    public List<UpdateAction> generateUpdateActions(PotentialUpdate update) {
        return Arrays.asList(
                new UpdateAction.DeleteFile(file),
                new UpdateAction.MoveDownloadedFile(update.temporaryJarStorage(), new File(file.getParentFile(), update.getFileName()))
        );
    }
}